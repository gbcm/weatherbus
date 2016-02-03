package io.pivotal.controller;

import io.pivotal.model.Coordinate;
import io.pivotal.model.DepartureWithTemperature;
import io.pivotal.service.BusService;
import io.pivotal.service.response.DepartureResponse;
import io.pivotal.service.WeatherService;
import io.pivotal.service.response.ForecastResponse;
import io.pivotal.service.response.TemperatureResponse;
import io.pivotal.view.WeatherBusPresenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping("/wb")
public class WeatherBusController {
    @Autowired
    private BusService busService;

    @Autowired
    private WeatherService weatherService;

    @RequestMapping("")
    public @ResponseBody String getWeatherBus(@RequestParam String stopId) throws Exception {
        List<DepartureResponse> departureResponses = busService.getDeparturesForStop(stopId);
        Coordinate coordinate = busService.getCoordinatesForStop(stopId);

        ForecastResponse forecastResponse = weatherService.getForecast(coordinate);
        TemperatureResponse temperatureResponse = weatherService.getTemperature(coordinate);

        SortedMap<Date, Double> forecast = new TreeMap<>();
        forecast.put(new Date(), temperatureResponse.getTemp());
        for (ForecastResponse.TimedTemp tt : forecastResponse.getForecast()) {
            forecast.put(new Date(tt.getTimeInMillisec() * 1000), tt.getTemp());
        }

        List<DepartureWithTemperature> dwt = new ArrayList<>();

        for (DepartureResponse departureResponse : departureResponses) {
            long departureTimeMs = departureResponse.getPredictedTime();
            if (departureTimeMs == 0) {
                departureTimeMs = departureResponse.getScheduledTime();
            }

            for (Map.Entry<Date, Double> temp : forecast.entrySet()) {
                if (departureTimeMs < temp.getKey().getTime()) {
                    dwt.add(new DepartureWithTemperature(departureResponse, temp.getValue()));
                    break;
                }
            }
        }

        double lastTemp = forecast.get(forecast.lastKey());
        List<DepartureResponse> remainingDepartureResponses = departureResponses.subList(dwt.size(), departureResponses.size());
        for (DepartureResponse departureResponse : remainingDepartureResponses) {
            dwt.add(new DepartureWithTemperature(departureResponse, lastTemp));
        }

        return new WeatherBusPresenter(coordinate.getLatitude(), coordinate.getLongitude(), stopId, dwt).toJson();
    }
}
