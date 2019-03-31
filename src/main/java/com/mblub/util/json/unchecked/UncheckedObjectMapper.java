package com.mblub.util.json.unchecked;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.file.Path;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Extension of Jackson ObjectMapper with methods that catch IOException and
 * rethrow as UncheckedIOException
 * 
 * @author mike
 *
 */
public class UncheckedObjectMapper extends ObjectMapper {
  private static final long serialVersionUID = 1L;

  public UncheckedObjectMapper() {
    super();
  }

  @Override
  public UncheckedObjectMapper setPropertyNamingStrategy(PropertyNamingStrategy s) {
    return (UncheckedObjectMapper) super.setPropertyNamingStrategy(s);
  }

  @Override
  public UncheckedObjectMapper enable(SerializationFeature f) {
    return (UncheckedObjectMapper) super.enable(f);
  }

  @Override
  public <T> T readValue(String src, Class<T> valueType) {
    try {
      return super.readValue(src, valueType);
    } catch (IOException ioe) {
      throw new UncheckedIOException(ioe);
    }
  }

  @Override
  public <T> T readValue(String src, @SuppressWarnings("rawtypes") TypeReference valueType) {
    try {
      return super.readValue(src, valueType);
    } catch (IOException ioe) {
      throw new UncheckedIOException(ioe);
    }
  }

  @Override
  public <T> T readValue(File src, Class<T> valueType) {
    try {
      return super.readValue(src, valueType);
    } catch (IOException ioe) {
      throw new UncheckedIOException(ioe);
    }
  }

  @Override
  public void writeValue(File resultFile, Object value) {
    try {
      super.writeValue(resultFile, value);
    } catch (IOException ioe) {
      throw new UncheckedIOException(ioe);
    }
  }

  @Override
  public void writeValue(Writer writer, Object value) {
    try {
      super.writeValue(writer, value);
    } catch (IOException ioe) {
      throw new UncheckedIOException(ioe);
    }
  }

  @Override
  public void writeValue(OutputStream out, Object value) {
    try {
      super.writeValue(out, value);
    } catch (IOException ioe) {
      throw new UncheckedIOException(ioe);
    }
  }

  public void writeValue(Path resultPath, Object value) {
    if (resultPath == null) {
      throw new UncheckedIOException(new IOException("cannot write json value due to null Path"));
    }
    writeValue(resultPath.toFile(), value);
  }

  @Override
  public String writeValueAsString(Object value) {
    try {
      return super.writeValueAsString(value);
    } catch (JsonProcessingException jpe) {
      throw new UncheckedIOException(jpe);
    }
  }

  @Override
  public UncheckedObjectMapper disable(SerializationFeature f) {
    return (UncheckedObjectMapper) super.disable(f);
  }
}
