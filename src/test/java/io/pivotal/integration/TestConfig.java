package io.pivotal.integration;

import com.google.gson.Gson;
import io.pivotal.TestUtilities;
import io.pivotal.service.*;
import io.pivotal.service.response.*;
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
    public IRetrofitBusService getOneBusAwayService() {
        return new IRetrofitBusService() {
            Gson gson = new Gson();

            @Override
            public DeparturesCollectionResponse getDepartures(@Query("stopId") String stopId) {
                try {
                    return gson.fromJson(
                            TestUtilities.fixtureReader("DeparturesForStop"),
                            DeparturesCollectionResponse.class);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public SingleStopResponse getStopForId(@Query("stopId") String stopId) {
                try {
                    return gson.fromJson(
                            TestUtilities.fixtureReader("StopInfo"),
                            SingleStopResponse.class);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public StopsCollectionResponse getStops(@Query("lat") double lat, @Query("lng") double lng, @Query("latSpan") double latSpan, @Query("lngSpan") double lngSpan) {
                try {
                    return gson.fromJson(
                            TestUtilities.fixtureReader("StopsForLocation"),
                            StopsCollectionResponse.class);
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
