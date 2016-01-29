package io.pivotal.integration;

import com.google.gson.Gson;
import io.pivotal.TestUtilities;
import io.pivotal.service.*;
import io.pivotal.service.response.ForecastResponse;
import io.pivotal.service.response.StopsForLocationResponse;
import io.pivotal.service.response.TemperatureResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import retrofit.http.Path;
import retrofit.http.Query;

import java.io.FileNotFoundException;

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

            @Override
            public StopsForLocationResponse getStopsForLocation(@Query("lat") double lat, @Query("lon") double lng, @Query("latSpan") double latSpan, @Query("lonSpan") double lngSpan) {
                try {
                    return gson.fromJson(
                            TestUtilities.fixtureReader("StopsForLocation"),
                            StopsForLocationResponse.class);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    @Bean
    public IRetrofitWeatherService getWeatherService() {
        return new IRetrofitWeatherService() {
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
