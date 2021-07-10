package net.getnova.framework.influx;

import java.time.Instant;
import java.util.Map;
import net.getnova.framework.influx.Measurement.LongField;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

class InfluxClientTest {

  @Test
  @Disabled
  void write() {
    final InfluxProperties properties = new InfluxProperties();
    properties.setBucket("root");
    properties.setOrg("root");
    properties.setToken("root");

    final InfluxClient client = new InfluxClient(properties);

    client.write(Flux.just(new Measurement("test", Map.of("ab", "b"), new LongField("a", 10L), Instant.now())),
      WritePrecision.MS).block();

    final String query = "from(bucket: \"" + properties.getBucket() + "\")\n"
                         + "  |> range(start: -30d)\n";

    System.out.println(query);
    client.query(query, LongField.class).collectList().block().forEach(System.out::println);
  }
}
