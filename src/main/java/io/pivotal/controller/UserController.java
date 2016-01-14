package io.pivotal.controller;

import io.pivotal.domain.User;
import io.pivotal.domain.UserRepository;
import io.pivotal.errorHandling.ErrorMessages;
import io.pivotal.errorHandling.ErrorPathConstants;
import io.pivotal.errorHandling.ErrorPresenter;
import io.pivotal.errorHandling.UserNotFoundException;
import io.pivotal.view.JsonListPresenter;
import io.pivotal.view.JsonPresenter;
import io.pivotal.view.StopPresenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pivotal on 1/12/16.
 */
@RequestMapping("/users")
@Controller
public class UserController {
    @Autowired
    UserRepository userRepository;

    @RequestMapping("stops")
    public @ResponseBody
    String getStops(@RequestParam String username) throws Exception {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundException();
        }

        List<JsonPresenter> presenters = new ArrayList<>();
        for (String stopId : user.getStopIds()) {
            presenters.add(new StopPresenter(stopId));
        }

        return new JsonListPresenter(presenters).toJson();
    }

    @RequestMapping("addUser")
    public @ResponseBody
    String addUser(@RequestParam String username) throws Exception {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return "User: " + username + " already exists";
        }
        userRepository.save(new User(username));
        return "User: " + username + " added.";
    }

    @RequestMapping("addStop")
    public @ResponseBody
    String addStop(@RequestParam String username, @RequestParam String stopId) throws Exception {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return "User not found";
        }
        user.getStopIds().add(stopId);
        userRepository.save(user);
        return "Stop " + stopId + " added!";
    }


}

