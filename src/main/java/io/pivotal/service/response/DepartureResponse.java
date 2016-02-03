package io.pivotal.service.response;

import lombok.Data;

@Data
public class DepartureResponse {
    String routeShortName;

    String headsign;

    long predictedTime;

    long scheduledTime;

    public DepartureResponse() {}

    public DepartureResponse(String routeShortName, String headsign, long predictedTime, long scheduledTime) {
        this.routeShortName = routeShortName;
        this.headsign = headsign;
        this.predictedTime = predictedTime;
        this.scheduledTime = scheduledTime;
    }
}
