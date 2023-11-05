package com.brahvim.nerd.processing_wrapper;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.brahvim.nerd.processing_callback_interfaces.workflow.NerdSketchAllWorkflowsListener;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PShape;

public class NerdSketch extends PApplet implements NerdSketchAllWorkflowsListener {

	// region Fields.

	// region `public` fields.
	// region Constants.
	// region `static` constants.
	public static final File EXEC_DIR = new File("");
	public static final String EXEC_DIR_PATH = NerdSketch.EXEC_DIR.getAbsolutePath().concat(File.separator);

	public static final File DATA_DIR = new File("data");
	public static final String DATA_DIR_PATH = NerdSketch.DATA_DIR.getAbsolutePath().concat(File.separator);
	// endregion
	// endregion
	// endregion

	private final NerdSketchSettings SKETCH_SETTINGS;
	private final List<NerdModule> MODULES = new ArrayList<>(3);
	private final Map<NerdModule, Class<? extends NerdModule>>
	//////////////////////////////////////////////////////////
	MODULES_TO_CLASSES_MAP = new HashMap<>(3);
	// endregion

	// region Constructions.
	@SuppressWarnings("unused")
	private NerdSketch() {
		throw new IllegalAccessError("Please instantiate `" + this.getClass().getSimpleName()
				+ "`es the way they're supposed to be! Sorry...");
	}

	public NerdSketch(final NerdSketchSettings p_settings) {
		this.SKETCH_SETTINGS = p_settings;
	}
	// endregion

	@Override
	public void settings() {
		if (this.SKETCH_SETTINGS.fullScreen)
			super.fullScreen();
		else
			super.size(this.SKETCH_SETTINGS.width, this.SKETCH_SETTINGS.height);
	}

	@Override
	public void setup() {
	}

	@Override
	public void draw() {
	}

	@Override
	public void exit() {
	}

	@Override
	public void dispose() {
	}

	// region Hardware callbacks!
	@Override
	public void mouseClicked() {
	}

	@Override
	public void mouseDragged() {
	}

	@Override
	public void mousePressed() {
	}

	@Override
	public void mouseReleased() {
	}

	@Override
	public void keyPressed() {
	}

	@Override
	public void keyReleased() {
	}

	@Override
	public void keyTyped() {
	}
	// endregion

	// FIXME The ACTUAL utilities are NOT here yet!
	// region Rendering utilities!
	public PImage svgToImage(final PShape p_shape, final float p_width, final float p_height) {
		if (p_shape == null)
			throw new NullPointerException("`svgToImage(null , p_width, p_height)` won't work.");

		final PGraphics buffer = super.createGraphics(PApplet.ceil(p_width), PApplet.ceil(p_height), PConstants.P3D);

		if (buffer == null)
			throw new NullPointerException("`svgToImage()`'s `buffer` is `null`!");

		buffer.beginDraw();
		buffer.shape(p_shape, 0, 0, p_width, p_height);
		buffer.endDraw();
		return buffer;
	}

	// region `createGraphics()` overrides and overloads.
	// region Actual overrides.
	@Override
	public PGraphics createGraphics(final int p_width, final int p_height, final String p_renderer,
			final String p_path) {
		return super.makeGraphics(p_width, p_height, p_renderer, p_path, false);
	}

	@Override
	public PGraphics createGraphics(final int p_width, final int p_height, final String p_renderer) {
		return this.createGraphics(p_width, p_height, p_renderer, NerdSketch.EXEC_DIR_PATH);
	}

	@Override
	public PGraphics createGraphics(final int p_width, final int p_height) {
		return this.createGraphics(p_width, p_height, super.sketchRenderer());
	}
	// endregion

	// region Utilitarian overloads.
	public PGraphics createGraphics(final float p_width, final float p_height) {
		return this.createGraphics((int)p_width, (int)p_height, super.sketchRenderer());
	}

	public PGraphics createGraphics(final float p_size) {
		final int size = (int)p_size;
		return this.createGraphics(size, size, super.sketchRenderer());
	}

	public PGraphics createGraphics(final int p_size) {
		return this.createGraphics(p_size, p_size, super.sketchRenderer());
	}

	public PGraphics createGraphics() {
		return this.createGraphics(super.width, super.height, super.sketchRenderer());
	}
	// endregion
	// endregion

	// region File system utlities.
	public static String fromExecDir(final String p_path) {
		return NerdSketch.EXEC_DIR_PATH + p_path;
	}

	public static String fromDataDir(final String p_path) {
		return NerdSketch.DATA_DIR_PATH + p_path;
	}
	// endregion
	// endregion

}
