package com.socialone.event.aop;

import java.util.Collection;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.framework.ProxyConfig;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.integration.annotation.Publisher;
import org.springframework.util.ClassUtils;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

/**
 * Post-processes beans that contain the method-level for @{@link Publisher} @{@link Subscribe} annotations based on guava framework.
 */
@SuppressWarnings("serial")
public class GuavaPublisherAnnotationBeanPostProcessor extends ProxyConfig implements BeanPostProcessor, BeanClassLoaderAware, BeanFactoryAware,
        InitializingBean, Ordered {

    private volatile EventBus eventBus;

    private volatile Advisor publisherAdvisor = new AbstractAdvisor(Publisher.class, new MethodInterceptor() {
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            Object event = invocation.proceed();
            if (event instanceof Collection) {
                Collection<?> eventCollection = (Collection<?>) event;
                for (Object publishedEvent : eventCollection) {
                    eventBus.post(publishedEvent);
                }
            } else if (event != null) {
                eventBus.post(event);
            }
            return event;
        }
    });

    private volatile Advisor subscriberAdvisor = new AbstractAdvisor(Subscribe.class, null);

    private volatile int order = Ordered.LOWEST_PRECEDENCE;

    private volatile BeanFactory beanFactory;

    private volatile ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void setBeanClassLoader(ClassLoader classLoader) {
        this.beanClassLoader = classLoader;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getOrder() {
        return this.order;
    }

    public void afterPropertiesSet() {
        this.eventBus = beanFactory.getBean(EventBus.class);
    }

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> targetClass = AopUtils.getTargetClass(bean);
        if (targetClass == null) {
            return bean;
        }

        if (AopUtils.canApply(this.subscriberAdvisor, targetClass)) {
            eventBus.register(bean);
        }

        if (AopUtils.canApply(this.publisherAdvisor, targetClass)) {
            if (bean instanceof Advised) {
                ((Advised) bean).addAdvisor(this.publisherAdvisor);
            } else {
                ProxyFactory proxyFactory = new ProxyFactory(bean);
                // Copy our properties (proxyTargetClass etc) inherited from ProxyConfig.
                proxyFactory.copyFrom(this);
                proxyFactory.addAdvisor(this.publisherAdvisor);
                bean = proxyFactory.getProxy(this.beanClassLoader);
            }
        }

        return bean;
    }

}
