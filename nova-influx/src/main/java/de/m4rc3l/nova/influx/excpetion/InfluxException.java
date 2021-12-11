package de.m4rc3l.nova.influx.excpetion;

import java.io.IOException;

public class InfluxException extends IOException {

  public InfluxException() {
  }

  public InfluxException(final String message) {
    super(message);
  }

  public InfluxException(final String message, final Throwable cause) {
    super(message, cause);
  }
}
