package com.mblub.util.mp4.model;

public class VideoInfo {
  int trackCount;
  int width;
  int height;
  int duration;

  public int getTrackCount() {
    return trackCount;
  }

  public void setTrackCount(int trackCount) {
    this.trackCount = trackCount;
  }

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public int getDuration() {
    return duration;
  }

  public void setDuration(int duration) {
    this.duration = duration;
  }

  @Override
  public String toString() {
    return width + "x" + height + ", " + duration + "s";
  }
}
