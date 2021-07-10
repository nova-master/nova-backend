package net.getnova.framework.influx;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(InfluxProperties.class)
public class InfluxConfiguration {

  @Bean
  InfluxClient influxClient(final InfluxProperties properties) {
    return new InfluxClient(properties);
  }
}
