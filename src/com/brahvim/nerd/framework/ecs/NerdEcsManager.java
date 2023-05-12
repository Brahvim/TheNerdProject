package com.brahvim.nerd.framework.ecs;

import java.util.HashMap;
import java.util.LinkedList;

import com.brahvim.nerd.papplet_wrapper.NerdSketch;

public class NerdEcsManager {

    // region Fields.
    protected final LinkedList<NerdEcsEntity> ENTITIES = new LinkedList<>();
    protected final HashMap<String, NerdEcsEntity> STRING_MAP = new HashMap<>();
    protected final HashMap<Integer, NerdEcsEntity> HASHES_MAP = new HashMap<>();
    protected final LinkedList<NerdEcsComponent> COMPONENTS = new LinkedList<>();
    protected final HashMap<Class<? extends NerdEcsComponent>, NerdEcsComponent> CLASS_MAP = new HashMap<>();

    // private final HashSet<Object>
    private final NerdSketch SKETCH;
    // endregion

    public NerdEcsManager(final NerdSketch p_sketch) {
        this.SKETCH = p_sketch;
    }

    protected void updateAll() {
    }

    protected void updateComponents() {
        for (final NerdEcsComponent c : this.COMPONENTS)
            c.update();
    }

    // protected void updateEntities() {
    // for (final NerdEcsEntity e : this.ENTITIES)
    // e.update();
    // }

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
