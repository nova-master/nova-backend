package de.m4rc3l.nova.core.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

public class JsonRawSerializer extends JsonSerializer<String> {

  @Override
  public void serialize(
    final String value,
    final JsonGenerator gen,
    final SerializerProvider serializers
  ) throws IOException {
    gen.writeRawValue(value);
  }
}
