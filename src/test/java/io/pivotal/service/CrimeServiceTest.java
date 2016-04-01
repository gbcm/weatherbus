package io.pivotal.service;

import com.google.gson.Gson;
import io.pivotal.TestUtilities;
import io.pivotal.service.response.CrimeDetail;
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

    private Gson gson = new Gson();

    @Test
    public void testGetCrimeInfo() throws Exception {
        CrimeResponse response = gson.fromJson(
                TestUtilities.fixtureReader("CrimeDetail"),
                CrimeResponse.class);

        CrimeDetail expected = new CrimeDetail("all", 8, "DISPUTE", new ArrayList<CrimeDetail>() {{
            add(new CrimeDetail("violent", 3, "ASSAULT", new ArrayList<CrimeDetail>() {{
                add(new CrimeDetail("HOMICIDE", 1, null, null));
                add(new CrimeDetail("ASSAULT", 2, null, null));
            }}));
            add(new CrimeDetail("regular", 1, "PICKPOCKET", new ArrayList<CrimeDetail>() {{
                add(new CrimeDetail("PICKPOCKET", 1, null, null));
            }}));
            add(new CrimeDetail("mild", 4, "DISPUTE", new ArrayList<CrimeDetail>() {{
                add(new CrimeDetail("DISPUTE", 4, null, null));
            }}));
        }});

        when(service.getCrimeInfo(10,15)).thenReturn(response);

        assertEquals(expected, subject.getCrimeInfo(10,15));
    }
}