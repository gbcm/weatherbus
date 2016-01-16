package io.pivotal.domain;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "weatherbus_user")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @ManyToMany
    @JoinTable(name="users_stops")
    private Set<BusStop> stops;

    protected User() {}

    public User(String username) {
        this.username = username;
        this.stops = new HashSet<>();
    }
}