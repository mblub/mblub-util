package com.mblub.util.run;

import java.io.PrintStream;
import java.util.function.Supplier;

public interface Controller extends Runnable {

  public default int getMinimumArgCount() {
    return 0;
  }

  public default String getUsage() {
    return "command must include at least " + getMinimumArgCount() + " argument(s)";
  }

  @SuppressWarnings("unused")
  public default Controller initialize(Supplier<PrintStream> outStreamSupplier, String[] args) {
    return this;
  }
}
