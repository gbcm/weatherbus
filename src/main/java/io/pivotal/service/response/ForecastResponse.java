package io.pivotal.service.response;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class ForecastResponse {
    private double latitude;
    private double longitude;
    private List<TimedTemp> forecast;

    @Data
    public static class TimedTemp {
        @SerializedName("time_epoch")
        private final long timeInSeconds;
        private final double temp;
    }
}
