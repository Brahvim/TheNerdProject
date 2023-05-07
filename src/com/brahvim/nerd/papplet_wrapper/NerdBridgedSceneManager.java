package com.brahvim.nerd.papplet_wrapper;

import java.util.LinkedHashSet;

import com.brahvim.nerd.framework.scenes.NerdSceneManager;

public class NerdBridgedSceneManager extends NerdSceneManager {

    // region Constructors.
    public NerdBridgedSceneManager(NerdSketch p_sketch,
            LinkedHashSet<SceneChangeListener> p_listeners) {
        super(p_sketch, p_listeners);
    }

    public NerdBridgedSceneManager(NerdSketch p_sketch,
            LinkedHashSet<SceneChangeListener> p_listeners,
            SceneManagerSettings p_settings) {
        super(p_sketch, p_listeners, p_settings);
    }

    public NerdBridgedSceneManager(NerdSketch p_sketch, SceneManagerSettings p_settings) {
        super(p_sketch, p_settings);
    }

    public NerdBridgedSceneManager(NerdSketch p_sketch, SceneManagerSettings p_settings,
            LinkedHashSet<SceneChangeListener> p_listeners) {
        super(p_sketch, p_settings, p_listeners);
    }
    // endregion

    // region Workflow callbacks.
    @Override
    protected void runPre() {
        super.runPre();
    }

    @Override
    protected void runPost() {
        super.runPost();
    }

    @Override
    protected void runDraw() {
        super.runDraw();
    }

    @Override
    protected void runExit() {
        super.runExit();
    }

    @Override
    protected void runDispose() {
        super.runDispose();
    }

    // Too expensive! Need a `push()` and `pop()`:
    /*
     * @Override
     * protected void runPreDraw() {
     * super.runPreDraw();
     * }
     * 
     * @Override
     * protected void runPostDraw() {
     * super.runPostDraw();
     * }
     */
    // endregion

}
