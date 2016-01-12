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

import java.util.ArrayList;
import java.util.List;

import static net.javacrumbs.jsonunit.spring.JsonUnitResultMatchers.json;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(MockitoJUnitRunner.class)
public class BusesControllerTest {
    @Mock
    BusService busService;
    @InjectMocks
    BusesController subject;
    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(subject).build();
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
        Coordinate coordinate = new Coordinate(47.654365, -122.305214);

        when(busService.getCoordinatesForStop("1_75403")).thenReturn(coordinate);
        mockMvc.perform(get("/buses/coordinates?stopId=1_75403")).andExpect(
                json().isEqualTo(TestUtilities.jsonFileToString(
                        "src/test/resources/output/StopCoordinates.json")));
    }
}