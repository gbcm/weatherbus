package io.pivotal.view;

import io.pivotal.model.DepartureWithTemperature;
import lombok.Data;

import java.util.List;

/**
 * Created by pivotal on 1/12/16.
 */
@Data
public class WeatherBusPresenter extends JsonPresenter {
    private double latitude;
    private double longitude;
    private String stopId;
    private List<DepartureWithTemperature> departures;

    public WeatherBusPresenter(double latitude, double longitude, String stopId, List<DepartureWithTemperature> departures) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.stopId = stopId;
        this.departures = departures;
    }
}
