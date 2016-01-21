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
    public class TimedTemp {
        @SerializedName("time_epoch")
        private long timeInMillisec;
        private double temp;
    }
}
