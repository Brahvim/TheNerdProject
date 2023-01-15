package com.brahvim.nerd.scene_api;

import com.brahvim.nerd.processing_wrapper.Sketch;

/**
 * Just like {@code Scene}s, {@code Layer}s
 * are used by extending this class.
 * To add a {@code Layer} to a {@code Scene}, use the
 * {@code SceneClass::startLayer(LayerClass.class)}
 * method, where {@code LayerClass} and {@code SceneClass}
 * are any two classes extending the {@code Layer}
 * and {@code Scene} classes, respectively.
 *
 * @author Brahvim Bhaktvatsal
 */
public class NerdLayer implements HasSketchEvents {
    // region `protected` fields.
    protected final NerdScene SCENE;
    protected final Sketch SKETCH;
    protected final SceneManager MANAGER;
    protected final NerdLayer LAYER = this;
    // endregion

    // region `private` fields.
    private boolean active = true;
    // ^^^ `private` because otherwise you won't be able to track changes.
    // This isn't C#!
    // endregion

    public NerdLayer(NerdScene.LayerKey p_key) {
        // region Verify and 'use' key.
        if (p_key == null) {
            throw new IllegalArgumentException("""
                    Please use a `NerdSceneManager` instance to make a `NerdScene`!""");
        } else if (p_key.isUsed()) {
            throw new IllegalArgumentException("""
                    Please use a `NerdSceneManager` instance to make a `NerdScene`! That is a used key!""");
        } else if (!p_key.isFor(this.getClass()))
            throw new IllegalArgumentException("""
                    Please use a `NerdSceneManager` instance to make a `NerdScene`! That key is not for me!""");

        p_key.use();
        // endregion

        this.SCENE = p_key.getScene();
        this.SKETCH = p_key.getSketch();
        this.MANAGER = this.SCENE.MANAGER;
    }

    // region Activity status.
    public boolean isActive() {
        return this.active;
    }

    public void setActive(boolean p_toggleState) {
        this.active = p_toggleState;
        this.onToggled(p_toggleState);
    }
    // endregion

    // region `Layer`-only (`protected`) callbacks!
    protected void onToggled(boolean p_toggleState) {
    }
    // endregion

    // region App workflow callbacks.
    protected void setup() {
    }

    protected void pre() {
    }

    protected void draw() {
    }

    protected void post() {
    }
    // endregion

}
