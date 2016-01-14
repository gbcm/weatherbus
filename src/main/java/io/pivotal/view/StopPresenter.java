package io.pivotal.view;

import lombok.Data;
import lombok.ToString;

/**
 * Created by pivotal on 1/14/16.
 */
@Data
@ToString
public class StopPresenter extends JsonPresenter {
    private final String id;

    public StopPresenter(String id) {
        this.id = id;
    }
}
