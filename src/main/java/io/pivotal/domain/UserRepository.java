package io.pivotal.domain;

import org.springframework.data.repository.CrudRepository;

/**
 * Created by pivotal on 1/12/16.
 */
public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
}
