package io.pivotal.service.response;

import com.google.gson.annotations.SerializedName;
import io.pivotal.model.StopInfo;
import lombok.Data;

import java.util.List;

@Data
public class StopsForLocationResponse {
    DataResponse data;

    @Data
    public class DataResponse {
        @SerializedName("list")
        List<StopInfo> stops;
    }
}
