package io.pivotal.view;

/**
 * Created by pivotal on 1/6/16.
 */
public class TemperaturePresenter {
    private final Double temp;
    private final Double latitude;
    private final Double longitude;

    public TemperaturePresenter(Double temp, Double latitude, Double longitude) {
        this.temp = temp;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "6";
    }
}
