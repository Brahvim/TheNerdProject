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

import org.jetbrains.annotations.Nullable;

import com.brahvim.nerd.interfaces.OnCatch;

// Brought to you, *from* my other (currently supa'-duper secret, ";P!) project, "AGC"!:

// Bite Cereal! ":D!
// *Just add milk!*
public class ByteSerial {
    @Nullable
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
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    // region From bytes!
    public static Object fromBytes(byte[] p_data) {
        try {
            return ByteSerial.fromBytesImpl(p_data);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Object fromBytes(byte[] p_data, OnCatch p_onIo) {
        try {
            return ByteSerial.fromBytesImpl(p_data);
        } catch (IOException e) {
            p_onIo.onCatch(e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Object fromBytes(byte[] p_data, OnCatch p_onIo, OnCatch p_onClassNotFound) {
        try {
            return ByteSerial.fromBytesImpl(p_data);
        } catch (IOException e) {
            p_onIo.onCatch(e);
        } catch (ClassNotFoundException e) {
            p_onClassNotFound.onCatch(e);
        }

        return null;
    }

    // region [DEPRECATED] ...after casting!
    /*
     * @SuppressWarnings("unchecked")
     * public static <T> T fromBytesCasted(byte[] p_data) {
     * try {
     * return (T) ByteSerial.fromBytesImpl(p_data);
     * } catch (ClassNotFoundException e) {
     * e.printStackTrace();
     * } catch (ClassCastException e) {
     * e.printStackTrace();
     * } catch (IOException e) {
     * e.printStackTrace();
     * }
     * 
     * return null;
     * }
     * 
     * @SuppressWarnings("unchecked")
     * public static <T> T fromBytesCasted(
     * byte[] p_data,
     * OnCatch p_onIo,
     * OnCatch p_onClassCast) {
     * try {
     * return (T) ByteSerial.fromBytesImpl(p_data);
     * } catch (IOException e) {
     * p_onIo.onCatch(e);
     * } catch (ClassCastException e) {
     * p_onClassCast.onCatch(e);
     * } catch (ClassNotFoundException e) {
     * e.printStackTrace();
     * }
     * return null;
     * }
     * 
     * @SuppressWarnings("unchecked")
     * public static <T> T fromBytesCasted(
     * byte[] p_data,
     * OnCatch p_onClassNotFound,
     * OnCatch p_onClassCast,
     * OnCatch p_onIo) {
     * try {
     * return (T) ByteSerial.fromBytesImpl(p_data);
     * } catch (IOException e) {
     * p_onIo.onCatch(e);
     * } catch (ClassCastException e) {
     * p_onClassCast.onCatch(e);
     * } catch (ClassNotFoundException e) {
     * p_onClassNotFound.onCatch(e);
     * }
     * return null;
     * }
     */
    // endregion

    @Nullable
    private static Object fromBytesImpl(byte[] p_data) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(p_data);
                ObjectInputStream ois = new ObjectInputStream(bis)) {
            return ois.readObject();
        }
    }
    // endregion

    // region From files!
    // region [DEPRECATED] ...after casting!
    /*
     * @SuppressWarnings("unchecked")
     * public static <T> T fromFileCasted(String p_filePath)
     * throws IOException, ClassNotFoundException, ClassCastException {
     * return (T) ByteSerial.fromFileImpl(new File(p_filePath));
     * }
     * 
     * @SuppressWarnings("unchecked")
     * public static <T> T fromFileCasted(String p_filePath, OnCatch p_onIo) {
     * try {
     * return (T) ByteSerial.fromFileImpl(new File(p_filePath));
     * } catch (ClassNotFoundException e) {
     * e.printStackTrace();
     * } catch (ClassCastException e) {
     * e.printStackTrace();
     * } catch (IOException e) {
     * p_onIo.onCatch(e);
     * }
     * 
     * return null;
     * }
     * 
     * @SuppressWarnings("unchecked")
     * public static <T> T fromFileCasted(String p_filePath, OnCatch p_onIo, OnCatch
     * p_onClassNotFound) {
     * try {
     * return (T) ByteSerial.fromFileImpl(new File(p_filePath));
     * } catch (ClassNotFoundException e) {
     * p_onClassNotFound.onCatch(e);
     * } catch (IOException e) {
     * p_onIo.onCatch(e);
     * }
     * 
     * return null;
     * }
     * 
     * @SuppressWarnings("unchecked")
     * public static <T> T fromFileCasted(
     * String p_filePath,
     * OnCatch p_onIo,
     * OnCatch p_onClassCast,
     * OnCatch p_onClassNotFound) {
     * try {
     * return (T) ByteSerial.fromFileImpl(new File(p_filePath));
     * } catch (ClassNotFoundException e) {
     * p_onClassNotFound.onCatch(e);
     * } catch (ClassCastException e) {
     * p_onClassCast.onCatch(e);
     * } catch (IOException e) {
     * p_onIo.onCatch(e);
     * }
     * 
     * return null;
     * }
     * 
     * // Throw-away, all exceptions!~
     * 
     * @SuppressWarnings("unchecked")
     * public static <T> T fromFileCasted(File p_file)
     * throws IOException, ClassCastException, ClassNotFoundException {
     * return (T) ByteSerial.fromFileImpl(p_file);
     * }
     */
    // endregion

    public static Object fromFile(String p_filePath) {
        try {
            return ByteSerial.fromFileImpl(new File(p_filePath));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Object fromFile(String p_filePath, OnCatch p_onIo) {
        try {
            return ByteSerial.fromFileImpl(new File(p_filePath));
        } catch (IOException e) {
            p_onIo.onCatch(e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Object fromFile(String p_filePath, OnCatch p_onIo, OnCatch p_onClassNotFound) {
        try {
            return ByteSerial.fromFileImpl(new File(p_filePath));
        } catch (ClassNotFoundException e) {
            p_onClassNotFound.onCatch(e);
        } catch (IOException e) {
            p_onIo.onCatch(e);
        }

        return null;
    }

    public static Object fromFile(File p_file) {
        try {
            return ByteSerial.fromFileImpl(p_file);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Object fromFile(File p_file, OnCatch p_onIo) {
        try {
            return ByteSerial.fromFileImpl(p_file);
        } catch (IOException e) {
            p_onIo.onCatch(e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Object fromFile(File p_file, OnCatch p_onIo, OnCatch p_onClassNotFound) {
        try {
            return ByteSerial.fromFileImpl(p_file);
        } catch (ClassNotFoundException e) {
            p_onClassNotFound.onCatch(e);
        } catch (IOException e) {
            p_onIo.onCatch(e);
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

    public static void toFile(Serializable p_object, String p_fileName, OnCatch p_onIo) {
        ByteSerial.toFile(p_object, new File(p_fileName), p_onIo);
    }

    public static void toFile(Serializable p_object, File p_file) {
        if (p_object == null)
            return;

        try (FileOutputStream fos = new FileOutputStream(p_file);
                ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(p_object);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void toFile(Serializable p_object, File p_file, OnCatch p_onIo) {
        if (p_object == null)
            return;

        try (FileOutputStream fos = new FileOutputStream(p_file);
                ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(p_object);
        } catch (IOException e) {
            p_onIo.onCatch(e);
        }
    }
    // endregion

}
