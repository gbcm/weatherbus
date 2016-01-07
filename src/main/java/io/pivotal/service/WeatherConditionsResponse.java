package io.pivotal.service;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * Created by pivotal on 1/6/16.
 */
@Data
public class WeatherConditionsResponse {
    public double getTempF() {
        return this.getCurrentObservation().getTempF();
    }

    @Data
    public class CurrentObservation {
        @SerializedName("temp_f")
        private double tempF;
    }

    @SerializedName("current_observation")
    private CurrentObservation currentObservation;
}
