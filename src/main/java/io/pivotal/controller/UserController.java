package io.pivotal.controller;

import io.pivotal.domain.User;
import io.pivotal.domain.UserRepository;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
            return "User not found";
        }
        String message = "Stops for " + username + ":<br/>";
        for (String stopId : user.getStopIds()) {
            message += "Stop: " + stopId + "<br/>";
        }
        return message;
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

