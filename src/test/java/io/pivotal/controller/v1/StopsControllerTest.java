package io.pivotal.controller.v1;

import com.google.gson.Gson;
import io.pivotal.TestUtilities;
import io.pivotal.model.Coordinate;
import io.pivotal.service.response.StopInfo;
import io.pivotal.service.BusService;
import io.pivotal.service.response.DepartureResponse;
import io.pivotal.service.WeatherService;
import io.pivotal.service.response.ForecastResponse;
import io.pivotal.service.response.TemperatureResponse;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.restdocs.RestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static net.javacrumbs.jsonunit.spring.JsonUnitResultMatchers.json;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(MockitoJUnitRunner.class)
public class StopsControllerTest {
    @Mock
    BusService busService;
    @Mock
    WeatherService weatherService;
    @InjectMocks
    StopsController subject;

    @Rule
    public RestDocumentation restDocumentation = new RestDocumentation("build/generated-snippets");

    private MockMvc mockMvc;

    Gson gson = new Gson();

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(subject)
                .apply(documentationConfiguration(this.restDocumentation))
                .build();
    }

    @Test
    public void testGetWeatherBus() throws Exception {
        String stopId = "1_75403";
        double latitude = 47.6098;
        double longitude = -122.3332;
        Coordinate coordinate = new Coordinate(latitude, longitude);

        List<DepartureResponse> departureResponses = new ArrayList<DepartureResponse>() {{
            add(new DepartureResponse("31", "CENTRAL MAGNOLIA FREMONT", 1453317145000L, 1453317145000L));
            add(new DepartureResponse("855", "Lynnwood", 0, 1516561850000L));
            add(new DepartureResponse("32", "SEATTLE CENTER FREMONT", 1516563660000L, 1516563660000L));
        }};

        ForecastResponse forecastResponse = gson.fromJson(
                TestUtilities.fixtureReader("WeatherServiceForecast"),
                ForecastResponse.class);
        TemperatureResponse temperatureResponse = gson.fromJson(
                TestUtilities.fixtureReader("WeatherServiceTemp"),
                TemperatureResponse.class);

        when(busService.getDeparturesForStop(stopId)).thenReturn(departureResponses);
        when(busService.getCoordinatesForStop(stopId)).thenReturn(coordinate);
        when(weatherService.getForecast(coordinate)).thenReturn(forecastResponse);
        when(weatherService.getTemperature(coordinate)).thenReturn(temperatureResponse);

        mockMvc.perform(get("/api/v1/stops/" + stopId))
                .andExpect(json().isEqualTo(TestUtilities.jsonFileToString("src/test/resources/v1/output/StopsObjectResponse.json")))
                .andDo(document(
                        "stopsObject",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    public void testGetStopsForCoordinate() throws Exception {
        double latitude = 47.653435;
        double longitude = -122.305641;
        double latitudeSpan = 0.01;
        double longitudeSpan = 0.01;
        List<StopInfo> stops = new ArrayList<StopInfo>() {{
            add(new StopInfo());
            get(0).setId("1_10914");
            get(0).setLatitude(47.656422);
            get(0).setLongitude(-122.312164);
            get(0).setName("15th Ave NE & NE Campus Pkwy");
            add(new StopInfo());
            get(1).setId("1_10917");
            get(1).setLatitude(47.655048);
            get(1).setLongitude(-122.312195);
            get(1).setName("15th Ave NE & NE 40th St");
        }};
        when(busService.getStopsForCoordinate(new Coordinate(latitude, longitude), latitudeSpan, longitudeSpan))
                .thenReturn(stops);

        mockMvc.perform(get("/api/v1/stops?lat=" + latitude + "&lng=" + longitude + "&latSpan=" + latitudeSpan + "&lngSpan=" + longitudeSpan))
                .andExpect(json().isEqualTo(TestUtilities.jsonFileToString("src/test/resources/v1/output/StopsCollectionResponse.json")))
                .andDo(document(
                        "stopsCollection",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    public void testGetStopsForCoordinateWithNoParams() throws Exception {
        double latitude = 47.653435;
        double longitude = -122.305641;
        double latitudeSpan = 0.01;
        double longitudeSpan = 0.01;
        List<StopInfo> stops = new ArrayList<StopInfo>() {{
            add(new StopInfo());
            get(0).setId("1_10914");
            get(0).setLatitude(47.656422);
            get(0).setLongitude(-122.312164);
            get(0).setName("15th Ave NE & NE Campus Pkwy");
            add(new StopInfo());
            get(1).setId("1_10917");
            get(1).setLatitude(47.655048);
            get(1).setLongitude(-122.312195);
            get(1).setName("15th Ave NE & NE 40th St");
        }};
        when(busService.getStopsForCoordinate(new Coordinate(latitude, longitude), latitudeSpan, longitudeSpan))
                .thenReturn(stops);

        mockMvc.perform(get("/api/v1/stops"))
                .andExpect(json().isEqualTo(TestUtilities.jsonFileToString("src/test/resources/v1/output/StopsCollectionResponse.json")))
                .andDo(document(
                        "stopsCollectionDefaultParams",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }
}