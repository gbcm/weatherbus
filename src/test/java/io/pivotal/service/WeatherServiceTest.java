package io.pivotal.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import retrofit.RetrofitError;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WeatherServiceTest {
    @Mock
    IFeignWeatherService mockService;
    WeatherService subject;

    @Before
    public void beforeEach() {
        subject = new WeatherService(mockService);
    }

    @Test
    public void testGetForecastWrapsErrorsWithServiceCallName() {
        when(mockService.getForecast(any(Double.class), any(Double.class))).thenThrow(RetrofitError.class);
    }
}
