package io.pivotal.service.response;

import lombok.Data;

import java.util.List;

@Data
public class StopsCollectionResponse {
    private List<StopResponse> data;
    private StopReferences included;

    public StopsCollectionResponse() {

    }

    public StopsCollectionResponse(List<StopResponse> data, StopReferences included) {
        this.data = data;
        this.included = included;
    }
}
