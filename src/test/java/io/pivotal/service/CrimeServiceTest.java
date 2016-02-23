package io.pivotal.service;

import com.google.gson.Gson;
import io.pivotal.TestUtilities;
import io.pivotal.service.response.CrimeInfo;
import io.pivotal.service.response.CrimeResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CrimeServiceTest {

    @Mock
    IFeignCrimeService service;

    @InjectMocks
    CrimeService subject;

    Gson gson = new Gson();

    @Test
    public void testGetCrimeInfo() throws Exception {
        CrimeResponse response = gson.fromJson(
                TestUtilities.fixtureReader("CrimeInfo"),
                CrimeResponse.class);
        when(service.getCrimeInfo(10,15)).thenReturn(response);

        CrimeInfo expected = new CrimeInfo(4, "CAR PROWL", 1);
        assertEquals(expected, subject.getCrimeInfo(10,15));
    }
}