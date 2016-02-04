package io.pivotal.service.response;

import lombok.Data;

@Data
public class StopResponse {
    private final String stopId;
    private final String name;
    private final double latitude;
    private final double longitude;
}
