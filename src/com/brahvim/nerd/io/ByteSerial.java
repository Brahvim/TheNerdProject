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
import java.util.function.Consumer;

// Brought to you, from my other (currently supa'-duper secret, ";P!) project, "AGC"!:

// ~~[* The "...using generics and casting!" methods take {@link Consumer<>} instances]~~
/**
 * <h3>A serialization utility.</h3>
 *
 * Have data serialized to {@code byte[]}s and files, and also deserialized from
 * them!<br>
 * <br>
 * Many methods take {@link Consumer<>} instances as arguments simply because
 * overloading would not be allowed otherwise.
 */

// Bite Cereal, ":D!
// "Just add milk!"
// ...Throw-away, all exceptions!~
public class ByteSerial {

    public static byte[] toBytes(Serializable p_object) {
        if (p_object == null)
            return null;

        try {
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(bos)) {
                // bos.flush();
                // oos.flush();

                oos.writeObject(p_object);

                oos.flush();
                oos.close(); // Wait, who decided to use a "try_resources" again?

                bos.flush();
                bos.close();
                return bos.toByteArray();
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    // region From bytes!
    public static Object fromBytes(byte[] p_data) {
        try {
            return ByteSerial.fromBytesImpl(p_data);
        } catch (final IOException e) {
            e.printStackTrace();
        } catch (final ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Object fromBytes(byte[] p_data, Consumer<IOException> p_onIo) {
        try {
            return ByteSerial.fromBytesImpl(p_data);
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

    public static Object fromBytes(byte[] p_data, Consumer<IOException> p_onIo,
            Consumer<ClassNotFoundException> p_onClassNotFound) {
        try {
            return ByteSerial.fromBytesImpl(p_data);
        } catch (final IOException e) {
            if (p_onIo == null)
                e.printStackTrace();
            else
                p_onIo.accept(e);
        } catch (final ClassNotFoundException e) {
            if (p_onClassNotFound == null)
                e.printStackTrace();
            else
                p_onClassNotFound.accept(e);
        }

        return null;
    }

    // region ...using generics and casting!
    @SuppressWarnings("unchecked")
    public static <T> T fromBytesCasted(byte[] p_data) {
        try {
            return (T) ByteSerial.fromBytesImpl(p_data);
        } catch (final ClassNotFoundException e) {
            e.printStackTrace();
        } catch (final ClassCastException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T fromBytesCasted(
            byte[] p_data,
            Consumer<IOException> p_onIo,
            Consumer<ClassCastException> p_onClassCast) {
        try {
            return (T) ByteSerial.fromBytesImpl(p_data);
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
    public static <T> T fromBytesCasted(
            byte[] p_data,
            Consumer<ClassNotFoundException> p_onClassNotFound,
            Consumer<ClassCastException> p_onClassCast,
            Consumer<IOException> p_onIo) {
        try {
            return (T) ByteSerial.fromBytesImpl(p_data);
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
    // endregion

    private static Object fromBytesImpl(byte[] p_data) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(p_data);
                ObjectInputStream ois = new ObjectInputStream(bis)) {
            return ois.readObject();
        }
    }
    // endregion

    // region From files!
    // region ...using generics and casting!
    // region Using the file's path.
    @SuppressWarnings("unchecked")
    public static <T> T fromFileCasted(String p_filePath)
            throws IOException, ClassNotFoundException, ClassCastException {
        return (T) ByteSerial.fromFileImpl(new File(p_filePath));
    }

    @SuppressWarnings("unchecked")
    public static <T> T fromFileCasted(String p_filePath, Consumer<IOException> p_onIo) {
        try {
            return (T) ByteSerial.fromFileImpl(new File(p_filePath));
        } catch (final ClassNotFoundException e) {
            e.printStackTrace();
        } catch (final ClassCastException e) {
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
    public static <T> T fromFileCasted(String p_filePath, Consumer<IOException> p_onIo,
            Consumer<ClassNotFoundException> p_onClassNotFound) {
        try {
            return (T) ByteSerial.fromFileImpl(new File(p_filePath));
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
            String p_filePath,
            Consumer<IOException> p_onIo,
            Consumer<ClassCastException> p_onClassCast,
            Consumer<ClassNotFoundException> p_onClassNotFound) {
        try {
            return (T) ByteSerial.fromFileImpl(new File(p_filePath));
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
    public static <T> T fromFileCasted(File p_file)
            throws IOException, ClassNotFoundException, ClassCastException {
        return (T) ByteSerial.fromFileImpl(p_file);
    }

    @SuppressWarnings("unchecked")
    public static <T> T fromFileCasted(File p_file, Consumer<IOException> p_onIo) {
        try {
            return (T) ByteSerial.fromFileImpl(p_file);
        } catch (final ClassNotFoundException e) {
            e.printStackTrace();
        } catch (final ClassCastException e) {
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
    public static <T> T fromFileCasted(File p_file, Consumer<IOException> p_onIo,
            Consumer<ClassNotFoundException> p_onClassNotFound) {
        try {
            return (T) ByteSerial.fromFileImpl(p_file);
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
            File p_file,
            Consumer<IOException> p_onIo,
            Consumer<ClassCastException> p_onClassCast,
            Consumer<ClassNotFoundException> p_onClassNotFound) {
        try {
            return (T) ByteSerial.fromFileImpl(p_file);
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

    public static Object fromFile(String p_filePath) {
        try {
            return ByteSerial.fromFileImpl(new File(p_filePath));
        } catch (final IOException e) {
            e.printStackTrace();
        } catch (final ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Object fromFile(String p_filePath, Consumer<IOException> p_onIo) {
        try {
            return ByteSerial.fromFileImpl(new File(p_filePath));
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

    public static Object fromFile(String p_filePath, Consumer<IOException> p_onIo,
            Consumer<ClassNotFoundException> p_onClassNotFound) {
        try {
            return ByteSerial.fromFileImpl(new File(p_filePath));
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

    public static Object fromFile(File p_file) {
        try {
            return ByteSerial.fromFileImpl(p_file);
        } catch (final IOException e) {
            e.printStackTrace();
        } catch (final ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Object fromFile(File p_file, Consumer<IOException> p_onIo) {
        try {
            return ByteSerial.fromFileImpl(p_file);
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

    public static Object fromFile(File p_file, Consumer<IOException> p_onIo,
            Consumer<ClassNotFoundException> p_onClassNotFound) {
        try {
            return ByteSerial.fromFileImpl(p_file);
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

    private static Object fromFileImpl(File p_file) throws IOException, ClassNotFoundException {
        try (FileInputStream fis = new FileInputStream(p_file);
                ObjectInputStream ois = new ObjectInputStream(fis);) {
            return ois.readObject();
        }
    }
    // endregion

    // region ...TO files!
    public static void toFile(Serializable p_object, String p_fileName) {
        ByteSerial.toFile(p_object, new File(p_fileName));
    }

    public static void toFile(Serializable p_object, String p_fileName, Consumer<IOException> p_onIo) {
        ByteSerial.toFile(p_object, new File(p_fileName), p_onIo);
    }

    public static void toFile(Serializable p_object, File p_file) {
        ByteSerial.toFile(p_object, p_file, null);
    }

    // The actual implementation:
    public static void toFile(Serializable p_object, File p_file, Consumer<IOException> p_onIo) {
        if (p_object == null)
            return;

        try (FileOutputStream fos = new FileOutputStream(p_file);
                ObjectOutputStream oos = new ObjectOutputStream(fos)) {
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
