package io.pivotal.service;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class ArrivalsAndDeparturesForStopResponse {
    @SerializedName("data")
    ArrivalsAndDeparturesResponseData data;
}

@Data
class ArrivalsAndDeparturesResponseData {
    @SerializedName("entry")
    ArrivalsAndDeparturesEntry entry;
}

@Data
class ArrivalsAndDeparturesEntry {
    @SerializedName("arrivalsAndDepartures")
    List<Departure> departures;
}
