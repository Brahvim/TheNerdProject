package com.brahvim.nerd.papplet_wrapper.sketch_managers;

import java.util.LinkedHashSet;

import com.brahvim.nerd.api.scene_api.NerdSceneManager;
import com.brahvim.nerd.papplet_wrapper.Sketch;

public class NerdBridgedSceneManager extends NerdSceneManager {

    // region Constructors.
    public NerdBridgedSceneManager(Sketch p_sketch,
            LinkedHashSet<SceneChangeListener> p_listeners) {
        super(p_sketch, p_listeners);
    }

    public NerdBridgedSceneManager(Sketch p_sketch,
            LinkedHashSet<SceneChangeListener> p_listeners,
            SceneManagerSettings p_settings) {
        super(p_sketch, p_listeners, p_settings);
    }

    public NerdBridgedSceneManager(Sketch p_sketch, SceneManagerSettings p_settings) {
        super(p_sketch, p_settings);
    }

    public NerdBridgedSceneManager(Sketch p_sketch, SceneManagerSettings p_settings,
            LinkedHashSet<SceneChangeListener> p_listeners) {
        super(p_sketch, p_settings, p_listeners);
    }
    // endregion

    // region Workflow callbacks.
    @Override
    public void runPre() {
        super.runPre();
    }

    @Override
    public void runPost() {
        super.runPost();
    }

    @Override
    public void runDraw() {
        super.runDraw();
    }

    @Override
    public void runExit() {
        super.runExit();
    }

    @Override
    public void runDispose() {
        super.runDispose();
    }

    // Too expensive! Need a `push()` and `pop()`:
    /*
     * @Override
     * public void runPreDraw() {
     * super.runPreDraw();
     * }
     * 
     * @Override
     * public void runPostDraw() {
     * super.runPostDraw();
     * }
     */
    // endregion

}
