package io.pivotal.model;

import io.pivotal.service.response.DepartureResponse;
import lombok.Data;

import java.text.DecimalFormat;

@Data
public class DepartureWithTemperature extends DepartureResponse {
    private Double temp;
    private String climacon_url;
    private String climacon;

    public DepartureWithTemperature(String routeShortName, String headsign, long predictedTime, long scheduledTime, Double tempF, String climacon_url, String climacon) {
        super(routeShortName, headsign, predictedTime, scheduledTime);
        this.temp = fToC(tempF);
        this.climacon_url = climacon_url;
        this.climacon = climacon;
    }

    public DepartureWithTemperature(DepartureResponse departureResponse, Double temperature, String climacon_url, String climacon) {
        this(departureResponse.getRouteShortName(), departureResponse.getHeadsign(), departureResponse.getPredictedTime(), departureResponse.getScheduledTime(), temperature, climacon_url, climacon);
    }

    private Double fToC(Double f) {
        DecimalFormat df = new DecimalFormat("#.#");
        if (f == null) {
            return null;
        }
        return Double.parseDouble(df.format((f - 32) * 5 / 9));
    }
}
