package io.pivotal.service;

import com.google.gson.Gson;
import io.pivotal.TestUtilities;
import io.pivotal.model.Coordinate;
import io.pivotal.model.StopInfo;
import io.pivotal.service.response.StopsForLocationResponse;
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

    @Test
    public void testGetStopsForCoordinate() throws Exception {
        Coordinate coordinate = new Coordinate(47.653435, -122.305641);
        double latSpan=0.01;
        double lngSpan= 0.01;
        StopsForLocationResponse response = gson.fromJson(
                TestUtilities.fixtureReader("StopsForLocation"),
                StopsForLocationResponse.class);
        when(mockService.getStopsForLocation(coordinate.getLatitude(), coordinate.getLongitude(), latSpan, lngSpan))
                .thenReturn(response);
        List<StopInfo> expected = new ArrayList<StopInfo>() {{
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
        List<StopInfo> actual = subject.getStopsForCoordinate(coordinate, latSpan, lngSpan);

        assertEquals(actual,expected);
    }
}
