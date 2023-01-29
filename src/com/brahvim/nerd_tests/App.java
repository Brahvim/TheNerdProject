package com.brahvim.nerd_tests;

import java.util.List;

import com.brahvim.nerd.papplet_wrapper.Sketch;
import com.brahvim.nerd.papplet_wrapper.SketchBuilder;
import com.brahvim.nerd_tests.scenes.TestScene1;

public class App {

    // region `App`'s Fields.
    public final static int BPM = 100,
            BPM_INT = (int) (App.BPM / 60_000.0f);

    private static Sketch sketchInstance;
    private static int tickCount;
    private static boolean tick;
    // endregion

    public static void main(String[] p_args) {
        LoadeableClasses.loadClasses(); // Handle this yourself, sorry!

        // region Building the `Sketch`!
        App.sketchInstance = new SketchBuilder()
                // .setFirstScene(LoadeableClasses.TEST_SCENE_5.getLoadedClassAsScene())
                .setStringTablePath(Sketch.DATA_DIR_PATH + "Nerd_StringTable.ini")
                .setIconPath("data/sunglass_nerd.png")
                .setFirstScene(TestScene1.class)
                .setTitle("The Nerd Project")
                .startFullscreen()
                .canResize()
                .onSketchConstruction((s) -> {
                    System.out.println(s.STRINGS.getString("Meta.onConstruct"));
                })
                .build(p_args);
        // endregion

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
