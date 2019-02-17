package com.mblub.util.io.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mblub.util.io.model.FileInfo;
import com.mblub.util.io.model.FileInfo.FileInfoHelper;
import com.mblub.util.io.unchecked.UncheckedFiles;

public class FileWatermark {
  private static final Logger LOG = LogManager.getLogger(FileWatermark.class);

  private Instant watermark;
  private Predicate<Path> filenameFilter;

  public Instant getWatermark() {
    return watermark;
  }

  public void setWatermark(Instant watermark) {
    this.watermark = watermark;
  }

  public FileWatermark withWatermark(Instant watermark) {
    setWatermark(watermark);
    return this;
  }

  public Predicate<Path> getFilenameFilter() {
    return filenameFilter;
  }

  public void setFilenameFilter(Predicate<Path> filenameFilter) {
    this.filenameFilter = filenameFilter;
  }

  public FileWatermark withFilenameFilter(Predicate<Path> filenameFilter) {
    setFilenameFilter(filenameFilter);
    return this;
  }

  public Stream<FileInfo> findNewerFiles(String drivePath, List<String> rootPaths) {
    return findSubFolders(drivePath, rootPaths).flatMap(this::findNewerFiles);
  }

  public Stream<Path> findSubFolders(String drivePath, List<String> rootPaths) {
    return rootPaths.stream().map(f -> Paths.get(drivePath, f)).map(WildcardExpander::new)
            .flatMap(WildcardExpander::getPaths)
            .flatMap(p -> UncheckedFiles.find(p, Integer.MAX_VALUE, this::isIncludedFolder)).peek(LOG::debug);
  }

  public boolean isIncludedFolder(Path path, BasicFileAttributes attr) {
    boolean isIncluded = attr.isDirectory() && !path.getFileName().toString().startsWith(".");
    if (isIncluded) {
      if (attr.lastModifiedTime().toInstant().compareTo(watermark) < 0) isIncluded = false;
    }
    return isIncluded;
  }

  public Stream<FileInfo> findNewerFiles(Path folder) {
    Stream<Path> files = UncheckedFiles.list(folder);
    if (filenameFilter != null) {
      files = files.filter(filenameFilter);
    }
    return files.map(FileInfoHelper::enrichedOf).filter(this::isIncludedFile);
  }

  public boolean isIncludedFile(FileInfo info) {
    boolean isIncluded = info.getModificationTime().compareTo(watermark) > 0;
    if (isIncluded && LOG.isDebugEnabled()) LOG.debug("  including " + info.getPath());
    return isIncluded;
  }

  public static class WildcardExpander {
    private Path videoFolderPath;
    private Stream<Path> expandedPaths;

    public WildcardExpander(Path videoFolderPath) {
      this.videoFolderPath = videoFolderPath;
    }

    public Stream<Path> getPaths() {
      expandedPaths = Stream.of(Paths.get("/"));
      videoFolderPath.iterator().forEachRemaining(p -> {
        if ("*".equals(p.toString())) {
          expandedPaths = expandedPaths.flatMap(UncheckedFiles::list).filter(Files::isDirectory)
                  .filter(p1 -> !p1.getFileName().toString().startsWith("."));
        } else {
          expandedPaths = expandedPaths.map(ep -> ep.resolve(p)).filter(Files::exists).filter(Files::isDirectory);
        }
      });
      return expandedPaths;
    }
  }
}
