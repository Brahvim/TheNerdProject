package com.brahvim.nerd.processing_wrapper;

import java.util.List;
import java.util.Map;

import com.brahvim.nerd.utils.NerdReflectionUtils;

import processing.event.MouseEvent;

public abstract class NerdModule {

	protected final NerdSketch<?> SKETCH;

	@SuppressWarnings("unused")
	private NerdModule() {
		this.SKETCH = null;
		NerdReflectionUtils.rejectStaticClassInstantiationFor(this.getClass());
	}

	protected NerdModule(final NerdSketch<?> p_sketch) {
		this.SKETCH = p_sketch;
	}

	public NerdSketch<?> getSketch() {
		return this.SKETCH;
	}

	public List<NerdModule> getSketchModules() {
		return this.SKETCH.MODULES;
	}

	public NerdSketchSettings<?> getSketchSettings() {
		return this.SKETCH.SKETCH_SETTINGS;
	}

	// interface NerdModuleRelinkBehavior { }
	// enum NerdModuleDefaultRelinkBehavior { }

	// /**
	// * Removes this {@link NerdModule} instance from the {@link NerdSketch} it was
	// * instantiated and stored by.
	// *
	// * @return The {@link NerdSketch} this {@link NerdModule} was instantiated by.
	// */
	// protected NerdSketch<?> unlinkFromSketch() {
	// this.SKETCH.CLASSES_TO_MODULES_MAP.remove(this.getClass());
	// return this.SKETCH;
	// }

	// protected NerdSketch<?> linkToSketch(NerdSketch<?> p_sketch) {
	// if (p_sketch == this.SKETCH)
	// return this.SKETCH;

	// this.unlinkFromSketch();
	// p_sketch.CLASSES_TO_MODULES_MAP.remove(this.getClass());
	// return this.SKETCH;
	// }

	protected Map<Class<? extends NerdModule>, NerdModule> getSketchModulesMap() {
		return this.SKETCH.CLASSES_TO_MODULES_MAP;
	}

	protected void assignModuleSettings(final NerdModuleSettings<?> p_settings) {
	}

	// region Callbacks for `NerdSketch<?>` to call!1!!!
	// region Workflow callbacks!
	protected void sketchConstructed(final NerdSketchSettings<?> p_settings) {
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
