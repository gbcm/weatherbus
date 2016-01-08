package io.pivotal.service;

import io.pivotal.Constants;
import io.pivotal.TestUtilities;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import retrofit.client.OkClient;
import retrofit.client.Request;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WeatherServiceTest {
    @Mock
    RestTemplate restTemplate = new RestTemplate();
    @Mock
    OkClient mockClient;
    @InjectMocks
    WeatherService subject;

    @Test
    public void testGetCurrentTemp() throws Exception {
        double latitude = 45.23;
        double longitude = -160.56;

        String url = String.format("%s/api/%s/conditions/q/%s,%s.json", Constants.WUNDERGROUND_ENDPOINT, Constants.WUNDERGROUND_API_KEY, latitude, longitude);
        ArgumentMatcher<Request> matchesRequestUrl = new TestUtilities.RequestWithUrl(url);

        when(mockClient.execute(argThat(matchesRequestUrl)))
                .thenReturn(new Response("", 200, "", Collections.EMPTY_LIST, new TypedByteArray("application/json",
                        TestUtilities.jsonFileToString("src/test/resources/input/CurrentTemp.json").getBytes())));
        ReflectionTestUtils.setField(subject, "client", mockClient);

        assertEquals(51.4, subject.getCurrentTemp(latitude, longitude), 0);
    }

    @Test
    public void testGetFutureTemp() throws Exception {
        double latitude = 45.23;
        double longitude = -160.56;

        String url = String.format("%s/api/%s/hourly/q/%s,%s.json", Constants.WUNDERGROUND_ENDPOINT, Constants.WUNDERGROUND_API_KEY, latitude, longitude);
        ArgumentMatcher<Request> matchesRequestUrl = new TestUtilities.RequestWithUrl(url);

        when(mockClient.execute(argThat(matchesRequestUrl)))
                .thenReturn(new Response("", 200, "", Collections.EMPTY_LIST, new TypedByteArray("application/json",
                        TestUtilities.jsonFileToString("src/test/resources/input/FutureTemp.json").getBytes())));

        ReflectionTestUtils.setField(subject, "client", mockClient);

        Map<Date, Double> expectedTemperatures = new HashMap<Date, Double>() {{
            put(new Date(1452211200L), 43.0);
            put(new Date(1452214800L), 42.0);
            put(new Date(1452218400L), 41.0);
        }};

        assertEquals(expectedTemperatures, subject.getFutureTemp(latitude, longitude));
    }
}