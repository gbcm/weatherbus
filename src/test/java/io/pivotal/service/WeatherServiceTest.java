package io.pivotal.service;

import io.pivotal.Constants;
import io.pivotal.TestUtilities;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by pivotal on 1/6/16.
 */
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

        when(mockClient.execute(any(Request.class)))
                .thenReturn(new Response("", 200, "", Collections.EMPTY_LIST, new TypedByteArray("application/json",
                        TestUtilities.jsonFileToString("src/test/resources/input/CurrentTemp.json").getBytes())));
        ReflectionTestUtils.setField(subject, "client", mockClient);

        assertEquals(51.4, subject.getCurrentTemp(latitude, longitude), 0);
    }

    @Test
    public void testGetFutureTemp() throws Exception {
        double latitude = 45.23;
        double longitude = -160.56;

        when(mockClient.execute(any(Request.class)))
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