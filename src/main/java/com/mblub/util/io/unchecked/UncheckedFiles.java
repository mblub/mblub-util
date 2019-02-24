package com.mblub.util.io.unchecked;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.FileStore;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.UserPrincipal;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

  public static BasicFileAttributes readAttributes(Path path) {
    try {
      return Files.readAttributes(path, BasicFileAttributes.class);
    } catch (IOException ioe) {
      throw new UncheckedIOException(ioe);
    }
  }

  public static InputStream newInputStream(Path path, OpenOption... options) {
    try {
      return Files.newInputStream(path, options);
    } catch (IOException ioe) {
      throw new UncheckedIOException(ioe);
    }
  }

  public static OutputStream newOutputStream(Path path, OpenOption... options) {
    try {
      return Files.newOutputStream(path, options);
    } catch (IOException ioe) {
      throw new UncheckedIOException(ioe);
    }
  }

  public static SeekableByteChannel newByteChannel(Path path, Set<? extends OpenOption> options,
          FileAttribute<?>... attrs) {
    try {
      return Files.newByteChannel(path, options, attrs);
    } catch (IOException ioe) {
      throw new UncheckedIOException(ioe);
    }
  }

  public static SeekableByteChannel newByteChannel(Path path, OpenOption... options) {
    try {
      return Files.newByteChannel(path, options);
    } catch (IOException ioe) {
      throw new UncheckedIOException(ioe);
    }
  }

  public static DirectoryStream<Path> newDirectoryStream(Path dir) {
    try {
      return Files.newDirectoryStream(dir);
    } catch (IOException ioe) {
      throw new UncheckedIOException(ioe);
    }
  }

  public static DirectoryStream<Path> newDirectoryStream(Path dir, String glob) {
    try {
      return Files.newDirectoryStream(dir, glob);
    } catch (IOException ioe) {
      throw new UncheckedIOException(ioe);
    }
  }

  public static DirectoryStream<Path> newDirectoryStream(Path dir, DirectoryStream.Filter<? super Path> filter) {
    try {
      return Files.newDirectoryStream(dir, filter);
    } catch (IOException ioe) {
      throw new UncheckedIOException(ioe);
    }
  }

  public static Path createFile(Path path, FileAttribute<?>... attrs) {
    try {
      return Files.createFile(path, attrs);
    } catch (IOException ioe) {
      throw new UncheckedIOException(ioe);
    }
  }

  public static Path createDirectory(Path dir, FileAttribute<?>... attrs) {
    try {
      return Files.createDirectory(dir, attrs);
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

  public static Path createTempFile(Path dir, String prefix, String suffix, FileAttribute<?>... attrs) {
    try {
      return Files.createTempFile(dir, prefix, suffix, attrs);
    } catch (IOException ioe) {
      throw new UncheckedIOException(ioe);
    }
  }

  public static Path createTempFile(String prefix, String suffix, FileAttribute<?>... attrs) {
    try {
      return Files.createTempFile(prefix, suffix, attrs);
    } catch (IOException ioe) {
      throw new UncheckedIOException(ioe);
    }
  }

  public static Path createTempDirectory(Path dir, String prefix, FileAttribute<?>... attrs) {
    try {
      return Files.createTempDirectory(dir, prefix, attrs);
    } catch (IOException ioe) {
      throw new UncheckedIOException(ioe);
    }
  }

  public static Path createTempDirectory(String prefix, FileAttribute<?>... attrs) {
    try {
      return Files.createTempDirectory(prefix, attrs);
    } catch (IOException ioe) {
      throw new UncheckedIOException(ioe);
    }
  }

  public static Path createSymbolicLink(Path link, Path target, FileAttribute<?>... attrs) {
    try {
      return Files.createSymbolicLink(link, target, attrs);
    } catch (IOException ioe) {
      throw new UncheckedIOException(ioe);
    }
  }

  public static Path createLink(Path link, Path existing) {
    try {
      return Files.createLink(link, existing);
    } catch (IOException ioe) {
      throw new UncheckedIOException(ioe);
    }
  }

  public static void delete(Path path) {
    try {
      Files.delete(path);
    } catch (IOException ioe) {
      throw new UncheckedIOException(ioe);
    }
  }

  public static boolean deleteIfExists(Path path) {
    try {
      return Files.deleteIfExists(path);
    } catch (IOException ioe) {
      throw new UncheckedIOException(ioe);
    }
  }

  public static Path copy(Path source, Path target, CopyOption... options) {
    try {
      return Files.copy(source, target, options);
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

  public static Path readSymbolicLink(Path link) {
    try {
      return Files.readSymbolicLink(link);
    } catch (IOException ioe) {
      throw new UncheckedIOException(ioe);
    }
  }

  public static FileStore getFileStore(Path path) {
    try {
      return Files.getFileStore(path);
    } catch (IOException ioe) {
      throw new UncheckedIOException(ioe);
    }
  }

  public static boolean isSameFile(Path path, Path path2) {
    try {
      return Files.isSameFile(path, path2);
    } catch (IOException ioe) {
      throw new UncheckedIOException(ioe);
    }
  }

  public static boolean isHidden(Path path) {
    try {
      return Files.isHidden(path);
    } catch (IOException ioe) {
      throw new UncheckedIOException(ioe);
    }
  }

  public static String probeContentType(Path path) {
    try {
      return Files.probeContentType(path);
    } catch (IOException ioe) {
      throw new UncheckedIOException(ioe);
    }
  }

  public static <V extends FileAttributeView> V getFileAttributeView(Path path, Class<V> type, LinkOption... options) {
    return Files.getFileAttributeView(path, type, options);
  }

  public static <A extends BasicFileAttributes> A readAttributes(Path path, Class<A> type, LinkOption... options) {
    try {
      return Files.readAttributes(path, type, options);
    } catch (IOException ioe) {
      throw new UncheckedIOException(ioe);
    }
  }

  public static Path setAttribute(Path path, String attribute, Object value, LinkOption... options) {
    try {
      return Files.setAttribute(path, attribute, value, options);
    } catch (IOException ioe) {
      throw new UncheckedIOException(ioe);
    }
  }

  public static Object getAttribute(Path path, String attribute, LinkOption... options) {
    try {
      return Files.getAttribute(path, attribute, options);
    } catch (IOException ioe) {
      throw new UncheckedIOException(ioe);
    }
  }

  public static Map<String, Object> readAttributes(Path path, String attributes, LinkOption... options) {
    try {
      return Files.readAttributes(path, attributes, options);
    } catch (IOException ioe) {
      throw new UncheckedIOException(ioe);
    }
  }

  public static Set<PosixFilePermission> getPosixFilePermissions(Path path, LinkOption... options) {
    try {
      return Files.getPosixFilePermissions(path, options);
    } catch (IOException ioe) {
      throw new UncheckedIOException(ioe);
    }
  }

  public static Path setPosixFilePermissions(Path path, Set<PosixFilePermission> perms) {
    try {
      return Files.setPosixFilePermissions(path, perms);
    } catch (IOException ioe) {
      throw new UncheckedIOException(ioe);
    }
  }

  public static UserPrincipal getOwner(Path path, LinkOption... options) {
    try {
      return Files.getOwner(path, options);
    } catch (IOException ioe) {
      throw new UncheckedIOException(ioe);
    }
  }

  public static Path setOwner(Path path, UserPrincipal owner) {
    try {
      return Files.setOwner(path, owner);
    } catch (IOException ioe) {
      throw new UncheckedIOException(ioe);
    }
  }

  public static boolean isSymbolicLink(Path path) {
    return Files.isSymbolicLink(path);
  }

  public static boolean isDirectory(Path path, LinkOption... options) {
    return Files.isDirectory(path, options);
  }

  public static boolean isRegularFile(Path path, LinkOption... options) {
    return Files.isRegularFile(path, options);
  }

  public static FileTime getLastModifiedTime(Path path, LinkOption... options) {
    try {
      return Files.getLastModifiedTime(path, options);
    } catch (IOException ioe) {
      throw new UncheckedIOException(ioe);
    }
  }

  public static Path setLastModifiedTime(Path path, FileTime time) {
    try {
      return Files.setLastModifiedTime(path, time);
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

  public static boolean exists(Path path, LinkOption... options) {
    return Files.exists(path, options);
  }

  public static boolean notExists(Path path, LinkOption... options) {
    return Files.notExists(path, options);
  }

  public static boolean isReadable(Path path) {
    return Files.isReadable(path);
  }

  public static boolean isWritable(Path path) {
    return Files.isWritable(path);
  }

  public static boolean isExecutable(Path path) {
    return Files.isExecutable(path);
  }

  public static Path walkFileTree(Path start, Set<FileVisitOption> options, int maxDepth,
          FileVisitor<? super Path> visitor) {
    try {
      return Files.walkFileTree(start, options, maxDepth, visitor);
    } catch (IOException ioe) {
      throw new UncheckedIOException(ioe);
    }
  }

  public static Path walkFileTree(Path start, FileVisitor<? super Path> visitor) {
    try {
      return Files.walkFileTree(start, visitor);
    } catch (IOException ioe) {
      throw new UncheckedIOException(ioe);
    }
  }

  public static BufferedReader newBufferedReader(Path path, Charset cs) {
    try {
      return Files.newBufferedReader(path, cs);
    } catch (IOException ioe) {
      throw new UncheckedIOException(ioe);
    }
  }

  public static BufferedReader newBufferedReader(Path path) {
    try {
      return Files.newBufferedReader(path);
    } catch (IOException ioe) {
      throw new UncheckedIOException(ioe);
    }
  }

  public static BufferedWriter newBufferedWriter(Path path, Charset cs, OpenOption... options) {
    try {
      return Files.newBufferedWriter(path, cs, options);
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

  public static long copy(InputStream in, Path target, CopyOption... options) {
    try {
      return Files.copy(in, target, options);
    } catch (IOException ioe) {
      throw new UncheckedIOException(ioe);
    }
  }

  public static long copy(Path source, OutputStream out) {
    try {
      return Files.copy(source, out);
    } catch (IOException ioe) {
      throw new UncheckedIOException(ioe);
    }
  }

  public static byte[] readAllBytes(Path path) {
    try {
      return Files.readAllBytes(path);
    } catch (IOException ioe) {
      throw new UncheckedIOException(ioe);
    }
  }

  public static List<String> readAllLines(Path path, Charset cs) {
    try {
      return Files.readAllLines(path, cs);
    } catch (IOException ioe) {
      throw new UncheckedIOException(ioe);
    }
  }

  public static List<String> readAllLines(Path path) {
    try {
      return Files.readAllLines(path);
    } catch (IOException ioe) {
      throw new UncheckedIOException(ioe);
    }
  }

  public static Path write(Path path, byte[] bytes, OpenOption... options) {
    try {
      return Files.write(path, bytes, options);
    } catch (IOException ioe) {
      throw new UncheckedIOException(ioe);
    }
  }

  public static Path write(Path path, Iterable<? extends CharSequence> lines, Charset cs, OpenOption... options) {
    try {
      return Files.write(path, lines, cs, options);
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

  public static Stream<Path> walk(Path start, int maxDepth, FileVisitOption... options) {
    try {
      return Files.walk(start, maxDepth, options);
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

  public static Stream<String> lines(Path path, Charset cs) {
    try {
      return Files.lines(path, cs);
    } catch (IOException ioe) {
      throw new UncheckedIOException(ioe);
    }
  }

  public static Stream<String> lines(Path path) {
    try {
      return Files.lines(path);
    } catch (IOException ioe) {
      throw new UncheckedIOException(ioe);
    }
  }
}
