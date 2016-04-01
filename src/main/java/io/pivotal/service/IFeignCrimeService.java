package io.pivotal.service;

import io.pivotal.service.response.CrimeResponse;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("weatherbus-crime")
@RequestMapping("/api")
public interface IFeignCrimeService {
    @RequestMapping(method = RequestMethod.GET, value = "/detail", consumes = "application/json")
    CrimeResponse getCrimeInfo(@RequestParam("lat") double latitude, @RequestParam("lng") double longitude);
}
