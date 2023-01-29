package com.brahvim.nerd.io;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class NerdClassLoader extends ClassLoader {
    public final static NerdClassLoader INSTANCE = new NerdClassLoader();

    private NerdClassLoader() {
    }

    @Override
    protected Class<?> findClass(String p_name) throws ClassNotFoundException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        InputStream classStream = this.getClass().getClassLoader().getResourceAsStream(
                p_name.replace('/', File.separatorChar)
                        + ".class");

        try {
            int nextValue = 0;
            while ((nextValue = classStream.read()) != -1) {
                stream.write(nextValue);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] classBytes = stream.toByteArray();
        return super.defineClass(p_name, classBytes, 0, classBytes.length);
    }
}
