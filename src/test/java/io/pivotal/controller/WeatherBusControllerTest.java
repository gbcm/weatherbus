package io.pivotal.controller;

import io.pivotal.TestUtilities;
import io.pivotal.model.Coordinate;
import io.pivotal.service.BusService;
import io.pivotal.service.Departure;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static net.javacrumbs.jsonunit.spring.JsonUnitResultMatchers.json;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(MockitoJUnitRunner.class)
public class WeatherBusControllerTest {
    @Mock
    BusService busService;
//    @Mock
//    WeatherService weatherService;
    @InjectMocks
    WeatherBusController subject;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(subject).build();
    }

    @Test
    public void testGetWeatherBus() throws Exception {
        String stopId = "1_75403";
        Coordinate coordinate = new Coordinate(47.654365, -122.305214);

        List<Departure> departures = new ArrayList<Departure>() {{
            add(new Departure("31", "CENTRAL MAGNOLIA FREMONT", 1452550769000L, 1452550571000L));
            add(new Departure("855", "Lynnwood", 0, 1452551256000L));
            add(new Departure("32", "SEATTLE CENTER FREMONT", 0, 1452554291000L));
        }};

        Map<Date, Double> futureTemps = new HashMap<Date, Double>() {{
            put(new Date(1452550769000L), 14.4);
            put(new Date(1452551256001L), 15.5);
        }};

        when(busService.getDeparturesForStop(stopId)).thenReturn(departures);
        when(busService.getCoordinatesForStop(stopId)).thenReturn(coordinate);
//        when(weatherService.getFutureTemp(coordinate)).thenReturn(futureTemps);

        mockMvc.perform(get("/wb?stopId=" + stopId)).andExpect(
                json().isEqualTo(TestUtilities.jsonFileToString(
                        "src/test/resources/output/WeatherBusResponse.json")));
    }

}