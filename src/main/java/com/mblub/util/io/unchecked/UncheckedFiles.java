package com.mblub.util.io.unchecked;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.CopyOption;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileTime;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

/**
 * Wrapper functions for java file utilities that catch IOException and rethrow
 * as UncheckedIOException
 * 
 * @author mike
 *
 */
public class UncheckedFiles {

  public static FileReader newFileReader(File file) {
    try {
      return new FileReader(file);
    } catch (FileNotFoundException fnfe) {
      throw new UncheckedIOException(fnfe);
    }
  }

  public static BufferedReader newBufferedReader(Path path) {
    try {
      return Files.newBufferedReader(path);
    } catch (IOException ioe) {
      throw new UncheckedIOException(ioe);
    }
  }

  public static BufferedWriter newBufferedWriter(Path path, OpenOption... options) {
    try {
      return Files.newBufferedWriter(path, options);
    } catch (IOException ioe) {
      throw new UncheckedIOException(ioe);
    }
  }

  public static Path move(Path source, Path target, CopyOption... options) {
    try {
      return Files.move(source, target, options);
    } catch (IOException ioe) {
      throw new UncheckedIOException(ioe);
    }
  }

  public static Path write(Path path, Iterable<? extends CharSequence> lines, OpenOption... options) {
    try {
      return Files.write(path, lines, options);
    } catch (IOException ioe) {
      throw new UncheckedIOException(ioe);
    }
  }

  public static Stream<Path> list(Path path) {
    try {
      return Files.list(path);
    } catch (IOException ioe) {
      throw new UncheckedIOException(ioe);
    }
  }

  public static Path createDirectories(Path dir, FileAttribute<?>... attrs) {
    try {
      return Files.createDirectories(dir, attrs);
    } catch (IOException ioe) {
      throw new UncheckedIOException(ioe);
    }
  }

  public static FileTime getLastModifiedTime(Path path, LinkOption... options) {
    try {
      return Files.getLastModifiedTime(path, options);
    } catch (IOException ioe) {
      throw new UncheckedIOException(ioe);
    }
  }

  public static BasicFileAttributes readAttributes(Path path) {
    try {
      return Files.readAttributes(path, BasicFileAttributes.class);
    } catch (IOException ioe) {
      throw new UncheckedIOException(ioe);
    }
  }

  public static long size(Path path) {
    try {
      return Files.size(path);
    } catch (IOException ioe) {
      throw new UncheckedIOException(ioe);
    }
  }

  public static Stream<Path> walk(Path start, FileVisitOption... options) {
    try {
      return Files.walk(start, options);
    } catch (IOException ioe) {
      throw new UncheckedIOException(ioe);
    }
  }

  public static Stream<Path> find(Path start, int maxDepth, BiPredicate<Path, BasicFileAttributes> matcher,
          FileVisitOption... options) {
    try {
      return Files.find(start, maxDepth, matcher, options);
    } catch (IOException ioe) {
      throw new UncheckedIOException(ioe);
    }
  }
}
