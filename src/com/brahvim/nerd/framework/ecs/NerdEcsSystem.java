package com.brahvim.nerd.framework.ecs;

import java.io.Serializable;
import java.util.HashSet;

import com.brahvim.nerd.framework.scene_api.NerdSceneState;
import com.brahvim.nerd.utils.NerdReflectionUtils;

public abstract class NerdEcsSystem<SystemComponentT extends NerdEcsComponent> implements Serializable {

	public static final long serialVersionUID = -481949954L;

	private final Class<SystemComponentT> COMPONENT_TYPE_CLASS;

	@SuppressWarnings("unchecked")
	protected NerdEcsSystem() {
		final Class<?> typeArg = NerdReflectionUtils.getFirstTypeArg(this);

		if (typeArg != null)
			throw new IllegalStateException(String.format(
					"`%s`s should NEVER be able to come to this state! Who modified the source code?!",
					this.getClass().getSimpleName()));

		this.COMPONENT_TYPE_CLASS = (Class<SystemComponentT>) typeArg;
	}

	public final Class<SystemComponentT> getComponentTypeClass() {
		return this.COMPONENT_TYPE_CLASS;
	}

	// region Sketch workflow callbacks.
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
	protected void mousePressed(final HashSet<SystemComponentT> p_components) {
	}

	protected void mouseReleased(final HashSet<SystemComponentT> p_components) {
	}

	protected void mouseMoved(final HashSet<SystemComponentT> p_components) {
	}

	protected void mouseClicked(final HashSet<SystemComponentT> p_components) {
	}

	protected void mouseDragged(final HashSet<SystemComponentT> p_components) {
	}

	protected void mouseWheel(final processing.event.MouseEvent p_mouseEvent,
			final HashSet<SystemComponentT> p_components) {
	}
	// endregion

	// region Keyboard events.
	protected void keyTyped(final HashSet<SystemComponentT> p_components) {
	}

	protected void keyPressed(final HashSet<SystemComponentT> p_components) {
	}

	protected void keyReleased(final HashSet<SystemComponentT> p_components) {
	}
	// endregion

	// region Touch events.
	protected void touchStarted(final HashSet<SystemComponentT> p_components) {
	}

	protected void touchMoved(final HashSet<SystemComponentT> p_components) {
	}

	protected void touchEnded(final HashSet<SystemComponentT> p_components) {
	}
	// endregion

	// region Window focus events.
	protected void focusLost(final HashSet<SystemComponentT> p_components) {
	}

	protected void resized(final HashSet<SystemComponentT> p_components) {
	}

	protected void focusGained(final HashSet<SystemComponentT> p_components) {
	}

	protected void monitorChanged(final HashSet<SystemComponentT> p_components) {
	}

	protected void fullscreenChanged(final boolean p_state, final HashSet<SystemComponentT> p_components) {
	}
	// endregion
	// endregion

}
