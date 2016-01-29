package io.pivotal.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class StopInfo {
    String id;
    String name;

    @SerializedName("lat")
    double latitude;

    @SerializedName("lon")
    double longitude;
}
