package com.mblub.util.run;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommandLineProcessor {
  protected static final Logger LOG = LogManager.getLogger(CommandLineProcessor.class);
  public static final String OUTPUT_ROOT_SYSTEM_PROPERTY = "outputRoot";
  private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HHmmssSSS");

  protected Supplier<LocalDateTime> currentDateTimeSupplier = LocalDateTime::now;
  protected Supplier<FileSystem> fileSystemSupplier = FileSystems::getDefault;
  protected Path outputRoot;
  protected Supplier<Controller> controllerSupplier;
  protected Consumer<Controller> controllerConsumer;

  public CommandLineProcessor(Supplier<Controller> controllerSupplier) {
    this.controllerSupplier = controllerSupplier;
    controllerConsumer = r -> r.run();
  }

  public void processCommandLine(String[] args) {
    Controller controller = controllerSupplier.get();

    if (args.length < controller.getMinimumArgCount()) {
      LOG.error(controller.getUsage());
      return;
    }

    outputRoot = generateOutputRootPath();
    controllerConsumer.accept(controller.initialize(outputRoot, args));
  }
  
  public Path getOutputRoot() {
    return outputRoot;
  }

  public Path generateOutputRootPath() {
    String outputRoot = System.getProperty(OUTPUT_ROOT_SYSTEM_PROPERTY);
    if (outputRoot == null) {
      LOG.warn("System property " + OUTPUT_ROOT_SYSTEM_PROPERTY
              + " not specified; output will be written to NullOutputStream.");
      return null;
    }
    return buildOutputRootPath(outputRoot);
  }

  public Path buildOutputRootPath(String outputRoot) {
    LocalDateTime currentDateTime = currentDateTimeSupplier.get();
    Path outputRootPath = fileSystemSupplier.get().getPath(outputRoot,
            currentDateTime.format(DateTimeFormatter.BASIC_ISO_DATE), currentDateTime.format(TIME_FORMAT));
    try {
      outputRootPath = Files.createDirectories(outputRootPath);
    } catch (IOException ioe) {
      throw new UncheckedIOException(ioe);
    }
    LOG.info("Will write output to " + outputRootPath);
    return outputRootPath;
  }
}
