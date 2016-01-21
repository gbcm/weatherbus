package io.pivotal.service;

import com.google.gson.Gson;
import io.pivotal.TestUtilities;
import io.pivotal.model.Coordinate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BusServiceTest {
    @Mock
    IOneBusAwayService mockService;
    @InjectMocks
    BusService subject;

    Gson gson = new Gson();

    @Test
    public void testGetDeparturesForStop() throws Exception {
        final String stopId = "12345";

        ArrivalsAndDeparturesForStopResponse response = gson.fromJson(
                TestUtilities.fixtureReader("DeparturesForStop"),
                ArrivalsAndDeparturesForStopResponse.class);
        when(mockService.getDeparturesForStop(stopId)).thenReturn(response);

        List<Departure> expectedDepartures = new ArrayList<Departure>() {{
            add(new Departure("31", "CENTRAL MAGNOLIA FREMONT", 1453317145000L, 1453317145000L));
            add(new Departure("855", "Lynnwood", 0, 1516561850000L));
            add(new Departure("32", "SEATTLE CENTER FREMONT", 1516563660000L, 1516563660000L));
        }};

        assertEquals(expectedDepartures, subject.getDeparturesForStop("12345"));
    }

    @Test
    public void testGetCoordinatesForStop() throws Exception {
        final String stopId = "1_75403";

        StopResponse stopResponse = gson.fromJson(
                TestUtilities.fixtureReader("StopInfo"),
                StopResponse.class);
        when(mockService.getCoordinatesForStop(stopId)).thenReturn(stopResponse);

        Coordinate expectedCoordinate = new Coordinate(47.6098, -122.3332);
        Coordinate coordinate = subject.getCoordinatesForStop(stopId);

        assertEquals(expectedCoordinate.getLatitude(), coordinate.getLatitude(), 0);
        assertEquals(expectedCoordinate.getLongitude(), coordinate.getLongitude(), 0);
    }

    @Test
    public void testGetStopName() throws Exception {
        final String stopId = "1_75403";

        StopResponse stopResponse = gson.fromJson(
                TestUtilities.fixtureReader("StopInfo"),
                StopResponse.class);
        when(mockService.getCoordinatesForStop(stopId)).thenReturn(stopResponse);

        assertEquals(subject.getStopName(stopId), "Stevens Way & Benton Ln");
    }
}
