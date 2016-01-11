package io.pivotal.view;

import com.cedarsoftware.util.io.JsonWriter;
import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pivotal on 1/8/16.
 */
public class ForecastPresenter extends JsonPresenter{

    private final double latitude;
    private final double longitude;
    private final List<Forecast> forecast;

    public ForecastPresenter(double latitude, double longitude, List<Forecast> forecast) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.forecast = forecast;
    }

    public static class Forecast {
        private final long time_epoch;
        private final double temp;

        public Forecast(long time_epoch, double temp) {
            this.time_epoch = time_epoch;
            this.temp = temp;
        }
    }
}
