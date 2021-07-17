package net.getnova.framework.core;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import lombok.EqualsAndHashCode;
import net.getnova.framework.core.exception.HttpException;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ErrorProperties.IncludeAttribute;
import org.springframework.boot.autoconfigure.web.WebProperties.Resources;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

public class GlobalErrorWebExceptionHandler extends AbstractErrorWebExceptionHandler {

  private final ErrorProperties properties;

  public GlobalErrorWebExceptionHandler(
    final ErrorAttributes errorAttributes,
    final Resources resources,
    final ApplicationContext applicationContext,
    final ErrorProperties properties
  ) {
    super(errorAttributes, resources, applicationContext);
    this.properties = properties;
  }

  @Override
  protected RouterFunction<ServerResponse> getRoutingFunction(final ErrorAttributes attributes) {
    return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
  }

  private Mono<ServerResponse> renderErrorResponse(final ServerRequest request) {
    return this.causeToException(this.getError(request)).toResponse();
  }

  private HttpError causeToException(final Throwable cause) {
    if (cause instanceof HttpException) {
      final HttpException exception = (HttpException) cause;

      if (exception.getAdditionalProperties().size() == 0) {
        return new HttpError(
          exception.getStatus(),
          exception.getType(),
          exception.getMessage()
        );
      }

      return new HttpError(
        exception.getStatus(),
        exception.getType(),
        exception.getMessage(),
        exception.getAdditionalProperties()
      );
    }
    else if (cause instanceof ServerWebInputException) {
      final ServerWebInputException exception = (ServerWebInputException) cause;

      final Optional<String> property = Optional.ofNullable(exception.getMethodParameter())
        .map(MethodParameter::getParameterName);

      if (property.isPresent()) {
        return new HttpError(
          exception.getStatus(),
          "BAD_INPUT",
          exception.getReason(),
          Map.of("property", property.get())
        );
      }

      return new HttpError(
        exception.getStatus(),
        "BAD_INPUT",
        exception.getReason()
      );
    }
    else if (cause instanceof ResponseStatusException) {
      final ResponseStatusException exception = (ResponseStatusException) cause;

      if (this.properties.getIncludeMessage() == IncludeAttribute.NEVER || exception.getReason() == null) {
        return new HttpError(exception.getStatus(), "BASIC");
      }

      return new HttpError(
        exception.getStatus(),
        "UNKNOWN",
        exception.getReason()
      );
    }
    else {
      return new HttpError(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @EqualsAndHashCode
  private static final class HttpError {

    private final int status;
    private final Map<String, String> body;

    private HttpError(
      final HttpStatus status
    ) {
      this.status = status.value();
      this.body = Map.of("status", status.name());
    }

    private HttpError(
      final HttpStatus status,
      final String type
    ) {
      this.status = status.value();
      this.body = Map.of(
        "status", status.name(),
        "type", type
      );
    }

    private HttpError(
      final HttpStatus status,
      final String type,
      final String message
    ) {
      this.status = status.value();
      this.body = Map.of(
        "status", status.name(),
        "type", type,
        "message", message
      );
    }

    private HttpError(
      final HttpStatus status,
      final String type,
      final String message,
      final Map<String, String> additionalProperties
    ) {
      this.status = status.value();
      this.body = new LinkedHashMap<>(2 + additionalProperties.size());

      this.body.put("staus", status.name());
      this.body.put("type", type);
      this.body.put("message", message);
      this.body.putAll(additionalProperties);
    }

    private Mono<ServerResponse> toResponse() {
      return ServerResponse.status(this.status)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(this.body);
    }
  }
}
