package com.brahvim.nerd.io;

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
import java.lang.reflect.Modifier;
import java.util.function.Consumer;

import jogamp.newt.MonitorModeProps.Cache;

// Brought to you, from my other (currently supa'-dupa' secret, ";P!) project, "AGC"!:
// ...And yes, it got TOTALLY changed here! Woohoo!

/**
 * <h3>A serialization utility.</h3>
 *
 * Have data serialized to {@code byte[]}s and files, and also deserialized from
 * them!
 * <p>
 * Many methods take {@link Consumer} instances as arguments to provide a way to
 * handle exceptions. ...simply because overloading would not be allowed
 * otherwise!
 */

// Bite Cereal, ":D!
// "Just add milk!"
// ...Throw-away all exceptions!!!

public class NerdByteSerial {

	private NerdByteSerial() {
		throw new Error("Sorry, but `"
				+ this.getClass().getCanonicalName()
				+ "` is an uninstantiable, helper class.");
	}

	public static byte[] toBytes(final Serializable p_object) {
		if (p_object == null)
			return null;

		try (
				final ByteArrayOutputStream bos = new ByteArrayOutputStream();
				final ObjectOutputStream oos = new ObjectOutputStream(bos)) {
			oos.writeObject(p_object);
			oos.flush();
			bos.flush();
			return bos.toByteArray();
		} catch (final IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	// region From bytes!
	@SuppressWarnings("unchecked")
	public static <T> T fromBytes(final byte[] p_data) {
		try {
			return (T) NerdByteSerial.fromBytesImpl(p_data);
		} catch (final ClassNotFoundException | ClassCastException | IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T> T fromBytes(
			final byte[] p_data,
			final Consumer<IOException> p_onIo) {
		try {
			return (T) NerdByteSerial.fromBytesImpl(p_data);
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
	public static <T> T fromBytes(
			final byte[] p_data,
			final Consumer<IOException> p_onIo,
			final Consumer<ClassCastException> p_onClassCast) {
		try {
			return (T) NerdByteSerial.fromBytesImpl(p_data);
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
	public static <T> T fromBytes(
			final byte[] p_data,
			final Consumer<ClassNotFoundException> p_onClassNotFound,
			final Consumer<ClassCastException> p_onClassCast,
			final Consumer<IOException> p_onIo) {
		try {
			return (T) NerdByteSerial.fromBytesImpl(p_data);
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
	 * @param p_data   the bytes to deserialize back to an object.
	 * @param p_object the object to transform into the from the bytes!
	 */
	public static void fromBytesAssigning(final byte[] p_data, final Object p_object) {
		try { // Deserialize the object:
			final Object deserialized = NerdByteSerial.fromBytesImpl(p_data);
			NerdByteSerial.copyObjectFieldData(p_object, deserialized);
		} catch (final IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void copyObjectFieldData(final Object p_from, final Object p_to) {
		if (p_from.getClass().isAssignableFrom(p_to.getClass()))
			throw new UnsupportedOperationException("Cannot copy fields of objects from different hierarchies!");

		// For each field in the object to copy to...
		for (final Field f : p_to.getClass().getFields()) {
			final int fieldModifiers = f.getModifiers();
			final boolean editable = !("serialVersionUID".equals(f.getName())
					|| Modifier.isTransient(fieldModifiers)
					|| Modifier.isPublic(fieldModifiers));

			if (editable) { // ..that is not the `serialVersionUID`, nor `static`, nor `transient`...
				f.setAccessible(true); // ..making sure it is accessible if it is not...
				try {
					final Object value = f.get(p_from); // ..we get the value of.
					// ...Upon checking the types, if the the field, from the object to copy from,
					// has a hierarchy that differs from the other...
					if (value != null && !f.getType().isAssignableFrom(value.getClass())) {
						// ...we work no longer.
						throw new IllegalArgumentException("Incompatible field types: `" +
								f.getType().getName() + "` and `" + value.getClass().getName() + "`");
					}
					// But if it shows the slightest of similarities, we go on:
					f.set(p_to, value);
				} catch (final IllegalAccessException e) {
					// Bruh I wrote some scripture up there x)
					e.printStackTrace();
				}
			}
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
		return (T) NerdByteSerial.fromFileImpl(new File(p_filePath));
	}

	@SuppressWarnings("unchecked")
	public static <T> T fromFileCasted(final String p_filePath, final Consumer<IOException> p_onIo) {
		try {
			return (T) NerdByteSerial.fromFileImpl(new File(p_filePath));
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
			return (T) NerdByteSerial.fromFileImpl(new File(p_filePath));
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
	public static <T> T fromFileCasted(
			final String p_filePath,
			final Consumer<IOException> p_onIo,
			final Consumer<ClassCastException> p_onClassCast,
			final Consumer<ClassNotFoundException> p_onClassNotFound) {
		try {
			return (T) NerdByteSerial.fromFileImpl(new File(p_filePath));
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
		return (T) NerdByteSerial.fromFileImpl(p_file);
	}

	@SuppressWarnings("unchecked")
	public static <T> T fromFileCasted(final File p_file, final Consumer<IOException> p_onIo) {
		try {
			return (T) NerdByteSerial.fromFileImpl(p_file);
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
			return (T) NerdByteSerial.fromFileImpl(p_file);
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
	public static <T> T fromFileCasted(
			final File p_file,
			final Consumer<IOException> p_onIo,
			final Consumer<ClassCastException> p_onClassCast,
			final Consumer<ClassNotFoundException> p_onClassNotFound) {
		try {
			return (T) NerdByteSerial.fromFileImpl(p_file);
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
			return NerdByteSerial.fromFileImpl(new File(p_filePath));
		} catch (final IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static <T> T fromFile(final String p_filePath, final Consumer<IOException> p_onIo) {
		try {
			return NerdByteSerial.fromFileImpl(new File(p_filePath));
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
			return NerdByteSerial.fromFileImpl(new File(p_filePath));
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
			return NerdByteSerial.fromFileImpl(p_file);
		} catch (final IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static <T> T fromFile(final File p_file, final Consumer<IOException> p_onIo) {
		try {
			return NerdByteSerial.fromFileImpl(p_file);
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
			return NerdByteSerial.fromFileImpl(p_file);
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
	 * deserialized one. Only you can guarantee the equality of their types!
	 * 
	 * @param p_file   the file to deserialize an object from!
	 * @param p_object the object to transform into the from the bytes!
	 */
	public static void fromFileAssigning(final File p_file, final Object p_object) {
		try (final FileInputStream fis = new FileInputStream(p_file);) {
			final byte[] objectData = fis.readAllBytes();
			NerdByteSerial.fromBytesAssigning(objectData, p_object);
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
		NerdByteSerial.toFile(p_object, new File(p_fileName));
	}

	public static void toFile(final Serializable p_object, final String p_fileName,
			final Consumer<IOException> p_onIo) {
		NerdByteSerial.toFile(p_object, new File(p_fileName), p_onIo);
	}

	public static void toFile(final Serializable p_object, final File p_file) {
		NerdByteSerial.toFile(p_object, p_file, null);
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
