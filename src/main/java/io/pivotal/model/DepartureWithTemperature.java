package io.pivotal.model;

import io.pivotal.service.response.DepartureResponse;
import lombok.Data;

@Data
public class DepartureWithTemperature extends DepartureResponse {
    private Double temp;
    private String climacon_url;
    private String climacon;

    public DepartureWithTemperature(String routeShortName, String headsign, long predictedTime, long scheduledTime, Double temperature, String climacon_url, String climacon) {
        super(routeShortName, headsign, predictedTime, scheduledTime);
        this.temp = temperature;
        this.climacon_url = climacon_url;
        this.climacon = climacon;
    }

    public DepartureWithTemperature(DepartureResponse departureResponse, Double temperature, String climacon_url, String climacon) {
        this(departureResponse.getRouteShortName(), departureResponse.getHeadsign(), departureResponse.getPredictedTime(), departureResponse.getScheduledTime(), temperature, climacon_url, climacon);
    }
}
