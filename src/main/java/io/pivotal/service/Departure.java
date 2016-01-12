package io.pivotal.service;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Departure {
    @SerializedName("routeShortName")
    String routeShortName;

    @SerializedName("tripHeadsign")
    String headsign;

    @SerializedName("predictedDepartureTime")
    long predictedTime;

    @SerializedName("scheduledDepartureTime")
    long scheduledTime;

    public Departure() {}

    public Departure(String routeShortName, String headsign, long predictedTime, long scheduledTime) {
        this.routeShortName = routeShortName;
        this.headsign = headsign;
        this.predictedTime = predictedTime;
        this.scheduledTime = scheduledTime;
    }
}
