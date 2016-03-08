package io.pivotal.view.v1;

import io.pivotal.service.response.StopReferences;
import io.pivotal.view.JsonPresenter;
import lombok.Data;

@Data
public class SingleStopPresenter extends JsonPresenter {
    final private StopPresenter data;
    final private StopReferences included;
}
