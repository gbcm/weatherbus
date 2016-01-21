package io.pivotal.service;

import io.pivotal.service.response.ForecastResponse;
import io.pivotal.service.response.TemperatureResponse;
import retrofit.http.GET;
import retrofit.http.Query;

public interface IWeatherService {
    @GET("api/temp")
    TemperatureResponse getTemperature(@Query("lat") double lat, @Query("lng") double lng);

    @GET("api/forecast")
    ForecastResponse getForecast(@Query("lat") double lat, @Query("lng") double lng);
}
