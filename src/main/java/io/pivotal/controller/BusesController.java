package io.pivotal.controller;

import io.pivotal.model.Coordinate;
import io.pivotal.model.StopInfo;
import io.pivotal.service.BusService;
import io.pivotal.service.Departure;
import io.pivotal.view.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/buses")
@Controller
public class BusesController {
    @Autowired
    private BusService busService;

    @RequestMapping(path = "departures", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public @ResponseBody String getDepartures(@RequestParam String stopId) throws Exception {
        List<Departure> departures = busService.getDeparturesForStop(stopId);
        List<JsonPresenter> presenters = new ArrayList<>();

        for (Departure d: departures) {
            presenters.add(new DeparturePresenter(d));
        }

        return new JsonListPresenter(presenters).toJson();
    }

    @RequestMapping(path = "coordinates", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public @ResponseBody String getCoordinates(@RequestParam String stopId) throws Exception {
        Coordinate coordinate = busService.getCoordinatesForStop(stopId);
        JsonPresenter presenter = new CoordinatePresenter(stopId, coordinate);
        return presenter.toJson();
    }

    @RequestMapping(path = "stops", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public @ResponseBody String getStopsForCoordinate(@RequestParam double lat, @RequestParam double lng,
                                              @RequestParam double latSpan, @RequestParam double lngSpan) throws Exception {
        List<StopInfo> stops = busService.getStopsForCoordinate(new Coordinate(lat,lng), latSpan, lngSpan);
        List<JsonPresenter> presenters = new ArrayList<>();

        for (StopInfo stop: stops) {
            presenters.add(new StopInfoPresenter(stop));
        }

        return new JsonListPresenter(presenters).toJson();
    }
}
