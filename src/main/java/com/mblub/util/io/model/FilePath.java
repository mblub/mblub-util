package com.mblub.util.io.model;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FilePath implements Comparable<FilePath> {
  private Path parent;
  private Path fileName;
  private Path path;

  public FilePath() {

  }

  public FilePath(Path path) {
    this();
    setPath(path);
  }

  @JsonIgnore
  public Path getParentPath() {
    return parent;
  }

  @JsonProperty("folder")
  public String getParent() {
    return getParentPath().toString();
  }

  @JsonIgnore
  public void setParentPath(Path parent) {
    this.parent = parent;
    updatePath();
  }

  @JsonProperty("folder")
  public void setParent(String parentString) {
    setParentPath(Paths.get(parentString));
  }

  @JsonIgnore
  public Path getFileNamePath() {
    return fileName;
  }

  @JsonProperty("name")
  public String getFileName() {
    return getFileNamePath().toString();
  }

  @JsonIgnore
  public void setNamePath(Path name) {
    this.fileName = name;
    updatePath();
  }

  @JsonProperty("name")
  public void setName(String nameString) {
    setNamePath(Paths.get(nameString));
  }

  @JsonIgnore
  public Path getPath() {
    return path;
  }

  @JsonIgnore
  public void setPath(Path path) {
    this.path = path;
    if (path != null) {
      parent = path.getParent();
      fileName = path.getFileName();
    }
  }

  private void updatePath() {
    path = (parent == null || fileName == null ? null : parent.resolve(fileName));
  }

  @Override
  public int compareTo(FilePath o) {
    if (o == null) return 1;
    return new CompareToBuilder().append(parent, o.parent).append(fileName, o.fileName).toComparison();
  }

  @Override
  public boolean equals(Object otherObj) {
    if (otherObj == null) {
      return false;
    }
    if (otherObj == this) {
      return true;
    }
    if (otherObj.getClass() != getClass()) {
      return false;
    }
    FilePath o = (FilePath) otherObj;
    return new EqualsBuilder().append(parent, o.parent).append(fileName, o.fileName).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(parent).append(fileName).toHashCode();
  }

  @Override
  public String toString() {
    return String.valueOf(getPath());
  }
}