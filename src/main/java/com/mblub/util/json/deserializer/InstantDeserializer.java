package com.mblub.util.json.deserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class InstantDeserializer extends StdDeserializer<Instant> {
  private static final long serialVersionUID = 1L;

  protected InstantDeserializer() {
    super(Instant.class);
  }

  @Override
  public Instant deserialize(JsonParser parser, DeserializationContext context) throws IOException {
    return DateTimeFormatter.ISO_INSTANT.parse(parser.readValueAs(String.class), Instant::from);
  }
}
