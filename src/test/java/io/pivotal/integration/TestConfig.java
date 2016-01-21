package io.pivotal.integration;

import io.pivotal.Constants;
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

@Configuration
@Profile("test")
public class TestConfig {
    @Bean
    public IOneBusAwayService getOneBusAwayService() {
        return new IOneBusAwayService() {
            @Override
            public ArrivalsAndDeparturesForStopResponse getDeparturesForStop(@Path("stop") String stopId) {
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
            @Override
            public TemperatureResponse getTemperature(@Query("lat") double lat, @Query("lng") double lng) {
                return null;
            }

            @Override
            public ForecastResponse getForecast(@Query("lat") double lat, @Query("lng") double lng) {
                return null;
            }
        };
    }
}

