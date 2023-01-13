package com.brahvim.nerd_test;

import com.brahvim.nerd.processing_wrapper.Sketch;
import com.brahvim.nerd.processing_wrapper.SketchBuilder;
import com.brahvim.nerd_test.scenes.TestScene1;
import com.brahvim.nerd_test.scenes.TestScene2;
import com.brahvim.nerd_test.scenes.TestScene3;

import processing.opengl.PJOGL;

public class App {
    public static Sketch sketchInstance;

    private static boolean tick;
    private static int tickCount;
    public static final int BPM = 100,
            BPM_INT = (int) (App.BPM / 60_000.0f);

    public static void main(String[] p_args) {
        PJOGL.setIcon("data/sunglass_nerd.png");
        App.sketchInstance = new SketchBuilder()
                .setName("The Nerd Project")
                .setFirstScene(TestScene1.class)
                // .setIcon("path/to/icon")
                // .cacheScene(true, TestScene1.class)
                // .cacheAllScenes(true,
                // TestScene1.class, TestScene2.class)
                .startFullscreen()
                .canResize()
                .build(p_args);

        App.startTickThread();
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
