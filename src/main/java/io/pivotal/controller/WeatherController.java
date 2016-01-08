package io.pivotal.controller;

import io.pivotal.service.WeatherService;
import io.pivotal.view.TemperaturePresenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.net.UnknownServiceException;

/**
 * Created by pivotal on 1/4/16.
 */
@Controller
public class WeatherController {

    @Autowired
    WeatherService weatherService;

    @RequestMapping("/")
    public @ResponseBody String getCurrentTemp() throws UnknownServiceException {
        return renderTemp(weatherService.getCurrentTemp(47.6097, -122.3331));
    }

    @RequestMapping("/forecast")
    public @ResponseBody String getFutureTemp() throws UnknownServiceException {
        throw new NotImplementedException();
    }

    private String renderTemp(double temp) {
        return new TemperaturePresenter(47.6097, -122.3331, temp).toJson();
    }
}