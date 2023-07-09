package com.brahvim.nerd.framework.ecs;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.brahvim.nerd.framework.NerdTriConsumer;
import com.brahvim.nerd.framework.scene_api.NerdSceneState;
import com.brahvim.nerd.io.NerdByteSerialUtils;
import com.brahvim.nerd.io.net.NerdUdpSocket;
import com.brahvim.nerd.io.net.tcp.NerdTcpServer;
import com.brahvim.nerd.processing_wrapper.NerdModule;
import com.brahvim.nerd.processing_wrapper.NerdSketch;

public class NerdEcsModule extends NerdModule implements Serializable {

	// region Inner-classes.
	/* `package` */ class NerdEntitySerializationPacket implements Serializable {

		private final String NAME;
		private final NerdEcsEntity ENTITY;

		public NerdEntitySerializationPacket(final String p_name, final NerdEcsEntity p_entity) {
			this.NAME = p_name;
			this.ENTITY = p_entity;
		}

	}
	// endregion

	// region Fields.
	public static final long serialVersionUID = -6488574946L;

	private static final transient Class<? extends NerdEcsSystem<? extends NerdEcsComponent>>[] DEFAULT_ECS_SYSTEMS_ORDER = null;
	// // Loooooooooong declaration!:
	// (Class<? extends NerdEcsSystem<? extends NerdEcsComponent>>[]) Set
	// .<Class<? extends NerdEcsSystem<? extends NerdEcsComponent>>>of(null, null,
	// null).toArray();

	protected final LinkedList<NerdEcsEntity> ENTITIES = new LinkedList<>();
	protected final LinkedList<NerdEcsComponent> COMPONENTS = new LinkedList<>();
	protected final HashMap<String, NerdEcsEntity> NAME_TO_ENTITY_MAP = new HashMap<>();
	protected final HashMap<Class<? extends NerdEcsComponent>, HashSet<NerdEcsComponent>> CLASSES_TO_COMPONENTS_MAP = new HashMap<>();

	protected long numUnnamedEntities = 1;
	protected NerdEcsSystem<?>[] ecsSystems;
	// endregion

	@SafeVarargs
	public NerdEcsModule(final NerdSketch p_sketch, final Class<? extends NerdEcsSystem<?>>... p_systems) {
		super(p_sketch);
		this.setSystemsOrder(p_systems);
	}

	// region `callOnAllSystems()` overloads.
	@SuppressWarnings("all")
	protected void callOnAllSystems(final BiConsumer<NerdEcsSystem, HashSet<? extends NerdEcsComponent>> p_methodRef) {
		if (!(p_methodRef == null || this.ecsSystems == null))
			for (final NerdEcsSystem<?> s : this.ecsSystems)
				if (s != null)
					p_methodRef.accept(s, this.CLASSES_TO_COMPONENTS_MAP.get(s.getComponentTypeClass()));
	}

	@SuppressWarnings("all")
	protected <OtherArgT> void callOnAllSystems(
			final NerdTriConsumer<NerdEcsSystem, OtherArgT, HashSet<? extends NerdEcsComponent>> p_methodRef,
			final OtherArgT p_otherArg) {
		if (!(p_methodRef == null || this.ecsSystems == null))
			for (final NerdEcsSystem<?> s : this.ecsSystems)
				if (s != null)
					p_methodRef.accept(s, p_otherArg, this.CLASSES_TO_COMPONENTS_MAP.get(s.getComponentTypeClass()));
	}

	@SuppressWarnings("all")
	protected <OtherArgT> void callOnAllSystems(
			final BiConsumer<NerdEcsSystem<?>, OtherArgT> p_methodRef, final OtherArgT p_otherArg) {
		if (!(p_methodRef == null || this.ecsSystems == null))
			for (final NerdEcsSystem<?> s : this.ecsSystems)
				if (s != null)
					p_methodRef.accept(s, p_otherArg);
	}

	// @SuppressWarnings("unchecked")
	protected void callOnAllSystems(final Consumer<NerdEcsSystem<?>> p_methodRef) {
		if (!(p_methodRef == null || this.ecsSystems == null))
			for (final NerdEcsSystem<?> s : this.ecsSystems)
				if (s != null)
					p_methodRef.accept(s);
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

	@Override
	@SuppressWarnings("unchecked")
	protected void pre() {
		this.callOnAllSystems(NerdEcsSystem::pre);
	}

	@SuppressWarnings("all")
	protected void draw() {
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
	public static Class<? extends NerdEcsSystem<? extends NerdEcsComponent>>[] getDefaultEcsSystemsOrder() {
		return NerdEcsModule.DEFAULT_ECS_SYSTEMS_ORDER;
	}

	public NerdEcsEntity createEntity() {
		return this.createEntity(null);
	}

	public NerdEcsSystem<?>[] getEcsSystems() {
		return this.ecsSystems;
	}

	public NerdEcsEntity createEntity(final String p_name) {
		final NerdEcsEntity toRet = new NerdEcsEntity(this);
		this.renameEntity(toRet, p_name);
		// this.entitiesToAdd.add(toRet);
		this.ENTITIES.add(toRet);
		return toRet;
	}

	public void removeEntity(final NerdEcsEntity p_entity) {
		// this.entitiesToRemove.add(p_entity);
		this.ENTITIES.add(p_entity);
	}

	public String getEntityName(final NerdEcsEntity p_entity) {
		for (final Map.Entry<String, NerdEcsEntity> e : this.NAME_TO_ENTITY_MAP.entrySet())
			if (e.getValue() == p_entity)
				return e.getKey();

		// If the entity is not from another manager, this won't ever be returned.
		return "";
	}

	@SafeVarargs
	public final void setSystemsOrder(final Class<? extends NerdEcsSystem<?>>... p_ecsSystems) {
		if (p_ecsSystems == null) {
			this.ecsSystems = null;
			return;
		}

		// Objects.requireNonNull(p_ecsSystems, "`NerdEcsManager::setSystemsOrder()`
		// can't take `null`! Come on...");

		this.ecsSystems = new NerdEcsSystem<?>[p_ecsSystems.length];

		for (int i = 0; i < this.ecsSystems.length; i++) {
			final Class<? extends NerdEcsSystem<?>> systemClass = p_ecsSystems[i];

			NerdEcsSystem<? extends NerdEcsComponent> system = null;
			try {
				system = systemClass.getConstructor().newInstance();
			} catch (final InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}

			if (system == null)
				return;

			this.ecsSystems[i] = system;
			final Class<? extends NerdEcsComponent> systemComponentTypeClass = system.getComponentTypeClass();
			// If `systemComponentTypeClass` does not exist in the map,
			this.CLASSES_TO_COMPONENTS_MAP.computeIfAbsent(systemComponentTypeClass, k -> new HashSet<>());
			// ...then PUT IT THERE!
		}
	}

	/**
	 * @apiNote Entity names parsable as positive {@code long}s are reserved.
	 * @param p_entity is the entity you wish to rename, and
	 * @param p_name   is the new name you wish to assign to it!
	 */
	public void renameEntity(final NerdEcsEntity p_entity, final String p_name) {
		if (p_name == null || p_name.isBlank()) {
			this.NAME_TO_ENTITY_MAP.put(Long.toString(this.numUnnamedEntities++), p_entity);
			return;
		}

		if (p_name.charAt(0) != '-')
			try {
				Long.valueOf(p_name);
			} catch (final NumberFormatException e) {
				this.NAME_TO_ENTITY_MAP.put(p_name, p_entity);
				return;
			}

		throw new UnsupportedOperationException(
				"Entity names parsable as positive `long`s are reserved. Sorry.");

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
		if (!this.CLASSES_TO_COMPONENTS_MAP.keySet().contains(componentClass))
			this.CLASSES_TO_COMPONENTS_MAP.put(componentClass, new HashSet<>());
		else
			this.CLASSES_TO_COMPONENTS_MAP.get(componentClass).add(p_component);
	}

	protected final void removeComponent(final NerdEcsComponent p_component) {
		// this.componentsToRemove.add(p_component);
		this.COMPONENTS.add(p_component);

		// Check if we've ever used this exact subclass of `NerdEcsComponent`.
		// ...If we do see if this component exists here and can be removed!:
		final Class<? extends NerdEcsComponent> componentClass = p_component.getClass();
		if (this.CLASSES_TO_COMPONENTS_MAP.keySet().contains(componentClass))
			this.CLASSES_TO_COMPONENTS_MAP.get(componentClass).remove(p_component);
	}
	// endregion

	// region Iteration!
	public void forEachEntity(final Consumer<? super NerdEcsEntity> p_action) {
		if (p_action != null)
			this.ENTITIES.forEach(p_action);
	}

	public void forEachComponent(final Consumer<? super NerdEcsComponent> p_action) {
		if (p_action != null)
			this.COMPONENTS.forEach(p_action);
	}

	public void forEachEntityWithName(final BiConsumer<String, NerdEcsEntity> p_action) {
		if (p_action != null)
			this.NAME_TO_ENTITY_MAP.forEach(p_action);

		// Older method:
		// for (final Map.Entry<String, NerdEcsEntity> e :
		// this.ENTITY_TO_NAME_MAP.entrySet())
		// p_action.accept(e.getKey(), e.getValue());
	}

	public void forEachEntityUnnamed(final Consumer<NerdEcsEntity> p_action) {
		if (p_action != null)
			for (final Map.Entry<String, NerdEcsEntity> entry : this.NAME_TO_ENTITY_MAP.entrySet()) {
				final String name = entry.getKey();

				try {
					Long.valueOf(name);
				} catch (final NumberFormatException e) {
					p_action.accept(entry.getValue());
				}
			}
	}

	public void forEachComponentClassUsed(final Consumer<? super Class<? extends NerdEcsComponent>> p_action) {
		// ...yeah, I borrowed that syntax with the generics. Thanks, VSCode!
		if (p_action != null)
			this.CLASSES_TO_COMPONENTS_MAP.keySet().forEach(p_action);
	}
	// endregion

	// region Serialization.
	// region Saving.
	public byte[] serializeEntity(final NerdEcsEntity p_entity) {
		final String name = this.getEntityName(p_entity);

		if ("".equals(name))
			return new byte[0];

		return NerdByteSerialUtils
				.toBytes(new NerdEntitySerializationPacket(name, p_entity));
	}

	public byte[] serializeComponent(final NerdEcsComponent p_component) {
		return NerdByteSerialUtils.toBytes(p_component);
	}

	/**
	 * You get this entire manager, serialized to bytes!
	 *
	 * @return The bytes!
	 * @see NerdEcsModule#saveState(File)
	 */
	public byte[] saveState() {
		return NerdByteSerialUtils.toBytes(this);
	}

	/**
	 * This entire manager, serialized to a file as bytes!
	 *
	 * @return Nothing! The file ate it all...
	 * @see NerdEcsModule#saveState()
	 */
	public void saveState(final File p_file) {
		NerdByteSerialUtils.toFile(this, p_file);
	}
	// endregion

	// region Loading.
	/**
	 * Ever called {@link NerdEcsModule#saveState(File)}? This reverses that.
	 *
	 * @param p_file is the file in context.
	 */
	public void loadState(final File p_file) {
		this.loadStateImpl(NerdByteSerialUtils.fromFile(p_file));
	}

	/**
	 * Ever called {@link NerdEcsModule#saveState()}? This reverses the bytes you
	 * got from there, for free!
	 *
	 * @param p_serializedData better have the bytes I talked about!
	 */
	public void loadState(final byte[] p_serializedData) {
		this.loadStateImpl(NerdByteSerialUtils.fromBytes(p_serializedData));
	}

	private void loadStateImpl(final NerdEcsModule p_deserialized) {
		this.ecsSystems = p_deserialized.ecsSystems;
		this.numUnnamedEntities = p_deserialized.numUnnamedEntities;

		this.CLASSES_TO_COMPONENTS_MAP.clear();
		this.CLASSES_TO_COMPONENTS_MAP.putAll(p_deserialized.CLASSES_TO_COMPONENTS_MAP);

		// region Reducing `LinkedList` elements, and modifying `NAME_TO_ENTITY_MAP`.
		// Remove elements not available in the lists in the deserialized manager:
		for (final Map.Entry<?, ?> e : Map.<LinkedList<?>, LinkedList<?>>of(
				this.ENTITIES, p_deserialized.ENTITIES,
				this.COMPONENTS, p_deserialized.COMPONENTS).entrySet()) {
			final LinkedList<?> myList = (LinkedList<?>) e.getKey(), otherList = (LinkedList<?>) e.getValue();

			for (int i = myList.size() - 1; i != 0; i--) {
				final Object o = myList.get(i);
				if (!otherList.contains(o))
					myList.remove(o);
			}
		}

		// region Remove elements not available in the maps in the deserialized manager.

		// There's nothing like `Set::get()`! Storing stuff to remove then removing it!:
		final HashSet<String> toRemove = new HashSet<>();
		final HashMap<String, NerdEcsEntity> myMap = this.NAME_TO_ENTITY_MAP,
				otherMap = p_deserialized.NAME_TO_ENTITY_MAP;

		for (final Map.Entry<String, NerdEcsEntity> e : myMap.entrySet()) {
			final String key = e.getKey();
			if (!otherMap.containsKey(key))
				toRemove.add(key);
		}

		for (final String s : toRemove)
			myMap.remove(s);

		for (final Map.Entry<String, NerdEcsEntity> e : otherMap.entrySet())
			myMap.putIfAbsent(e.getKey(), e.getValue());
		// endregion
		// endregion

		// region Copying components over.
		final int iterations = this.COMPONENTS.size();
		for (int i = 0; i < iterations; i++) {
			final NerdEcsComponent orig = this.COMPONENTS.get(i), latest = p_deserialized.COMPONENTS.get(i);
			orig.copyFieldsFrom(latest);
		}
		// endregion
	}
	// endregion
	// endregion

	// region Networking.
	public void startSocket(final Class<NerdTcpServer> p_socketType, final String p_ip, final int p_port) {
	}

	public void startSocket(final Class<NerdUdpSocket> p_socketType) {
	}
	// endregion

	// region Events.
	// region Mouse events.
	@SuppressWarnings("all")
	public void mousePressed() {
		this.callOnAllSystems(NerdEcsSystem::mousePressed);
	}

	// @SuppressWarnings("all")
	// protected void mousePressed() {
	// for (final NerdEcsSystem s : this.SYSTEMS) {
	// if (s == null)
	// continue;
	// final int numComponents = this.COMPONENTS.size();
	// final int numComponentsMinusTwo = this.COMPONENTS.size() - 2;
	// for (final int i = 0; i < numComponents;) {
	// final NerdEcsComponent c = this.COMPONENTS.get(i);
	// // The logic/Math here needs improvement, sure...
	// final NerdEcsComponent p = i < 0 ? null : this.COMPONENTS.get(i + 1);
	// final NerdEcsComponent n = i > numComponentsMinusTwo ? null :
	// this.COMPONENTS.get(i + 1);
	// if (c == null)
	// continue;
	// if (c.getClass().equals(s.getComponentTypeClass())) {
	// if (!s.mousePressed(i, p, c, n)) // Perhaps the methods also allow for
	// breaking iteration?
	// break;
	// }
	// }
	// }
	// }

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
