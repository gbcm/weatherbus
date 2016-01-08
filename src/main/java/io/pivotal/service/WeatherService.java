package io.pivotal.service;

import io.pivotal.Constants;
import org.springframework.stereotype.Component;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.http.GET;
import retrofit.http.Path;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.net.UnknownServiceException;
import java.util.Date;
import java.util.Map;

/**
 * Created by pivotal on 1/6/16.
 */
@Component
public class WeatherService {

    OkClient client = new OkClient();
    IWundergroundService service = null;

    public double getCurrentTemp(double latitude, double longitude) throws UnknownServiceException {
        try {
            return getWundergroundService()
                    .getConditionsResponse(Double.toString(latitude), Double.toString(longitude))
                    .getTempF();
        }
        catch (RetrofitError e) {
            e.printStackTrace();
            throw new UnknownServiceException(e.getMessage());
        }
    }

    public Map<Date, Double> getFutureTemp(double latitude, double longitude) throws UnknownServiceException {
        try {
            return getWundergroundService()
                    .getForecastResponse(Double.toString(latitude), Double.toString(longitude))
                    .getTemps();
        } catch (RetrofitError e) {
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

        @GET("/api/" + Constants.WUNDERGROUND_API_KEY + "/hourly/q/{latitude},{longitude}.json")
        WeatherForecastResponse getForecastResponse(@Path("latitude") String latitude, @Path("longitude") String longitude);
    }
}
