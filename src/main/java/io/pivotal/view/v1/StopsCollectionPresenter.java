package io.pivotal.view.v1;

import io.pivotal.view.JsonPresenter;
import lombok.Data;

import java.util.List;

@Data
public class StopsCollectionPresenter extends JsonPresenter {
    private List<StopPresenter> data;

    public StopsCollectionPresenter(List<StopPresenter> data) {
        this.data = data;
    }
}
