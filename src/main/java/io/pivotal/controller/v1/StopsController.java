package io.pivotal.controller.v1;

import com.netflix.hystrix.exception.HystrixRuntimeException;
import io.pivotal.errorHandling.StopNotFoundException;
import io.pivotal.model.Coordinate;
import io.pivotal.model.DepartureWithTemperature;
import io.pivotal.service.BusService;
import io.pivotal.service.WeatherService;
import io.pivotal.service.response.DepartureResponse;
import io.pivotal.service.response.ForecastResponse;
import io.pivotal.service.response.StopResponse;
import io.pivotal.service.response.TemperatureResponse;
import io.pivotal.view.WeatherBusPresenter;
import io.pivotal.view.v1.StopPresenter;
import io.pivotal.view.v1.StopsCollectionPresenter;
import io.pivotal.view.v1.StopsObjectPresenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.UnknownServiceException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/api/v1/stops")
public class StopsController {

    @Autowired
    private BusService busService;
    @Autowired
    private WeatherService weatherService;

    @RequestMapping(produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public
    @ResponseBody
    String getStopsForCoordinate(
            @RequestParam(name = "lat", required = false, defaultValue = "47.653435") double lat,
            @RequestParam(name = "lng", required = false, defaultValue = "-122.305641") double lng,
            @RequestParam(name = "latSpan", required = false, defaultValue = "0.01") double latSpan,
            @RequestParam(name = "lngSpan", required = false, defaultValue = "0.01") double lngSpan)
            throws UnknownServiceException {
        List<StopResponse> stops = busService.getStops(new Coordinate(lat, lng), latSpan, lngSpan);
        List<StopPresenter> presenters = new ArrayList<>();

        for (StopResponse stop : stops) {
            presenters.add(new StopPresenter(stop));
        }

        return new StopsCollectionPresenter(presenters).toJson();
    }

    @RequestMapping(path = "/{stopId}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public
    @ResponseBody
    String getWeatherBus(@PathVariable String stopId) throws UnknownServiceException, StopNotFoundException {
        List<DepartureResponse> departureResponses = busService.getDepartures(stopId);
        Coordinate coordinate = busService.getCoordinates(stopId);

        TemperatureResponse temperatureResponse = null;
        ForecastResponse forecastResponse = null;
        try {
            temperatureResponse = weatherService.getTemperature(coordinate);
            forecastResponse = weatherService.getForecast(coordinate);
        } catch (HystrixRuntimeException e) {
        }

        List<DepartureWithTemperature> dwt = getDepartureWithTemperatures(departureResponses, temperatureResponse, forecastResponse);
        return new StopsObjectPresenter(
                new WeatherBusPresenter(coordinate.getLatitude(),
                coordinate.getLongitude(),
                stopId,
                dwt)
        ).toJson();
    }

    private List<DepartureWithTemperature> getDepartureWithTemperatures(
            List<DepartureResponse> departureResponses,
            TemperatureResponse temperatureResponse,
            ForecastResponse forecastResponse)
    {
        List<ForecastResponse.TimedTemp> timedTemps = new ArrayList<>();
        if (forecastResponse != null) {
            timedTemps = forecastResponse.getForecast();
            timedTemps.sort((o1, o2) -> (int) (o1.getTimeInSeconds() - o2.getTimeInSeconds()));
        }

        final ForecastResponse.TimedTemp currentTimedTemp;
        if (temperatureResponse == null) {
            currentTimedTemp = null;
        } else {
            currentTimedTemp = new ForecastResponse.TimedTemp(
                    Math.floorDiv((new Date()).getTime(), 1000),
                    temperatureResponse.getTemp(),
                    temperatureResponse.getClimacon_url(),
                    temperatureResponse.getClimacon());
        }

        List<DepartureWithTemperature> dwt = new ArrayList<>();
        for (DepartureResponse departure : departureResponses) {
            ForecastResponse.TimedTemp lastTimedTemp = currentTimedTemp;
            boolean foundForecast = false;
            for (ForecastResponse.TimedTemp timedTemp : timedTemps) {
                long departureTime = Math.floorDiv(departure.getPredictedTime(), 1000);
                if (departureTime == 0) {
                    departureTime = Math.floorDiv(departure.getScheduledTime(), 1000);
                }
                if (timedTemp.getTimeInSeconds() > departureTime) {
                    dwt.add(new DepartureWithTemperature(
                            departure,
                            lastTimedTemp.getTemp(),
                            lastTimedTemp.getClimacon_url(),
                            lastTimedTemp.getClimacon()
                    ));
                    foundForecast = true;
                    break;
                }
                lastTimedTemp = timedTemp;
            }

            if (!foundForecast) {
                if (lastTimedTemp != null) {
                    dwt.add(new DepartureWithTemperature(
                            departure,
                            lastTimedTemp.getTemp(),
                            lastTimedTemp.getClimacon_url(),
                            lastTimedTemp.getClimacon()
                    ));
                } else {
                    dwt.add(new DepartureWithTemperature(departure, null, null, null));
                }
            }
        }
        return dwt;
    }
}
