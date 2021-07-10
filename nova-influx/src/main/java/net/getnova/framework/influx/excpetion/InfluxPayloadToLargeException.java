package net.getnova.framework.influx.excpetion;

public class InfluxPayloadToLargeException extends InfluxException {

  public InfluxPayloadToLargeException(final String message) {
    super(message);
  }
}
