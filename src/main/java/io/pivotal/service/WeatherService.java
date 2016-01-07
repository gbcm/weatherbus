package io.pivotal.service;

import org.springframework.stereotype.Component;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.http.GET;
import io.pivotal.Constants;
import retrofit.http.Path;

import java.net.UnknownServiceException;

/**
 * Created by pivotal on 1/6/16.
 */
@Component
public class WeatherService {

    OkClient client = new OkClient();
    IWundergroundService service = null;

    public double getTemp(double latitude, double longitude) throws UnknownServiceException {
        try {
            return getWundergroundService().getConditionsResponse(Double.toString(latitude), Double.toString(longitude)).getTempF();
        }
        catch (RetrofitError e) {
            e.printStackTrace();
            throw new UnknownServiceException(e.getMessage());
        }
    }

    private IWundergroundService getWundergroundService() {
        if (service == null) {
            RestAdapter.Builder builder = new RestAdapter.Builder().setEndpoint(Constants.WUNDERGROUND_ENDPOINT);

            builder.setClient(client);
            RestAdapter adapter = builder.build();
            service = adapter.create(IWundergroundService.class);
        }
        return service;
    }

    private interface IWundergroundService {
        @GET("/api/" + Constants.WUNDERGROUND_API_KEY + "/conditions/q/{latitude},{longitude}.json")
        WeatherConditionsResponse getConditionsResponse(@Path("latitude") String latitude, @Path("longitude") String longitude);
    }
}
