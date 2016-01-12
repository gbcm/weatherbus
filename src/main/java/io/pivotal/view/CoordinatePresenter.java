package io.pivotal.view;

import io.pivotal.model.Coordinate;
import lombok.Data;

@Data
public class CoordinatePresenter extends JsonPresenter {

    private final double longitude;
    private final double latitude;
    private final String stopId;

    public CoordinatePresenter(String stopId, Coordinate coordinate) {
        this.longitude = coordinate.getLongitude();
        this.latitude = coordinate.getLatitude();
        this.stopId = stopId;
    }
}
