package com.brahvim.nerd.framework;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class NerdCallUtils {

    public static <ObjectT> void callIfNotNull(
            final ObjectT p_object, final Consumer<ObjectT> p_methodReference) {
        if (!(p_object == null || p_methodReference == null))
            p_methodReference.accept(p_object);
    }

    public static <ObjectT, ArgT> void callIfNotNull(
            final ObjectT p_object, final BiConsumer<ObjectT, ArgT> p_methodReference, final ArgT p_arg) {
        if (!(p_object == null || p_methodReference == null))
            p_methodReference.accept(p_object, p_arg);
    }

    public static <ObjectT, FirstArgT, SecondArgT> void callIfNotNull(
            final ObjectT p_object, final NerdTriConsumer<ObjectT, FirstArgT, SecondArgT> p_methodReference,
            final FirstArgT p_arg1, final SecondArgT p_arg2) {
        if (!(p_object == null || p_methodReference == null))
            p_methodReference.accept(p_object, p_arg1, p_arg2);
    }

}
