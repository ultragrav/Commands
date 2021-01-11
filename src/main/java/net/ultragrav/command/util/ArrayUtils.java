package net.ultragrav.command.util;

import com.google.common.collect.Lists;

import java.util.List;
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

    public static List<String> listNonNull(String[] strs) {
        List<String> args = Lists.newArrayList();
        for (int i = 0; i < strs.length - 1; i++) {
            String str = strs[i];
            if (str == null) continue;
            if (str.isEmpty()) continue;
            args.add(str);
        }
        args.add(strs[strs.length - 1]);
        return args;
    }
}