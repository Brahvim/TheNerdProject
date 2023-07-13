package com.brahvim.nerd.processing_wrapper;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.brahvim.nerd.framework.NerdQuadConsumer;
import com.brahvim.nerd.framework.NerdReflectionUtils;
import com.brahvim.nerd.framework.NerdTriConsumer;

import processing.event.MouseEvent;

public abstract class NerdModule {

	protected final NerdSketch SKETCH;

	protected NerdModule(final NerdSketch p_sketch) {
		this.SKETCH = p_sketch;
	}

	public NerdSketch getSketch() {
		return this.SKETCH;
	}

	protected LinkedHashSet<NerdModule> getModulesUpdatedFramely() {
		return this.SKETCH.MODULES;
	}

	protected HashMap<Class<? extends NerdModule>, NerdModule> getSketchModulesMap() {
		return this.SKETCH.CLASS_TO_MODULES_MAP;
	}

	// region Methods to call methods on other modules.
	protected static <ModuleT extends NerdModule> void callOnModule(
			final ModuleT p_module,
			final Consumer<ModuleT> p_method) {
		NerdReflectionUtils.callIfNotNull(p_module, p_method);
	}

	protected static <ModuleT extends NerdModule, ArgT> void callOnModule(
			final ModuleT p_object, final BiConsumer<ModuleT, ArgT> p_methodReference, final ArgT p_arg) {
		if (!(p_object == null || p_methodReference == null))
			p_methodReference.accept(p_object, p_arg);
	}

	protected static <ModuleT extends NerdModule, FirstArgT, SecondArgT> void callOnModule(
			final ModuleT p_object, final NerdTriConsumer<ModuleT, FirstArgT, SecondArgT> p_methodReference,
			final FirstArgT p_arg1, final SecondArgT p_arg2) {
		if (!(p_object == null || p_methodReference == null))
			p_methodReference.accept(p_object, p_arg1, p_arg2);
	}

	protected static <ModuleT extends NerdModule, FirstArgT, SecondArgT, ThirdArgT> void callOnModule(
			final ModuleT p_object, final NerdQuadConsumer<ModuleT, FirstArgT, SecondArgT, ThirdArgT> p_methodReference,
			final FirstArgT p_arg1, final SecondArgT p_arg2, final ThirdArgT p_arg3) {
		if (!(p_object == null || p_methodReference == null))
			p_methodReference.accept(p_object, p_arg1, p_arg2, p_arg3);
	}
	// endregion

	// region Calling the callbacks!1!!!
	// region Workflow callbacks!
	protected void sketchConstructed(final NerdSketchBuilderSettings p_settings) {
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
