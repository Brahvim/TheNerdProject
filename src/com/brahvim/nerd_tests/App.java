package com.brahvim.nerd_tests;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.LinkedHashSet;

import com.brahvim.nerd.papplet_wrapper.Sketch;
import com.brahvim.nerd.papplet_wrapper.SketchBuilder;
import com.brahvim.nerd.scene_api.NerdScene;
import com.brahvim.nerd_tests.scenes.TestScene1;
import com.brahvim.nerd_tests.scenes.TestScene2;
import com.brahvim.nerd_tests.scenes.TestScene3;
import com.brahvim.nerd_tests.scenes.TestScene4;

public class App {

    public enum LoadedClasses {
        TEST_SCENE_5("", "");

        // region Fields, methods, ...the usual OOP, y'know?
        // region Fields.
        public Class<? extends NerdScene> SCENE_CLASSES;

        private final URL URL;
        private final String QUAL_NAME;

        private Class<?> loadedClass;
        // endregion Fields.

        private LoadedClasses(String p_urlString, String p_fullyQualifiedName) {
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
        public Class<?> getLoadedClass() {
            return this.loadedClass;
        }

        protected void setLoadedClass(Class<?> p_class) {
            this.loadedClass = p_class;
        }

        private URL getUrl() {
            return this.URL;
        }
        // endregion
        // endregion
    }

    // region `App`'s Fields.
    public final static int BPM = 100,
            BPM_INT = (int) (App.BPM / 60_000.0f);

    private static Sketch sketchInstance;
    private static int tickCount;
    private static boolean tick;
    // endregion

    public static void main(String[] p_args) {
        App.loadClasses(); // Handle this yourself, sorry!

        App.sketchInstance = new SketchBuilder()
                .setTitle("The Nerd Project")
                .setFirstScene(LoadedClasses.TEST_SCENE_5.SCENE_CLASSES)
                .setIconPath("data/sunglass_nerd.png")
                .setStringTablePath(Sketch.DATA_DIR_PATH + "Nerd_StringTable.ini")

                // TODO: Add window resizing hint arrows to the `OpenGL` renderer.
                // TODO: Perhaps find a fix for `JAVA2D` fullscreen windows not fitting well!
                // .useJavaRenderer()
                // .preLoadAssets(TestScene4.class) // This works, too! ...of course!
                // .preLoadAssets(TestScene1.class, TestScene2.class) // Works!

                .startFullscreen()
                .canResize()
                .build(p_args);

        App.startTickThread();
    }

    private static void loadClasses() {
        // Just because a `URLClassLoader` can take many URLs does not mean that I
        // should store all `URL`s into a single `URLClassLoader` and then fetch each
        // `Class<?>` myself in a second loop. Even if it means constructing a
        // completely new `URLClassLoader`, every time, I will still use this single
        // loop because modifying two loops would be too much!

        // (I could use an interface with two methods - one returning a
        // `ToLoad` instance with the `url` and `fullyQualifiedName` set, and the other
        // taking in the class to save it somewhere, but I won't...?)

        // Get all `URL`s as an array, in order:
        LinkedHashSet<URL> urlSet = new LinkedHashSet<>();

        for (LoadedClasses c : LoadedClasses.values())
            urlSet.add(c.getUrl());

        // Construct the loader:
        URLClassLoader loader = new URLClassLoader(
                (URL[]) urlSet.toArray(), ClassLoader.getSystemClassLoader());

        for (LoadedClasses c : LoadedClasses.values()) {// Load classes using `forName()`.
            try {
                Class<?> loadedClass = Class.forName(c.QUAL_NAME, true, loader);
                c.setLoadedClass(loadedClass);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public Sketch getSketchInstance() {
        return App.sketchInstance;
    }

    // region Ticking.
    private static void startTickThread() {
        new Thread(() -> {
            try {
                Thread.sleep(App.BPM_INT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            App.tick = true;
            App.tickCount++;
        }).start();
    }

    public static boolean hasTick() {
        return App.tick;
    }

    public static int getTickCount() {
        return App.tickCount;
    }

    public static void resetTickCount() {
        App.tickCount = 0;
    }
    // endregion

}
