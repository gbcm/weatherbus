package io.pivotal.service;

import io.pivotal.Constants;
import io.pivotal.model.Coordinate;
import org.springframework.stereotype.Component;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.http.GET;
import retrofit.http.Path;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.net.UnknownServiceException;
import java.util.List;
import java.util.Map;

@Component
public class BusService {
    OkClient client = new OkClient();
    IOneBusAwayService service = null;

    public List<Departure> getDeparturesForStop(String stopId) throws UnknownServiceException {
        try {
            ArrivalsAndDeparturesForStopResponse response = getOneBusAwayService()
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
            StopResponse response = getOneBusAwayService()
                    .getCoordinatesForStop(stopId);
            return response.getCoordinates();
        }
        catch (RetrofitError e) {
            e.printStackTrace();
            throw new UnknownServiceException(e.getMessage());
        }
    }

    private IOneBusAwayService getOneBusAwayService() {
        if (service == null) {
            RestAdapter.Builder builder = new RestAdapter.Builder().setEndpoint(Constants.ONEBUSAWAY_ENDPOINT);
            builder.setClient(client);
            RestAdapter adapter = builder.build();
            service = adapter.create(IOneBusAwayService.class);
        }

        return service;
    }

    private interface IOneBusAwayService {
        @GET("/api/where/arrivals-and-departures-for-stop/{stop}.json?key=" +
                Constants.ONEBUSAWAY_KEY + "&minutesBefore=15&minutesAfter=45")
        ArrivalsAndDeparturesForStopResponse getDeparturesForStop(@Path("stop") String stopId);

        @GET("/api/where/stop/{stop}.json?key=" + Constants.ONEBUSAWAY_KEY)
        StopResponse getCoordinatesForStop(@Path("stop") String stopId);
    }
}
