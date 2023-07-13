package com.brahvim.nerd.processing_wrapper;

import java.util.Collection;
import java.util.HashMap;

import processing.event.MouseEvent;

public class NerdExtensionsModule extends NerdModule {

	protected Collection<NerdExtension> extensions;
	protected HashMap<String, NerdExtension> nameToExtensionsMap;

	public NerdExtensionsModule(final NerdSketch p_sketch) {
		super(p_sketch);
	}

	@SuppressWarnings("unchecked")
	public <RetT extends NerdExtension> RetT getNerdExt(final String p_extName) {
		return (RetT) this.nameToExtensionsMap.get(p_extName);
	}

	// region Callbacks.
	@Override
	protected void sketchConstructed(final NerdSketchBuilderSettings p_settings) {
		this.nameToExtensionsMap = p_settings.nerdExtensions;
		this.extensions = this.nameToExtensionsMap.values();

		for (final NerdExtension e : this.extensions)
			e.sketchConstructed(p_settings);
	}

	@Override
	protected void dispose() {
		for (final NerdExtension e : this.extensions)
			e.dispose();
	}

	@Override
	protected void draw() {
		for (final NerdExtension e : this.extensions)
			e.draw();
	}

	@Override
	protected void exit() {
		for (final NerdExtension e : this.extensions)
			e.exit();
	}

	@Override
	protected void post() {
		for (final NerdExtension e : this.extensions)
			e.post();
	}

	@Override
	protected void pre() {
		for (final NerdExtension e : this.extensions)
			e.pre();
	}

	@Override
	protected void settings() {
		for (final NerdExtension e : this.extensions)
			e.settings();
	}

	@Override
	protected void postSetup() {
		for (final NerdExtension e : this.extensions)
			e.setup();
	}

	@Override
	protected void focusGained() {
		for (final NerdExtension e : this.extensions)
			e.focusGained();
	}

	@Override
	protected void focusLost() {
		for (final NerdExtension e : this.extensions)
			e.focusLost();
	}

	@Override
	protected void fullscreenChanged(final boolean p_state) {
		for (final NerdExtension e : this.extensions)
			e.fullscreenChanged(p_state);
	}

	@Override
	protected void monitorChanged() {
		for (final NerdExtension e : this.extensions)
			e.monitorChanged();
	}

	@Override
	protected void resized() {
		for (final NerdExtension e : this.extensions)
			e.resized();
	}

	@Override
	public void mouseClicked() {
		for (final NerdExtension e : this.extensions)
			e.mouseClicked();
	}

	@Override
	public void mouseDragged() {
		for (final NerdExtension e : this.extensions)
			e.mouseDragged();
	}

	@Override
	public void mouseMoved() {
		for (final NerdExtension e : this.extensions)
			e.mouseMoved();
	}

	@Override
	public void mousePressed() {
		for (final NerdExtension e : this.extensions)
			e.mousePressed();
	}

	@Override
	public void mouseReleased() {
		for (final NerdExtension e : this.extensions)
			e.mouseReleased();
	}

	@Override
	public void mouseWheel(final MouseEvent p_mouseEvent) {
		for (final NerdExtension e : this.extensions)
			e.mouseWheel(p_mouseEvent);
	}

	@Override
	public void keyPressed() {
		for (final NerdExtension e : this.extensions)
			e.keyPressed();
	}

	@Override
	public void keyReleased() {
		for (final NerdExtension e : this.extensions)
			e.keyReleased();
	}

	@Override
	public void keyTyped() {
		for (final NerdExtension e : this.extensions)
			e.keyTyped();
	}

	@Override
	protected void touchEnded() {
		for (final NerdExtension e : this.extensions)
			e.touchEnded();
	}

	@Override
	protected void touchMoved() {
		for (final NerdExtension e : this.extensions)
			e.touchMoved();
	}

	@Override
	protected void touchStarted() {
		for (final NerdExtension e : this.extensions)
			e.touchStarted();
	}
	// endregion

}
