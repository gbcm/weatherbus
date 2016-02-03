package io.pivotal.view;

import io.pivotal.domain.BusStop;
import io.pivotal.service.response.StopResponse;
import lombok.Data;

@Data
public class StopPresenter extends JsonPresenter {
    private String id;
    private String name;

    public StopPresenter(BusStop stop) {
        id = stop.getApiId();
        name = stop.getName();
    }
}
