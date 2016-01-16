package io.pivotal.domain;

import org.springframework.data.repository.CrudRepository;

/**
 * Created by pivotal on 1/15/16.
 */
public interface BusStopRepository extends CrudRepository<BusStop, Long> {
    BusStop findByApiId(String apiId);
}
