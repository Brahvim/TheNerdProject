package com.brahvim.nerd_tests;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import com.brahvim.nerd.papplet_wrapper.Sketch;
import com.brahvim.nerd.scene_api.NerdLayer;
import com.brahvim.nerd.scene_api.NerdScene;

/*
 * TODO: Improve `LoadeableClasses`!
 *
 * Make this a full class with the same fields.
 * (...as `final` objects, that get set in the constructor, just like here!)
 *
 * Declare a `public static HashSet<LoadeableClass>` field, which would store every
 * `LoadeableClass` instance.
 *
 * Make a `public static` method, `loadClasses()`, just like the one here, and load 'em all up!
 *
 * Users would then be able to make `enum`s storing `LoadeableClass`es, and also organize them
 * all they like, :D!~
 *
 * It is possible to include in `LoadeableClass` a generic parameter, then `LoadeableClass<ClassT>` 
 * could be extended to make `LoadeableNerdScene` and `LoadeableNerdLayer`, from where on, it would 
 * be possible even to make a class that stored a `NerdScene` and related `NerdLayer`s. The JAR or 
 * CLASS file containing this `NerdScene` subclass could also be parsed to help know what needs to 
 * be loaded and linked beforehand in an automated manner. Users can always do it themselves instead.
 * Users could even make their own `enum` worrying  just about `NerdScene`s (and the names of the
 * `NerdLayer`s used by them - a process I wish to see us successfully automate!), and this could
 * lead to the users being able to dynamically load **parts of an app** with extreme ease!
*/

public enum LoadeableClasses {
    // PS *I beg you,* layers first!
    // `Layer`s are a parameterized type - they won't exist without a scene!

    TEST_SCENE_5(
            // ...for some reason I can use only a JAR file now. No `.class` ones!
            "file:/" + Sketch.DATA_DIR_PATH + "TestScene5.jar",
            "com.brahvim.nerd_tests.scenes.TestScene5");

    // region Fields, methods, ...the usual OOP, y'know?
    // region Fields.
    public final URL URL;
    public final String QUAL_NAME;

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
