package io.pivotal.view;

import io.pivotal.service.Departure;
import lombok.Data;

@Data
public class DeparturePresenter extends JsonPresenter {
    private String routeShortName;
    private String headsign;
    private long predictedTime;
    private long scheduledTime;

    public DeparturePresenter(Departure departure) {
        routeShortName = departure.getRouteShortName();
        headsign = departure.getHeadsign();
        predictedTime = departure.getPredictedTime();
        scheduledTime = departure.getScheduledTime();
    }
}
