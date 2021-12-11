package de.m4rc3l.nova.core.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;

public class JsonRawDeserializer extends JsonDeserializer<String> {

  @Override
  public String deserialize(final JsonParser parser, final DeserializationContext ctxt) throws IOException {
    return parser.getCodec().readTree(parser).toString();
  }
}
