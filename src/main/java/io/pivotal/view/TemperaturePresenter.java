package io.pivotal.view;

import com.cedarsoftware.util.io.JsonWriter;
import lombok.Data;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pivotal on 1/6/16.
 */
@Data
@ToString
public class TemperaturePresenter {
    private final Double temp;
    private final Double latitude;
    private final Double longitude;

    public TemperaturePresenter(Double latitude, Double longitude, Double temp) {
        this.temp = temp;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String toJson() {
        Map options = new HashMap<String, Object>() {{
            put(JsonWriter.TYPE, false);
        }};

        return JsonWriter.objectToJson(this, options);
    }
}
