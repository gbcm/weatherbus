package io.pivotal.view;

import io.pivotal.service.Departure;
import lombok.Data;

@Data
public class DeparturePresenter extends JsonPresenter {
    private String routeShortName;
    private String tripHeadsign;
    private long predictedDepartureTime;
    private long scheduledDepartureTime;

    public DeparturePresenter(Departure departure) {
        routeShortName = departure.getRouteShortName();
        tripHeadsign = departure.getHeadsign();
        predictedDepartureTime = departure.getPredictedTime();
        scheduledDepartureTime = departure.getScheduledTime();
    }
}
