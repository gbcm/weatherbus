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

import java.util.Collections;

import static org.junit.Assert.assertEquals;
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
    public void testGetTemp() throws Exception {
        double latitude = 45.23;
        double longitude = -160.56;
        String request = String.format("%s/api/%s/conditions/q/%s,%s.json", Constants.WUNDERGROUND_ENDPOINT, Constants.WUNDERGROUND_API_KEY, latitude, longitude);
        when(mockClient.execute(any(Request.class)))
                .thenReturn(new Response(request, 200, "", Collections.EMPTY_LIST, new TypedByteArray("application/json",
                        TestUtilities.jsonFileToString("src/test/resources/TestForecast.json").getBytes())));
        ReflectionTestUtils.setField(subject, "client", mockClient);

        assertEquals(51.4, subject.getTemp(latitude, longitude), 0);
    }
}