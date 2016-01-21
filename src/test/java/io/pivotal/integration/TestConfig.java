package io.pivotal.integration;

import com.google.gson.Gson;
import io.pivotal.Constants;
import io.pivotal.TestUtilities;
import io.pivotal.service.*;
import io.pivotal.service.response.ForecastResponse;
import io.pivotal.service.response.TemperatureResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.http.Path;
import retrofit.http.Query;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Configuration
@Profile("test")
public class TestConfig {
    @Bean
    public IOneBusAwayService getOneBusAwayService() {
        return new IOneBusAwayService() {
            Gson gson = new Gson();
            @Override
            public ArrivalsAndDeparturesForStopResponse getDeparturesForStop(@Path("stop") String stopId) {

                try {
                    return gson.fromJson(
                            TestUtilities.fixtureReader("DeparturesForStop"),
                            ArrivalsAndDeparturesForStopResponse.class);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public StopResponse getCoordinatesForStop(@Path("stop") String stopId) {
                return new StopResponseBuilder().build();
            }
        };
    }

    @Bean
    public IWeatherService getWeatherService() {
        return new IWeatherService() {
            Gson gson = new Gson();
            @Override
            public TemperatureResponse getTemperature(@Query("lat") double lat, @Query("lng") double lng) {
                try {
                    return gson.fromJson(
                            TestUtilities.fixtureReader("WeatherServiceTemp"),
                            TemperatureResponse.class);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public ForecastResponse getForecast(@Query("lat") double lat, @Query("lng") double lng) {
                try {
                    return gson.fromJson(
                            TestUtilities.fixtureReader("WeatherServiceForecast"),
                            ForecastResponse.class);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }
}
