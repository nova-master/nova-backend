package net.getnova.framework.influx;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import lombok.Data;
import net.getnova.framework.influx.Measurement.Field;
import net.getnova.framework.influx.excpetion.InfluxAuthException;
import net.getnova.framework.influx.excpetion.InfluxException;
import net.getnova.framework.influx.excpetion.InfluxInvalidRequest;
import net.getnova.framework.influx.excpetion.InfluxPayloadToLargeException;
import net.getnova.framework.influx.excpetion.InfluxQuotaException;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class InfluxClient {

  private static final String API_VERSION = "v2";

  private static final String VND_FLUX = "application/vnd.flux;charset=utf-8";
  private static final String LINE_PROTOCOL_TYPE = "text/plain;charset=utf-8";
  private static final String LINE_SEPARATOR = "\n";

  private static final CsvMapper CSV_MAPPER = new CsvMapper();
  private static final CsvSchema CSV_SCHEMA = CsvSchema.emptySchema().withHeader();

  private final InfluxProperties properties;
  private final WebClient client;

  public InfluxClient(final InfluxProperties properties) {
    this.properties = properties;
    this.client = WebClient.builder()
      .baseUrl(properties.getUri() + "/api/" + API_VERSION + "/")
      .defaultHeader(HttpHeaders.AUTHORIZATION, "Token " + properties.getToken())
      .build();
  }

  public Mono<Void> write(final Flux<Measurement<?>> measurements, final WritePrecision precision) {
    final Mono<String> body = measurements.map(measurement -> measurement.toLineProtocol(precision))
      .collectList()
      .map(data -> String.join(LINE_SEPARATOR, data));

    return this.client.post()
      .uri(builder -> builder.path("write")
        .queryParam("bucket", this.properties.getBucket())
        .queryParam("org", this.properties.getOrg())
        .queryParam("precision", precision)
        .build()
      )
      .body(body, String.class)
      .header(HttpHeaders.CONTENT_TYPE, LINE_PROTOCOL_TYPE)
      .exchangeToMono(this::handleResponse);
  }

  public Mono<Void> delete(final OffsetDateTime start, final OffsetDateTime end, final String predicate) {
    final DeleteRequest body = new DeleteRequest(start, end, predicate);

    return this.client.post()
      .uri(builder -> builder.path("delete")
        .queryParam("bucket", this.properties.getBucket())
        .queryParam("org", this.properties.getOrg()).build()
      )
      .body(body, DeleteRequest.class)
      .exchangeToMono(this::handleResponse);
  }

  public <F extends Field> Flux<Measurement<F>> query(final String query, final Class<F> clazz) {
    return this.client.post()
      .uri(builder -> builder.path("query")
        .queryParam("org", this.properties.getOrg()).build()
      )
      .header(HttpHeaders.CONTENT_TYPE, VND_FLUX)
      .body(Mono.just(query), String.class)
      // string for every line
      .exchangeToMono(response -> handleResponse(response, () -> response.bodyToMono(String.class)))
      .flatMapIterable(this::parseCsv)
      .map(data -> {
        try {
          return new Measurement<>(
            data.get("_measurement"),
            this.getTags(data),
            clazz.getDeclaredConstructor(String.class, String.class)
              .newInstance(
                data.get("_field"),
                data.get("_value")
              ),
            Instant.parse(data.get("_time"))
          );
        }
        catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
          throw Exceptions.propagate(e);
        }
      });
  }

  public String getBucket() {
    return this.properties.getBucket();
  }

  private Map<String, String> getTags(final Map<String, String> data) {
    final Map<String, String> result = new HashMap<>();

    data.forEach((key, value) -> {
      switch (key) {
        case "":
        case "result":
        case "table":
          break;
        default:
          if (!key.startsWith("_")) {
            result.put(key, value);
          }
      }
    });

    return result;
  }

  private Mono<Void> handleResponse(final ClientResponse response) {
    return this.handleResponse(response, Mono::empty);
  }

  private <T> Mono<T> handleResponse(final ClientResponse response, final Supplier<Mono<T>> success) {
    switch (response.statusCode()) {
      case NO_CONTENT:
        return Mono.empty();
      case OK:
        return success.get();
      case BAD_REQUEST:
        return this.createException(response, InfluxInvalidRequest::new);
      case UNAUTHORIZED:
      case FORBIDDEN:
        return this.createException(response, InfluxAuthException::new);
      case NOT_FOUND:
      case PAYLOAD_TOO_LARGE:
        return this.createException(response, InfluxPayloadToLargeException::new);
      case TOO_MANY_REQUESTS:
      case SERVICE_UNAVAILABLE:
        final List<String> retryAfter = response.headers().header(HttpHeaders.RETRY_AFTER); // seconds

        return retryAfter.size() == 0
          ? Mono.error(new InfluxQuotaException())
          : Mono.error(new InfluxQuotaException(Long.parseLong(retryAfter.get(0))));
      default:
        return this.createException(response, InfluxException::new);
    }
  }

  private <T> Mono<T> createException(final ClientResponse response, final Function<String, InfluxException> ex) {
    return response.bodyToMono(InfluxErrorResponse.class).flatMap(error -> Mono.error(ex.apply(error.getMessage())));
  }

  private Iterable<Map<String, String>> parseCsv(final String data) {
    return () -> {
      try {
        return CSV_MAPPER.readerFor(Map.class)
          .with(CSV_SCHEMA)
          .readValues(data.strip());
      }
      catch (IOException e) {
        throw Exceptions.propagate(e);
      }
    };
  }

  @Data
  private static final class DeleteRequest {

    private final OffsetDateTime start;
    private final OffsetDateTime end;
    private final String predicate;
  }

  @Data
  private static final class InfluxErrorResponse {

    private final String message;

    InfluxErrorResponse(
      @JsonProperty("message") final String message
    ) {
      this.message = message;
    }
  }
}
