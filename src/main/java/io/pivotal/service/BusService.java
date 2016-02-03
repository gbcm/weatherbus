package io.pivotal.service;

import io.pivotal.errorHandling.StopNotFoundException;
import io.pivotal.model.Coordinate;
import io.pivotal.service.response.StopInfo;
import io.pivotal.service.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.UnknownServiceException;
import java.util.ArrayList;
import java.util.List;

@Component
public class BusService {
    private IRetrofitBusService busService;

    @Autowired
    public BusService(IRetrofitBusService busService) {
        this.busService = busService;
    }

    public List<DepartureResponse> getDepartures(String stopId) {
//        return busService.getDepartures(stopId);
        return null;
    }

    public Coordinate getCoordinates(String stopId) {
//        CoordinatesResponse coord =  busService.getCoordinates(stopId);
//        return new Coordinate(coord.getLatitude(), coord.getLongitude());
    return null;
    }

    public List<StopInfo> getStops(Coordinate coord,
                                       double latSpan,
                                       double lngSpan) {
        List<StopInfo> stops = new ArrayList<>();
//        for (StopResponse stop : busService.getStops(coord.getLatitude(),
//                coord.getLongitude(),
//                latSpan,
//                lngSpan)){
//            stops.add(new StopInfo(
//                    stop.getId(),
//                    stop.getName(),
//                    stop.getLatitude(),
//                    stop.getLongitude()));
//        }
        return stops;
    }

    public String getStopName(String stopId) throws UnknownServiceException, StopNotFoundException {
        try {
            StopResponse response = busService.getCoordinates(stopId);
            if (response == null || response.getData() == null) {
                throw new StopNotFoundException();
            }
            return response.getData().getEntry().getName();
        }
        catch (RetrofitError e) {
            e.printStackTrace();
            throw new UnknownServiceException(e.getMessage());
        }
    }
}
