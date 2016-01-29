package io.pivotal.integration;

import io.pivotal.TestUtilities;
import io.pivotal.WeatherbusApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static net.javacrumbs.jsonunit.spring.JsonUnitResultMatchers.json;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@WebAppConfiguration
@SpringApplicationConfiguration(WeatherbusApplication.class)
public class SomeIntegrationTests {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    @Test
    public void testGetCoordinates() throws Exception {
        mockMvc.perform(get("/buses/coordinates?stopId=1_75403")).andExpect(
                json().isEqualTo(TestUtilities.jsonFileToString(
                        "src/test/resources/output/StopCoordinates.json")));
    }

    @Test
    public void testGetWB() throws Exception {
        mockMvc.perform(get("/wb?stopId=1_75403")).andExpect(
                json().isEqualTo(TestUtilities.jsonFileToString(
                        "src/test/resources/output/WeatherBusResponse.json")));
    }

    @Test
    public void testGetStopsForLocation() throws Exception {
        mockMvc.perform(get("/buses/stops?lat=47.653435&lng=122.305641&latSpan=0.01&lngSpan=0.01")).andExpect(
                json().isEqualTo(TestUtilities.jsonFileToString(
                        "src/test/resources/output/StopsForCoordinate.json")));
    }
}