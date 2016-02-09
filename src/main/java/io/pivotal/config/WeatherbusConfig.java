package io.pivotal.config;

import com.google.gson.Gson;
import io.pivotal.Constants;
import io.pivotal.service.IRetrofitBusService;
import io.pivotal.service.IRetrofitWeatherService;
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

    private static IRetrofitWeatherService createWeatherService(String host) {
        RestAdapter.Builder builder = new RestAdapter.Builder().setEndpoint(host);
        builder.setClient(new OkClient());
        RestAdapter adapter = builder.build();
        return adapter.create(IRetrofitWeatherService.class);
    }

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

        @Bean
        public IRetrofitWeatherService getWeatherService() {
            return createWeatherService(Constants.WEATHER_SERVICE_ENDPOINT);
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

        @Bean
        public IRetrofitWeatherService getWeatherService() {
            String vcapServices = System.getenv("VCAP_SERVICES");
            Gson gson = new Gson();
            CFServices cfs = gson.fromJson(vcapServices, CFServices.class);

            String host = null;
            for (CFServices.Service service : cfs.getServices()) {
                if (service.getName().equals("weatherbus-weather") && service.getProperty() != null) {
                    host = service.getProperty().getHost();
                }
            }

            if (host == null) {
                throw new IllegalArgumentException("Cannot load weatherbus-weather host URI!");
            }

            return createWeatherService(host);
        }
    }
}
