package io.pivotal.service.response;

import lombok.Data;

@Data
public class TemperatureResponse {
    private double temp;
    private double latitude;
    private double longitude;
}
