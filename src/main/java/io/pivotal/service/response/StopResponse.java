package io.pivotal.service.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
public class StopResponse {
    private String stopId;
    private String name;
    private double latitude;
    private double longitude;
    private String direction;
    private List<String> routeIds;

    public StopResponse() {}

    public StopResponse(String stopId, String name, double latitude, double longitude, String direction, List<String> routeIds) {
        this.stopId = stopId;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.direction = direction;
        this.routeIds = routeIds;
    }
}
