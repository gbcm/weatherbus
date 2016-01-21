package io.pivotal.service;

import io.pivotal.Constants;
import retrofit.http.GET;
import retrofit.http.Path;

public interface IWeatherService {
    @GET("/?lat={lat}&lng={lng}")
    WeatherConditionsResponse getCurrentTemp(@Path("lat") double lat, @Path("lng") double lng);
}
