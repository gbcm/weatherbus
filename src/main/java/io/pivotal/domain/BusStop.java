package io.pivotal.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "bus_stop")
public class BusStop {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "apiId", nullable = false, unique = true)
    private String apiId;

    @Column(name = "name", nullable = false)
    private String name;

    public BusStop() {
    }

    public BusStop(String name, String apiId) {
        this.name = name;
        this.apiId = apiId;
    }
}
