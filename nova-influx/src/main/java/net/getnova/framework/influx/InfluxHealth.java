package net.getnova.framework.influx;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class InfluxHealth {

  private final String name;
  private final String message;
  private final Status status;
  private final String version;
  private final String commit;

  public InfluxHealth(
    @JsonProperty("name") final String name,
    @JsonProperty("message") final String message,
    @JsonProperty("status") final Status status,
    @JsonProperty("version") final String version,
    @JsonProperty("commit") final String commit
  ) {
    this.name = name;
    this.message = message;
    this.status = status;
    this.version = version;
    this.commit = commit;
  }

  public enum Status {
    @JsonProperty("pass") PASS,
    @JsonProperty("fail") FAIL;
  }
}
