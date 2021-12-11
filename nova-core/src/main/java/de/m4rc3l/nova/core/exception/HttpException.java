package de.m4rc3l.nova.core.exception;

import java.util.Collections;
import java.util.Map;
import org.springframework.http.HttpStatus;

public abstract class HttpException extends RuntimeException {

  public HttpException() {
  }

  public HttpException(final String message) {
    super(message);
  }

  public HttpException(final Throwable cause) {
    super(cause);
  }

  public HttpException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public abstract HttpStatus getStatus();

  public abstract String getType();

  public abstract String getMessage();

  public Map<String, String> getAdditionalProperties() {
    return Collections.emptyMap();
  }
}
