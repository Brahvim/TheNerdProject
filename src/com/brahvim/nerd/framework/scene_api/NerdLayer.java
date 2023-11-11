package com.brahvim.nerd.framework.scene_api;

import com.brahvim.nerd.io.asset_loader.NerdAssetsModule;
import com.brahvim.nerd.processing_wrapper.NerdSketch;
import com.brahvim.nerd.processing_wrapper.graphics_backends.generic.NerdGenericGraphics;
import com.brahvim.nerd.window_management.NerdDisplayModule;
import com.brahvim.nerd.window_management.NerdInputModule;
import com.brahvim.nerd.window_management.NerdWindowModule;

import processing.core.PGraphics;

/**
 * Just like {@link NerdScene}s, {@link NerdLayer}s
 * are used via inheritance, and not anonymous classes.
 *
 * <p>
 * To add a {@link NerdLayer} to a {@link NerdScene}, use the
 * {@link NerdScene#addLayer(Class)}, passing in your {@link NerdLayer}
 * subclass.
 */
public abstract class NerdLayer<SketchPGraphicsT extends PGraphics> {

	// region `protected` fields.
	// Seriously, why did I set these to be `protected`?
	public final NerdLayer<SketchPGraphicsT> LAYER = this;

	// Forgive me for breaking naming conventions here.
	// Forgive me. Please!
	protected NerdSketch<SketchPGraphicsT> SKETCH;
	protected NerdWindowModule<SketchPGraphicsT> WINDOW;
	protected NerdScenesModule<SketchPGraphicsT> MANAGER;
	protected NerdGenericGraphics<SketchPGraphicsT> GRAPHICS;

	// Non-generic ones:
	protected NerdSceneState STATE;
	protected NerdInputModule INPUT;
	protected NerdAssetsModule ASSETS;
	protected NerdDisplayModule DISPLAY;

	protected NerdScene<SketchPGraphicsT> SCENE;
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
