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

    // region Event callbacks.
    // region Mouse event callbacks.
    @Override
    protected void mousePressed() {
        super.mousePressed();
    }

    @Override
    protected void mouseReleased() {
        super.mouseReleased();
    }

    @Override
    protected void mouseMoved() {
        super.mouseMoved();
    }

    @Override
    protected void mouseClicked() {
        super.mouseClicked();
    }

    @Override
    protected void mouseDragged() {
        super.mouseDragged();
    }

    @Override
    protected void mouseWheel(final processing.event.MouseEvent p_mouseEvent) {
        super.mouseWheel(p_mouseEvent);
    }
    // endregion

    // region Touch event callbacks.
    @Override
    protected void touchStarted() {
        super.touchStarted();
    }

    @Override
    protected void touchMoved() {
        super.touchMoved();
    }

    @Override
    protected void touchEnded() {
        super.touchEnded();
    }
    // endregion

    // region Window event callbacks.
    @Override
    protected void resized() {
        super.resized();
    }

    @Override
    protected void focusLost() {
        super.focusLost();
    }

    @Override
    protected void focusGained() {
        super.focusGained();
    }

    @Override
    protected void monitorChanged() {
        super.monitorChanged();
    }

    @Override
    protected void fullscreenChanged(final boolean p_state) {
        super.fullscreenChanged(p_state);
    }

    // endregion

    // region Keyboard event callbacks.
    @Override
    protected void keyTyped() {
        super.keyTyped();
    }

    @Override
    protected void keyPressed() {
        super.keyPressed();
    }

    @Override
    protected void keyReleased() {
        super.keyReleased();
    }
    // endregion
    // endregion

}
