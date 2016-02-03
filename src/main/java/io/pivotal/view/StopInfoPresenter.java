package io.pivotal.view;

import io.pivotal.service.response.StopInfo;
import lombok.Data;

@Data
public class StopInfoPresenter extends JsonPresenter {
    private String id;
    private String name;
    private double latitude;
    private double longitude;

    public StopInfoPresenter(StopInfo stop) {
        id = stop.getId();
        name = stop.getName();
        latitude = stop.getLatitude();
        longitude = stop.getLongitude();
    }
}
