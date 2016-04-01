package io.pivotal.integration;

import com.google.gson.Gson;
import io.pivotal.TestUtilities;
import io.pivotal.service.IFeignBusService;
import io.pivotal.service.IFeignCrimeService;
import io.pivotal.service.IFeignWeatherService;
import io.pivotal.service.response.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.FileNotFoundException;

@Configuration
@Profile("test")
public class TestConfig {
    @Bean
    public IFeignBusService getOneBusAwayService() {
        return new IFeignBusService() {
            Gson gson = new Gson();

            @Override
            public DeparturesCollectionResponse getDepartures(String stopId) {
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
            public SingleStopResponse getStopForId(String stopId) {
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
            public StopsCollectionResponse getStops(double lat, double lng, double latSpan, double lngSpan) {
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
    public IFeignWeatherService getWeatherService() {
        return new IFeignWeatherService() {
            Gson gson = new Gson();
            @Override
            public TemperatureResponse getTemperature(double lat, double lng) {
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
            public ForecastResponse getForecast(double lat, double lng) {
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

    @Bean
    public IFeignCrimeService getCrimeService() {
        return new IFeignCrimeService() {
            Gson gson = new Gson();
            @Override
            public CrimeResponse getCrimeInfo(@RequestParam("lat") double latitude, @RequestParam("lng") double longitude) {
                try {
                    return gson.fromJson(
                            TestUtilities.fixtureReader("CrimeDetail"),
                            CrimeResponse.class);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }
}
