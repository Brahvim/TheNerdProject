package com.brahvim.nerd.framework.ecs;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

import com.brahvim.nerd.papplet_wrapper.NerdSketch;

public class NerdEcsManager {

    // region Fields.
    protected final LinkedList<NerdEcsEntity> ENTITIES = new LinkedList<>();
    protected final LinkedList<NerdEcsSystem<?>> SYSTEMS = new LinkedList<>();
    protected final HashMap<String, NerdEcsEntity> ENTITY_STRING_MAP = new HashMap<>();
    // protected final HashMap<Integer, NerdEcsEntity> ENTITY_HASHES_MAP;
    protected final LinkedList<NerdEcsComponent> COMPONENTS = new LinkedList<>();
    protected final HashMap<Class<? extends NerdEcsComponent>, HashSet<NerdEcsComponent>> COMPONENT_CLASS_MAP = new HashMap<>();

    private final HashSet<NerdEcsEntity> ENTITIES_TO_ADD = new HashSet<>();
    private final HashSet<NerdEcsEntity> ENTITIES_TO_REMOVE = new HashSet<>();

    private final HashSet<NerdEcsComponent> COMPONENTS_TO_ADD = new HashSet<>();
    private final HashSet<NerdEcsComponent> COMPONENTS_TO_REMOVE = new HashSet<>();
    private final NerdSketch SKETCH;
    // endregion

    public NerdEcsManager(final NerdSketch p_sketch) {
        this.SKETCH = p_sketch;
    }

    protected void runUpdates() {
        this.ENTITIES.removeAll(this.ENTITIES_TO_REMOVE);
        this.COMPONENTS.removeAll(this.COMPONENTS_TO_REMOVE);

        this.ENTITIES.addAll(this.ENTITIES_TO_ADD);
        this.COMPONENTS.addAll(this.COMPONENTS_TO_ADD);

        this.ENTITIES_TO_ADD.clear();
        this.COMPONENTS_TO_ADD.clear();
        this.ENTITIES_TO_REMOVE.clear();
        this.COMPONENTS_TO_REMOVE.clear();

        // Run all systems:
        for (Map.Entry<Class<? extends NerdEcsComponent>, HashSet<NerdEcsComponent>> e : this.COMPONENT_CLASS_MAP
                .entrySet()) {
            // this.SYSTEMS.get
        }
    }

    // region Public API! (For entities only!)
    public NerdEcsEntity createEntity() {
        final NerdEcsEntity toRet = new NerdEcsEntity();
        this.ENTITIES_TO_ADD.add(toRet);
        return toRet;
    }

    public void removeEntity(final NerdEcsEntity p_entity) {
        this.ENTITIES_TO_REMOVE.add(p_entity);
    }
    // endregion

    protected void addComponent(final NerdEcsComponent p_component) {
        this.COMPONENTS_TO_ADD.add(p_component);
    }

    protected void removeComponent(final NerdEcsComponent p_component) {
        this.COMPONENTS_TO_REMOVE.add(p_component);
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
