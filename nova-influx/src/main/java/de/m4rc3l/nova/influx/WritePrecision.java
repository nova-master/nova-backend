package de.m4rc3l.nova.influx;

import java.time.Instant;
import java.util.function.BiConsumer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum WritePrecision {

  MS("ms", (builder, instant) -> builder.append(instant.toEpochMilli())),
  S("s", (builder, instant) -> builder.append(instant.getEpochSecond())),
  US("us", (builder, instant) -> {
    throw new UnsupportedOperationException("WritePrecision.US is not implemented");
  }),
  NS("ns", (builder, instant) -> {
    throw new UnsupportedOperationException("WritePrecision.NS is not implemented");
  });

  private final String value;
  @Getter
  private final BiConsumer<StringBuilder, Instant> appendTimestamp;

  @Override
  public String toString() {
    return this.value;
  }
}
