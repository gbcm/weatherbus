package io.pivotal.domain;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by pivotal on 1/12/16.
 */
@Data
@Entity
@Table(name = "weatherbus_user")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @ElementCollection
    @CollectionTable(name = "StopId", joinColumns=@JoinColumn(name = "stopId_id"))
    @Column(name = "stopId")
    private Set<String> stopIds;

    protected User() {}

    public User(String username) {
        this.username = username;
        this.stopIds = new HashSet<>();
    }
}