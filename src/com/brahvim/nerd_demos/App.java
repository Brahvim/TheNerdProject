package com.brahvim.nerd_demos;

import com.brahvim.nerd.openal.NerdAl;
import com.brahvim.nerd.openal.NerdAlExt;
import com.brahvim.nerd.papplet_wrapper.NerdSketchBuilder;
import com.brahvim.nerd.papplet_wrapper.NerdSketch;
import com.brahvim.nerd.papplet_wrapper.NerdSketchBuildArtifacts;
import com.brahvim.nerd.scene_api.NerdScene;
import com.brahvim.nerd_demos.scenes.scene1.DemoScene1;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.glu.GLU;

import processing.opengl.PGL;
import processing.opengl.PGraphicsOpenGL;

public class App {

    /*
     * // TODO: ECS.
     * // TODO: Make it easier to animate, somehow!?
     * // TODO: Collision Algorithms (also for 3D space).
     * // TODO: Android port with OpenAL (OpenAL Javacpp wrapper?).
     */

    public static final Class<? extends NerdScene> FIRST_SCENE_CLASS =
            // Use directly in `setFirstSceneClass()` below!:
            // LoadedSceneClass.DEMO_SCENE_5.getSceneClassLoader();
            DemoScene1.class;

    // region `App`'s *other* fields.
    public static final int BPM = 100,
            BPM_INT = (int) (App.BPM / 60_000.0f);

    public static volatile GL GL;
    public static volatile GLU GLU;
    public static volatile PGL PGL;
    public static volatile NerdAl OPENAL;
    public static volatile GLWindow WINDOW;
    public static volatile PGraphicsOpenGL GRAPHICS;

    private static volatile int tickCount;
    private static volatile boolean tick;
    // endregion

    public static void main(final String[] p_args) {
        // region Building the `Sketch`!
        final NerdSketchBuilder builder = new NerdSketchBuilder();
        builder.setStringTablePath(NerdSketch.fromDataDir("Nerd_StringTable.json"))
                .setIconPath("data/sunglass_nerd.png")
                .setFirstScene(App.FIRST_SCENE_CLASS)
                .setTitle("The Nerd Project")
                .setAntiAliasing(4)
                .addNerdExt(new NerdAlExt(s -> {
                    // ...for `DemoScene3`!!!:
                    s.monoSources = Integer.MAX_VALUE;
                    s.stereoSources = Integer.MAX_VALUE;
                }))
                // .preventCloseOnEscape()
                // .startFullscreen()
                .canResize()

                .addSketchConstructionListener(
                        s -> System.out.println(s.STRINGS.get("Meta.onConstruct")))

                .setSceneManagerSettings(s -> s.ON_PRELOAD.preloadOnlyOnce = false);

        // Build the sketch and collect build artifacts:
        final NerdSketchBuildArtifacts artifacts = builder.build(p_args);
        App.OPENAL = (NerdAl) artifacts.getExtObject("OpenAL");
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
