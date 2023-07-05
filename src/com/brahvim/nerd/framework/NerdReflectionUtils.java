package com.brahvim.nerd.framework;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class NerdReflectionUtils {

    private NerdReflectionUtils() {
        throw new Error("Sorry, but `"
                + this.getClass().getCanonicalName()
                + "` is an uninstantiable, helper class.");
    }

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

    public static <ObjectT, FirstArgT, SecondArgT, ThirdArgT> void callIfNotNull(
            final ObjectT p_object, final NerdQuadConsumer<ObjectT, FirstArgT, SecondArgT, ThirdArgT> p_methodReference,
            final FirstArgT p_arg1, final SecondArgT p_arg2, final ThirdArgT p_arg3) {
        if (!(p_object == null || p_methodReference == null))
            p_methodReference.accept(p_object, p_arg1, p_arg2, p_arg3);
    }

    @SafeVarargs
    public static final boolean isMethodOverridden(
            final Object p_object,
            final String p_methodName,
            final Class<?>... p_parameterTypes) {
        try {
            p_object.getClass().getDeclaredMethod(p_methodName, p_parameterTypes);
            return true; // Method exists in the class, indicating it was overridden!
        } catch (final NoSuchMethodException e) {
            return false;
        }
    }

}
