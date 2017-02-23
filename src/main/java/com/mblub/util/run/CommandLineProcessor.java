package com.mblub.util.run;

import java.io.PrintStream;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CommandLineProcessor {
  protected Supplier<PrintStream> outStreamSupplier;
  protected Supplier<Controller> controllerSupplier;
  protected Consumer<Controller> controllerConsumer;

  public CommandLineProcessor(Supplier<Controller> controllerSupplier) {
    this.controllerSupplier = controllerSupplier;
    outStreamSupplier = () -> System.out;
    controllerConsumer = r -> r.run();
  }

  public void processCommandLine(String[] args) {
    Controller controller = controllerSupplier.get();

    if (args.length < controller.getMinimumArgCount()) {
      outStreamSupplier.get().println(controller.getUsage());
      return;
    }

    controllerConsumer.accept(controller.initialize(outStreamSupplier, args));
  }
}
