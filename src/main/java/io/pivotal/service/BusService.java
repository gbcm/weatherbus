package io.pivotal.service;

import io.pivotal.errorHandling.StopNotFoundException;
import io.pivotal.model.Coordinate;
import io.pivotal.service.response.DepartureResponse;
import io.pivotal.service.response.SingleStopResponse;
import io.pivotal.service.response.StopResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import retrofit.RetrofitError;

import java.util.List;

@Component
public class BusService {
    private IFeignBusService busService;

    @Autowired
    public BusService(IFeignBusService busService) {
        this.busService = busService;
    }

    public List<DepartureResponse> getDepartures(String stopId) {
        return busService.getDepartures(stopId).getData();
    }

    public Coordinate getCoordinates(String stopId) throws StopNotFoundException {
        try {
            SingleStopResponse ssr = busService.getStopForId(stopId);
            return new Coordinate(ssr.getData().getLatitude(), ssr.getData().getLongitude());
        } catch (RetrofitError e) {
            throw new StopNotFoundException();
        }
    }

    public String getStopName(String stopId) throws StopNotFoundException {
        try {
            SingleStopResponse ssr = busService.getStopForId(stopId);
            return ssr.getData().getName();
        } catch (RetrofitError e) {
            throw new StopNotFoundException();
        }
    }

    public List<StopResponse> getStops(Coordinate coord,
                                       double latSpan,
                                       double lngSpan) {
        return busService.getStops(
                coord.getLatitude(),
                coord.getLongitude(),
                latSpan,
                lngSpan)
                .getData();
    }


}
