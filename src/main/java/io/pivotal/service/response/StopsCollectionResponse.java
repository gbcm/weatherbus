package io.pivotal.service.response;

import lombok.Data;

import java.util.List;

@Data
public class StopsCollectionResponse {
    private final List<StopResponse> data;
}
