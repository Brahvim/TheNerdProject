package com.brahvim.nerd.framework.ecs;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.brahvim.nerd.framework.TriConsumer;
import com.brahvim.nerd.framework.scene_api.NerdSceneState;
import com.brahvim.nerd.processing_wrapper.NerdSketch;

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

	// region `callOnAllSystems()` overloads.
	@SuppressWarnings("all")
	protected void callOnAllSystems(final BiConsumer<NerdEcsSystem, HashSet<? extends NerdEcsComponent>> p_methodRef) {
		if (p_methodRef != null)
			for (final NerdEcsSystem s : this.systems)
				p_methodRef.accept(s, this.COMPONENTS_TO_CLASSES_MAP.get(s.getComponentTypeClass()));
	}

	@SuppressWarnings("all")
	protected <OtherArgT> void callOnAllSystems(
			final TriConsumer<NerdEcsSystem, OtherArgT, HashSet<? extends NerdEcsComponent>> p_methodRef,
			OtherArgT p_otherArg) {
		if (p_methodRef != null)
			for (final NerdEcsSystem s : this.systems)
				p_methodRef.accept(s, p_otherArg,
						this.COMPONENTS_TO_CLASSES_MAP
								.get(s.getComponentTypeClass()));
	}

	@SuppressWarnings("all")
	protected <OtherArgT> void callOnAllSystems(
			final BiConsumer<NerdEcsSystem, OtherArgT> p_methodRef, OtherArgT p_otherArg) {
		if (p_methodRef != null)
			for (final NerdEcsSystem s : this.systems)
				p_methodRef.accept(s, p_otherArg);
	}

	@SuppressWarnings("all")
	protected void callOnAllSystems(final Consumer<NerdEcsSystem> p_method) {
		if (p_method != null)
			for (final NerdEcsSystem<?> s : this.systems)
				p_method.accept(s);
	}
	// endregion

	// region Sketch workflow callbacks (declared as `protected`).
	@SuppressWarnings("all")
	protected synchronized void preload() {
		this.callOnAllSystems(NerdEcsSystem::preload);
	}

	@SuppressWarnings("all")
	protected void sceneChanged() {
		this.callOnAllSystems(NerdEcsSystem::sceneChanged);
	}

	@SuppressWarnings("all")
	protected void setup(final NerdSceneState p_state) {
		this.callOnAllSystems(NerdEcsSystem::setup, p_state);
	}

	@SuppressWarnings("all")
	protected void pre() {
		this.callOnAllSystems(NerdEcsSystem::pre);
	}

	@SuppressWarnings("all")
	protected void draw() {
		this.ENTITIES.removeAll(this.ENTITIES_TO_REMOVE);
		this.COMPONENTS.removeAll(this.COMPONENTS_TO_REMOVE);

		this.ENTITIES.addAll(this.ENTITIES_TO_ADD);
		this.COMPONENTS.addAll(this.COMPONENTS_TO_ADD);

		this.ENTITIES_TO_ADD.clear();
		this.COMPONENTS_TO_ADD.clear();
		this.ENTITIES_TO_REMOVE.clear();
		this.COMPONENTS_TO_REMOVE.clear();

		this.callOnAllSystems(NerdEcsSystem::draw);
	}

	@SuppressWarnings("all")
	protected void post() {
		this.callOnAllSystems(NerdEcsSystem::post);
	}

	@SuppressWarnings("all")
	protected void exit() {
		this.callOnAllSystems(NerdEcsSystem::exit);
	}

	@SuppressWarnings("all")
	protected void dispose() {
		this.callOnAllSystems(NerdEcsSystem::dispose);
	}
	// endregion

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

	protected final void addComponent(final NerdEcsComponent p_component) {
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

	protected final void removeComponent(final NerdEcsComponent p_component) {
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
		this.callOnAllSystems(NerdEcsSystem::mousePressed);
	}

	@SuppressWarnings("all")
	public void mouseReleased() {
		this.callOnAllSystems(NerdEcsSystem::mouseReleased);
	}

	@SuppressWarnings("all")
	public void mouseMoved() {
		this.callOnAllSystems(NerdEcsSystem::mouseMoved);
	}

	@SuppressWarnings("all")
	public void mouseClicked() {
		this.callOnAllSystems(NerdEcsSystem::mouseClicked);
	}

	@SuppressWarnings("all")
	public void mouseDragged() {
		this.callOnAllSystems(NerdEcsSystem::mouseDragged);
	}

	@SuppressWarnings(/* { */ "all" /* , unused } */)
	public void mouseWheel(final processing.event.MouseEvent p_mouseEvent) {
		this.callOnAllSystems(NerdEcsSystem::mouseWheel, p_mouseEvent);
	}
	// endregion

	// region Keyboard events.
	@SuppressWarnings("all")
	public void keyTyped() {
		this.callOnAllSystems(NerdEcsSystem::keyTyped);
	}

	@SuppressWarnings("all")
	public void keyPressed() {
		this.callOnAllSystems(NerdEcsSystem::keyPressed);
	}

	@SuppressWarnings("all")
	public void keyReleased() {
		this.callOnAllSystems(NerdEcsSystem::keyReleased);
	}
	// endregion

	// region Touch events.
	@SuppressWarnings("all")
	public void touchStarted() {
		this.callOnAllSystems(NerdEcsSystem::touchStarted);
	}

	@SuppressWarnings("all")
	public void touchMoved() {
		this.callOnAllSystems(NerdEcsSystem::touchMoved);
	}

	@SuppressWarnings("all")
	public void touchEnded() {
		this.callOnAllSystems(NerdEcsSystem::touchEnded);
	}
	// endregion

	// region Window focus event
	@SuppressWarnings("all")
	public void focusLost() {
		this.callOnAllSystems(NerdEcsSystem::focusLost);
	}

	@SuppressWarnings("all")
	public void resized() {
		this.callOnAllSystems(NerdEcsSystem::resized);
	}

	@SuppressWarnings("all")
	public void focusGained() {
		this.callOnAllSystems(NerdEcsSystem::focusGained);
	}

	@SuppressWarnings("all")
	public void monitorChanged() {
		this.callOnAllSystems(NerdEcsSystem::monitorChanged);
	}

	@SuppressWarnings("all")
	public void fullscreenChanged(final boolean p_state) {
		this.callOnAllSystems(NerdEcsSystem::fullscreenChanged, p_state);
	}
	// endregion
	// endregion

}
