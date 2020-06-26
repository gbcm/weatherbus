package io.pivotal.controller.v1;

import com.netflix.hystrix.exception.HystrixRuntimeException;
import io.pivotal.errorHandling.StopNotFoundException;
import io.pivotal.model.Coordinate;
import io.pivotal.model.DepartureWithTemperature;
import io.pivotal.service.BusService;
import io.pivotal.service.CrimeService;
import io.pivotal.service.WeatherService;
import io.pivotal.service.response.*;
import io.pivotal.view.WeatherBusPresenter;
import io.pivotal.view.v1.*;
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
    @Autowired
    private CrimeService crimeService;

    @RequestMapping(produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public
    @ResponseBody
    String getStopsForCoordinate(
            @RequestParam(name = "lat", required = false, defaultValue = "47.653435") double lat,
            @RequestParam(name = "lng", required = false, defaultValue = "-122.305641") double lng,
            @RequestParam(name = "latSpan", required = false, defaultValue = "0.01") double latSpan,
            @RequestParam(name = "lngSpan", required = false, defaultValue = "0.01") double lngSpan)
            throws UnknownServiceException {
        StopsCollectionResponse response = busService.getStops(new Coordinate(lat, lng), latSpan, lngSpan);
        List<StopPresenter> presenters = new ArrayList<>();

        for (StopResponse stop : response.getData()) {
            presenters.add(new StopPresenter(stop));
        }

        return new StopsCollectionPresenter(presenters, response.getIncluded()).toJson();
    }

    @RequestMapping(path = "/single/{stopId}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public
    @ResponseBody
    String getSingleStop(@PathVariable String stopId) throws UnknownServiceException, StopNotFoundException {
        SingleStopResponse response = busService.getStopInfo(stopId);
        return new SingleStopPresenter(new StopPresenter(response.getData()), response.getIncluded()).toJson();
    }

    @RequestMapping(path = "/crime", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public
    @ResponseBody
    CrimeResponse getNumberOfCrimes(@RequestParam String stopId) throws StopNotFoundException {
        Coordinate coordinate = busService.getCoordinates(stopId);
        CrimeDetail crimeDetail = crimeService.getCrimeInfo(coordinate.getLatitude(), coordinate.getLongitude());
        CrimeResponse response = new CrimeResponse();
        response.setData(crimeDetail);
        return response;
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
            e.printStackTrace();
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
