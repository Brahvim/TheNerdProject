package com.brahvim.nerd.processing_wrapper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import processing.core.PGraphics;
import processing.event.MouseEvent;

public abstract class NerdModule<SketchPGraphicsT extends PGraphics> {

	protected final NerdSketch<SketchPGraphicsT> SKETCH;
	protected final Map<NerdModuleCallbackNameEnum, NerdModuleCallback<SketchPGraphicsT, ?>> a = new HashMap<>(0);

	protected NerdModule(final NerdSketch<SketchPGraphicsT> p_sketch) {
		this.SKETCH = p_sketch;
	}

	public NerdSketch<SketchPGraphicsT> getSketch() {
		return this.SKETCH;
	}

	public NerdSketchSettings<SketchPGraphicsT> getSketchSettings() {
		return this.SKETCH.SKETCH_SETTINGS;
	}

	public Collection<NerdModule<SketchPGraphicsT>> getSketchModules() {
		return this.SKETCH.MODULES;
	}

	protected Map<Class<? extends NerdModule<SketchPGraphicsT>>, NerdModule<SketchPGraphicsT>> getSketchModulesMap() {
		return this.SKETCH.CLASSES_TO_MODULES_MAP;
	}

	// region Callbacks for `NerdSketch<?>` to call!1!!!
	// region Workflow callbacks!
	protected void settings() {
	}

	protected void preSetup() {
	}

	protected void postSetup() {
	}

	protected void pre() {
	}

	protected void preDraw() {
	}

	protected void draw() {
	}

	protected void postDraw() {
	}

	protected void post() {
	}

	protected void exit() {
	}

	protected void dispose() {
	}
	// endregion

	// region Hardware callbacks!
	protected void focusGained() {
	}

	protected void focusLost() {
	}

	protected void fullscreenChanged(final boolean p_state) {
	}

	protected void monitorChanged() {
	}

	protected void resized() {
	}

	protected void mouseClicked() {
	}

	protected void mouseDragged() {
	}

	protected void mouseMoved() {
	}

	protected void mousePressed() {
	}

	protected void mouseReleased() {
	}

	protected void mouseWheel(final MouseEvent p_mouseEvent) {
	}

	protected void keyPressed() {
	}

	protected void keyReleased() {
	}

	protected void keyTyped() {
	}

	protected void touchEnded() {
	}

	protected void touchMoved() {
	}

	protected void touchStarted() {
	}
	// endregion
	// endregion

}
