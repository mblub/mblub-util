package com.mblub.util.mp4.dao;

import java.io.IOException;
import java.io.Serializable;
import java.io.UncheckedIOException;
import java.nio.file.Path;

import com.coremedia.iso.IsoFile;
import com.coremedia.iso.boxes.MovieBox;
import com.coremedia.iso.boxes.MovieHeaderBox;
import com.coremedia.iso.boxes.TrackBox;
import com.coremedia.iso.boxes.TrackHeaderBox;
import com.googlecode.mp4parser.FileDataSourceImpl;
import com.mblub.util.mp4.model.VideoInfo;

public class Mp4Processor implements Serializable {

  private static final long serialVersionUID = 1L;
  private Path mp4File;

  public Mp4Processor(Path mp4File) {
    this.mp4File = mp4File;
  }

  /**
   * Get some fields of the first track whose width and height are non-zero.
   *
   * @return
   * @throws IOException
   */
  public VideoInfo getVideoInfo() {
    VideoInfo info = new VideoInfo();
    try (IsoFile isoFile = new IsoFile(new FileDataSourceImpl(mp4File.toFile()))) {
      MovieBox movieBox = isoFile.getMovieBox();
      if (movieBox != null) {
        MovieHeaderBox movieHeaderBox = movieBox.getMovieHeaderBox();
        double lengthInSeconds = (double) movieHeaderBox.getDuration() / movieHeaderBox.getTimescale();
        int duration = (int) Math.ceil(lengthInSeconds);
        info.setDuration(duration);
        info.setTrackCount(movieBox.getTrackCount());
        for (TrackBox trackBox : movieBox.getBoxes(TrackBox.class)) {
          TrackHeaderBox header = trackBox.getTrackHeaderBox();
          info.setWidth((int) header.getWidth());
          info.setHeight((int) header.getHeight());
          if (info.getWidth() > 0 && info.getHeight() > 0) {
            break;
          }
        }
      }
    } catch (IOException ioe) {
      throw new UncheckedIOException(ioe);
    }
    return info;
  }
}
