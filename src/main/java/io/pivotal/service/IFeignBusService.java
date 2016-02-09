package io.pivotal.service;

import io.pivotal.service.response.DeparturesCollectionResponse;
import io.pivotal.service.response.SingleStopResponse;
import io.pivotal.service.response.StopsCollectionResponse;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("weatherbus-bus")
@RequestMapping("/api/v1")
public interface IFeignBusService {
    @RequestMapping(method = RequestMethod.GET, value = "/departures", consumes = "application/json")
    DeparturesCollectionResponse getDepartures(@RequestParam("stopId") String stopId);

    @RequestMapping(method = RequestMethod.GET, value = "/stops/{stopId}", consumes = "application/json")
    SingleStopResponse getStopForId(@PathVariable("stopId") String stopId);

    @RequestMapping(method = RequestMethod.GET, value = "/stops", consumes = "application/json")
    StopsCollectionResponse getStops(@RequestParam("lat") double lat, @RequestParam("lng") double lng,
                                     @RequestParam("latSpan") double latSpan, @RequestParam("lngSpan") double lngSpan);
}
