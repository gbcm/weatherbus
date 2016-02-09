package io.pivotal.service;

import io.pivotal.service.response.ForecastResponse;
import io.pivotal.service.response.TemperatureResponse;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("weatherbus-weather")
public interface IFeignWeatherService {
    @RequestMapping(method = RequestMethod.GET, value = "/api/temp", consumes = "application/json")
    TemperatureResponse getTemperature(@RequestParam("lat") double lat, @RequestParam("lng") double lng);

    @RequestMapping(method = RequestMethod.GET, value = "/api/forecast", consumes = "application/json")
    ForecastResponse getForecast(@RequestParam("lat") double lat, @RequestParam("lng") double lng);
}
