package com.mblub.util.stream;

import java.util.function.Predicate;

public class StreamUtil {

    public static <T> Predicate<T> not(Predicate<T> predicate) {
        return predicate.negate();
    }
}
