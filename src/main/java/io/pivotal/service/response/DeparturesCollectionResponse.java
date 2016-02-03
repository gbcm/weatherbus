package io.pivotal.service.response;

import lombok.Data;

import java.util.List;

@Data
public class DeparturesCollectionResponse {
    private final List<DepartureResponse> data;
}
