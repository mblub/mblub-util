package com.mblub.util.stream;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

public class StreamUtil {

  public static <T> Predicate<T> not(Predicate<T> predicate) {
    return predicate.negate();
  }

  public static Stream<String> streamResourceLines(String resourcePath) {
    return streamResourceLines(resourcePath, InputStreamReader::new);
  }

  public static Stream<String> streamResourceLines(String resourcePath, Charset cs) {
    return streamResourceLines(resourcePath, in -> new InputStreamReader(in, cs));
  }

  private static Stream<String> streamResourceLines(String resourcePath, Function<InputStream, Reader> streamToReader) {
    return Optional.ofNullable(resourcePath).filter(StringUtils::isNotEmpty)
            .map(Thread.currentThread().getContextClassLoader()::getResourceAsStream).map(streamToReader)
            .map(BufferedReader::new).map(BufferedReader::lines).orElse(Stream.empty());
  }
}
