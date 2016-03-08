package io.pivotal.service.response;

import lombok.Data;

@Data
public class SingleStopResponse {
    private StopResponse data;
    private StopReferences included;

    public SingleStopResponse() {

    }

    public SingleStopResponse(StopResponse data, StopReferences included) {
        this.data = data;
        this.included = included;
    }
}
