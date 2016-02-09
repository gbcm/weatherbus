package io.pivotal.config;

import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WeatherbusConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("POST", "GET");
    }

    @Profile("default")
    @EnableFeignClients(basePackages = "io.pivotal")
    static class DevConfig {
    }

    @Profile("cloud")
    @EnableEurekaClient
    @EnableFeignClients(basePackages = "io.pivotal")
    static class CloudConfig {
    }
}
