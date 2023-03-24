package com.brahvim.nerd_tests;

import com.brahvim.nerd.openal.AlContext;
import com.brahvim.nerd.papplet_wrapper.NerdSketchBuilder;
import com.brahvim.nerd.papplet_wrapper.Sketch;
import com.brahvim.nerd.scene_api.NerdScene;
import com.brahvim.nerd.scene_api.SceneManager.SceneManagerSettings;
import com.brahvim.nerd_tests.scenes.TestScene3;

public class App {

    // TODOs!:
    /*
     * // TODO: Particle system.
     * // TODO: NETWORKING. GOOD, EASY NETWORKING.
     * // TODO: Just make it easier to animate, somehow!
     * // TODO: Collision Algorithms (also for 3D space)?!
     * // TODO: ANDROID OpenAL. Get it done!~ (SWIG wrapper!)
     * // TODO: SHADERS. Remake the "PostFX" Processing library?
     */

    public final static Class<? extends NerdScene> FIRST_SCENE_CLASS =
            // Use directly in `setFirstSceneClass()` below!:
            TestScene3.class;
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
        NerdSketchBuilder builder = new NerdSketchBuilder();
        builder.setStringTablePath(Sketch.fromDataDir("Nerd_StringTable.json"))
                .setIconPath("data/sunglass_nerd.png")
                .setFirstScene(App.FIRST_SCENE_CLASS)
                .setTitle("The Nerd Project")
                .setAntiAliasing(4)
                .setAlContextSettings(() -> {
                    var toRet = new AlContext.AlContextSettings();
                    // ...for `TestScene3`!!!:
                    toRet.monoSources = Integer.MAX_VALUE;
                    toRet.stereoSources = Integer.MAX_VALUE;
                    return toRet;
                })

                // .preventCloseOnEscape()
                // .startFullscreen()
                .canResize()

                // ...apparently these listeners take literally `0` millseconds to finish
                // calling! They're much faster, actually! That `0` millisecond time included
                // starting and stopping a `MillisTimer`!
                // ..they should be faster than a v-table thingy anyway, amirite?
                .addSketchConstructionListener((s) -> System.out.println(
                        s.STRINGS.get("Meta.onConstruct"))

                // These work too - commenting them out so they don't clog-the-log!:
                // System.out.println(s.STRINGS.fromArray("Meta.arrExample", 0));
                // System.out.println(s.STRINGS.randomFromArray("Meta.arrExample"));
                )

                .setSceneManagerSettings(() -> {
                    var toRet = new SceneManagerSettings();
                    toRet.onScenePreload.onlyFirstPreload = false;
                    return toRet;
                });

        builder.build(p_args);
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
