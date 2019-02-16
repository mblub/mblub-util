package com.mblub.util.mp4.model;

import java.time.Instant;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mblub.util.io.model.FileInfo;
import com.mblub.util.json.deserializer.InstantDeserializer;
import com.mblub.util.json.serializer.InstantSerializer;

import fr.javatic.mongo.jacksonCodec.objectId.Id;

public class VideoFileInfo {
  private String documentId;
  private FileInfo fileInfo;
  private VideoInfo videoInfo;

  @JsonDeserialize(using = InstantDeserializer.class)
  @JsonSerialize(using = InstantSerializer.class)
  private Instant lastModified;

  @Id
  public String getDocumentId() {
    return documentId;
  }

  @Id
  public void setDocumentId(String documentId) {
    this.documentId = documentId;
  }

  public FileInfo getFileInfo() {
    return fileInfo;
  }

  public void setFileInfo(FileInfo fileInfo) {
    this.fileInfo = fileInfo;
  }

  public VideoInfo getVideoInfo() {
    return videoInfo;
  }

  public void setVideoInfo(VideoInfo videoInfo) {
    this.videoInfo = videoInfo;
  }

  public Instant getLastModified() {
    return lastModified;
  }

  public void setLastModified(Instant lastModified) {
    this.lastModified = lastModified;
  }

  @Override
  public String toString() {
    return fileInfo.getFilePath() + ": " + videoInfo;
  }
}
