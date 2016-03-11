package io.pivotal.service.response;

import lombok.Data;

import java.util.List;

@Data
public class StopReferences {
    private List<RouteReference> routes;

    public StopReferences() {}

    public StopReferences(List<RouteReference> routes) {
        this.routes = routes;
    }

    @Data
    static public class RouteReference {
        private String id;
        private String shortName;
        private String longName;

        public RouteReference() {}

        public RouteReference(String id, String shortName, String longName) {
            this.id = id;
            this.shortName = shortName;
            this.longName = longName;
        }
    }
}
