package io.pivotal.service;

import io.pivotal.model.Coordinate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import retrofit.RetrofitError;

import java.net.UnknownServiceException;
import java.util.List;

@Component
public class BusService {
    @Autowired
    IOneBusAwayService service;

    public List<Departure> getDeparturesForStop(String stopId) throws UnknownServiceException {
        try {
            ArrivalsAndDeparturesForStopResponse response = service
                    .getDeparturesForStop(stopId);
            return response.getData().getEntry().getDepartures();
        }
        catch (RetrofitError e) {
            e.printStackTrace();
            throw new UnknownServiceException(e.getMessage());
        }
    }

    public Coordinate getCoordinatesForStop(String stopId) throws UnknownServiceException {
        try {
            StopResponse response = service
                    .getCoordinatesForStop(stopId);
            return response.getCoordinates();
        }
        catch (RetrofitError e) {
            e.printStackTrace();
            throw new UnknownServiceException(e.getMessage());
        }
    }
}
