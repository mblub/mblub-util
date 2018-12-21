package com.mblub.util.run;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommandLineProcessor {
  protected static final Logger LOG = LogManager.getLogger(CommandLineProcessor.class);
  protected Supplier<Path> outputRootSupplier;
  protected Supplier<Controller> controllerSupplier;
  protected Consumer<Controller> controllerConsumer;

  public CommandLineProcessor(Supplier<Controller> controllerSupplier) {
    this.controllerSupplier = controllerSupplier;
    // TODO: find some platform-independent way to do this, or maybe use a
    // Void/Mock FileSystem
    outputRootSupplier = () -> Paths.get("/dev/null");
    controllerConsumer = r -> r.run();
  }

  public void processCommandLine(String[] args) {
    Controller controller = controllerSupplier.get();

    if (args.length < controller.getMinimumArgCount()) {
      LOG.error(controller.getUsage());
      return;
    }

    controllerConsumer.accept(controller.initialize(outputRootSupplier.get(), args));
  }
}
