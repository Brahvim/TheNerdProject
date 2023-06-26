package com.brahvim.nerd.framework.ecs;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.brahvim.nerd.framework.NerdTriConsumer;
import com.brahvim.nerd.framework.scene_api.NerdSceneState;
import com.brahvim.nerd.io.NerdByteSerial;
import com.brahvim.nerd.processing_wrapper.NerdSketch;

public class NerdEcsManager implements Serializable {

	// public enum SerializationTactic {
	// KEEP_FLAGS(),
	// REMOVE_FLAGS(),
	// FLAGS_EVALUATE_NEXT_FRAME(),
	// FLAGS_EVALUATE_IMMEDIATELY(),
	// }

	// region Fields.
	public static final long serialVersionUID = -6488574946L;

	public static final transient NerdEcsSystem<?>[] DEFAULT_ECS_SYSTEMS_ORDER = {
			// ...Will fill this in logically, as Nerd gets more ECS wrappers!
	};

	protected final LinkedList<NerdEcsEntity> ENTITIES = new LinkedList<>();
	protected final LinkedList<NerdEcsComponent> COMPONENTS = new LinkedList<>();
	protected final HashMap<String, NerdEcsEntity> ENTITY_TO_NAME_MAP = new HashMap<>();
	protected final HashMap<Class<? extends NerdEcsComponent>, HashSet<NerdEcsComponent>> COMPONENTS_TO_CLASSES_MAP = new HashMap<>();

	protected NerdEcsSystem<?>[] systemsInOrder;

	private final transient NerdSketch SKETCH;

	// private SerializationTactic serializationTactic;

	// private HashSet<NerdEcsEntity> entitiesToAdd = new HashSet<>();
	// private HashSet<NerdEcsEntity> entitiesToRemove = new HashSet<>();

	// private HashSet<NerdEcsComponent> componentsToAdd = new HashSet<>();
	// private HashSet<NerdEcsComponent> componentsToRemove = new HashSet<>();
	// endregion

	public NerdEcsManager(final NerdSketch p_sketch, final NerdEcsSystem<?>[] p_systems) {
		this.SKETCH = p_sketch;
		this.setSystemsOrder(p_systems);
	}

	// region `callOnAllSystems()` overloads.
	@SuppressWarnings("all")
	protected void callOnAllSystems(final BiConsumer<NerdEcsSystem, HashSet<? extends NerdEcsComponent>> p_methodRef) {
		if (p_methodRef != null)
			for (final NerdEcsSystem s : this.systemsInOrder)
				p_methodRef.accept(s, this.COMPONENTS_TO_CLASSES_MAP.get(s.getComponentTypeClass()));
	}

	@SuppressWarnings("all")
	protected <OtherArgT> void callOnAllSystems(
			final NerdTriConsumer<NerdEcsSystem, OtherArgT, HashSet<? extends NerdEcsComponent>> p_methodRef,
			final OtherArgT p_otherArg) {
		if (p_methodRef != null)
			for (final NerdEcsSystem<?> s : this.systemsInOrder)
				p_methodRef.accept(s, p_otherArg,
						this.COMPONENTS_TO_CLASSES_MAP
								.get(s.getComponentTypeClass()));
	}

	protected <OtherArgT> void callOnAllSystems(
			final BiConsumer<NerdEcsSystem<?>, OtherArgT> p_methodRef, final OtherArgT p_otherArg) {
		if (p_methodRef != null)
			for (final NerdEcsSystem<?> s : this.systemsInOrder)
				p_methodRef.accept(s, p_otherArg);
	}

	// @SuppressWarnings("unchecked")
	protected void callOnAllSystems(final Consumer<NerdEcsSystem<?>> p_method) {
		if (p_method != null)
			for (final NerdEcsSystem<?> s : this.systemsInOrder)
				p_method.accept(s);
	}
	// endregion

	// region Sketch workflow callbacks (declared as `protected`).
	@SuppressWarnings("unchecked")
	protected synchronized void preload() {
		this.callOnAllSystems(NerdEcsSystem::preload);
	}

	@SuppressWarnings("unchecked")
	protected void sceneChanged() {
		this.callOnAllSystems(NerdEcsSystem::sceneChanged);
	}

	@SuppressWarnings("unchecked")
	protected void setup(final NerdSceneState p_state) {
		this.callOnAllSystems(NerdEcsSystem::setup, p_state);
	}

	@SuppressWarnings("unchecked")
	protected void pre() {
		this.callOnAllSystems(NerdEcsSystem::pre);
	}

	@SuppressWarnings("all")
	protected void draw() {
		// this.ENTITIES.removeAll(this.entitiesToRemove);
		// this.COMPONENTS.removeAll(this.componentsToRemove);

		// this.ENTITIES.addAll(this.entitiesToAdd);
		// this.COMPONENTS.addAll(this.componentsToAdd);

		// this.entitiesToAdd.clear();
		// this.componentsToAdd.clear();
		// this.entitiesToRemove.clear();
		// this.componentsToRemove.clear();

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
		final NerdEcsEntity toRet = new NerdEcsEntity(this);
		// this.entitiesToAdd.add(toRet);
		this.ENTITIES.add(toRet);
		return toRet;
	}

	public NerdEcsSystem<?>[] getSystemsOrder() {
		return this.systemsInOrder;
	}

	public void removeEntity(final NerdEcsEntity p_entity) {
		// this.entitiesToRemove.add(p_entity);
		this.ENTITIES.add(p_entity);
	}

	public void setSystemsOrder(final NerdEcsSystem<?>[] p_ecsSystems) {
		Objects.requireNonNull(p_ecsSystems, "That can't be `null`! Come on...");

		for (final NerdEcsSystem<?> s : p_ecsSystems) {
			final Class<? extends NerdEcsComponent> systemComponentTypeClass = s.getComponentTypeClass();
			// If `systemComponentTypeClass` does not exist in the map,
			this.COMPONENTS_TO_CLASSES_MAP.computeIfAbsent(systemComponentTypeClass, k -> new HashSet<>());
			// ...then PUT IT THERE!
		}

		this.systemsInOrder = p_ecsSystems;
	}
	// endregion

	// region Dear systems and entities, secretly use this stuff. Hehe!
	protected final void addComponent(final NerdEcsComponent p_component) {
		// this.componentsToAdd.add(p_component);
		this.COMPONENTS.add(p_component);

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
		// this.componentsToRemove.add(p_component);
		this.COMPONENTS.add(p_component);

		// Check if we've ever used this exact subclass of `NerdEcsComponent`.
		// ...If we do see if this component exists here and can be removed!:
		final Class<? extends NerdEcsComponent> componentClass = p_component.getClass();
		if (this.COMPONENTS_TO_CLASSES_MAP.keySet().contains(componentClass))
			this.COMPONENTS_TO_CLASSES_MAP.get(componentClass).remove(p_component);
	}
	// endregion

	// TODO: Region of code on iteration!

	// region Serialization.
	// region Saving.
	public byte[] saveState() {
		return this.saveState(/* true */);
	}

	/*
	 * public byte[] saveState(final SerializationTactic p_serializationTactic) {
	 * final byte[] toRet;
	 * 
	 * if (p_serializationTactic == SerializationTactic.REMOVE_FLAGS) {
	 * var a = this.entitiesToAdd;
	 * var b = this.componentsToAdd;
	 * var c = this.entitiesToRemove;
	 * var d = this.componentsToRemove;
	 * 
	 * this.entitiesToAdd = new HashSet<>();
	 * this.componentsToAdd = new HashSet<>();
	 * this.entitiesToRemove = new HashSet<>();
	 * this.componentsToRemove = new HashSet<>();
	 * 
	 * toRet = NerdByteSerial.toBytes(this);
	 * 
	 * this.entitiesToAdd = a;
	 * this.componentsToAdd = b;
	 * this.entitiesToRemove = c;
	 * this.componentsToRemove = d;
	 * } else
	 * toRet = NerdByteSerial.toBytes(this);
	 * 
	 * return toRet;
	 * }
	 */

	public void saveState(final File p_file /* , final boolean p_keepAddAndRemoveFlags */) {
		/*
		 * if (p_keepAddAndRemoveFlags)
		 * NerdByteSerial.toFile(this, p_file);
		 * else {
		 * var a = this.entitiesToAdd;
		 * var b = this.componentsToAdd;
		 * var c = this.entitiesToRemove;
		 * var d = this.componentsToRemove;
		 * 
		 * this.entitiesToAdd = new HashSet<>();
		 * this.componentsToAdd = new HashSet<>();
		 * this.entitiesToRemove = new HashSet<>();
		 * this.componentsToRemove = new HashSet<>();
		 * 
		 * NerdByteSerial.toFile(this, p_file);
		 * 
		 * this.entitiesToAdd = a;
		 * this.componentsToAdd = b;
		 * this.entitiesToRemove = c;
		 * this.componentsToRemove = d;
		 * }
		 */

		NerdByteSerial.toFile(this, p_file);
	}
	// endregion

	// region Loading.
	public void loadState(final byte[] p_serializedData) {
		this.loadStateImpl(NerdByteSerial.fromBytes(p_serializedData));
	}

	public void loadState(final File p_file) {
		this.loadStateImpl(NerdByteSerial.fromFile(p_file));
	}

	private void loadStateImpl(final NerdEcsManager p_deserialized) {

	}
	// endregion
	// endregion

	// region Networking.
	// endregion

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
