package com.brahvim.nerd.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.function.Consumer;

// Brought to you, from my other (currently supa'-dupa' secret, ";P!) project, "AGC"!:
// ...And yes, it got TOTALLY changed here! Woohoo!

/**
 * <h3>A serialization utility.</h3> Have data serialized to {@code byte[]}s and
 * files, and also deserialized from them!
 * <p>
 * Many methods take {@link Consumer} instances as arguments to provide a way to
 * handle exceptions. ...simply because
 * overloading would not be allowed otherwise!
 */

// Bite Cereal, ":D!
// "Just add milk!"
// ...Throw-away all exceptions!!!

public class NerdByteSerialUtils {

	private NerdByteSerialUtils() {
		NerdReflectionUtils.rejectStaticClassInstantiationFor(this.getClass());
	}

	/**
	 * @param p_object is the object to convert to bytes.
	 * @return A {@code byte[0]} if a serialization error occurs, or if
	 *         {@code p_object} is {@code null}.
	 */
	public static byte[] toBytes(final Serializable p_object) {
		if (p_object == null)
			return new byte[0];

		try (final ByteArrayOutputStream bos = new ByteArrayOutputStream();
				final ObjectOutputStream oos = new ObjectOutputStream(bos)) {
			oos.writeObject(p_object);
			oos.flush();
			bos.flush();
			return bos.toByteArray();
		} catch (final IOException e) {
			e.printStackTrace();
		}

		return new byte[0];
	}

	/** @deprecated Broken! Bummer... */
	@Deprecated
	public static void copyFieldValues(final Object p_from, final Object p_to) {
		NerdByteSerialUtils.copyFieldValues(p_from, p_to, new String[0], 0);
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
		loop1: for (final Field f : p_to.getClass().getFields()) {
			final String name = f.getName();

			for (final String s : p_fieldsToSkip)
				if (name.equals(s))
					continue loop1;

			final boolean editable = !("serialVersionUID".equals(name)
					// || Modifier.isTransient(fieldModifiers)
					// || Modifier.isPublic(fieldModifiers)
					|| (f.getModifiers() & p_fieldExclusionFlagsEnum) != 0);

			if (editable) { // ..that is not the `serialVersionUID`, ~~nor `static`, nor `transient`~~...
				f.setAccessible(true); // ..making sure it is accessible, if it is not...
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
					f.set(p_to, value);
				} catch (final IllegalAccessException e) {
					// Bruh I wrote some scripture up there x)
					e.printStackTrace();
				}
			}
		}
	}

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

	// region From bytes!
	@SuppressWarnings("unchecked")
	public static <T> T fromBytes(final byte[] p_data) {
		try {
			return (T) NerdByteSerialUtils.fromBytesImpl(p_data);
		} catch (final ClassNotFoundException | ClassCastException | IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T> T fromBytes(final byte[] p_data, final Consumer<IOException> p_onIo) {
		try {
			return (T) NerdByteSerialUtils.fromBytesImpl(p_data);
		} catch (final IOException e) {
			if (p_onIo == null)
				e.printStackTrace();
			else
				p_onIo.accept(e);
		} catch (final ClassCastException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T> T fromBytes(final byte[] p_data, final Consumer<IOException> p_onIo,
			final Consumer<ClassCastException> p_onClassCast) {
		try {
			return (T) NerdByteSerialUtils.fromBytesImpl(p_data);
		} catch (final IOException e) {
			if (p_onIo == null)
				e.printStackTrace();
			else
				p_onIo.accept(e);
		} catch (final ClassCastException e) {
			if (p_onClassCast == null)
				e.printStackTrace();
			else
				p_onClassCast.accept(e);
		} catch (final ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T> T fromBytes(final byte[] p_data, final Consumer<ClassNotFoundException> p_onClassNotFound,
			final Consumer<ClassCastException> p_onClassCast, final Consumer<IOException> p_onIo) {
		try {
			return (T) NerdByteSerialUtils.fromBytesImpl(p_data);
		} catch (final IOException e) {
			if (p_onIo == null)
				e.printStackTrace();
			else
				p_onIo.accept(e);
		} catch (final ClassCastException e) {
			if (p_onClassCast == null)
				e.printStackTrace();
			else
				p_onClassCast.accept(e);
		} catch (final ClassNotFoundException e) {
			if (p_onClassNotFound == null)
				e.printStackTrace();
			else
				p_onClassNotFound.accept(e);
		}
		return null;
	}

	/**
	 * Deserialize an object from bytes, then edit a given one to match the
	 * deserialized one. Only you can guarantee the equality of their types!
	 *
	 * @deprecated since it uses
	 *             {@linkplain NerdByteSerialUtils#copyFieldValues(Object, Object)
	 *             NerdByteSerialUtils::copyFieldValues(Object, Object)}, which is
	 *             itself deprecated.
	 * @param p_data   the bytes to deserialize back to an object.
	 * @param p_object the object to transform into the from the bytes!
	 */
	@Deprecated
	public static void fromBytesAssigning(final byte[] p_data, final Object p_object) {
		try { // Deserialize the object:
			final Object deserialized = NerdByteSerialUtils.fromBytesImpl(p_data);
			NerdByteSerialUtils.copyFieldValues(p_object, deserialized);
		} catch (final IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static Object fromBytesImpl(final byte[] p_data) throws IOException, ClassNotFoundException {
		try (final ByteArrayInputStream bis = new ByteArrayInputStream(p_data);
				final ObjectInputStream ois = new ObjectInputStream(bis)) {
			return ois.readObject();
		}
	}
	// endregion

	// region From files!
	// region ...using generics and casting!
	// region Using the file's path.
	@SuppressWarnings("unchecked")
	public static <T> T fromFileCasted(final String p_filePath)
			throws IOException, ClassNotFoundException, ClassCastException {
		return (T) NerdByteSerialUtils.fromFileImpl(new File(p_filePath));
	}

	@SuppressWarnings("unchecked")
	public static <T> T fromFileCasted(final String p_filePath, final Consumer<IOException> p_onIo) {
		try {
			return (T) NerdByteSerialUtils.fromFileImpl(new File(p_filePath));
		} catch (final ClassNotFoundException | ClassCastException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			if (p_onIo == null)
				e.printStackTrace();
			else
				p_onIo.accept(e);
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T> T fromFileCasted(final String p_filePath, final Consumer<IOException> p_onIo,
			final Consumer<ClassNotFoundException> p_onClassNotFound) {
		try {
			return (T) NerdByteSerialUtils.fromFileImpl(new File(p_filePath));
		} catch (final ClassNotFoundException e) {
			if (p_onClassNotFound == null)
				e.printStackTrace();
			else
				p_onClassNotFound.accept(e);
		} catch (final IOException e) {
			if (p_onIo == null)
				e.printStackTrace();
			else
				p_onIo.accept(e);
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T> T fromFileCasted(final String p_filePath, final Consumer<IOException> p_onIo,
			final Consumer<ClassCastException> p_onClassCast,
			final Consumer<ClassNotFoundException> p_onClassNotFound) {
		try {
			return (T) NerdByteSerialUtils.fromFileImpl(new File(p_filePath));
		} catch (final ClassNotFoundException e) {
			if (p_onClassNotFound == null)
				e.printStackTrace();
			else
				p_onClassNotFound.accept(e);
		} catch (final ClassCastException e) {
			if (p_onClassCast == null)
				e.printStackTrace();
			else
				p_onClassCast.accept(e);
		} catch (final IOException e) {
			if (p_onIo == null)
				e.printStackTrace();
			else
				p_onIo.accept(e);
		}

		return null;
	}
	// endregion

	// region Using a `File` object.
	@SuppressWarnings("unchecked")
	public static <T> T fromFileCasted(final File p_file)
			throws IOException, ClassNotFoundException, ClassCastException {
		return (T) NerdByteSerialUtils.fromFileImpl(p_file);
	}

	@SuppressWarnings("unchecked")
	public static <T> T fromFileCasted(final File p_file, final Consumer<IOException> p_onIo) {
		try {
			return (T) NerdByteSerialUtils.fromFileImpl(p_file);
		} catch (final ClassNotFoundException | ClassCastException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			if (p_onIo == null)
				e.printStackTrace();
			else
				p_onIo.accept(e);
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T> T fromFileCasted(final File p_file, final Consumer<IOException> p_onIo,
			final Consumer<ClassNotFoundException> p_onClassNotFound) {
		try {
			return (T) NerdByteSerialUtils.fromFileImpl(p_file);
		} catch (final ClassNotFoundException e) {
			if (p_onClassNotFound == null)
				e.printStackTrace();
			else
				p_onClassNotFound.accept(e);
		} catch (final IOException e) {
			if (p_onIo == null)
				e.printStackTrace();
			else
				p_onIo.accept(e);
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T> T fromFileCasted(final File p_file, final Consumer<IOException> p_onIo,
			final Consumer<ClassCastException> p_onClassCast,
			final Consumer<ClassNotFoundException> p_onClassNotFound) {
		try {
			return (T) NerdByteSerialUtils.fromFileImpl(p_file);
		} catch (final ClassNotFoundException e) {
			if (p_onClassNotFound == null)
				e.printStackTrace();
			else
				p_onClassNotFound.accept(e);
		} catch (final ClassCastException e) {
			if (p_onClassCast == null)
				e.printStackTrace();
			else
				p_onClassCast.accept(e);
		} catch (final IOException e) {
			if (p_onIo == null)
				e.printStackTrace();
			else
				p_onIo.accept(e);
		}

		return null;
	}
	// endregion
	// endregion

	public static <T> T fromFile(final String p_filePath) {
		try {
			return NerdByteSerialUtils.fromFileImpl(new File(p_filePath));
		} catch (final IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static <T> T fromFile(final String p_filePath, final Consumer<IOException> p_onIo) {
		try {
			return NerdByteSerialUtils.fromFileImpl(new File(p_filePath));
		} catch (final IOException e) {
			if (p_onIo == null)
				e.printStackTrace();
			else
				p_onIo.accept(e);
		} catch (final ClassNotFoundException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static <T> T fromFile(final String p_filePath, final Consumer<IOException> p_onIo,
			final Consumer<ClassNotFoundException> p_onClassNotFound) {
		try {
			return NerdByteSerialUtils.fromFileImpl(new File(p_filePath));
		} catch (final ClassNotFoundException e) {
			if (p_onClassNotFound == null)
				e.printStackTrace();
			else
				p_onClassNotFound.accept(e);
		} catch (final IOException e) {
			if (p_onIo == null)
				e.printStackTrace();
			else
				p_onIo.accept(e);
		}

		return null;
	}

	public static <T> T fromFile(final File p_file) {
		try {
			return NerdByteSerialUtils.fromFileImpl(p_file);
		} catch (final IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static <T> T fromFile(final File p_file, final Consumer<IOException> p_onIo) {
		try {
			return NerdByteSerialUtils.fromFileImpl(p_file);
		} catch (final IOException e) {
			if (p_onIo == null)
				e.printStackTrace();
			else
				p_onIo.accept(e);
		} catch (final ClassNotFoundException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static <T> T fromFile(final File p_file, final Consumer<IOException> p_onIo,
			final Consumer<ClassNotFoundException> p_onClassNotFound) {
		try {
			return NerdByteSerialUtils.fromFileImpl(p_file);
		} catch (final ClassNotFoundException e) {
			if (p_onClassNotFound == null)
				e.printStackTrace();
			else
				p_onClassNotFound.accept(e);
		} catch (final IOException e) {
			if (p_onIo == null)
				e.printStackTrace();
			else
				p_onIo.accept(e);
		}

		return null;
	}

	/**
	 * Deserialize an object from a file, then edit a given one to match the
	 * deserialized one. Only you can guarantee
	 * the equality of their types!
	 *
	 * @deprecated since it uses
	 *             {@link NerdByteSerialUtils#fromBytesAssigning(byte[], Object)
	 *             NerdByteSerialUtils::fromBytesAssigning(byte[], Object)},
	 *             which is deprecated, since it uses
	 *             {@link NerdByteSerialUtils#copyFieldValues(Object, Object)
	 *             NerdByteSerialUtils::copyFieldValues(Object, Object)},
	 *             which is also deprecated.
	 * @param p_file   the file to deserialize an object from!
	 * @param p_object the object to transform into the from the bytes!
	 */
	@Deprecated
	public static void fromFileAssigning(final File p_file, final Object p_object) {
		try (final FileInputStream fis = new FileInputStream(p_file);) {
			final byte[] objectData = fis.readAllBytes();
			NerdByteSerialUtils.fromBytesAssigning(objectData, p_object);
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private static <T> T fromFileImpl(final File p_file) throws IOException, ClassNotFoundException {
		try (final FileInputStream fis = new FileInputStream(p_file);
				final ObjectInputStream ois = new ObjectInputStream(fis);) {
			return (T) ois.readObject();
		}
	}
	// endregion

	// region ...TO files!
	public static void toFile(final Serializable p_object, final String p_fileName) {
		NerdByteSerialUtils.toFile(p_object, new File(p_fileName));
	}

	public static void toFile(final Serializable p_object, final String p_fileName,
			final Consumer<IOException> p_onIo) {
		NerdByteSerialUtils.toFile(p_object, new File(p_fileName), p_onIo);
	}

	public static void toFile(final Serializable p_object, final File p_file) {
		NerdByteSerialUtils.toFile(p_object, p_file, null);
	}

	// The actual implementation:
	public static void toFile(final Serializable p_object, final File p_file, final Consumer<IOException> p_onIo) {
		if (p_object == null)
			return;

		try (final FileOutputStream fos = new FileOutputStream(p_file);
				final ObjectOutputStream oos = new ObjectOutputStream(fos)) {
			oos.writeObject(p_object);
		} catch (final IOException e) {
			if (p_onIo == null)
				e.printStackTrace();
			else
				p_onIo.accept(e);
		}
	}
	// endregion

}
