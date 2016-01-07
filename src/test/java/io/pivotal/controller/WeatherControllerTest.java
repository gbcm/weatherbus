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
    public void testRenderTemp() throws Exception {
        when(weatherService.getTemp(47.6097, -122.3331)).thenReturn(36.2);
        mockMvc.perform(get("/")).andExpect(
                json().isEqualTo(TestUtilities.jsonFileToString("src/test/resources/ExpectedTempRender.json")));
    }

    @Test(expected=ServletException.class)
    public void testRenderTempNetworkFailure() throws Exception {
        when(weatherService.getTemp(47.6097, -122.3331)).thenThrow(RetrofitError.networkError("Whateva", new IOException()));
        mockMvc.perform(get("/"));
    }
}
