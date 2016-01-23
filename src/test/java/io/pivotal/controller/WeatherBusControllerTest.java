package io.pivotal.controller;

import com.google.gson.Gson;
import io.pivotal.TestUtilities;
import io.pivotal.model.Coordinate;
import io.pivotal.service.BusService;
import io.pivotal.service.Departure;
import io.pivotal.service.IRetrofitWeatherService;
import io.pivotal.service.WeatherService;
import io.pivotal.service.response.ForecastResponse;
import io.pivotal.service.response.TemperatureResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static net.javacrumbs.jsonunit.spring.JsonUnitResultMatchers.json;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(MockitoJUnitRunner.class)
public class WeatherBusControllerTest {
    @Mock
    BusService busService;
    @Mock
    WeatherService weatherService;
    @InjectMocks
    WeatherBusController subject;

    private MockMvc mockMvc;

    Gson gson = new Gson();

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(subject).build();
    }

    @Test
    public void testGetWeatherBus() throws Exception {
        String stopId = "1_75403";
        double latitude = 47.6098;
        double longitude = -122.3332;
        Coordinate coordinate = new Coordinate(latitude, longitude);

        List<Departure> departures = new ArrayList<Departure>() {{
            add(new Departure("31", "CENTRAL MAGNOLIA FREMONT", 1453317145000L, 1453317145000L));
            add(new Departure("855", "Lynnwood", 0, 1516561850000L));
            add(new Departure("32", "SEATTLE CENTER FREMONT", 1516563660000L, 1516563660000L));
        }};

        ForecastResponse forecastResponse = gson.fromJson(
                TestUtilities.fixtureReader("WeatherServiceForecast"),
                ForecastResponse.class);
        TemperatureResponse temperatureResponse = gson.fromJson(
                TestUtilities.fixtureReader("WeatherServiceTemp"),
                TemperatureResponse.class);

        when(busService.getDeparturesForStop(stopId)).thenReturn(departures);
        when(busService.getCoordinatesForStop(stopId)).thenReturn(coordinate);
        when(weatherService.getForecast(coordinate)).thenReturn(forecastResponse);
        when(weatherService.getTemperature(coordinate)).thenReturn(temperatureResponse);

        mockMvc.perform(get("/wb?stopId=" + stopId)).andExpect(
                json().isEqualTo(TestUtilities.jsonFileToString(
                        "src/test/resources/output/WeatherBusResponse.json")));
    }

}