package com.mblub.util;

import java.util.Optional;
import java.util.function.Predicate;

public class OptionalEx<T> {
  private Optional<T> delegate;
  
  private OptionalEx(Optional<T> delegate) {
    this.delegate = delegate;
  }
  
  public static <T> OptionalEx<T> ofOptional(Optional<T> delegate) {
    return new OptionalEx<>(delegate);
  }
  
  public static <T> OptionalEx<T> of(T value) {
    return ofOptional(Optional.of(value));
  }
  
  public boolean isPresent() {
    return delegate.isPresent();
  }
  
  public T get() {
    return delegate.get();
  }
  
  public OptionalEx<T> filter(Predicate<? super T> predicate) {
    delegate = delegate.filter(predicate);
    return this;
  }
  
  public OptionalEx<T> orAlternate(Optional<T> alternate) {
    if (! delegate.isPresent()) delegate = alternate;
    return this;
  }
}
