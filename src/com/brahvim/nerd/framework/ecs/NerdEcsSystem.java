package com.brahvim.nerd.framework.ecs;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Optional;

import com.brahvim.nerd.framework.NerdReflectionUtils;
import com.brahvim.nerd.framework.scene_api.NerdSceneState;

public abstract class NerdEcsSystem<SystemComponentT extends NerdEcsComponent> implements Serializable {

	public static final long serialVersionUID = -481949954L;

	private final Class<SystemComponentT> COMPONENT_TYPE_CLASS;

	@SuppressWarnings("unchecked")
	protected NerdEcsSystem() {
		final Optional<Class<?>> optionalTypeArg = NerdReflectionUtils.getFirstTypeArg(this);

		if (!optionalTypeArg.isPresent())
			throw new IllegalStateException(String.format(
					"`%s`s should NEVER be able to come to this state! Who modified the source code?!",
					this.getClass().getSimpleName()));

		this.COMPONENT_TYPE_CLASS = (Class<SystemComponentT>) optionalTypeArg.get();
	}

	public final Class<SystemComponentT> getComponentTypeClass() {
		return this.COMPONENT_TYPE_CLASS;
	}

	// region Sketch workflow callbacks.
	protected synchronized void preload(final HashSet<SystemComponentT> p_components) {
	}

	protected void sceneChanged(final HashSet<SystemComponentT> p_components) {
	}

	protected void setup(final NerdSceneState p_state, final HashSet<SystemComponentT> p_components) {
	}

	protected void pre(final HashSet<SystemComponentT> p_components) {
	}

	protected void draw(final HashSet<SystemComponentT> p_components) {
	}

	protected void post(final HashSet<SystemComponentT> p_components) {
	}

	protected void exit(final HashSet<SystemComponentT> p_components) {
	}

	protected void dispose(final HashSet<SystemComponentT> p_components) {
	}
	// endregion

	// region Events.
	// region Mouse events.
	public void mousePressed(final HashSet<SystemComponentT> p_components) {
	}

	public void mouseReleased(final HashSet<SystemComponentT> p_components) {
	}

	public void mouseMoved(final HashSet<SystemComponentT> p_components) {
	}

	public void mouseClicked(final HashSet<SystemComponentT> p_components) {
	}

	public void mouseDragged(final HashSet<SystemComponentT> p_components) {
	}

	public void mouseWheel(final processing.event.MouseEvent p_mouseEvent,
			final HashSet<SystemComponentT> p_components) {
	}
	// endregion

	// region Keyboard events.
	public void keyTyped(final HashSet<SystemComponentT> p_components) {
	}

	public void keyPressed(final HashSet<SystemComponentT> p_components) {
	}

	public void keyReleased(final HashSet<SystemComponentT> p_components) {
	}
	// endregion

	// region Touch events.
	public void touchStarted(final HashSet<SystemComponentT> p_components) {
	}

	public void touchMoved(final HashSet<SystemComponentT> p_components) {
	}

	public void touchEnded(final HashSet<SystemComponentT> p_components) {
	}
	// endregion

	// region Window focus events.
	public void focusLost(final HashSet<SystemComponentT> p_components) {
	}

	public void resized(final HashSet<SystemComponentT> p_components) {
	}

	public void focusGained(final HashSet<SystemComponentT> p_components) {
	}

	public void monitorChanged(final HashSet<SystemComponentT> p_components) {
	}

	public void fullscreenChanged(final boolean p_state, final HashSet<SystemComponentT> p_components) {
	}
	// endregion
	// endregion

}
