package io.pivotal.controller;

import io.pivotal.service.WeatherService;
import io.pivotal.view.ForecastPresenter;
import io.pivotal.view.TemperaturePresenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.parsing.ParseState;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.UnknownServiceException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * Created by pivotal on 1/4/16.
 */
@Controller
public class WeatherController {

    @Autowired
    WeatherService weatherService;

    @RequestMapping("/")
    public @ResponseBody String getCurrentTemp(@RequestParam double lat, @RequestParam double lng) throws UnknownServiceException {
        return new TemperaturePresenter(lat, lng, weatherService.getCurrentTemp(lat, lng)).toJson();
    }

    @RequestMapping("/forecast")
    public @ResponseBody String getFutureTemp(@RequestParam double lat, @RequestParam double lng) throws UnknownServiceException {
        return renderForecast(lat, lng, weatherService.getFutureTemp(lat, lng));
    }

    private String renderForecast(double lat, double lng, Map<Date, Double> forecast) {
        List<ForecastPresenter.Forecast> forecasts = new ArrayList<>();
        for (Map.Entry<Date, Double> kvp : forecast.entrySet()) {
            forecasts.add(new ForecastPresenter.Forecast(kvp.getKey().getTime(), kvp.getValue()));
        }
        return new ForecastPresenter(lat, lng, forecasts).toJson();
    }
}