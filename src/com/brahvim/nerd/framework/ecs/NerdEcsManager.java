package com.brahvim.nerd.framework.ecs;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import com.brahvim.nerd.papplet_wrapper.NerdSketch;

public class NerdEcsManager {

    // region Fields.
    public static final NerdEcsSystem<?>[] DEFAULT_ECS_SYSTEMS_ORDER = {

    };

    protected final NerdEcsSystem<?>[] SYSTEMS;
    protected final LinkedList<NerdEcsEntity> ENTITIES = new LinkedList<>();
    protected final LinkedList<NerdEcsComponent> COMPONENTS = new LinkedList<>();
    protected final HashMap<String, NerdEcsEntity> ENTITY_STRING_MAP = new HashMap<>();
    protected final HashMap<Class<? extends NerdEcsComponent>, HashSet<NerdEcsComponent>> COMPONENT_CLASS_MAP = new HashMap<>();

    private final HashSet<NerdEcsEntity> ENTITIES_TO_ADD = new HashSet<>();
    private final HashSet<NerdEcsEntity> ENTITIES_TO_REMOVE = new HashSet<>();

    private final HashSet<NerdEcsComponent> COMPONENTS_TO_ADD = new HashSet<>();
    private final HashSet<NerdEcsComponent> COMPONENTS_TO_REMOVE = new HashSet<>();

    @SuppressWarnings("unused")
    private final NerdSketch SKETCH;
    // endregion

    public NerdEcsManager(final NerdSketch p_sketch, final NerdEcsSystem<?>[] p_systems) {
        this.SKETCH = p_sketch;
        this.SYSTEMS = p_systems;
    }

    @SuppressWarnings("all")
    protected void runUpdates() {
        this.ENTITIES.removeAll(this.ENTITIES_TO_REMOVE);
        this.COMPONENTS.removeAll(this.COMPONENTS_TO_REMOVE);

        for (final NerdEcsSystem s : this.SYSTEMS)
            s.update(this.COMPONENT_CLASS_MAP.get(s.getComponentTypeClass()));

        this.ENTITIES.addAll(this.ENTITIES_TO_ADD);
        this.COMPONENTS.addAll(this.COMPONENTS_TO_ADD);

        this.ENTITIES_TO_ADD.clear();
        this.COMPONENTS_TO_ADD.clear();
        this.ENTITIES_TO_REMOVE.clear();
        this.COMPONENTS_TO_REMOVE.clear();
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
        for (final NerdEcsSystem<?> s : this.SYSTEMS)
            s.mousePressed();
    }

    public void mouseReleased() {
        for (final NerdEcsSystem<?> s : this.SYSTEMS)
            s.mouseReleased();
    }

    public void mouseMoved() {
        for (final NerdEcsSystem<?> s : this.SYSTEMS)
            s.mouseMoved();
    }

    public void mouseClicked() {
        for (final NerdEcsSystem<?> s : this.SYSTEMS)
            s.mouseClicked();
    }

    public void mouseDragged() {
        for (final NerdEcsSystem<?> s : this.SYSTEMS)
            s.mouseDragged();
    }

    // @SuppressWarnings("unused")
    public void mouseWheel(final processing.event.MouseEvent p_mouseEvent) {
        for (final NerdEcsSystem<?> s : this.SYSTEMS)
            s.mouseWheel(p_mouseEvent);
    }
    // endregion

    // region Keyboard events.
    public void keyTyped() {
        for (final NerdEcsSystem<?> s : this.SYSTEMS)
            s.keyTyped();
    }

    public void keyPressed() {
        for (final NerdEcsSystem<?> s : this.SYSTEMS)
            s.keyPressed();
    }

    public void keyReleased() {
        for (final NerdEcsSystem<?> s : this.SYSTEMS)
            s.keyReleased();
    }
    // endregion

    // region Touch events.
    public void touchStarted() {
        for (final NerdEcsSystem<?> s : this.SYSTEMS)
            s.touchStarted();
    }

    public void touchMoved() {
        for (final NerdEcsSystem<?> s : this.SYSTEMS)
            s.touchMoved();
    }

    public void touchEnded() {
        for (final NerdEcsSystem<?> s : this.SYSTEMS)
            s.touchEnded();
    }
    // endregion

    // region Window focus events.
    public void focusLost() {
        for (final NerdEcsSystem<?> s : this.SYSTEMS)
            s.focusLost();
    }

    public void exit() {
        for (final NerdEcsSystem<?> s : this.SYSTEMS)
            s.exit();
    }

    public void resized() {
        for (final NerdEcsSystem<?> s : this.SYSTEMS)
            s.resized();
    }

    public void focusGained() {
        for (final NerdEcsSystem<?> s : this.SYSTEMS)
            s.focusGained();
    }

    public void monitorChanged() {
        for (final NerdEcsSystem<?> s : this.SYSTEMS)
            s.monitorChanged();
    }

    public void fullscreenChanged(final boolean p_state) {
        for (final NerdEcsSystem<?> s : this.SYSTEMS)
            s.fullscreenChanged(p_state);
    }
    // endregion
    // endregion

}
