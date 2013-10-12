/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clemble.social;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Before a request is handled: 
 * 1. sets the current User in the {@link SecurityContext} from a cookie, if present and the user is still connected to Facebook.
 * 2. requires that the user sign-in if he/she hasn't already.
 */
public final class UserInterceptor extends HandlerInterceptorAdapter {

    @Inject
    final private ConnectionRepositoryCache connectionRepositoryContext;

    final private UserCookieGenerator userCookieGenerator = new UserCookieGenerator();

    public UserInterceptor(ConnectionRepositoryCache connectionRepositoryContext) {
        this.connectionRepositoryContext = connectionRepositoryContext;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }

    private boolean newUser(HttpServletRequest request, HttpServletResponse response) {
        String userId = userCookieGenerator.readCookieValue(request);
        return (userId != null && connectionRepositoryContext.getRepository(userId) != null);
    }

    private boolean requireSignIn(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!request.getServletPath().startsWith("/signin")) {
            new RedirectView("/signin", true).render(null, request, response);
            return false;
        } else {
            return true;
        }
    }
}