package com.clemble.social.controller;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.google.api.Google;
import org.springframework.social.linkedin.api.LinkedIn;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.vkontakte.api.VKontakte;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.clemble.social.ConnectionRepositoryCache;
import com.clemble.social.UserCookieGenerator;
import com.clemble.social.event.connection.ConnectionAddedEvent;
import com.google.common.eventbus.EventBus;

@Controller
public class TestController {

    @Inject
    private ConnectionRepositoryCache connectionRepositoryCache;

    @Inject
    private UserCookieGenerator userCookieGenerator;

    @Inject
    private EventBus eventBus;

    @RequestMapping(method = RequestMethod.GET, value = "/populate/{providerId}/{userIdentifier}")
    public String populate(@PathVariable("userIdentifier") String userID, @PathVariable("providerId") String providerID, Model model, HttpServletRequest httpRequest) {
        ConnectionRepository connectionRepository = connectionRepositoryCache.getRepository(userID);
        MultiValueMap<String, Connection<?>> availableConnections = connectionRepository.findAllConnections();
        for (String service : availableConnections.keySet()) {
            List<Connection<?>> connections = availableConnections.get(service);
            for (Connection<?> connection : connections) {
                try {
                    eventBus.post(new ConnectionAddedEvent(providerID, connection));
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }
        return doDisplay(userCookieGenerator.readCookieValue(httpRequest), model);
    }

    private String doDisplay(String userID, Model model) {
        ConnectionRepository connectionRepository = connectionRepositoryCache.getRepository(userID);
        if (connectionRepository != null) {
            MultiValueMap<String, Connection<?>> availableConnections = connectionRepository.findAllConnections();
            for (String service : availableConnections.keySet()) {
                List<Connection<?>> connections = availableConnections.get(service);
                for (Connection<?> connection : connections) {
                    Object api = connection.getApi();
                    try {
                        if (api instanceof Facebook) {
                            Facebook facebook = (Facebook) api;
                            model.addAttribute("friends", facebook.friendOperations().getFriends());
                        } else if (api instanceof Twitter) {
                            Twitter twitter = (Twitter) api;
                            model.addAttribute("twitter", twitter.friendOperations().getFriends());
                        } else if (api instanceof LinkedIn) {
                            LinkedIn linkedIn = (LinkedIn) api;
                            model.addAttribute("linkedIn", linkedIn.connectionOperations().getConnections());
                        } else if (api instanceof VKontakte) {
                            VKontakte kontakte = (VKontakte) api;
                            model.addAttribute("vkontakte", kontakte.friendsOperations().get());
                        } else if (api instanceof Google) {
                            Google google = (Google) api;
                            model.addAttribute("google", google.personOperations().contactQuery().getPage().getItems());
                        }
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
            }
        }
        return "home";
    }

}
