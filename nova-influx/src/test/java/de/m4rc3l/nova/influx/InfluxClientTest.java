package de.m4rc3l.nova.influx;

import static org.junit.jupiter.api.Assertions.*;

import de.m4rc3l.nova.influx.Measurement.DoubleField;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
class InfluxClientTest {

  private static InfluxClient client;

  @BeforeAll
  static void beforeAll() {
    final InfluxProperties properties = new InfluxProperties();
    properties.setBucket("root");
    properties.setOrg("root");
    properties.setToken("root");

    client = new InfluxClient(properties);
  }

  @Test
  void write() {
//    client.write(Flux.just(new Measurement("sensor", Map.of("ab", "b"), new LongField("a", 10L), Instant.now())),
//      WritePrecision.MS).block();

    final String query = "from(bucket: \"root\")\n"
                         + "  |> range(start: 1626356724)\n"
                         + "  |> filter(fn: (r) => r._measurement == \"sensor\" "
                         + "                       and r.sensor_id == \"102a4e35-e33d-44ae-a7e2-039fe4199bbc\")\n"
                         + "  |> drop(columns: [\"sensor_id\"])\n"
                         + "  |> aggregateWindow(every: 1h, fn: mean)\n";

    System.out.println(query);
    client.query(query, DoubleField.class).collectList().block().forEach(System.out::println);
  }

  @Test
  void health() {
    final InfluxHealth health = client.health().block();

    assertNotNull(health);
    assertNotNull(health.getName());
    assertNotNull(health.getStatus());
  }
}
