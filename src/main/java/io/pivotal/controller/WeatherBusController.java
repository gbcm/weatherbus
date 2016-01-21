package io.pivotal.controller;


import io.pivotal.model.Coordinate;
import io.pivotal.model.DepartureWithTemperature;
import io.pivotal.service.BusService;
import io.pivotal.service.Departure;
import io.pivotal.service.IWeatherService;
import io.pivotal.view.WeatherBusPresenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.Instant;
import java.util.*;

@Controller
@RequestMapping("/wb")
public class WeatherBusController {
    @Autowired
    private BusService busService;

    @Autowired
    private IWeatherService weatherService;

    @RequestMapping("")
    public @ResponseBody String getWeatherBus(@RequestParam String stopId) throws Exception {
        List<Departure> departures = busService.getDeparturesForStop(stopId);
        Coordinate coordinate = busService.getCoordinatesForStop(stopId);
//        SortedMap<Date, Double> forecast = new TreeMap<>(weatherService.getFutureTemp(coordinate));
        Map.Entry<Date, Double> tempNow = new AbstractMap.SimpleEntry<>(Date.from(Instant.now()), weatherService.getCurrentTemp(coordinate.getLatitude(), coordinate.getLongitude()));

        List<DepartureWithTemperature> dwt = new ArrayList<>();

        for (Departure departure : departures) {
            long departureTimeMs = departure.getPredictedTime();
            if (departureTimeMs == 0) {
                departureTimeMs = departure.getScheduledTime();
            }

//            Map.Entry<Date, Double> previousTemp = tempNow;
//            for (Map.Entry<Date, Double> temp : forecast.entrySet()) {
//                long tempTimeMs = temp.getKey().getTime();
//                if (tempTimeMs > departureTimeMs) {
//                    dwt.add(new DepartureWithTemperature(departure, previousTemp.getValue()));
//                    break;
//                }
//
//                previousTemp = temp;
//            }
        }

//        double lastTemp = forecast.get(forecast.lastKey());
        List<Departure> remainingDepartures = departures.subList(dwt.size(), departures.size());
//        for (Departure departure : remainingDepartures) {
//            dwt.add(new DepartureWithTemperature(departure, lastTemp));
//        }

        return new WeatherBusPresenter(coordinate.getLatitude(), coordinate.getLongitude(), stopId, dwt).toJson();
    }
}
