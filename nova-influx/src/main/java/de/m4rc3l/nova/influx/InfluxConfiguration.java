package de.m4rc3l.nova.influx;

import org.springframework.boot.actuate.autoconfigure.health.ConditionalOnEnabledHealthIndicator;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(InfluxProperties.class)
public class InfluxConfiguration {

  @Bean
  InfluxClient influxClient(final InfluxProperties properties) {
    return new InfluxClient(properties);
  }

  @Bean
  @ConditionalOnEnabledHealthIndicator("influxdb")
  InfluxHealthIndicator influxHealthIndicator(final InfluxClient client) {
    return new InfluxHealthIndicator(client);
  }
}
