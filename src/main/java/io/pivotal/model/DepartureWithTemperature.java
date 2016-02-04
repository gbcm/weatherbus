package io.pivotal.model;

import io.pivotal.service.response.DepartureResponse;
import lombok.Data;

/**
 * Created by pivotal on 1/12/16.
 */
@Data
public class DepartureWithTemperature extends DepartureResponse {
    private double temp;

    public DepartureWithTemperature(String routeShortName, String headsign, long predictedTime, long scheduledTime, double temperature) {
        super(routeShortName, headsign, predictedTime, scheduledTime);
        this.temp = temperature;
    }

    public DepartureWithTemperature(DepartureResponse departureResponse, double temperature) {
        this(departureResponse.getRouteShortName(), departureResponse.getHeadsign(), departureResponse.getPredictedTime(), departureResponse.getScheduledTime(), temperature);
    }
}
