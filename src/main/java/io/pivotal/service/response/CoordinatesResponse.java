package io.pivotal.service.response;

import lombok.Data;

@Data
public class CoordinatesResponse {
    private double longitude;
    private double latitude;
    private String stopId;
}
