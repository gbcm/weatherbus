package io.pivotal.service.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class StopResponse {
    private String stopId;
    private String name;
    private double latitude;
    private double longitude;
    private String direction;

    public StopResponse() {}

    public StopResponse(String stopId, String name, double latitude, double longitude, String direction) {
        this.stopId = stopId;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.direction = direction;
    }
}
