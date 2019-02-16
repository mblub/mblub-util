package com.mblub.util.mp4.run;

import static java.util.stream.Collectors.toSet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.mblub.util.io.model.FileInfo;
import com.mblub.util.io.model.FileInfo.FileInfoHelper;
import com.mblub.util.io.unchecked.UncheckedFiles;
import com.mblub.util.json.unchecked.UncheckedObjectMapper;
import com.mblub.util.mp4.dao.Mp4Processor;
import com.mblub.util.mp4.model.VideoFileInfo;
import com.mblub.util.mp4.model.VideoInfo;

public class MongoImportGenerator implements Serializable {
  private static final long serialVersionUID = 1L;
  private static final Pattern MP4_FILENAME_PATTERN = Pattern.compile("[^.].*\\.mp4");
  private static final UncheckedObjectMapper OBJECT_MAPPER = new UncheckedObjectMapper();

  /**
   * Write mp4 info about all files from root folder and all its tree
   *
   * @throws IOException
   */
  private void run(String rootFoldersFile, String mongoImportFile, String previousFiles) throws IOException {

    Set<Path> previousFileScenes = StringUtils.isEmpty(previousFiles) ? Collections.emptySet()
            : Arrays.stream(previousFiles.split(",")).map(Paths::get).map(UncheckedFiles::newBufferedReader)
                    .flatMap(BufferedReader::lines).map(s -> OBJECT_MAPPER.readValue(s, VideoFileInfo.class))
                    .map(VideoFileInfo::getFileInfo).map(FileInfo::getPath).collect(toSet());

    try (PrintWriter writer = openPrintWriter(mongoImportFile)) {
      UncheckedFiles.newBufferedReader(Paths.get(rootFoldersFile)).lines().map(Paths::get).flatMap(UncheckedFiles::walk)
              .filter(p -> MP4_FILENAME_PATTERN.matcher(p.getFileName().toString()).matches())
              .filter(p -> !previousFileScenes.contains(p)).map(this::pathToVideoFileInfo)
              .map(OBJECT_MAPPER::writeValueAsString).forEach(writer::println);
    }
  }

  protected VideoFileInfo pathToVideoFileInfo(Path mp4File) {
    System.out.println("Processing file " + mp4File);
    VideoInfo videoInfo = new Mp4Processor(mp4File).getVideoInfo();
    VideoFileInfo videoFileInfo = new VideoFileInfo();
    videoFileInfo.setFileInfo(FileInfoHelper.enrichedOf(mp4File));
    videoFileInfo.setVideoInfo(videoInfo);
    videoFileInfo.setLastModified(Instant.now());
    return videoFileInfo;
  }

  protected static PrintWriter openPrintWriter(String filePath) {
    return new PrintWriter(UncheckedFiles.newBufferedWriter(Paths.get(filePath), StandardOpenOption.CREATE));
  }

  public static void main(String[] args) throws IOException {
    if (args.length < 2 || args.length > 3) {
      System.out.println(
              "Usage: " + MongoImportGenerator.class.getName() + " <mp4Folder> <mongoImportFile> [<previousFiles>]");
      System.exit(0);
    }

    new MongoImportGenerator().run(args[0], args[1], args.length == 3 ? args[2] : "");
  }

}
