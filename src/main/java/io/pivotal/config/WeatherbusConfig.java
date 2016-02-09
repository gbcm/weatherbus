package io.pivotal.config;

import io.pivotal.Constants;
import io.pivotal.service.IRetrofitBusService;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

@Configuration
public class WeatherbusConfig extends WebMvcConfigurerAdapter {

    private static IRetrofitBusService createBusService(String host) {
        RestAdapter.Builder builder = new RestAdapter.Builder().setEndpoint(host);
        builder.setClient(new OkClient());
        RestAdapter adapter = builder.build();
        return adapter.create(IRetrofitBusService.class);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("POST", "GET");
    }

    @Profile("default")
    @EnableFeignClients(basePackages = "io.pivotal")
    static class DevConfig {
        @Bean
        public IRetrofitBusService getBusService() {
            return createBusService(Constants.BUS_SERVICE_ENDPOINT);
        }
    }

    @Profile("cloud")
    @EnableEurekaClient
    @EnableFeignClients(basePackages = "io.pivotal")
    static class CloudConfig {
        @Bean
        public IRetrofitBusService getBusService() {
            return createBusService(Constants.BUS_SERVICE_ENDPOINT);
        }
    }
}
