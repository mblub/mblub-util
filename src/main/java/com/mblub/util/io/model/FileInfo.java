package com.mblub.util.io.model;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mblub.util.io.unchecked.UncheckedFiles;
import com.mblub.util.json.deserializer.InstantDeserializer;
import com.mblub.util.json.serializer.InstantSerializer;

public class FileInfo implements Comparable<FileInfo> {
  private FilePath filePath;
  private String fileKey;
  private long size;

  @JsonDeserialize(using = InstantDeserializer.class)
  @JsonSerialize(using = InstantSerializer.class)
  private Instant modificationTime;

  @JsonDeserialize(using = InstantDeserializer.class)
  @JsonSerialize(using = InstantSerializer.class)
  private Instant creationTime;

  public FilePath getFilePath() {
    return filePath;
  }

  public void setFilePath(FilePath filePath) {
    this.filePath = filePath;
  }

  @JsonIgnore
  public Path getPath() {
    return getFilePath().getPath();
  }

  @JsonIgnore
  public void setPath(Path path) {
    filePath = new FilePath(path);
  }

  public FileInfo withPath(Path path) {
    setPath(path);
    return this;
  }

  public String getFileKey() {
    return fileKey;
  }

  public void setFileKey(String fileKey) {
    this.fileKey = fileKey;
  }

  public long getSize() {
    return size;
  }

  public void setSize(long size) {
    this.size = size;
  }

  public Instant getModificationTime() {
    return modificationTime;
  }

  public void setModificationTime(Instant modificationTime) {
    this.modificationTime = modificationTime;
  }

  public Instant getCreationTime() {
    return creationTime;
  }

  public void setCreationTime(Instant creationTime) {
    this.creationTime = creationTime;
  }

  @Override
  public int compareTo(FileInfo o) {
    return o == null ? 1 : filePath.compareTo(o.filePath);
  }

  @Override
  public int hashCode() {
    return filePath.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) return false;
    if (obj == this) return true;
    if (!(obj instanceof FileInfo)) return false;
    return filePath.equals(((FileInfo) obj).filePath);
  }

  @Override
  public String toString() {
    return filePath.toString();
  }

  public static class FileInfoHelper {
    public static FileInfo of(Path path) {
      return new FileInfo().withPath(path);
    }

    public static FileInfo enrich(FileInfo info) {
      Path path = info.getPath();
      if (path == null) {
        throw new UnsupportedOperationException("Cannot enrich due to null path.");
      }

      BasicFileAttributes attr = UncheckedFiles.readAttributes(path);
      info.setCreationTime(attr.creationTime().toInstant());
      info.setModificationTime(attr.lastModifiedTime().toInstant());
      info.setSize(UncheckedFiles.size(path));
      info.setFileKey(String.valueOf(attr.fileKey()));

      return info;
    }

    public static FileInfo enrichedOf(Path path) {
      return enrich(of(path));
    }
  }
}
