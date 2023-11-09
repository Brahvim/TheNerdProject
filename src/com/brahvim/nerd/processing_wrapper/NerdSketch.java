package com.brahvim.nerd.processing_wrapper;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Robot;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.brahvim.nerd.processing_callback_interfaces.workflow.NerdSketchAllWorkflowsListener;
import com.brahvim.nerd.processing_wrapper.graphics_backends.generic.NerdGenericGraphics;
import com.brahvim.nerd.utils.NerdAwtUtils;
import com.brahvim.nerd.utils.NerdReflectionUtils;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PShape;

public abstract class NerdSketch<RendererT extends NerdGenericGraphics> extends PApplet
		implements NerdSketchAllWorkflowsListener {

	// region Fields.
	// region Public fields.
	// region Constants.
	// region `static` constants.
	public static final File EXEC_DIR = new File("");
	public static final String EXEC_DIR_PATH = NerdSketch.EXEC_DIR.getAbsolutePath().concat(File.separator);

	public static final File DATA_DIR = new File("data");
	public static final String DATA_DIR_PATH = NerdSketch.DATA_DIR.getAbsolutePath().concat(File.separator);
	// endregion

	// region Instance constants.
	public final Robot ROBOT;
	// public final NerdAssetsModule ASSETS;

	// region `java.awt` constants.
	public final GraphicsEnvironment LOCAL_GRAPHICS_ENVIRONMENT = GraphicsEnvironment
			.getLocalGraphicsEnvironment();

	public GraphicsDevice[] JAVA_SCREENS = this.LOCAL_GRAPHICS_ENVIRONMENT
			.getScreenDevices();

	public GraphicsDevice DEFAULT_JAVA_SCREEN = this.LOCAL_GRAPHICS_ENVIRONMENT
			.getDefaultScreenDevice();

	public DisplayMode DEFAULT_JAVA_SCREEN_MODE = this.DEFAULT_JAVA_SCREEN
			.getDisplayMode();

	public int DEFAULT_REFRESH_RATE = this.DEFAULT_JAVA_SCREEN_MODE
			.getRefreshRate();
	// endregion
	// endregion
	// endregion
	// endregion
	// Timers! (`millis()` returns `int`s!):
	protected int frameStartTime, pframeTime, frameTime;
	protected final RendererT GRAPHICS;
	// protected NerdGraphics nerdGraphics;
	// protected NerdWindowModule window;
	// protected NerdInputModule input;
	protected PFont defaultFont;

	// region Protected fields.
	protected final NerdSketchSettings SKETCH_SETTINGS;
	protected final List<NerdModule> MODULES = new ArrayList<>(3);
	protected final Map<NerdModule, Class<? extends NerdModule>>
	//////////////////////////////////////////////////////////
	MODULES_TO_CLASSES_MAP = new HashMap<>(3);
	// endregion
	// endregion

	// region Constructions.
	protected NerdSketch() {
		this.ROBOT = null;
		this.GRAPHICS = null;
		this.SKETCH_SETTINGS = null;
		NerdReflectionUtils.rejectStaticClassInstantiationFor(this.getClass());
	}

	@SuppressWarnings("unchecked")
	protected NerdSketch(final NerdSketchSettings p_settings) {
		this.SKETCH_SETTINGS = p_settings;
		this.ROBOT = NerdAwtUtils.createAwtRobot();
		this.GRAPHICS = (RendererT) super.getGraphics();
	}
	// endregion

	@Override
	public void settings() {
		super.smooth(this.SKETCH_SETTINGS.antiAliasing);

		if (this.SKETCH_SETTINGS.fullScreen)
			super.fullScreen(PConstants.P3D);
		else
			super.size(this.SKETCH_SETTINGS.width, this.SKETCH_SETTINGS.height, PConstants.P3D);
	}

	@Override
	public void setup() {
		super.surface.setResizable(this.SKETCH_SETTINGS.canResize);
		this.forEachNerdModule(NerdModule::preSetup);

		super.registerMethod("pre", this);
		super.registerMethod("post", this);
		// super.frameRate(this.DEFAULT_REFRESH_RATE);
		// super.surface.setTitle(this.SKETCH_SETTINGS.INITIAL_WINDOW_TITLE);

		// this.nerdGraphics = new NerdGraphics(this, super.getGraphics());
		this.defaultFont = super.createFont("SansSerif",
				72 * ((float) super.displayWidth / (float) super.displayHeight));

		// ...Also makes these changes to `this.nerdGraphics`, haha:
		super.background(0); // ..This, instead of `NerdAbstractCamera::clear()`.
		super.textFont(this.defaultFont);
		super.rectMode(PConstants.CENTER);
		super.imageMode(PConstants.CENTER);
		super.textAlign(PConstants.CENTER, PConstants.CENTER);

		// if (this.SKETCH_SETTINGS.canResize)
		// this.genericWindow.setResizable(true);
		// this.genericWindow.centerWindow();

		this.forEachNerdModule(NerdModule::postSetup);
	}

	@Override
	public void pre() {
		this.pframeTime = this.frameStartTime;
		this.frameStartTime = super.millis(); // Timestamp!
		this.frameTime = this.frameStartTime - this.pframeTime;

		this.forEachNerdModule(NerdModule::pre);
	}

	@Override
	public void preDraw() {
		this.forEachNerdModule(NerdModule::preDraw);
	}

	@Override
	public void draw() {
		this.forEachNerdModule(NerdModule::draw);
	}

	@Override
	public void postDraw() {
		this.forEachNerdModule(NerdModule::postDraw);
	}

	@Override
	public void post() {
		this.forEachNerdModule(NerdModule::post);
	}

	@Override
	public void exit() {
		this.forEachNerdModule(NerdModule::exit);
	}

	@Override
	public void dispose() {
		this.forEachNerdModule(NerdModule::dispose);
	}

	// region `NerdModule` management.
	public <RetT extends NerdModule> RetT getModule(final Class<RetT> p_moduleClass) {
		for (final NerdModule m : this.MODULES)
			if (p_moduleClass.isInstance(m))
				return p_moduleClass.cast(m);
		throw new NoSuchElementException("No `NerdModule` of type `" + p_moduleClass.getSimpleName() + "` was found.");
	}

	public void forEachNerdModule(final Consumer<NerdModule> p_task) {
		if (p_task == null)
			return;
		this.MODULES.forEach(p_task);
	}

	public <OtherArgT> void forEachNerdModule(
			final BiConsumer<NerdModule, OtherArgT> p_task, final OtherArgT p_otherArg) {
		if (p_task == null)
			return;

		for (final NerdModule m : this.MODULES)
			p_task.accept(m, p_otherArg);
	}
	// endregion

	// region Hardware callbacks!
	// Mouse:

	@Override
	public void mouseClicked() {
		this.forEachNerdModule(NerdModule::mouseClicked);
	}

	@Override
	public void mouseWheel(final processing.event.MouseEvent p_event) {
		this.forEachNerdModule(NerdModule::mouseWheel, p_event);
	}

	@Override
	public void mouseDragged() {
		this.forEachNerdModule(NerdModule::mouseDragged);
	}

	@Override
	public void mousePressed() {
		this.forEachNerdModule(NerdModule::mousePressed);
	}

	@Override
	public void mouseReleased() {
		this.forEachNerdModule(NerdModule::mouseReleased);
	}

	// Keys:

	@Override
	public void keyPressed() {
		this.forEachNerdModule(NerdModule::keyPressed);
	}

	@Override
	public void keyReleased() {
		this.forEachNerdModule(NerdModule::keyReleased);
	}

	@Override
	public void keyTyped() {
		this.forEachNerdModule(NerdModule::keyTyped);
	}

	// Touches:

	public void touchStarted() {
		this.forEachNerdModule(NerdModule::touchStarted);
	}

	public void touchMoved() {
		this.forEachNerdModule(NerdModule::touchMoved);
	}

	public void touchEnded() {
		this.forEachNerdModule(NerdModule::touchEnded);
	}
	// endregion

	// FIXME The ACTUAL utilities are NOT here yet!
	// region Rendering utilities!
	public PImage svgToImage(final PShape p_shape, final float p_width, final float p_height) {
		if (p_shape == null)
			throw new NullPointerException("`svgToImage(null , p_width, p_height)` won't work.");

		final NerdGenericGraphics buffer = super.createGraphics(PApplet.ceil(p_width), PApplet.ceil(p_height),
				PConstants.P3D);

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
		return this.createGraphics((int) p_width, (int) p_height, super.sketchRenderer());
	}

	public PGraphics createGraphics(final float p_size) {
		final int size = (int) p_size;
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

	// region File system utilities.
	public static String fromExecDir(final String p_path) {
		return NerdSketch.EXEC_DIR_PATH + p_path;
	}

	public static String fromDataDir(final String p_path) {
		return NerdSketch.DATA_DIR_PATH + p_path;
	}
	// endregion
	// endregion

}
