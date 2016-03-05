package io.pivotal.view.v1;

import io.pivotal.view.JsonPresenter;
import lombok.Data;

@Data
public class SingleStopPresenter extends JsonPresenter {
    final private StopPresenter data;
}
