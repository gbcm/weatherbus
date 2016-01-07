package io.pivotal.controller;

import io.pivotal.service.WeatherService;
import io.pivotal.view.TemperaturePresenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.UnknownServiceException;

/**
 * Created by pivotal on 1/4/16.
 */
@Controller
public class WeatherController {

    @Autowired
    WeatherService weatherService;

    @RequestMapping("/")
    public @ResponseBody String renderTemp() throws UnknownServiceException {
        return new TemperaturePresenter(47.6097, -122.3331, weatherService.getTemp(47.6097, -122.3331)).toString();
    }

}