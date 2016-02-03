package io.pivotal.view;

import io.pivotal.domain.BusStop;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class StopPresenter extends JsonPresenter {
    private final String id;
    private final String name;

    public StopPresenter(BusStop stop) {
        this.id = stop.getApiId();
        this.name = stop.getName();
    }
}
