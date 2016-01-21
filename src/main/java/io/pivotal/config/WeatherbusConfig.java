package io.pivotal.config;

import io.pivotal.Constants;
import io.pivotal.service.IOneBusAwayService;
import io.pivotal.service.IWeatherService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

@Configuration
@Profile({ "default", "cloud" })
public class WeatherbusConfig {
    @Bean
    public IOneBusAwayService getOneBusAwayService() {
        RestAdapter.Builder builder = new RestAdapter.Builder().setEndpoint(Constants.ONEBUSAWAY_ENDPOINT);
        builder.setClient(new OkClient());
        RestAdapter adapter = builder.build();
        return adapter.create(IOneBusAwayService.class);
    }

    @Bean
    public IWeatherService getWeatherService() {
        RestAdapter.Builder builder = new RestAdapter.Builder().setEndpoint(Constants.WEATHER_SERVICE_ENDPOINT);
        builder.setClient(new OkClient());
        RestAdapter adapter = builder.build();
        return adapter.create(IWeatherService.class);
    }
}
