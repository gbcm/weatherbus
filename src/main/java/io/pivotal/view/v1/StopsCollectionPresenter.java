package io.pivotal.view.v1;

import io.pivotal.view.JsonListPresenter;
import io.pivotal.view.JsonPresenter;
import io.pivotal.view.StopInfoPresenter;
import lombok.Data;

import java.util.List;

@Data
public class StopsCollectionPresenter extends JsonPresenter {
    private List<StopInfoPresenter> data;

    public StopsCollectionPresenter(List<StopInfoPresenter> data) {
        this.data = data;
    }
}
