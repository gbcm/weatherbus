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

import java.util.ArrayList;
import java.util.List;

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
        List<CrimeInfo.Offense> offenses = new ArrayList<>();

        offenses.add(new CrimeInfo.Offense("CAR PROWL", 2));
        offenses.add(new CrimeInfo.Offense("STOLEN PROPERTY", 1));
        offenses.add(new CrimeInfo.Offense("RECKLESS BURNING", 1));

        CrimeResponse response = gson.fromJson(
                TestUtilities.fixtureReader("CrimeInfo"),
                CrimeResponse.class);
        when(service.getCrimeInfo(10,15)).thenReturn(response);

        CrimeInfo expected = new CrimeInfo(4, "CAR PROWL", 1, offenses);
        assertEquals(expected, subject.getCrimeInfo(10,15));
    }
}