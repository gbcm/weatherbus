package io.pivotal.service;

import io.pivotal.service.response.*;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

import java.util.List;

public interface IRetrofitBusService {
    @GET("/v1/departures")
    DeparturesCollectionResponse getDepartures(@Query("stopId") String stopId);

    @GET("/v1/stops/{stopId}")
    SingleStopResponse getStopForId(@Path("stopId") String stopId);

    @GET("/v1/stops")
    StopsCollectionResponse getStops(@Query("lat") double lat, @Query("lng") double lng,
                                     @Query("latSpan") double latSpan, @Query("lngSpan") double lngSpan);
}
