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

        public TimedTemp() {}
        public TimedTemp(long timeInSeconds, double temp, String climacon) {
            this.timeInSeconds = timeInSeconds;
            this.temp = temp;
            this.climacon = climacon;
        }

        @SerializedName("time_epoch")
        private long timeInSeconds;
        private double temp;
        private String climacon;
    }
}
