package com.mblub.util.stream;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

public class StreamUtil {

  public static <T> Predicate<T> not(Predicate<T> predicate) {
    return predicate.negate();
  }

  public static Stream<String> streamResourceLines(String resourcePath) {
    return Optional.ofNullable(resourcePath).filter(StringUtils::isNotEmpty)
            .map(Thread.currentThread().getContextClassLoader()::getResourceAsStream).map(InputStreamReader::new)
            .map(BufferedReader::new).map(BufferedReader::lines).orElse(Stream.empty());
  }
}
