package io.pivotal.model;

import com.google.gson.annotations.SerializedName;
import io.pivotal.service.Departure;
import lombok.Data;

/**
 * Created by pivotal on 1/12/16.
 */
@Data
public class DepartureWithTemperature extends Departure {
    private double temp;

    public DepartureWithTemperature(String routeShortName, String headsign, long predictedTime, long scheduledTime, double temperature) {
        super(routeShortName, headsign, predictedTime, scheduledTime);
        this.temp = temperature;
    }

    public DepartureWithTemperature(Departure departure, double temperature) {
        this(departure.getRouteShortName(), departure.getHeadsign(), departure.getPredictedTime(), departure.getScheduledTime(), temperature);
    }
}
