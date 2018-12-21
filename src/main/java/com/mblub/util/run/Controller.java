package com.mblub.util.run;

import java.nio.file.Path;

public interface Controller extends Runnable {

  public default int getMinimumArgCount() {
    return 0;
  }

  public default String getUsage() {
    return "command must include at least " + getMinimumArgCount() + " argument(s)";
  }

  public default Controller initialize(Path outputRootPath, String[] args) {
    return this;
  }
}
