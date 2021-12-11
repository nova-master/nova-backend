package de.m4rc3l.nova.influx.excpetion;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InfluxQuotaException extends InfluxException {

  private final long retryAfter;

  public InfluxQuotaException() {
    this.retryAfter = -1;
  }

  /**
   * Returns the seconds after the request can be executed again.
   * <br>
   * If {@code -1}, no data was received.
   *
   * @return the seconds after the request can be executed again.
   */
  public long getRetryAfter() {
    return this.retryAfter;
  }
}
