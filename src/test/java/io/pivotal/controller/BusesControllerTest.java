package io.pivotal.controller;

import io.pivotal.TestUtilities;
import io.pivotal.model.Coordinate;
import io.pivotal.model.StopInfo;
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
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.util.ArrayList;
import java.util.List;

import static net.javacrumbs.jsonunit.spring.JsonUnitResultMatchers.json;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(MockitoJUnitRunner.class)
public class BusesControllerTest {
    @Mock
    BusService busService;
    @InjectMocks
    BusesController subject;
    private MockMvc mockMvc;

    @Mock
    HandlerExceptionResolver handlerExceptionResolver;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(subject)
                .setHandlerExceptionResolvers(handlerExceptionResolver)
                .build();
    }

    @Test
    public void testGetDepartures() throws Exception {
        List<Departure> departures = new ArrayList<Departure>() {{
            add(new Departure("31", "CENTRAL MAGNOLIA FREMONT", 1452550769000L, 1452550571000L));
            add(new Departure("855", "Lynnwood", 0, 1452551256000L));
            add(new Departure("32", "SEATTLE CENTER FREMONT", 0, 1452554291000L));
        }};

        when(busService.getDeparturesForStop("35")).thenReturn(departures);
        mockMvc.perform(get("/buses/departures?stopId=35")).andExpect(
                json().isEqualTo(TestUtilities.jsonFileToString(
                        "src/test/resources/output/DeparturesForStop.json")));
    }

    @Test
    public void testGetCoordinates() throws Exception {
        Coordinate coordinate = new Coordinate(47.6098, -122.3332);

        when(busService.getCoordinatesForStop("1_75403")).thenReturn(coordinate);
        mockMvc.perform(get("/buses/coordinates?stopId=1_75403")).andExpect(
                json().isEqualTo(TestUtilities.jsonFileToString(
                        "src/test/resources/output/StopCoordinates.json")));
    }

    @Test(expected = MissingServletRequestParameterException.class)
    public void testGetDeparturesWithOutParams() throws Throwable {
        try {
            mockMvc.perform(get("/buses/departures"));
        } finally {
            verifyNoMoreInteractions(busService);
        }
    }

    @Test
    public void testGetStopsForCoordinate() throws Exception {
        double latitude = 47.653435;
        double longitude = 122.305641;
        double latidudeSpan = 0.01;
        double longitudeSpan = 0.01;
        List<StopInfo> stops = new ArrayList<StopInfo>() {{
            add(new StopInfo());
            get(0).setId("1_10914");
            get(0).setLatitude(47.656422);
            get(0).setLongitude(-122.312164);
            get(0).setName("15th Ave NE & NE Campus Pkwy");
            add(new StopInfo());
            get(1).setId("1_10917");
            get(1).setLatitude(47.655048);
            get(1).setLongitude(-122.312195);
            get(1).setName("15th Ave NE & NE 40th St");
        }};
        when(busService.getStopsForCoordinate(new Coordinate(latitude,longitude),latidudeSpan,longitudeSpan))
                .thenReturn(stops);

        mockMvc.perform(get("/buses/stops?lat=" + Double.toString(latitude) + "&lng=" + Double.toString(longitude)
                + "&latSpan=" + Double.toString(latidudeSpan) + "&lngSpan=" + Double.toString(longitudeSpan)))
                .andExpect(json().isEqualTo(TestUtilities.jsonFileToString(
                        "src/test/resources/output/StopsForCoordinate.json"
                )));
    }
}