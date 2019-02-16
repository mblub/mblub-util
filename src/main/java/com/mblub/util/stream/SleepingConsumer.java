package com.mblub.util.stream;

import java.util.function.Consumer;

public class SleepingConsumer<T> implements Consumer<T> {
  protected int sleepSeconds = 0;
  protected long sleepMillis = 0L;

  public int getSleepSeconds() {
    return sleepSeconds;
  }

  public void setSleepSeconds(int sleepSeconds) {
    this.sleepSeconds = sleepSeconds;
    this.sleepMillis = sleepSeconds * 1000L;
  }

  public SleepingConsumer<T> withSleepSeconds(int sleepSeconds) {
    setSleepSeconds(sleepSeconds);
    return this;
  }

  public long getSleepMillis() {
    return sleepMillis;
  }

  public void setSleepMillis(long sleepMillis) {
    this.sleepMillis = sleepMillis;
  }

  public SleepingConsumer<T> withSleepMillis(long sleepMillis) {
    setSleepMillis(sleepMillis);
    return this;
  }

  @Override
  public void accept(T t) {
    try {
      Thread.sleep(sleepMillis);
    } catch (InterruptedException e) {
      // TODO: how should this be handled, if at all?
    }
  }
}
