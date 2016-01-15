package io.pivotal.service;

import com.google.gson.Gson;
import io.pivotal.Constants;
import io.pivotal.TestUtilities;
import io.pivotal.model.Coordinate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;
import retrofit.client.OkClient;
import retrofit.client.Request;
import org.mockito.runners.MockitoJUnitRunner;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;
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
                new FileReader("src/test/resources/input/DeparturesForStop.json"),
                ArrivalsAndDeparturesForStopResponse.class);
        when(mockService.getDeparturesForStop(stopId)).thenReturn(response);

        List<Departure> expectedDepartures = new ArrayList<Departure>() {{
            add(new Departure("31", "CENTRAL MAGNOLIA FREMONT", 1452550769000L, 1452550571000L));
            add(new Departure("855", "Lynnwood", 0, 1452551256000L));
            add(new Departure("32", "SEATTLE CENTER FREMONT", 0, 1452554291000L));
        }};

        assertEquals(expectedDepartures, subject.getDeparturesForStop("12345"));
    }

    @Test
    public void testGetCoordinatesForStop() throws Exception {
        final String stopId = "1_75403";

        StopResponse stopResponse = gson.fromJson(
                new FileReader("src/test/resources/input/StopInfo.json"),
                StopResponse.class);
        when(mockService.getCoordinatesForStop(stopId)).thenReturn(stopResponse);

        Coordinate expectedCoordinate = new Coordinate(47.654365, -122.305214);
        Coordinate coordinate = subject.getCoordinatesForStop(stopId);

        assertEquals(expectedCoordinate.getLatitude(), coordinate.getLatitude(), 0);
        assertEquals(expectedCoordinate.getLongitude(), coordinate.getLongitude(), 0);
    }
}
