package com.brahvim.nerd.api;

import java.util.function.Consumer;

// ...Please forgive me.
public class NerdUtils {

    private NerdUtils() {
        System.err.println("""
                ...Just like `java.util.Objects`, nope, \
                I can't offer you an instance of me! Sorry...""");
    }

    /**
     * Call a method on the given object, if neither is {@code null}.
     */
    // ChatGPT wrote this... (modified):
    public static <T> void callIfNotNull(final T p_object, final Consumer<T> p_method) {
        // ^^^ /* ...`T` and not `ObjectT`! */
        if (!(p_object == null || p_method == null))
            p_method.accept(p_object);
    }

}
