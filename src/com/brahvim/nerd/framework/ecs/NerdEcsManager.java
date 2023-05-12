package com.brahvim.nerd.framework.ecs;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import com.brahvim.nerd.papplet_wrapper.NerdSketch;

public class NerdEcsManager {

    // region Fields.
    public static final NerdEcsSystem<?>[] DEFAULT_ECS_SYSTEMS_ORDER = {
            // ...Will fill this in logically, as Nerd gets more ECS wrappers!
    };

    protected final LinkedList<NerdEcsEntity> ENTITIES = new LinkedList<>();
    protected final LinkedList<NerdEcsComponent> COMPONENTS = new LinkedList<>();
    protected final HashMap<String, NerdEcsEntity> ENTITY_STRING_MAP = new HashMap<>();
    protected final HashMap<Class<? extends NerdEcsComponent>, HashSet<NerdEcsComponent>> COMPONENTS_TO_CLASSES_MAP = new HashMap<>();

    private final HashSet<NerdEcsEntity> ENTITIES_TO_ADD = new HashSet<>();
    private final HashSet<NerdEcsEntity> ENTITIES_TO_REMOVE = new HashSet<>();

    protected NerdEcsSystem<?>[] systems;

    private final HashSet<NerdEcsComponent> COMPONENTS_TO_ADD = new HashSet<>();
    private final HashSet<NerdEcsComponent> COMPONENTS_TO_REMOVE = new HashSet<>();

    @SuppressWarnings("unused")
    private final NerdSketch SKETCH;
    // endregion

    public NerdEcsManager(final NerdSketch p_sketch, final NerdEcsSystem<?>[] p_systems) {
        this.SKETCH = p_sketch;
        this.setSystemsOrder(p_systems);
    }

    @SuppressWarnings("all")
    protected void runUpdates() {
        this.ENTITIES.removeAll(this.ENTITIES_TO_REMOVE);
        this.COMPONENTS.removeAll(this.COMPONENTS_TO_REMOVE);

        this.ENTITIES.addAll(this.ENTITIES_TO_ADD);
        this.COMPONENTS.addAll(this.COMPONENTS_TO_ADD);

        this.ENTITIES_TO_ADD.clear();
        this.COMPONENTS_TO_ADD.clear();
        this.ENTITIES_TO_REMOVE.clear();
        this.COMPONENTS_TO_REMOVE.clear();

        for (final NerdEcsSystem s : this.systems)
            s.update(this.COMPONENTS_TO_CLASSES_MAP.get(s.getComponentTypeClass()));
    }

    // region Public API!
    public NerdEcsEntity createEntity() {
        final NerdEcsEntity toRet = new NerdEcsEntity();
        this.ENTITIES_TO_ADD.add(toRet);
        toRet.ecsMan = this;
        return toRet;
    }

    public void removeEntity(final NerdEcsEntity p_entity) {
        this.ENTITIES_TO_REMOVE.add(p_entity);
    }

    public NerdEcsSystem<?>[] getSystemsOrder() {
        return this.systems;
    }

    @SuppressWarnings("all")
    public void setSystemsOrder(final NerdEcsSystem<?>[] p_ecsSystems) {
        this.systems = p_ecsSystems;

        for (final NerdEcsSystem s : p_ecsSystems) {
            final Class<? extends NerdEcsComponent> systemComponentTypeClass = s.getComponentTypeClass();
            if (!this.COMPONENTS_TO_CLASSES_MAP.containsKey(systemComponentTypeClass))
                this.COMPONENTS_TO_CLASSES_MAP.put(systemComponentTypeClass, new HashSet<NerdEcsComponent>());
        }

        // for (final Map.Entry<Class<? extends NerdEcsComponent>,
        // HashSet<NerdEcsComponent>> e : this.COMPONENTS_TO_CLASSES_MAP
        // .entrySet()) {
        // }
    }
    // endregion

    protected void addComponent(final NerdEcsComponent p_component) {
        this.COMPONENTS_TO_ADD.add(p_component);

        // Check if we've ever used this exact subclass of `NerdEcsComponent`.
        // If not, give it a `HashSet<NerdEcsComponent>` of its own!
        // ...Else, we go adding those components in!:
        final Class<? extends NerdEcsComponent> componentClass = p_component.getClass();
        if (!this.COMPONENTS_TO_CLASSES_MAP.keySet().contains(componentClass))
            this.COMPONENTS_TO_CLASSES_MAP.put(componentClass, new HashSet<>());
        else
            this.COMPONENTS_TO_CLASSES_MAP.get(componentClass).add(p_component);
    }

    protected void removeComponent(final NerdEcsComponent p_component) {
        this.COMPONENTS_TO_REMOVE.add(p_component);

        // Check if we've ever used this exact subclass of `NerdEcsComponent`.
        // ...If we do see if this component exists here and can be removed!:
        final Class<? extends NerdEcsComponent> componentClass = p_component.getClass();
        if (this.COMPONENTS_TO_CLASSES_MAP.keySet().contains(componentClass))
            this.COMPONENTS_TO_CLASSES_MAP.get(componentClass).remove(p_component);
    }

    // region Events.
    // region Mouse events.
    @SuppressWarnings("all")
    public void mousePressed() {
        for (final NerdEcsSystem s : this.systems)
            s.mousePressed(this.COMPONENTS_TO_CLASSES_MAP.get(s.getComponentTypeClass()));
    }

    @SuppressWarnings("all")
    public void mouseReleased() {
        for (final NerdEcsSystem s : this.systems)
            s.mouseReleased(this.COMPONENTS_TO_CLASSES_MAP.get(s.getComponentTypeClass()));
    }

    @SuppressWarnings("all")
    public void mouseMoved() {
        for (final NerdEcsSystem s : this.systems)
            s.mouseMoved(this.COMPONENTS_TO_CLASSES_MAP.get(s.getComponentTypeClass()));
    }

    @SuppressWarnings("all")
    public void mouseClicked() {
        for (final NerdEcsSystem s : this.systems)
            s.mouseClicked(this.COMPONENTS_TO_CLASSES_MAP.get(s.getComponentTypeClass()));
    }

    @SuppressWarnings("all")
    public void mouseDragged() {
        for (final NerdEcsSystem s : this.systems)
            s.mouseDragged(this.COMPONENTS_TO_CLASSES_MAP.get(s.getComponentTypeClass()));
    }

    @SuppressWarnings(/* { */ "all" /* , unused } */)
    public void mouseWheel(final processing.event.MouseEvent p_mouseEvent) {
        for (final NerdEcsSystem s : this.systems)
            s.mouseWheel(p_mouseEvent, this.COMPONENTS_TO_CLASSES_MAP.get(s.getComponentTypeClass()));
    }
    // endregion

    // region Keyboard events.
    @SuppressWarnings("all")
    public void keyTyped() {
        for (final NerdEcsSystem s : this.systems)
            s.keyTyped(this.COMPONENTS_TO_CLASSES_MAP.get(s.getComponentTypeClass()));
    }

    @SuppressWarnings("all")
    public void keyPressed() {
        for (final NerdEcsSystem s : this.systems)
            s.keyPressed(this.COMPONENTS_TO_CLASSES_MAP.get(s.getComponentTypeClass()));
    }

    @SuppressWarnings("all")
    public void keyReleased() {
        for (final NerdEcsSystem s : this.systems)
            s.keyReleased(this.COMPONENTS_TO_CLASSES_MAP.get(s.getComponentTypeClass()));
    }
    // endregion

    // region Touch events.
    @SuppressWarnings("all")
    public void touchStarted() {
        for (final NerdEcsSystem s : this.systems)
            s.touchStarted(this.COMPONENTS_TO_CLASSES_MAP.get(s.getComponentTypeClass()));
    }

    @SuppressWarnings("all")
    public void touchMoved() {
        for (final NerdEcsSystem s : this.systems)
            s.touchMoved(this.COMPONENTS_TO_CLASSES_MAP.get(s.getComponentTypeClass()));
    }

    @SuppressWarnings("all")
    public void touchEnded() {
        for (final NerdEcsSystem s : this.systems)
            s.touchEnded(this.COMPONENTS_TO_CLASSES_MAP.get(s.getComponentTypeClass()));
    }
    // endregion

    // region Window focus event
    @SuppressWarnings("all")
    public void focusLost() {
        for (final NerdEcsSystem s : this.systems)
            s.focusLost(this.COMPONENTS_TO_CLASSES_MAP.get(s.getComponentTypeClass()));
    }

    @SuppressWarnings("all")
    public void exit() {
        for (final NerdEcsSystem s : this.systems)
            s.exit(this.COMPONENTS_TO_CLASSES_MAP.get(s.getComponentTypeClass()));
    }

    @SuppressWarnings("all")
    public void resized() {
        for (final NerdEcsSystem s : this.systems)
            s.resized(this.COMPONENTS_TO_CLASSES_MAP.get(s.getComponentTypeClass()));
    }

    @SuppressWarnings("all")
    public void focusGained() {
        for (final NerdEcsSystem s : this.systems)
            s.focusGained(this.COMPONENTS_TO_CLASSES_MAP.get(s.getComponentTypeClass()));
    }

    @SuppressWarnings("all")
    public void monitorChanged() {
        for (final NerdEcsSystem s : this.systems)
            s.monitorChanged(this.COMPONENTS_TO_CLASSES_MAP.get(s.getComponentTypeClass()));
    }

    @SuppressWarnings("all")
    public void fullscreenChanged(final boolean p_state) {
        for (final NerdEcsSystem s : this.systems)
            s.fullscreenChanged(p_state, this.COMPONENTS_TO_CLASSES_MAP.get(s.getComponentTypeClass()));
    }
    // endregion
    // endregion

}
