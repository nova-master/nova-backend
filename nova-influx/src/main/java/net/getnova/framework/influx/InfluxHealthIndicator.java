package net.getnova.framework.influx;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.AbstractReactiveHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Health.Builder;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class InfluxHealthIndicator extends AbstractReactiveHealthIndicator {

  private final InfluxClient client;

  @Override
  protected Mono<Health> doHealthCheck(final Builder builder) {
    return this.client.health()
      .map(health -> {
        switch (health.getStatus()) {
          case PASS:
            builder.up();
          case FAIL:
            builder.down();
          default:
            builder.unknown();
        }

        return builder.withDetail("status", health.getStatus())
          .withDetail("message", health.getMessage())
          .withDetail("version", health.getVersion())
          .withDetail("commit", health.getCommit())
          .build();
      });
  }
}
