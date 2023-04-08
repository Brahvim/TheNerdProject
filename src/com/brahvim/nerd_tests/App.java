package com.brahvim.nerd_tests;

import com.brahvim.nerd.openal.AlContext;
import com.brahvim.nerd.openal.NerdAl;
import com.brahvim.nerd.openal.NerdAlExt;
import com.brahvim.nerd.papplet_wrapper.NerdSketchBuilder;
import com.brahvim.nerd.papplet_wrapper.Sketch;
import com.brahvim.nerd.scene_api.NerdScene;
import com.brahvim.nerd.scene_api.SceneManager.SceneManagerSettings;
import com.brahvim.nerd_tests.scenes.TcpTestScene;

public class App {

    // TODOs!:
    /*
     * // TODO: NETWORKING. GOOD, EASY NETWORKING.
     * // TODO: Just make it easier to animate, somehow!
     * // TODO: Collision Algorithms (also for 3D space)?!
     * // TODO: ANDROID Port with OpenAL. Get that done! (SWIG wrapper!)
     */

    public static final Class<? extends NerdScene> FIRST_SCENE_CLASS =
            // Use directly in `setFirstSceneClass()` below!:
            TcpTestScene.class; // `LoadedSceneClass.TEST_SCENE_5.getSceneClassLoader();`

    // region `App`'s *other* fields.
    public static final int BPM = 100,
            BPM_INT = (int) (App.BPM / 60_000.0f);

    public static volatile NerdAl AL;

    private static volatile int tickCount;
    private static volatile boolean tick;
    // endregion

    public static void main(final String[] p_args) {
        // region Building the `Sketch`!
        final NerdSketchBuilder builder = new NerdSketchBuilder();
        builder.setStringTablePath(Sketch.fromDataDir("Nerd_StringTable.json"))
                .setIconPath("data/sunglass_nerd.png")
                .setFirstScene(App.FIRST_SCENE_CLASS)
                .setTitle("The Nerd Project")
                .setAntiAliasing(4)
                .addNerdExt(new NerdAlExt(() -> {
                    final var toRet = new AlContext.AlContextSettings();
                    // ...for `TestScene3`!!!:
                    toRet.monoSources = Integer.MAX_VALUE;
                    toRet.stereoSources = Integer.MAX_VALUE;
                    return toRet;
                }))
                // .preventCloseOnEscape()
                // .startFullscreen()
                .canResize()

                // ...apparently these listeners take literally `0` millseconds to finish
                // calling! They're much faster, actually! That `0` millisecond time included
                // starting and stopping a `MillisTimer`!
                // ..they should be faster than a v-table thingy anyway, amirite?
                .addSketchConstructionListener((s) -> {
                    System.out.println(s.STRINGS.get("Meta.onConstruct"));

                    // ...Also do some actual work!:
                    App.AL = s.getNerdExt("OpenAL");

                    // These work too - commenting them out so they don't clog-the-log!:
                    // System.out.println(s.STRINGS.fromArray("Meta.arrExample", 0));
                    // System.out.println(s.STRINGS.randomFromArray("Meta.arrExample"));
                })

                .setSceneManagerSettings(() -> {
                    final var toRet = new SceneManagerSettings();
                    // TODO Review this!
                    // toRet.onScenePreload.onlyFirstPreload = false;
                    return toRet;
                });

        builder.build(p_args);
        // endregion

        App.startTickThread();
    }

    // region Ticking.
    private static void startTickThread() {
        new Thread(() -> {
            try {
                Thread.sleep(App.BPM_INT);
            } catch (final InterruptedException e) {
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
