package com.brahvim.nerd.processing_wrapper;

import java.util.LinkedHashSet;

import com.brahvim.nerd.framework.ecs.NerdEcsSystem;
import com.brahvim.nerd.framework.scene_api.NerdSceneManager;

public class NerdBridgedSceneManager extends NerdSceneManager {

	@SafeVarargs
	public NerdBridgedSceneManager(
			final NerdSketch p_sketch,
			final NerdSceneManagerSettings p_settings,
			final LinkedHashSet<NerdSceneChangeListener> p_listeners,
			final Class<? extends NerdEcsSystem<?>>... p_ecsSystems) {
		super(p_sketch, p_settings, p_listeners, p_ecsSystems);
	}

	// region Workflow callbacks.
	@Override
	protected void runPre() {
		super.runPre();
	}

	@Override
	protected void runPost() {
		super.runPost();
	}

	@Override
	protected void runDraw() {
		super.runDraw();
	}

	@Override
	protected void runExit() {
		super.runExit();
	}

	@Override
	protected void runDispose() {
		super.runDispose();
	}

	// Too expensive! Need a `push()` and `pop()`:
	/*
	 * @Override
	 * protected void runPreDraw() {
	 * super.runPreDraw();
	 * }
	 * 
	 * @Override
	 * protected void runPostDraw() {
	 * super.runPostDraw();
	 * }
	 */
	// endregion

	// region Event callbacks.
	// region Mouse event callbacks.
	@Override
	protected void mousePressed() {
		super.mousePressed();
	}

	@Override
	protected void mouseReleased() {
		super.mouseReleased();
	}

	@Override
	protected void mouseMoved() {
		super.mouseMoved();
	}

	@Override
	protected void mouseClicked() {
		super.mouseClicked();
	}

	@Override
	protected void mouseDragged() {
		super.mouseDragged();
	}

	@Override
	protected void mouseWheel(final processing.event.MouseEvent p_mouseEvent) {
		super.mouseWheel(p_mouseEvent);
	}
	// endregion

	// region Touch event callbacks.
	@Override
	protected void touchStarted() {
		super.touchStarted();
	}

	@Override
	protected void touchMoved() {
		super.touchMoved();
	}

	@Override
	protected void touchEnded() {
		super.touchEnded();
	}
	// endregion

	// region Window event callbacks.
	@Override
	protected void resized() {
		super.resized();
	}

	@Override
	protected void focusLost() {
		super.focusLost();
	}

	@Override
	protected void focusGained() {
		super.focusGained();
	}

	@Override
	protected void monitorChanged() {
		super.monitorChanged();
	}

	@Override
	protected void fullscreenChanged(final boolean p_state) {
		super.fullscreenChanged(p_state);
	}

	// endregion

	// region Keyboard event callbacks.
	@Override
	protected void keyTyped() {
		super.keyTyped();
	}

	@Override
	protected void keyPressed() {
		super.keyPressed();
	}

	@Override
	protected void keyReleased() {
		super.keyReleased();
	}
	// endregion
	// endregion

}
