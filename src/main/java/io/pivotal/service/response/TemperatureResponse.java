package io.pivotal.service.response;

import lombok.Data;

@Data
public class TemperatureResponse {
    private double temp;
    private String climacon_url;
    private String climacon;
    private double latitude;
    private double longitude;
}
