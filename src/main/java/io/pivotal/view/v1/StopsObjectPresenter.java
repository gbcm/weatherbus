package io.pivotal.view.v1;

import io.pivotal.view.JsonPresenter;
import io.pivotal.view.WeatherBusPresenter;
import lombok.Data;

@Data
public class StopsObjectPresenter extends JsonPresenter {
    private WeatherBusPresenter data;

    public StopsObjectPresenter(WeatherBusPresenter data) {
        this.data = data;
    }
}
