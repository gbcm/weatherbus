package io.pivotal.view.v1;

import io.pivotal.service.response.StopReferences;
import io.pivotal.view.JsonPresenter;
import lombok.Data;

import java.util.List;

@Data
public class StopsCollectionPresenter extends JsonPresenter {
    private List<StopPresenter> data;
    private StopReferences included;

    public StopsCollectionPresenter(List<StopPresenter> data, StopReferences included) {
        this.data = data;
        this.included = included;
    }
}
