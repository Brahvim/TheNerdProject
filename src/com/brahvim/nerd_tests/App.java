package com.brahvim.nerd_tests;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;

import com.brahvim.nerd.papplet_wrapper.NerdSketchBuilder;
import com.brahvim.nerd.papplet_wrapper.Sketch;
import com.brahvim.nerd.scene_api.NerdScene;
import com.brahvim.nerd_tests.scenes.TestScene4;

public class App {

    // TODOs!:
    /*
     * // ????: Don't forget - AL can stream audio!
     * // TODO: Learn to use `PhantomReference<AlObject>`!
     * // TODO: Get Listener functions done! (Also `EXEfx.AL_METERS_PER_UNIT`.)
     * // TODO: Box2D and Bullet? :D?
     * // TODO: Multiple monitor support!
     * // TODO: Collision Algorithms for 3D space.
     * // TODO: Learn to compile OpenAL for Android.
     */

    public final static Class<? extends NerdScene> FIRST_SCENE_CLASS =
            // Use directly in `setFirstSceneClass()` below!:
            TestScene4.class;
    // LoadedSceneClass.TEST_SCENE_5.getSceneClassLoader();

    // region `App`'s *other* fields.
    public final static int BPM = 100,
            BPM_INT = (int) (App.BPM / 60_000.0f);

    private static Sketch sketchInstance;
    private static int tickCount;
    private static boolean tick;
    // endregion

    public static void main(String[] p_args) {
        // region Building the `Sketch`!
        App.sketchInstance = new NerdSketchBuilder()
                .setStringTablePath(Sketch.fromDataDir("Nerd_StringTable.json"))
                .setIconPath("data/sunglass_nerd.png")
                .setFirstScene(App.FIRST_SCENE_CLASS)
                .setTitle("The Nerd Project")
                .setAntiAliasing(4)
                // .preventCloseOnEscape()
                // .startFullscreen()
                .usesOpenAl()
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
