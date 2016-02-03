package io.pivotal.service;

import io.pivotal.service.response.*;
import retrofit.http.GET;
import retrofit.http.Query;

import java.util.List;

public interface IRetrofitBusService {
    @GET("/v1/departures")
    List<DepartureResponse> getDepartures(@Query("stopId") String stopId);

    @GET("/v1/coordinates")
    CoordinatesResponse getCoordinates(@Query("stopId") String stopId);

    @GET("/v1/stops")
    List<StopResponse> getStops(@Query("lat") double lat, @Query("lng") double lng,
                                @Query("latSpan") double latSpan, @Query("lngSpan") double lngSpan);
}
