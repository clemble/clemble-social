package com.clemble.social.controller;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.linkedin.api.LinkedIn;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.vkontakte.api.VKontakte;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.clemble.social.ConnectionRepositoryCache;
import com.clemble.social.UserCookieGenerator;

@Controller
public class SocialNetworkController {

    @Inject
    private ConnectionRepositoryCache connectionRepositoryCache;

    @Inject
    private UserCookieGenerator userCookieGenerator;

    public SocialNetworkController() {
    }

    @RequestMapping(value = "/signout", method = RequestMethod.GET)
    public String signout(Model model, HttpServletRequest httpRequest) {
        return "signin";
    }

    @RequestMapping(value = "/signin", method = RequestMethod.GET)
    public String home(Model model, HttpServletRequest httpRequest) {
        return doDisplay(userCookieGenerator.readCookieValue(httpRequest), model);
    }

    private String doDisplay(String userID, Model model) {
        if(userID == null)
            return "signin";
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
