package net.ultragrav.command.util;

import java.util.function.Function;

public class ArrayUtils {
    public static <I, O> O[] convert(I[] arr, Function<I, O> func, Function<Integer, O[]> arraySupplier) {
        O[] ret = arraySupplier.apply(arr.length);
        for (int i = 0; i < arr.length; i ++) {
            ret[i] = func.apply(arr[i]);
        }
        return ret;
    }

    public static <T> String[] toString(T[] arr) {
        return convert(arr, T::toString, String[]::new);
    }

    public static <T extends Enum<T>> String[] enumName(T[] arr) {
        return convert(arr, T::name, String[]::new);
    }
}
