package com.brahvim.nerd.processing_wrapper;

import java.util.List;
import java.util.Map;

import com.brahvim.nerd.utils.NerdReflectionUtils;

import processing.event.MouseEvent;

public abstract class NerdModule {

	protected final NerdSketch SKETCH;

	@SuppressWarnings("unused")
	private NerdModule() {
		this.SKETCH = null;
		NerdReflectionUtils.rejectStaticClassInstantiationFor(this.getClass());
	}

	protected NerdModule(final NerdSketch p_sketch) {
		this.SKETCH = p_sketch;
	}

	public NerdSketch getSketch() {
		return this.SKETCH;
	}

	protected List<NerdModule> getSketchModules() {
		return this.SKETCH.MODULES;
	}

	protected Map<Class<? extends NerdModule>, NerdModule> getSketchModulesMap() {
		return this.SKETCH.MODULES_TO_CLASSES_MAP;
	}

	protected void assignModuleSettings(final NerdModuleSettings<?> p_settings) {
	}

	// region Callbacks for `NerdSketch` to call!1!!!
	// region Workflow callbacks!
	protected void sketchConstructed(final NerdSketchSettings p_settings) {
	}

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
