package io.pivotal.controller.v1;

import com.google.gson.Gson;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import io.pivotal.TestUtilities;
import io.pivotal.model.Coordinate;
import io.pivotal.service.BusService;
import io.pivotal.service.CrimeService;
import io.pivotal.service.WeatherService;
import io.pivotal.service.response.*;
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
import org.springframework.web.util.NestedServletException;

import java.util.ArrayList;
import java.util.List;

import static net.javacrumbs.jsonunit.spring.JsonUnitResultMatchers.json;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(MockitoJUnitRunner.class)
public class StopsControllerTest {
    @Mock
    BusService busService;
    @Mock
    WeatherService weatherService;
    @Mock
    CrimeService crimeService;
    @InjectMocks
    StopsController subject;

    @Rule
    public RestDocumentation restDocumentation = new RestDocumentation("build/generated-snippets");

    private MockMvc mockMvc;

    Gson gson = new Gson();

    String stopId = "1_75403";
    double latitude = 47.6098;
    double longitude = -122.3332;
    Coordinate coordinate = new Coordinate(latitude, longitude);

    private StopsCollectionResponse stopsCollectionResponse;

    @Before
    public void setup() throws Exception {
        List<DepartureResponse> departureResponses = new ArrayList<DepartureResponse>() {{
            add(new DepartureResponse("31", "CENTRAL MAGNOLIA FREMONT", 1896376792000L, 1896376792000L));
            add(new DepartureResponse("855", "Lynnwood", 0, 1896376812000L));
            add(new DepartureResponse("32", "SEATTLE CENTER FREMONT", 0, 1896376852000L));
        }};
        when(busService.getDepartures(stopId)).thenReturn(departureResponses);

        when(busService.getCoordinates(stopId)).thenReturn(coordinate);

        List<StopResponse> stops = new ArrayList<StopResponse>() {{
            add(new StopResponse("1_10914", "15th Ave NE & NE Campus Pkwy", 47.656422, -122.312164, "S", new ArrayList<String>() {{
                add("1_100223");
                add("40_100451");
                add("40_586");
            }}));
            add(new StopResponse("1_10917","15th Ave NE & NE 40th St",47.655048,-122.312195, "S", new ArrayList<String>() {{
                add("1_100140");
                add("1_100223");
                add("40_586");
            }}));
        }};

        stopsCollectionResponse = new StopsCollectionResponse(stops, new StopReferences(new ArrayList<StopReferences.RouteReference>() {{
            add(new StopReferences.RouteReference("1_100223","43", ""));
            add(new StopReferences.RouteReference("1_100140","25", ""));
            add(new StopReferences.RouteReference("40_100451","556", "Issaquah - Northgate"));
            add(new StopReferences.RouteReference("40_586","586", "Tacoma - U. District"));
        }}));

        this.mockMvc = MockMvcBuilders.standaloneSetup(subject)
                .apply(documentationConfiguration(this.restDocumentation).uris()
                    .withScheme("http")
                    .withHost("weatherbus-prime-dev.cfapps.io"))
                .build();
    }

    @Test
    public void testGetWeatherBus() throws Exception {
        ForecastResponse forecastResponse = gson.fromJson(
                TestUtilities.fixtureReader("WeatherServiceForecast"),
                ForecastResponse.class);
        TemperatureResponse temperatureResponse = gson.fromJson(
                TestUtilities.fixtureReader("WeatherServiceTemp"),
                TemperatureResponse.class);

        when(weatherService.getForecast(coordinate)).thenReturn(forecastResponse);
        when(weatherService.getTemperature(coordinate)).thenReturn(temperatureResponse);

        mockMvc.perform(get("/api/v1/stops/" + stopId))
                .andExpect(json().isEqualTo(TestUtilities.jsonFileToString("src/test/resources/v1/output/DeparturesResponse.json")))
                .andDo(document(
                        "stopsObject",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    public void testGetSingleStop() throws Exception {

        StopResponse stop = new StopResponse(stopId, "15th Ave NE & NE 40th St", 47.655048, -122.312195, "S", new ArrayList<String>() {{
            add("1_100140");
            add("29_880");
        }});

        when(busService.getStopInfo(stopId)).thenReturn(new SingleStopResponse(stop, new StopReferences(new ArrayList<StopReferences.RouteReference>() {{
            add(new StopReferences.RouteReference("1_100140","25", ""));
            add(new StopReferences.RouteReference("29_880","880", "Mukilteo - University District"));
        }})));

        mockMvc.perform(get("/api/v1/stops/single/" + stopId))
                .andExpect(json().isEqualTo(TestUtilities.jsonFileToString("src/test/resources/v1/output/SingleStopResponse.json")))
                .andDo(document(
                        "stopsObject",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    public void testGetWeatherBusMissingWeather() throws Exception {
        when(weatherService.getForecast(coordinate)).thenThrow(HystrixRuntimeException.class);
        when(weatherService.getTemperature(coordinate)).thenThrow(HystrixRuntimeException.class);

        mockMvc.perform(get("/api/v1/stops/" + stopId))
                .andExpect(json().isEqualTo(TestUtilities.jsonFileToString("src/test/resources/v1/output/StopsResponseNoTemperature.json")))
                .andDo(document(
                        "stopsObject",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test(expected = ArithmeticException.class)
    public void testGetWeatherBusRuntimeException() throws Throwable {
        when(weatherService.getForecast(coordinate)).thenThrow(ArithmeticException.class);
        when(weatherService.getTemperature(coordinate)).thenThrow(ArithmeticException.class);

        try {
            mockMvc.perform(get("/api/v1/stops/" + stopId));
        } catch (NestedServletException ex) {
            throw ex.getCause();
        }
    }

    @Test
    public void testGetStopsForCoordinate() throws Exception {
        double latitudeSpan = 0.01;
        double longitudeSpan = 0.01;
        when(busService.getStops(coordinate, latitudeSpan, longitudeSpan))
                .thenReturn(stopsCollectionResponse);

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
        when(busService.getStops(new Coordinate(latitude, longitude), latitudeSpan, longitudeSpan))
                .thenReturn(stopsCollectionResponse);

        mockMvc.perform(get("/api/v1/stops"))
                .andExpect(json().isEqualTo(TestUtilities.jsonFileToString("src/test/resources/v1/output/StopsCollectionResponse.json")))
                .andDo(document(
                        "stopsCollectionDefaultParams",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    public void testGetNumberOfCrimes() throws Exception {
        CrimeDetail response = new CrimeDetail("all", 8, "DISPUTE", new ArrayList<CrimeDetail>() {{
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

        when(crimeService.getCrimeInfo(coordinate.getLatitude(), coordinate.getLongitude())).thenReturn(response);

        mockMvc.perform(get("/api/v1/stops/crime?stopId=" + stopId))
                .andExpect(json().isEqualTo(TestUtilities.jsonFileToString("src/test/resources/v1/output/CrimeResponse.json")))
                .andDo(document(
                        "crime",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }
}