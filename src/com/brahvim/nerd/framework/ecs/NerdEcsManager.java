package com.brahvim.nerd.framework.ecs;

import com.brahvim.nerd.papplet_wrapper.NerdSketch;

public class NerdEcsManager {

    // region Fields.
    public final NerdEcsEntityManager ENTITY_MAN;
    public final NerdEcsSystemManager SYSTEM_MAN;
    public final NerdEcsComponentManager COMPONENT_MAN;

    private final NerdSketch SKETCH;
    // endregion

    public NerdEcsManager(final NerdSketch p_sketch) {
        this.SKETCH = p_sketch;
        this.ENTITY_MAN = new NerdEcsEntityManager();
        this.SYSTEM_MAN = new NerdEcsSystemManager();
        this.COMPONENT_MAN = new NerdEcsComponentManager();
    }

    protected void updateAll() {
    }

    protected void updateComponents() {
        this.COMPONENT_MAN.update();
    }

    protected void updateEntities() {
        this.ENTITY_MAN.update();
    }

    // region Events.
    // region Mouse events.
    public void mousePressed() {
    }

    public void mouseReleased() {
    }

    public void mouseMoved() {
    }

    public void mouseClicked() {
    }

    public void mouseDragged() {
    }

    // @SuppressWarnings("unused")
    public void mouseWheel(final processing.event.MouseEvent p_mouseEvent) {
    }
    // endregion

    // region Keyboard events.
    public void keyTyped() {
    }

    public void keyPressed() {
    }

    public void keyReleased() {
    }
    // endregion

    // region Touch events.
    public void touchStarted() {
    }

    public void touchMoved() {
    }

    public void touchEnded() {
    }
    // endregion

    // region Window focus events.
    public void focusLost() {
    }

    public void exit() {
    }

    public void resized() {
    }

    public void focusGained() {
    }

    public void monitorChanged() {
    }

    public void fullscreenChanged(final boolean p_state) {
    }
    // endregion
    // endregion

}
