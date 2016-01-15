package io.pivotal.controller;

import io.pivotal.domain.User;
import io.pivotal.domain.UserRepository;
import io.pivotal.errorHandling.*;
import io.pivotal.view.*;
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

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public @ResponseBody String addUser(@RequestBody String username) throws Exception {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            throw new UserAlreadyExistsException();
        }
        User saved = userRepository.save(new User(username));
        return new UserMessagePresenter(saved).toJson();
    }

    @RequestMapping(value = "{username}/stops", method = RequestMethod.POST)
    public @ResponseBody String addStop(
            @PathVariable("username")  String username,
            @RequestBody String stopId) throws Exception {

        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundException();
        }
        user.getStopIds().add(stopId);
        userRepository.save(user);
        return "";
    }
}

