package de.m4rc3l.nova.influx.excpetion;

public class InfluxPayloadToLargeException extends InfluxException {

  public InfluxPayloadToLargeException(final String message) {
    super(message);
  }
}
