package com.brahvim.nerd_tests;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import com.brahvim.nerd.papplet_wrapper.Sketch;
import com.brahvim.nerd.papplet_wrapper.SketchBuilder;
import com.brahvim.nerd.scene_api.NerdScene;
import com.brahvim.nerd_tests.scenes.TestScene1;
import com.brahvim.nerd_tests.scenes.TestScene2;
import com.brahvim.nerd_tests.scenes.TestScene3;
import com.brahvim.nerd_tests.scenes.TestScene4;

public class App {

    // region Fields.
    public final static int BPM = 100,
            BPM_INT = (int) (App.BPM / 60_000.0f);

    private static Sketch sketchInstance;
    private static int tickCount;
    private static boolean tick;
    // endregion

    public static void main(String[] p_args) {

        // region Class loading attempt. OOf.
        /*
         * Class<?> testScene5Class = null;
         * 
         * try {
         * URLClassLoader loader = new URLClassLoader(new URL[] {
         * new File(Sketch.EXEC_DIR,
         * "src\\com\\brahvim\\nerd_tests\\scenes\\TestScene5.class")
         * .toURI().toURL()
         * });
         * 
         * testScene5Class =
         * loader.loadClass("com.brahvim.nerd_tests.scenes.TestScene5");
         * loader.close();
         * 
         * // testScene5Class = (Class<? extends NerdScene>) Class.forName(
         * // "../../../../../../com.brahvim.nerd_tests.scenes.TestScene5",
         * // true, NerdClassLoader.INSTANCE);
         * } catch (MalformedURLException e) {
         * e.printStackTrace();
         * } catch (IOException e) {
         * e.printStackTrace();
         * } catch (ClassNotFoundException e) {
         * e.printStackTrace();
         * }
         * 
         * System.out.println(testScene5Class.getSimpleName());
         */
        // endregion

        // region ANOTHER attempt at class loading. Also a failure.
        /*
         * URL fileUrl = null;
         * URLClassLoader child = null;
         * 
         * try {
         * child = new URLClassLoader(
         * new URL[] { fileUrl = new File("file:/TestScene5.jar").toURI().toURL() },
         * App.class.getClassLoader());
         * // child.loadClass("com.brahvim.nerd_tests.scenes.TestScene5");
         * child.close();
         * } catch (MalformedURLException e) {
         * e.printStackTrace();
         * } catch (IOException e) {
         * e.printStackTrace();
         * }
         * // catch (ClassNotFoundException e) {
         * // e.printStackTrace();
         * // }
         * 
         * System.out.println(fileUrl);
         * 
         * try {
         * System.out.println(Class.forName("TestScene5").getSimpleName());
         * } catch (ClassNotFoundException e) {
         * e.printStackTrace();
         * }
         */
        // endregion

        // Working class loading, thanks to:
        // [https://kostenko.org/blog/2019/06/runtime-class-loading.html]
        URLClassLoader childClassLoader = null;

        try {
            URL jarUrl = new URL("file:/" + Sketch.DATA_DIR_PATH + "TestScene5.jar");
            childClassLoader = new URLClassLoader(
                    new URL[] { jarUrl },
                    ClassLoader.getSystemClassLoader());

            System.out.printf("Got the URL: `%s`.\n", jarUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Class<? extends NerdScene> testScene5Class = null;
        try {
            Class<?> loadedClass = Class.forName("com.brahvim.nerd_tests.scenes.TestScene5", true, childClassLoader);
            testScene5Class = (Class<? extends NerdScene>) loadedClass;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        App.sketchInstance = new SketchBuilder()
                .setTitle("The Nerd Project")
                .setFirstScene(testScene5Class)
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
