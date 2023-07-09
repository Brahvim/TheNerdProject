package com.brahvim.nerd.processing_wrapper;

import java.util.HashMap;

import processing.event.MouseEvent;

public class NerdExtModule extends NerdModule {

	public final HashMap<String, NerdExt> EXTENSIONS;

	public NerdExtModule(final NerdSketch p_sketch) {
		super(p_sketch);
		this.EXTENSIONS = new HashMap<>();
	}

	// region Callbacks.
	@Override
	protected void dispose() {
		this.EXTENSIONS.values().forEach(e -> e.dispose());
	}

	@Override
	protected void draw() {
		this.EXTENSIONS.values().forEach(e -> e.draw());
	}

	@Override
	protected void exit() {
		this.EXTENSIONS.values().forEach(e -> e.exit());
	}

	@Override
	protected void post() {
		this.EXTENSIONS.values().forEach(e -> e.post());
	}

	@Override
	protected void pre() {
		this.EXTENSIONS.values().forEach(e -> e.pre());
	}

	@Override
	protected void settings() {
		this.EXTENSIONS.values().forEach(e -> e.settings());
	}

	@Override
	protected void setup() {
		this.EXTENSIONS.values().forEach(e -> e.setup());
	}

	@Override
	public void focusGained() {
		this.EXTENSIONS.values().forEach(e -> e.focusGained());
	}

	@Override
	public void focusLost() {
		this.EXTENSIONS.values().forEach(e -> e.focusLost());
	}

	@Override
	public void fullscreenChanged(final boolean p_state) {
		this.EXTENSIONS.values().forEach(e -> e.fullscreenChanged(p_state));
	}

	@Override
	public void monitorChanged() {
		this.EXTENSIONS.values().forEach(e -> e.monitorChanged());
	}

	@Override
	public void resized() {
		this.EXTENSIONS.values().forEach(e -> e.resized());
	}

	@Override
	public void mouseClicked() {
		this.EXTENSIONS.values().forEach(e -> e.mouseClicked());
	}

	@Override
	public void mouseDragged() {
		this.EXTENSIONS.values().forEach(e -> e.mouseDragged());
	}

	@Override
	public void mouseMoved() {
		this.EXTENSIONS.values().forEach(e -> e.mouseMoved());
	}

	@Override
	public void mousePressed() {
		this.EXTENSIONS.values().forEach(e -> e.mousePressed());
	}

	@Override
	public void mouseReleased() {
		this.EXTENSIONS.values().forEach(e -> e.mouseReleased());
	}

	@Override
	public void mouseWheel(final MouseEvent p_mouseEvent) {
		this.EXTENSIONS.values().forEach(e -> e.mouseWheel(p_mouseEvent));
	}

	@Override
	public void keyPressed() {
		this.EXTENSIONS.values().forEach(e -> e.keyPressed());
	}

	@Override
	public void keyReleased() {
		this.EXTENSIONS.values().forEach(e -> e.keyReleased());
	}

	@Override
	public void keyTyped() {
		this.EXTENSIONS.values().forEach(e -> e.keyTyped());
	}

	@Override
	public void touchEnded() {
		this.EXTENSIONS.values().forEach(e -> e.touchEnded());
	}

	@Override
	public void touchMoved() {
		this.EXTENSIONS.values().forEach(e -> e.touchMoved());
	}

	@Override
	public void touchStarted() {
		this.EXTENSIONS.values().forEach(e -> e.touchStarted());
	}
	// endregion

}
