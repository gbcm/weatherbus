package io.pivotal.config;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class CFServices {
    @SerializedName("user-provided")
    private List<Service> services;

    @Data
    public class Service {
        @SerializedName("credentials")
        private Property property;

        @SerializedName("name")
        private String name;
    }

    @Data
    public class Property {
        private String host;
    }

}
