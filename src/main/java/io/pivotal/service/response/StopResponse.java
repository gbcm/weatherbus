package io.pivotal.service.response;

import lombok.Data;

@Data
public class StopResponse {
    private String id;
    private String name;
    private double latitude;
    private double longitude;
}
