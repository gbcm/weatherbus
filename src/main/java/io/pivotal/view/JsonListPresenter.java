package io.pivotal.view;


import com.cedarsoftware.util.io.JsonWriter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonListPresenter {
    private List<JsonPresenter> presenters;

    public JsonListPresenter(List<JsonPresenter> presenters) {
        this.presenters = presenters;
    }

    public String toJson() {
        Map options = new HashMap<String, Object>() {{
            put(JsonWriter.TYPE, false);
        }};

        return JsonWriter.objectToJson(presenters, options);
    }
}
