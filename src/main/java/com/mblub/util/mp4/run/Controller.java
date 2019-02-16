package com.mblub.util.mp4.run;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Pattern;

import com.mblub.util.mp4.dao.Mp4Processor;
import com.mblub.util.mp4.model.VideoInfo;

public class Controller implements Serializable {
  private static final long serialVersionUID = 1L;
  private static final Pattern MP4_FILENAME_PATTERN = Pattern.compile("[^.].*\\.mp4");

  private Map<Integer, Set<Path>> filesByHeight;
  private Map<Path, Integer> filesByName;

  /**
   * Process the file(s)
   *
   * @throws IOException
   */
  private void run(String rootFolder) throws IOException {
    filesByHeight = new TreeMap<>();
    filesByName = new TreeMap<>();

    // recurse through folders
    Files.walk(Paths.get(rootFolder)).filter(p -> MP4_FILENAME_PATTERN.matcher(p.getFileName().toString()).matches())
            .forEach(this::processOneFile);

    System.out.println();
    System.out.println("=== SUMMARY ===");
    System.out.println();
    filesByHeight.forEach(this::reportFilesForHeight);

    System.out.println();
    filesByName.forEach((k, v) -> System.out.println("  " + k + ": " + v));
  }

  protected void processOneFile(Path mp4File) {
    System.out.println("Processing file " + mp4File);
    VideoInfo info = new Mp4Processor(mp4File).getVideoInfo();
    System.out.println(info);
    filesByHeight.computeIfAbsent(info.getHeight(), k -> new TreeSet<>()).add(mp4File);
    filesByName.put(mp4File.getFileName(), info.getHeight());
  }

  protected void reportFilesForHeight(int height, Set<Path> files) {
    System.out.println(height);
    files.forEach(f -> System.out.println("  " + f));
    System.out.println();
  }

  public static void main(String[] args) throws IOException {
    if (args.length != 1) {
      System.out.println("Usage: " + Controller.class.getName() + " <mp4Folder>");
      System.exit(0);
    }

    String rootFolder = args[0];
    new Controller().run(rootFolder);
  }

}
