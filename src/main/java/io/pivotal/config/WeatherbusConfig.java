package io.pivotal.config;

import com.google.gson.Gson;
import io.pivotal.Constants;
import io.pivotal.service.IOneBusAwayService;
import io.pivotal.service.IWeatherService;
import org.springframework.cloud.Cloud;
import org.springframework.cloud.CloudFactory;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Configuration
public class WeatherbusConfig {

    @Profile("default")
    static class DevConfig {
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

    @Profile("cloud")
    static class CloudConfig {
        @Bean
        public IOneBusAwayService getOneBusAwayService() {
            RestAdapter.Builder builder = new RestAdapter.Builder().setEndpoint(Constants.ONEBUSAWAY_ENDPOINT);
            builder.setClient(new OkClient());
            RestAdapter adapter = builder.build();
            return adapter.create(IOneBusAwayService.class);
        }

        @Bean
        public IWeatherService getWeatherService() {
            String vcapServices = System.getenv("VCAP_SERVICES");
            System.out.println("##############################");
            System.out.println("vcapServices");
            System.out.println(vcapServices);

            Gson gson = new Gson();
            CFServices cfs = gson.fromJson(vcapServices, CFServices.class);
            System.out.println("##############################");
            System.out.println("CFServices maybe");
            System.out.println(cfs.getServices().get(0).getProperty().getHost());
            System.out.println(cfs.toString());

            CloudFactory cloudFactory = new CloudFactory();
            Cloud cloud = cloudFactory.getCloud();
            ServiceInfo si = cloud.getServiceInfo("weatherbus-weather-dev");
            System.out.println("##############################");
            System.out.println("ServiceInfo class name");
            System.out.println(si.getClass().getName());
            System.out.println(si.getClass());
            System.out.println("##############################");
            System.out.println("Prop list");
            Properties properties = cloud.getCloudProperties();
            for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                System.out.println(entry.getKey().toString());
                System.out.println(entry.getValue().toString());
            }
            System.out.println("##############################");
            System.out.println("Url prop");
            System.out.println(cloud.getCloudProperties().getProperty("url"));
            System.out.println("##############################");

            return null;
        }
    }
}
