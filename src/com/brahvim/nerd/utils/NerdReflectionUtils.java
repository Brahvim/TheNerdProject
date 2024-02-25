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
	public static void rejectStaticClassInstantiationFor(final Object p_object) throws InstantiationError {
		throw new InstantiationError("Please instantiate `" + p_object.getClass().getSimpleName()
				+ "`es the way they're supposed to be! Sorry...");
	}

	// region Call method if object isn't `null`.
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
	// endregion

	// region Copy field values (like serialization! This guy got deprecated...)
	/** @deprecated Broken! Bummer... */
	@Deprecated
	public static void copyFieldValues(final Object p_from, final Object p_to) {
		NerdReflectionUtils.copyFieldValues(p_from, p_to, new String[0], 0);
	}

	/** @deprecated Broken! Bummer... */
	@Deprecated
	public static void copyFieldValues(
			final Object p_from,
			final Object p_to,
			final String[] p_fieldsToSkip,
			final int p_fieldExclusionFlagsEnum) {
		if (!p_from.getClass().isAssignableFrom(p_to.getClass()))
			throw new UnsupportedOperationException("Cannot copy fields of objects from different hierarchies!");

		// For each field in the object to copy to...
		loop1: for (final var f : p_to.getClass().getFields()) {
			final String name = f.getName();

			for (final var s : p_fieldsToSkip)
				if (name.equals(s))
					continue loop1;

			final boolean editable = !("serialVersionUID".equals(name)
					// || Modifier.isTransient(fieldModifiers)
					// || Modifier.isPublic(fieldModifiers)
					|| (f.getModifiers() & p_fieldExclusionFlagsEnum) != 0);

			if (editable) { // ..that is not the `serialVersionUID`, ~~nor `static`, nor `transient`~~...
				f.setAccessible(true); // ..making sure it is accessible, if it is not... NOSONAR!
				try {
					final Object value = f.get(p_from); // ...we get the value of.
					// ...Upon checking the types, if the the field, from the object to copy from,
					// has a hierarchy that differs from the other...
					if ( // value != null && // What if the value is actually `null`?!
					!f.getType().isAssignableFrom(value.getClass()))
						// ...we work no longer.
						throw new IllegalArgumentException(String.format(
								"Incompatible field types: `%s` and `%s`!",
								f.getType().getName(),
								value.getClass().getName()));
					// But if it shows the slightest of similarities, we go on:
					f.set(p_to, value); // NOSONAR! Let me do this!
				} catch (final IllegalAccessException e) {
					// Bruh I wrote some scripture up there x)
					e.printStackTrace();
				}
			}
		}
	}
	// endregion

	/**
	 * <p>
	 * Suppose we have a class, {@code C}, which extends {@code B}, which extends
	 * {@code A}. This method will therefore
	 * return {@code 3} when given an object of class {@code C}.
	 *
	 * @param p_object is the object whose class hierarchy depth is to be found.
	 * @return Returns the depth of the hierarchy of the class of the given object.
	 */
	public static int getClassHierarchyDepthOf(final Object p_object) {
		int depth = 0;
		Class<?> currentClass = p_object.getClass();

		while (currentClass.getSuperclass() != null) {
			currentClass = currentClass.getSuperclass();
			depth++;
		}

		return depth;
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

		throw new IllegalStateException("Unable to determine the type parameter!");
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
