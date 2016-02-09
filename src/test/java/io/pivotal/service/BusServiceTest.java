package io.pivotal.service;

import com.google.gson.Gson;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import io.pivotal.TestUtilities;
import io.pivotal.errorHandling.StopNotFoundException;
import io.pivotal.model.Coordinate;
import io.pivotal.service.response.*;
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
    IFeignBusService mockService;
    @InjectMocks
    BusService subject;

    Gson gson = new Gson();

    @Test
    public void testGetDeparturesForStop() throws Exception {
        final String stopId = "12345";

        DeparturesCollectionResponse response = gson.fromJson(
                TestUtilities.fixtureReader("DeparturesForStop"),
                DeparturesCollectionResponse.class);
        when(mockService.getDepartures(stopId)).thenReturn(response);

        List<DepartureResponse> expectedDepartureResponses = new ArrayList<DepartureResponse>() {{
            add(new DepartureResponse("31", "CENTRAL MAGNOLIA FREMONT", 1896376792000L, 1896376792000L));
            add(new DepartureResponse("855", "Lynnwood", 0, 1896376812000L));
            add(new DepartureResponse("32", "SEATTLE CENTER FREMONT", 0, 1896376852000L));
        }};

        assertEquals(expectedDepartureResponses, subject.getDepartures("12345"));
    }

    @Test
    public void testGetCoordinatesForStop() throws Exception {
        final String stopId = "1_75403";

        SingleStopResponse stopResponse = gson.fromJson(
                TestUtilities.fixtureReader("StopInfo"),
                SingleStopResponse.class);
        when(mockService.getStopForId(stopId)).thenReturn(stopResponse);

        Coordinate expectedCoordinate = new Coordinate(47.6098, -122.3332);
        Coordinate coordinate = subject.getCoordinates(stopId);

        assertEquals(expectedCoordinate.getLatitude(), coordinate.getLatitude(), 0);
        assertEquals(expectedCoordinate.getLongitude(), coordinate.getLongitude(), 0);
    }

    @Test(expected = StopNotFoundException.class)
    public void testGetCoordinates_invalidStopId() throws Throwable {
        String stopId = "bogus";
        when(mockService.getStopForId(stopId)).thenThrow(HystrixRuntimeException.class);
        subject.getCoordinates(stopId);
    }

    @Test
    public void testGetStopName() throws Exception {
        final String stopId = "1_75403";

        SingleStopResponse stopResponse = gson.fromJson(
                TestUtilities.fixtureReader("StopInfo"),
                SingleStopResponse.class);
        when(mockService.getStopForId(stopId)).thenReturn(stopResponse);

        assertEquals(subject.getStopName(stopId), "The name of the stop");
    }

    @Test(expected = StopNotFoundException.class)
    public void testGetStopName_invalidStopId() throws Throwable {
        String stopId = "bogus";
        when(mockService.getStopForId(stopId)).thenThrow(HystrixRuntimeException.class);
        subject.getStopName(stopId);
    }


    @Test
    public void testGetStopsForCoordinate() throws Exception {
        Coordinate coordinate = new Coordinate(47.653435, -122.305641);
        double latSpan=0.01;
        double lngSpan= 0.01;
        StopsCollectionResponse response = gson.fromJson(
                TestUtilities.fixtureReader("StopsForLocation"),
                StopsCollectionResponse.class);
        when(mockService.getStops(
                coordinate.getLatitude(),
                coordinate.getLongitude(),
                latSpan,
                lngSpan))
                .thenReturn(response);
        List<StopResponse> expected = new ArrayList<>();
        expected.add(new StopResponse(
                "1_10914",
                "15th Ave NE & NE Campus Pkwy",
                47.656422,
                -122.312164));
        expected.add(new StopResponse(
                "1_10917",
                "15th Ave NE & NE 40th St",
                47.655048,
                -122.312195));

        assertEquals(expected, subject.getStops(coordinate,latSpan,lngSpan));
    }
}
