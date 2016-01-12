package io.pivotal.service;

import io.pivotal.Constants;
import io.pivotal.TestUtilities;
import io.pivotal.model.Coordinate;
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
    OkClient mockClient;
    @InjectMocks
    BusService subject;

    @Test
    public void testGetDeparturesForStop() throws Exception {
        String url = String.format("%s/api/where/arrivals-and-departures-for-stop/12345.json" +
                "?key=%s&minutesBefore=15&minutesAfter=45", Constants.ONEBUSAWAY_ENDPOINT,
                Constants.ONEBUSAWAY_KEY);
        ArgumentMatcher<Request> matchesRequestUrl = new TestUtilities.RequestWithUrl(url);

        when(mockClient.execute(argThat(matchesRequestUrl)))
                .thenReturn(new Response("", 200, "", Collections.EMPTY_LIST,
                        new TypedByteArray("application/json",
                                TestUtilities.jsonFileToString("src/test/resources/input/DeparturesForStop.json").getBytes())));
        ReflectionTestUtils.setField(subject, "client", mockClient);

        List<Departure> expectedDepartures = new ArrayList<Departure>() {{
            add(new Departure("31", "CENTRAL MAGNOLIA FREMONT", 1452550769000L, 1452550571000L));
            add(new Departure("855", "Lynnwood", 0, 1452551256000L));
            add(new Departure("32", "SEATTLE CENTER FREMONT", 0, 1452554291000L));
        }};

        assertEquals(expectedDepartures, subject.getDeparturesForStop("12345"));
        verify(mockClient).execute(argThat(matchesRequestUrl));
    }

    @Test
    public void testGetCoordinatesForStop() throws Exception {
        String url = String.format("%s/api/where/stop/1_75403.json?key=%s",
                Constants.ONEBUSAWAY_ENDPOINT, Constants.ONEBUSAWAY_KEY);
        ArgumentMatcher<Request> matchesRequestUrl = new TestUtilities.RequestWithUrl(url);

        when(mockClient.execute(argThat(matchesRequestUrl)))
                .thenReturn(new Response("", 200, "", Collections.EMPTY_LIST,
                        new TypedByteArray("application/json",
                                TestUtilities.jsonFileToString("src/test/resources/input/StopInfo.json").getBytes())));
        ReflectionTestUtils.setField(subject, "client", mockClient);

        Coordinate expectedCoordinate = new Coordinate(47.654365, -122.305214);
        Coordinate coordinate = subject.getCoordinatesForStop("1_75403");

        assertEquals(expectedCoordinate.getLatitude(), coordinate.getLatitude(), 0);
        assertEquals(expectedCoordinate.getLongitude(), coordinate.getLongitude(), 0);

        verify(mockClient).execute(argThat(matchesRequestUrl));
    }
}
