package com.brahvim.nerd.framework;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;

// ...Please forgive me. I know people don't like such classes!
public class NerdUtils {

    private NerdUtils() {
        System.err.println(
                "...Just like `java.util.Objects`, nope, I can't offer you an instance of me. Sorry...");
    }

    /**
     * Calls a method on the given object, if neither argument is {@code null}.
     * 
     * @author ChatGPT
     */
    // ChatGPT wrote this... (modified):
    public static <ObjectT> void callIfNotNull(final ObjectT p_object, final Consumer<ObjectT> p_method) {
        if (!(p_object == null || p_method == null))
            p_method.accept(p_object);
    }

    /**
     * Calls a method on a given object with the given arguments, if and only if
     * neither the method or the object, are {@code null}.
     * 
     * @param <ObjectT> - the type of the object to call the method on!
     * @param p_object  - the object to call the method on!
     * @param p_method  - the method reference to call.
     * @param p_args    - the arguments to pass to the method.
     * @throws IllegalArgumentException if the object or method reference is
     *                                  {@code null}!
     * @author ChatGPT
     */
    public static <ObjectT> void callWithArgsIfNotNull(
            final ObjectT p_object, final Consumer<ObjectT> p_method, final Object... p_args) {
        Objects.requireNonNull(p_object, "The object passed in cannot be `null`!");
        Objects.requireNonNull(p_method, "The method reference passed in cannot be `null`!");

        // If there are no arguments, ...do this!:
        if (p_args == null || p_args.length == 0)
            p_method.accept(p_object);
        else {
            // Get the arguments' types to ensure that the array passed has them correct:
            final Class<?>[] argTypes = Arrays.stream(p_args)
                    .map(Object::getClass)
                    .toArray(Class[]::new);
            try {
                p_object.getClass()
                        .getMethod(p_method.toString(), argTypes)
                        .invoke(p_object, p_args);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }

}
