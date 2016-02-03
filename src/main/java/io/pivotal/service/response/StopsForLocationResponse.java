package io.pivotal.service.response;

import lombok.Data;

import java.util.List;

@Data
public class StopsForLocationResponse {
    List<StopResponse> data;
}
