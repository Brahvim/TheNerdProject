package com.brahvim.nerd.framework.ecs;

import java.io.File;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.brahvim.nerd.framework.NerdTriConsumer;
import com.brahvim.nerd.framework.scene_api.NerdSceneState;
import com.brahvim.nerd.io.NerdByteSerial;
import com.brahvim.nerd.io.net.NerdSocket;
import com.brahvim.nerd.io.net.NerdUdpSocket;
import com.brahvim.nerd.io.net.tcp.NerdTcpClient;
import com.brahvim.nerd.io.net.tcp.NerdTcpServer;
import com.brahvim.nerd.processing_wrapper.NerdSketch;

public class NerdEcsManager implements Serializable {

	private class NerdEcsManagerSocket {

		public NerdUdpSocket udpSocket;
		public NerdTcpServer tcpServer;
		public NerdTcpClient tcpClient;

	}

	// region Fields.
	public static final long serialVersionUID = -6488574946L;

	public static final transient NerdEcsSystem<?>[] DEFAULT_ECS_SYSTEMS_ORDER = {
			// ...Will fill this in logically, as Nerd gets more ECS wrappers!
	};

	protected final LinkedList<NerdEcsEntity> ENTITIES = new LinkedList<>();
	protected final LinkedList<NerdEcsComponent> COMPONENTS = new LinkedList<>();
	protected final HashMap<String, NerdEcsEntity> NAME_TO_ENTITY_MAP = new HashMap<>();
	protected final HashMap<Class<? extends NerdEcsComponent>, HashSet<NerdEcsComponent>> CLASSES_TO_COMPONENTS_MAP = new HashMap<>();

	protected long numUnnamedEntities = 1;
	protected NerdEcsSystem<?>[] systemsOrder;

	private final transient NerdSketch SKETCH;
	private final transient NerdEcsManager.NerdEcsManagerSocket SOCKET_MANAGER = new NerdEcsManagerSocket();
	// endregion

	public NerdEcsManager(final NerdSketch p_sketch, final NerdEcsSystem<?>[] p_systems) {
		this.SKETCH = p_sketch;
		this.setSystemsOrder(p_systems);
	}

	// region `callOnAllSystems()` overloads.
	@SuppressWarnings("all")
	protected void callOnAllSystems(final BiConsumer<NerdEcsSystem, HashSet<? extends NerdEcsComponent>> p_methodRef) {
		if (p_methodRef != null)
			for (final NerdEcsSystem s : this.systemsOrder)
				p_methodRef.accept(s, this.CLASSES_TO_COMPONENTS_MAP.get(s.getComponentTypeClass()));
	}

	@SuppressWarnings("all")
	protected <OtherArgT> void callOnAllSystems(
			final NerdTriConsumer<NerdEcsSystem, OtherArgT, HashSet<? extends NerdEcsComponent>> p_methodRef,
			final OtherArgT p_otherArg) {
		if (p_methodRef != null)
			for (final NerdEcsSystem<?> s : this.systemsOrder)
				p_methodRef.accept(s, p_otherArg,
						this.CLASSES_TO_COMPONENTS_MAP
								.get(s.getComponentTypeClass()));
	}

	protected <OtherArgT> void callOnAllSystems(
			final BiConsumer<NerdEcsSystem<?>, OtherArgT> p_methodRef, final OtherArgT p_otherArg) {
		if (p_methodRef != null)
			for (final NerdEcsSystem<?> s : this.systemsOrder)
				p_methodRef.accept(s, p_otherArg);
	}

	// @SuppressWarnings("unchecked")
	protected void callOnAllSystems(final Consumer<NerdEcsSystem<?>> p_method) {
		if (p_method != null)
			for (final NerdEcsSystem<?> s : this.systemsOrder)
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
		return this.createEntity(null);
	}

	public NerdEcsSystem<?>[] getSystemsOrder() {
		return this.systemsOrder;
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
		for (final var e : this.NAME_TO_ENTITY_MAP.entrySet())
			if (e.getValue() == p_entity)
				return e.getKey();

		// If the entity is not from another manager, this won't ever be returned.
		return null;
	}

	public void setSystemsOrder(final NerdEcsSystem<?>[] p_ecsSystems) {
		Objects.requireNonNull(p_ecsSystems, "That can't be `null`! Come on...");

		for (final NerdEcsSystem<?> s : p_ecsSystems) {
			final Class<? extends NerdEcsComponent> systemComponentTypeClass = s.getComponentTypeClass();
			// If `systemComponentTypeClass` does not exist in the map,
			this.CLASSES_TO_COMPONENTS_MAP.computeIfAbsent(systemComponentTypeClass, k -> new HashSet<>());
			// ...then PUT IT THERE!
		}

		this.systemsOrder = p_ecsSystems;
	}

	/**
	 * @apiNote Entity names parsable as positive {@code long}s are reserved.
	 * 
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
	/**
	 * You get this entire manager, serialized to bytes!
	 * 
	 * @return The bytes!
	 * @see NerdEcsManager#saveState(File)
	 */
	public byte[] saveState() {
		return NerdByteSerial.toBytes(this);
	}

	/**
	 * This entire manager, serialized to a file as bytes!
	 * 
	 * @return Nothing! The file ate it all...
	 * @see NerdEcsManager#saveState()
	 */
	public void saveState(final File p_file) {
		NerdByteSerial.toFile(this, p_file);
	}
	// endregion

	// region Loading.
	/**
	 * Ever called {@link NerdEcsManager#saveState(File)}? This reverses that.
	 * 
	 * @param p_file is the file in context.
	 */
	public void loadState(final File p_file) {
		this.loadStateImpl(NerdByteSerial.fromFile(p_file));
	}

	/**
	 * Ever called {@link NerdEcsManager#saveState()}? This reverses the bytes you
	 * got from there, for free!
	 * 
	 * @param p_serializedData better have the bytes I talked about!
	 */
	public void loadState(final byte[] p_serializedData) {
		this.loadStateImpl(NerdByteSerial.fromBytes(p_serializedData));
	}

	private void loadStateImpl(final NerdEcsManager p_deserialized) {

		this.systemsOrder = p_deserialized.systemsOrder;
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

		// Remove elements not available in the maps in the deserialized manager:
		{
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
		}
		// endregion

		// region Copying components over:
		final int iterations = this.COMPONENTS.size();
		for (int i = 0; i < iterations; i++) {
			final NerdEcsComponent orig = this.COMPONENTS.get(i), latest = p_deserialized.COMPONENTS.get(i);
			orig.copyFieldsFrom(latest);
		}
	}
	// endregion

	// endregion
	// endregion

	// region Networking.
	/**
	 * Shuts down, all communication capabilities of the ECS!
	 * Use if you wish to stop the communication.
	 */
	public void shutdownSocket() {
		if (this.SOCKET_MANAGER.udpSocket != null)
			this.SOCKET_MANAGER.udpSocket.close();

		if (this.SOCKET_MANAGER.tcpServer != null)
			this.SOCKET_MANAGER.tcpServer.shutdown();

		if (this.SOCKET_MANAGER.tcpClient != null)
			this.SOCKET_MANAGER.tcpClient.disconnect();

		// Set all of these to `null`, ...aaaaand they'll get GCed!
		this.SOCKET_MANAGER.udpSocket = null;
		this.SOCKET_MANAGER.tcpServer = null;
		this.SOCKET_MANAGER.tcpClient = null;
	}

	/**
	 * <p>
	 * A socket will be used <i>only</i> to <i>send</i> data! The socket provided
	 * will be expected to handle receiving data and making connections itself.
	 *
	 * <p>
	 * Said data will be sent according a strategy that the user may provide.
	 * 
	 * <ol>
	 * <li>For TCP servers, the default plan-of-action is to send data to all
	 * clients.
	 * 
	 * <li>For TCP clients, the default plan-of-action is to send data to all
	 * clients, the ECS was given to send data to.<br>
	 * 
	 * <li>For UDP sockets, the default plan-of-action is to send data to all
	 * sockets,
	 * the ECS was given to send data to.<br>
	 * </ol>
	 *
	 * <p>
	 * Once you provide such a socket, communications automatically begin! You can
	 * <i>sit back</i>, relax, and watch the ECS transfer over all of its data to
	 * the other side! ...or not. I dunno, <i>your choice!~</i>
	 * 
	 * @param p_socket is the socket. Pass that in, and live your dreams!
	 */
	public void startSocket(final NerdSocket p_socket) {
		// These three objects should be GC-able by final the time we're here!
		// (Id est, their references should be set to `null`!)
		this.shutdownSocket(); // This method call does exactly that.

		if (p_socket instanceof final NerdUdpSocket socket) {
			this.SOCKET_MANAGER.udpSocket = socket;
		} else if (p_socket instanceof final NerdTcpServer socket) {
			this.SOCKET_MANAGER.tcpServer = socket;
		} else if (p_socket instanceof final NerdTcpClient socket) {
			this.SOCKET_MANAGER.tcpClient = socket;
		} else
			throw new UnsupportedOperationException("`NerdEcsManager` does not support this type of socket, sorry!");
	}

	/**
	 * Ambiguity over what type of socket you used?
	 * This method is here to help!
	 * Yep, you'll need to check the output, but don't worry, here's the code!:<br>
	 * <br>
	 * 
	 * <pre>
	 * if (p_socket == null) {
	 * 	// Handle the `null`!
	 * } else if (p_socket instanceof final NerdUdpSocket udpSocket) {
	 * 	// Your turn!
	 * } else if (p_socket instanceof final NerdTcpServer tcpServer) {
	 * 	// Your turn!
	 * } else if (p_socket instanceof final NerdTcpClient tcpClient) {
	 * 	// Your turn!
	 * }
	 * </pre>
	 *
	 * @param <RetT> The type of socket returned. Don't set this! Look at the "See
	 *               Also" section!
	 * @return The socket!
	 * @see NerdEcsManager#getUdpSocket()
	 * @see NerdEcsManager#getTcpServer()
	 * @see NerdEcsManager#getTcpClient()
	 */
	@SuppressWarnings("unchecked")
	public <RetT extends NerdSocket> RetT getUnderlyingNerdSocket() {
		return (RetT) (this.SOCKET_MANAGER.udpSocket != null ? this.SOCKET_MANAGER.udpSocket
				: this.SOCKET_MANAGER.tcpServer != null ? this.SOCKET_MANAGER.tcpServer
						: this.SOCKET_MANAGER.tcpClient != null ? this.SOCKET_MANAGER.tcpClient
								: null);
	}

	public NerdUdpSocket getUdpSocket() {
		return this.SOCKET_MANAGER.udpSocket;
	}

	public NerdTcpServer getTcpServer() {
		return this.SOCKET_MANAGER.tcpServer;
	}

	public NerdTcpClient getTcpClient() {
		return this.SOCKET_MANAGER.tcpClient;
	}
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