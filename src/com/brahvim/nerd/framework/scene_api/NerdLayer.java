package com.brahvim.nerd.framework.scene_api;

import com.brahvim.nerd.framework.cameras.NerdAbstractCamera;
import com.brahvim.nerd.io.asset_loader.NerdAssetsModule;
import com.brahvim.nerd.processing_wrapper.NerdDisplayModule;
import com.brahvim.nerd.processing_wrapper.NerdGraphics;
import com.brahvim.nerd.processing_wrapper.NerdInputModule;
import com.brahvim.nerd.processing_wrapper.NerdSketch;
import com.brahvim.nerd.processing_wrapper.NerdWindowModule;

/**
 * Just like {@link NerdScene}s, {@link NerdLayer}s
 * are used via inheritance, and not anonymous classes.
 *
 * <p>
 * To add a {@link NerdLayer} to a {@link NerdScene}, use the
 * {@link NerdScene#addLayer(Class)}, passing in your {@link NerdLayer}
 * subclass.
 */
public abstract class NerdLayer {

	// region `protected` fields.
	// Seriously, why did I set these to be `protected`?
	public final NerdLayer LAYER = this;

	protected NerdSketch SKETCH;
	protected NerdSceneState STATE;
	protected NerdGraphics GRAPHICS;
	protected NerdInputModule INPUT;
	protected NerdAssetsModule ASSETS;
	protected NerdWindowModule WINDOW;
	protected NerdScenesModule MANAGER;
	protected NerdDisplayModule DISPLAY;
	protected NerdAbstractCamera CAMERA;

	protected NerdScene SCENE;
	// endregion

	// region `private` fields.
	private int timesActivated;
	private boolean active;
	// endregion

	protected NerdLayer() {
	}

	// region Activity status.
	public boolean isActive() {
		return this.active;
	}

	public void setActive(final boolean p_toggleState) {
		final boolean previouslyActive = this.active; // RECORD!!!!
		this.active = p_toggleState;

		if (this.active && !previouslyActive) {
			this.setup();
			this.timesActivated++;
		} else
			this.layerExit();
	}

	public int getTimesActivated() {
		return this.timesActivated;
	}
	// endregion

	// region Events.
	// region Mouse events.
	protected void mousePressed() {
	}

	protected void mouseReleased() {
	}

	protected void mouseMoved() {
	}

	protected void mouseClicked() {
	}

	protected void mouseDragged() {
	}

	protected void mouseWheel(final processing.event.MouseEvent p_mouseEvent) {
	}
	// endregion

	// region Keyboard events.
	protected void keyTyped() {
	}

	protected void keyPressed() {
	}

	protected void keyReleased() {
	}
	// endregion

	// region Touch events.
	protected void touchStarted() {
	}

	protected void touchMoved() {
	}

	protected void touchEnded() {
	}
	// endregion

	// region Window focus events.
	protected void focusLost() {
	}

	protected void exit() {
	}

	protected void resized() {
	}

	protected void focusGained() {
	}

	protected void monitorChanged() {
	}

	protected void fullscreenChanged(final boolean p_state) {
	}
	// endregion
	// endregion

	// region `protected` methods. Nobody can call them outside of this package!
	// region `NerdLayer`-only (`protected`) callbacks!
	protected void layerExit() {
	}
	// endregion

	// region App workflow callbacks.
	protected void setup() {
	}

	protected void pre() {
	}

	protected void draw() {
	}

	protected void post() {
	}
	// endregion
	// endregion

}
