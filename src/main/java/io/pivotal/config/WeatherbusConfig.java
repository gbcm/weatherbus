package io.pivotal.config;

import com.google.gson.Gson;
import io.pivotal.Constants;
import io.pivotal.service.IOneBusAwayService;
import io.pivotal.service.IRetrofitWeatherService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

@Configuration
public class WeatherbusConfig {

    private static IRetrofitWeatherService createWeatherService(String host) {
        RestAdapter.Builder builder = new RestAdapter.Builder().setEndpoint(host);
        builder.setClient(new OkClient());
        RestAdapter adapter = builder.build();
        return adapter.create(IRetrofitWeatherService.class);
    }

    private static IOneBusAwayService createBusService(String host) {
        RestAdapter.Builder builder = new RestAdapter.Builder().setEndpoint(host);
        builder.setClient(new OkClient());
        RestAdapter adapter = builder.build();
        return adapter.create(IOneBusAwayService.class);
    }

    @Profile("default")
    static class DevConfig {
        @Bean
        public IOneBusAwayService getOneBusAwayService() {
            return createBusService(Constants.ONEBUSAWAY_ENDPOINT);
        }

        @Bean
        public IRetrofitWeatherService getWeatherService() {
            return createWeatherService(Constants.WEATHER_SERVICE_ENDPOINT);
        }
    }

    @Profile("cloud")
    static class CloudConfig {
        @Bean
        public IOneBusAwayService getOneBusAwayService() {
            return createBusService(Constants.ONEBUSAWAY_ENDPOINT);
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
