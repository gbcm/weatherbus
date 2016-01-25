package io.pivotal.service;

import io.pivotal.model.Coordinate;
import io.pivotal.service.response.ForecastResponse;
import io.pivotal.service.response.TemperatureResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WeatherService {
    private IRetrofitWeatherService weatherService;

    @Autowired
    public WeatherService(IRetrofitWeatherService weatherService) {
        this.weatherService = weatherService;
    }

    public ForecastResponse getForecast(Coordinate coordinate) {
        return weatherService.getForecast(coordinate.getLatitude(), coordinate.getLongitude());
    }

    public TemperatureResponse getTemperature(Coordinate coordinate) {
        return weatherService.getTemperature(coordinate.getLatitude(), coordinate.getLongitude());
    }
}
