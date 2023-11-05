package com.brahvim.nerd.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class NerdReflectionUtils {

	private NerdReflectionUtils() {
		NerdReflectionUtils.rejectStaticClassInstantiationFor(this.getClass());
	}

	public static void rejectStaticClassInstantiationFor(final Object p_object) {
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
	public static final boolean isMethodOverridden(final Object p_object, final String p_methodName,
			final Class<?>... p_parameterTypes) {
		try {
			p_object.getClass().getDeclaredMethod(p_methodName, p_parameterTypes);
			return true; // Method exists in the class, indicating it was overridden!
		} catch (final NoSuchMethodException e) {
			return false;
		}
	}

	public static Class<?> getFirstTypeArg(final Object p_object) {
		final Class<?>[] toRet = NerdReflectionUtils.getTypeArgs(p_object);
		if (toRet.length == 0)
			return null;
		return toRet[0];
	}

	public static Class<?>[] getTypeArgs(final Object p_object) {
		// ...Trivial reflection tricks are where ChatGPT is my best friend ._.
		if (p_object == null)
			return new Class<?>[0];

		final Type genericSuperclass = p_object.getClass().getGenericSuperclass();

		if (!(genericSuperclass instanceof ParameterizedType))
			return new Class<?>[0];

		// One, derive the actual type arguments. Two, cast back!:
		return (Class<?>[]) ((ParameterizedType) genericSuperclass).getActualTypeArguments();
	}

}
