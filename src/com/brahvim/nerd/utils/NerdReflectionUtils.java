package com.brahvim.nerd.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.brahvim.nerd.utils.java_function_extensions.NerdQuadConsumer;
import com.brahvim.nerd.utils.java_function_extensions.NerdTriConsumer;

public class NerdReflectionUtils {

	private NerdReflectionUtils() {
		NerdReflectionUtils.rejectStaticClassInstantiationFor(this);
	}

	/**
	 * <h1>Have you written a utility class?</h1>
	 * Let a call to this method be the <i>only</i> statement
	 * in said class's private null-constructor - just as a good practice!
	 * <br>
	 * </br>
	 * I bet you won't be disappointed!
	 *
	 * @throws IllegalAccessError
	 *
	 */
	public static void rejectStaticClassInstantiationFor(final Object p_object) throws IllegalAccessError {
		throw new IllegalAccessError("Please instantiate `" + p_object.getClass().getSimpleName()
				+ "`es the way they're supposed to be! Sorry...");
	}

	public static <ObjectT> void callIfNotNull(final ObjectT p_object, final Consumer<ObjectT> p_methodReference) {
		if (!(p_object == null || p_methodReference == null))
			p_methodReference.accept(p_object);
	}

	public static <ObjectT, ArgT> void callIfNotNull(final ObjectT p_object,
			final BiConsumer<ObjectT, ArgT> p_methodReference, final ArgT p_arg) {
		if (!(p_object == null || p_methodReference == null))
			p_methodReference.accept(p_object, p_arg);
	}

	public static <ObjectT, FirstArgT, SecondArgT> void callIfNotNull(final ObjectT p_object,
			final NerdTriConsumer<ObjectT, FirstArgT, SecondArgT> p_methodReference, final FirstArgT p_arg1,
			final SecondArgT p_arg2) {
		if (!(p_object == null || p_methodReference == null))
			p_methodReference.accept(p_object, p_arg1, p_arg2);
	}

	public static <ObjectT, FirstArgT, SecondArgT, ThirdArgT> void callIfNotNull(final ObjectT p_object,
			final NerdQuadConsumer<ObjectT, FirstArgT, SecondArgT, ThirdArgT> p_methodReference, final FirstArgT p_arg1,
			final SecondArgT p_arg2, final ThirdArgT p_arg3) {
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

	@SuppressWarnings("unchecked")
	public static <RetT> Class<? extends RetT> getFirstTypeArg(final Object p_object) {
		if (p_object == null)
			return null;

		final Type genericSuperClass = p_object.getClass().getGenericSuperclass();

		if (genericSuperClass instanceof final ParameterizedType parameterizedType) {
			final Type[] typeArguments = parameterizedType.getActualTypeArguments();

			if (typeArguments.length > 0 && typeArguments[0] instanceof Class)
				return (Class<? extends RetT>) typeArguments[0];
		}

		throw new IllegalArgumentException("Unable to determine the type parameter!");
	}

	public static Type[] getTypeArgsAsTypes(final Object p_object) {
		// ...Trivial reflection tricks are where ChatGPT is my best friend ._.
		if (p_object == null)
			return new Class<?>[0];

		final Type genericSuperclass = p_object.getClass().getGenericSuperclass();

		// Impossible case?:
		if (!(genericSuperclass instanceof ParameterizedType))
			return new Class<?>[0];

		// One, derive the actual type arguments. Two, cast back!:
		return ((ParameterizedType) genericSuperclass).getActualTypeArguments();
	}

	/*
	 * public static Class<?>[] getTypeParameterClasses(final Class<?> p_class) {
	 * final Type genericSuperClass = p_class.getGenericSuperclass();
	 * 
	 * if (genericSuperClass instanceof final ParameterizedType parameterizedType) {
	 * final Type[] typeArguments = parameterizedType.getActualTypeArguments();
	 * 
	 * if (typeArguments.length > 0) {
	 * for (final var i : typeArguments)
	 * if (!(i instanceof Class<?>))
	 * return new Class<?>[0];
	 * return (Class<?>[]) typeArguments;
	 * }
	 * }
	 * 
	 * throw new IllegalArgumentException("Unable to determine the type parameter");
	 * }
	 */

}
