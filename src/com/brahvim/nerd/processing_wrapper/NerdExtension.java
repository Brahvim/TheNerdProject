package com.brahvim.nerd.processing_wrapper;

import java.util.function.Supplier;

import processing.event.MouseEvent;

public abstract class NerdExtension {

	protected final NerdSketch SKETCH;

	private Object extObject;

	// protected NerdExtension() { }

	protected NerdExtension(final Supplier<NerdExtSettings<? extends NerdExtension>> p_settingsSupplier) {
	}

	/* `package` */ NerdExtension init(final NerdCustomSketchBuilder p_builder) {
		this.extObject = this.initImpl(p_builder);
		return this;
	}

	protected abstract Object initImpl(final NerdCustomSketchBuilder p_builder);

	protected Object getExtObject() {
		return this.extObject;
	}

	// region Callbacks.
	// region Workflow callbacks.
	protected void sketchConstructed(final NerdSketchBuilderSettings p_settings) {
	}

	protected void settings() {
	}

	protected void setup() {
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

	// region Hardware callbacks.
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
