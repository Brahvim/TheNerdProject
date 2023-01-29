package com.brahvim.nerd_tests;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import com.brahvim.nerd.papplet_wrapper.Sketch;
import com.brahvim.nerd.scene_api.NerdLayer;
import com.brahvim.nerd.scene_api.NerdScene;

public enum LoadeableClasses {
    // PS *I beg you,* layers first!
    // `Layer`s are a parameterized type - they won't exist without a scene!

    TEST_SCENE_5(
            // ...for some reason I can use only a JAR file now. No `.class` ones!
            "file:/" + Sketch.DATA_DIR_PATH + "TestScene5.jar",
            "com.brahvim.nerd_tests.scenes.TestScene5");

    // region Fields, methods, ...the usual OOP, y'know?
    // region Fields.
    public Class<? extends NerdScene> SCENE_CLASSES;

    private final URL URL;
    private final String QUAL_NAME;

    private Class<?> loadedClass;
    // endregion Fields.

    private LoadeableClasses(String p_urlString, String p_fullyQualifiedName) {
        URL urlToSet = null;
        try {
            urlToSet = new URL(p_urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        this.URL = urlToSet;
        this.QUAL_NAME = p_fullyQualifiedName;
    }

    // region Methods.
    protected static void loadClasses() {
        // Get all `URL`s as an array, in order:

        final LoadeableClasses[] ENUM_VALUES = LoadeableClasses.values();
        final URL[] URL_ARRAY = new URL[ENUM_VALUES.length];

        // region Get all URLs and construct the loader:
        for (int i = 0; i < URL_ARRAY.length; i++)
            URL_ARRAY[i] = ENUM_VALUES[i].getUrl();

        final URLClassLoader LOADER = new URLClassLoader(
                URL_ARRAY, ClassLoader.getSystemClassLoader());
        // endregion

        for (LoadeableClasses c : ENUM_VALUES) { // Load classes using `forName()`.
            try {
                final Class<?> LOADED_CLASS = Class.forName(c.QUAL_NAME, true, LOADER);
                c.setLoadedClass(LOADED_CLASS);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    // region Getters and setters.
    // region `getLoadedClass()`-like.
    public Class<?> getLoadedClass() {
        return this.loadedClass;
    }

    @SuppressWarnings("unchecked")
    public <T> Class<? extends T> getLoadedClassAs() {
        return (Class<? extends T>) this.loadedClass;
    }

    @SuppressWarnings("unchecked")
    public Class<? extends NerdScene> getLoadedClassAsScene() {
        return (Class<? extends NerdScene>) this.loadedClass;
    }

    @SuppressWarnings("unchecked")
    public Class<? extends NerdLayer> getLoadedClassAsLayer() {
        return (Class<? extends NerdLayer>) this.loadedClass;
    }
    // endregion

    private URL getUrl() {
        return this.URL;
    }

    protected void setLoadedClass(Class<?> p_class) {
        this.loadedClass = p_class;
    }
    // endregion
    // endregion
    // endregion

}
