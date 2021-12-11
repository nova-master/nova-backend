package de.m4rc3l.nova.influx;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "nova.influx")
public class InfluxProperties {

  private String uri = "http://localhost:8086";
  private String bucket;
  private String org;
  private String token;
}
