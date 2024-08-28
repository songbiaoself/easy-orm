package com.coderevolt.util;

import java.util.function.Function;

@FunctionalInterface
public interface SelectFunction<T, R> extends Function<T, R> {
}
