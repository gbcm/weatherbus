package io.pivotal.controller;

import io.pivotal.model.Coordinate;
import io.pivotal.service.BusService;
import io.pivotal.service.Departure;
import io.pivotal.view.CoordinatePresenter;
import io.pivotal.view.DeparturePresenter;
import io.pivotal.view.JsonListPresenter;
import io.pivotal.view.JsonPresenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
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
}
