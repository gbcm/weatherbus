package io.pivotal.service.response;

import lombok.Data;

import java.util.List;

@Data
public class StopsCollectionResponse {
    private List<StopResponse> data;
}
