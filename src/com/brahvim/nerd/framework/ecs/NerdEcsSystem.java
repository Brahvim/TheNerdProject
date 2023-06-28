package com.brahvim.nerd.framework.ecs;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashSet;

import com.brahvim.nerd.framework.scene_api.NerdSceneState;

public abstract class NerdEcsSystem<SystemComponentT extends NerdEcsComponent> implements Serializable {

	public static final long serialVersionUID = -481949954L;

	private final Class<SystemComponentT> COMPONENT_TYPE_CLASS;

	@SuppressWarnings("unchecked")
	protected NerdEcsSystem() {
		// ...Trivial reflection tricks are where ChatGPT is my best friend ._.
		final Type thatOneGenericSuperclass = this.getClass().getGenericSuperclass();

		if (!(thatOneGenericSuperclass instanceof ParameterizedType)) {
			throw new RuntimeException(String.format(
					"Sorry, but not specifying the generic type argument in your"
							+ "`NerdEcsSystem` subclass, `%s`, is illegal.",
					this.getClass().getSimpleName()));
		}

		this.COMPONENT_TYPE_CLASS = (Class<SystemComponentT>)
		// One, derive the actual type arguments. Two, cast back! ^^^
		((ParameterizedType) thatOneGenericSuperclass).getActualTypeArguments()[0];
	}

	public final Class<SystemComponentT> getComponentTypeClass() {
		return this.COMPONENT_TYPE_CLASS;
	}

	// region Sketch workflow callbacks.
	protected synchronized void preload(/* final */ final HashSet<SystemComponentT> p_components) {
	}

	protected void sceneChanged(/* final */ final HashSet<SystemComponentT> p_components) {
	}

	protected void setup(final NerdSceneState p_state, /* final */ final HashSet<SystemComponentT> p_components) {
	}

	protected void pre(/* final */ final HashSet<SystemComponentT> p_components) {
	}

	protected void draw(/* final */ final HashSet<SystemComponentT> p_components) {
	}

	protected void post(/* final */ final HashSet<SystemComponentT> p_components) {
	}

	public void exit(/* final */ final HashSet<SystemComponentT> p_components) {
	}

	protected void dispose(/* final */ final HashSet<SystemComponentT> p_components) {
	}
	// endregion

	// region Events.
	// region Mouse events.
	public void mousePressed(/* final */ final HashSet<SystemComponentT> p_components) {
	}

	public void mouseReleased(/* final */ final HashSet<SystemComponentT> p_components) {
	}

	public void mouseMoved(/* final */ final HashSet<SystemComponentT> p_components) {
	}

	public void mouseClicked(/* final */ final HashSet<SystemComponentT> p_components) {
	}

	public void mouseDragged(/* final */ final HashSet<SystemComponentT> p_components) {
	}

	public void mouseWheel(final processing.event.MouseEvent p_mouseEvent,
			/* final */ final HashSet<SystemComponentT> p_components) {
	}
	// endregion

	// region Keyboard events.
	public void keyTyped(/* final */ final HashSet<SystemComponentT> p_components) {
	}

	public void keyPressed(/* final */ final HashSet<SystemComponentT> p_components) {
	}

	public void keyReleased(/* final */ final HashSet<SystemComponentT> p_components) {
	}
	// endregion

	// region Touch events.
	public void touchStarted(/* final */ final HashSet<SystemComponentT> p_components) {
	}

	public void touchMoved(/* final */ final HashSet<SystemComponentT> p_components) {
	}

	public void touchEnded(/* final */ final HashSet<SystemComponentT> p_components) {
	}
	// endregion

	// region Window focus events.
	public void focusLost(/* final */ final HashSet<SystemComponentT> p_components) {
	}

	public void resized(/* final */ final HashSet<SystemComponentT> p_components) {
	}

	public void focusGained(/* final */ final HashSet<SystemComponentT> p_components) {
	}

	public void monitorChanged(/* final */ final HashSet<SystemComponentT> p_components) {
	}

	public void fullscreenChanged(final boolean p_state, /* final */ final HashSet<SystemComponentT> p_components) {
	}
	// endregion
	// endregion

}
