package io.pivotal.service;

import com.google.gson.annotations.SerializedName;
import io.pivotal.model.Coordinate;
import lombok.Data;

@Data
public class StopResponse {
    public Coordinate getCoordinates() {
        StopEntry entry = getData().getEntry();
        return new Coordinate(entry.latitude, entry.longitude);
    }

    @SerializedName("data")
    StopData data;
}

@Data
class StopData {
    @SerializedName("entry")
    StopEntry entry;
}

@Data
class StopEntry {
    @SerializedName("lat")
    double latitude;

    @SerializedName("lon")
    double longitude;

    @SerializedName("name")
    String name;
}