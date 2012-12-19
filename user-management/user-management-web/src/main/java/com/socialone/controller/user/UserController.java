package com.socialone.controller.user;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.socialone.data.user.User;
import com.socialone.data.user.contact.Profile;
import com.socialone.service.user.UserRepository;
import com.socialone.service.user.contact.ProfileRepository;

@Controller
public class UserController {

    @Inject
    private ProfileRepository profileRepository;

    @Inject
    private UserRepository userRepository;

    @RequestMapping(method = RequestMethod.GET, value = "/user", produces = "application/json")
    public Collection<Profile> index() {
        return new ArrayList<Profile>();
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/user", produces = "application/json")
    public String create(@RequestBody User user) {
        User savedUser = userRepository.addUserProfile(user);
        return savedUser.getId();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/user/{userID}", produces = "application/json")
    public User show(@PathVariable("userID") String userID) {
        return userRepository.getUserProfile(userID);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/user/{userID}", produces = "application/json")
    public User update(@RequestBody User user) {
        return userRepository.updateUserProfile(user);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/user/{userID}", produces = "application/json")
    public void destroy(@PathVariable("userID") String userID) {
        userRepository.removeUserProfile(userID);
    }
}
