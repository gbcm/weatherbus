package io.pivotal.controller;

import io.pivotal.TestUtilities;
import io.pivotal.service.WeatherService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.NestedServletException;
import retrofit.RetrofitError;

import javax.servlet.ServletException;
import java.io.IOException;
import java.net.UnknownServiceException;
import java.util.Date;
import java.util.HashMap;

import static net.javacrumbs.jsonunit.spring.JsonUnitResultMatchers.json;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * Created by pivotal on 1/6/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class WeatherControllerTest {

    @Mock
    WeatherService weatherService;
    @InjectMocks
    WeatherController subject;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(subject).build();
    }

    @Test
    public void testGetCurrentTemp() throws Exception {
        when(weatherService.getCurrentTemp(47.6097, -122.3331)).thenReturn(36.2);
        mockMvc.perform(get("/")).andExpect(
                json().isEqualTo(TestUtilities.jsonFileToString("src/test/resources/output/CurrentTemp.json")));
    }

    @Test
    @Ignore
    public void testGetFutureTemp() throws Exception {
        HashMap<Date, Double> values = new HashMap<Date, Double>() {{
            put(new Date(1452222000L), 14.4);
            put(new Date(1452225600L), 15.5);
        }};

        when(weatherService.getFutureTemp(47.6097, -122.3331)).thenReturn(values);
        mockMvc.perform(get("/forecast")).andExpect(
                json().isEqualTo(TestUtilities.jsonFileToString("src/test/resources/output/FutureTemp.json")));
    }

    @Test(expected=ServletException.class)
    public void testGetCurrentTempNetworkFailure() throws Exception {
        when(weatherService.getCurrentTemp(47.6097, -122.3331)).thenThrow(RetrofitError.networkError("Whateva", new IOException()));
        mockMvc.perform(get("/"));
    }
}
