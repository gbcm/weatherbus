package io.pivotal.service;

import io.pivotal.Constants;
import io.pivotal.service.response.StopsForLocationResponse;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

public interface IOneBusAwayService {
    @GET("/api/where/arrivals-and-departures-for-stop/{stop}.json?key=" +
            Constants.ONEBUSAWAY_KEY + "&minutesBefore=15&minutesAfter=45")
    ArrivalsAndDeparturesForStopResponse getDeparturesForStop(@Path("stop") String stopId);

    @GET("/api/where/stop/{stop}.json?key=" + Constants.ONEBUSAWAY_KEY)
    StopResponse getCoordinatesForStop(@Path("stop") String stopId);

    @GET("/api/where/stops-for-location.json?key=" + Constants.ONEBUSAWAY_KEY)
    StopsForLocationResponse getStopsForLocation(@Query("lat") double lat, @Query("lon") double lng,
                                                 @Query("latSpan") double latSpan, @Query("lonSpan") double lngSpan);
}