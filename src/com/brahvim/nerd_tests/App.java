package com.brahvim.nerd_tests;

import com.brahvim.nerd.processing_wrapper.Sketch;
import com.brahvim.nerd.processing_wrapper.SketchBuilder;
import com.brahvim.nerd_tests.scenes.TestScene1;
import com.brahvim.nerd_tests.scenes.TestScene2;
import com.brahvim.nerd_tests.scenes.TestScene3;
import com.brahvim.nerd_tests.scenes.TestScene4;

public class App {
    // region Fields.
    public final static int BPM = 100,
            BPM_INT = (int) (App.BPM / 60_000.0f);

    private static Sketch sketchInstance;
    private static boolean tick;
    private static int tickCount;
    // endregion

    public static void main(String[] p_args) {
        App.sketchInstance = new SketchBuilder()
                .setTitle("The Nerd Project")
                .setFirstScene(TestScene4.class)
                .setIconPath("data/sunglass_nerd.png")

                // TODO: Add window resizing hint arrows to the `OpenGL` renderer.
                // .useJavaRenderer()
                // .cacheScene(true, TestScene1.class)
                // .preLoadAssets(TestScene1.class)
                // .cacheAllScenes(true,
                // TestScene1.class, TestScene2.class)

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
