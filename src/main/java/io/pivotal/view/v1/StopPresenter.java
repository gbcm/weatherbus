package io.pivotal.view.v1;

import io.pivotal.service.response.StopResponse;
import lombok.Data;

@Data
public class StopPresenter {
    private String id;
    private String name;
    private double latitude;
    private double longitude;

    public StopPresenter(StopResponse stop){
        id = stop.getStopId();
        name = stop.getName();
        latitude = stop.getLatitude();
        longitude = stop.getLongitude();
    }
}
