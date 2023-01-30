package com.brahvim.nerd_tests;

import com.brahvim.nerd.papplet_wrapper.NerdSketchBuilder;
import com.brahvim.nerd.papplet_wrapper.Sketch;
import com.brahvim.nerd_tests.scenes.TestScene2;
import com.brahvim.nerd_tests.scenes.TestScene4;

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
        App.sketchInstance = new NerdSketchBuilder()
                // .setFirstScene(LoadeableClasses.TEST_SCENE_5.getLoadedClassAsScene())
                .setStringTablePath(Sketch.fromDataDir("Nerd_StringTable.json"))
                .setIconPath("data/sunglass_nerd.png")
                .setFirstScene(TestScene4.class)
                .setTitle("The Nerd Project")
                .startFullscreen()
                .canResize()

                // ...apparently these listeners take literally `0` millseconds to finish
                // calling! They're much faster, actually! That `0` millisecond time included
                // starting and stopping a `MillisTimer`!
                // ..they should be faster than a v-table thingy anyway, amirite?
                .onSketchConstructed((s) -> {
                    System.out.println(s.STRINGS.get("Meta.onConstruct"));

                    // These work too - commenting them out so they don't clog-the-log!:
                    // System.out.println(s.STRINGS.fromArray("Meta.arrExample", 0));
                    // System.out.println(s.STRINGS.randomFromArray("Meta.arrExample"));
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
