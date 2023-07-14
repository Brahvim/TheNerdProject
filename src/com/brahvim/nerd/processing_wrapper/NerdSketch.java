package com.brahvim.nerd.processing_wrapper;

import java.awt.AWTException;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.function.Function;

import com.brahvim.nerd.io.asset_loader.NerdAsset;
import com.brahvim.nerd.io.asset_loader.NerdAssetLoader;
import com.brahvim.nerd.io.asset_loader.NerdAssetsModule;
import com.brahvim.nerd.processing_wrapper.necessary_modules.NerdInputModule;
import com.brahvim.nerd.processing_wrapper.necessary_modules.NerdWindowModule;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.glu.GLU;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PShape;
import processing.event.MouseEvent;
import processing.opengl.PGL;
import processing.opengl.PGraphicsOpenGL;
import processing.opengl.PJOGL;

/**
 * <h1>¯\_(ツ)_/¯</h1>
 */
public class NerdSketch extends PApplet {

	// region Inner classes.
	protected final class NerdSketchOnlyAssetsModule extends NerdAssetsModule {

		protected NerdSketchOnlyAssetsModule(final NerdSketch p_sketch) {
			super(p_sketch);
		}

		@Override
		public <AssetT> NerdAsset addAsset(final NerdAssetLoader<AssetT> p_type) {
			final NerdAsset toRet = super.addAsset(p_type);
			toRet.startLoading();
			return toRet;
		}

		@Override
		public <AssetT> NerdAsset addAsset(final NerdAssetLoader<AssetT> p_type, final Runnable p_onLoad) {
			final NerdAsset toRet = super.addAsset(p_type, p_onLoad);
			toRet.startLoading();
			return toRet;
		}

	}

	/** Certain setting for the parent {@link NerdSketch}. */
	public class NerdSketchSettings {

		public final String INITIAL_WINDOW_TITLE;
		public final String ICON_PATH;
		public final int ANTI_ALIASING;
		public final boolean USES_OPENGL;
		public final String RENDERER_NAME;

		// Settings for the window:
		public final float INIT_SCR;
		public final int INIT_WIDTH, INIT_HEIGHT;
		public final int INIT_WIDTH_HALF, INIT_HEIGHT_HALF;
		public final int INIT_WIDTH_QUART, INIT_HEIGHT_QUART;

		public final boolean INITIALLY_RESIZABLE, STARTED_FULLSCREEN;

		public boolean closeOnEscape, canFullscreen, f11Fullscreen, altEnterFullscreen;

		protected NerdSketchSettings(final NerdSketchBuilderSettings p_settings) {
			this.ICON_PATH = p_settings.iconPath;
			this.RENDERER_NAME = p_settings.renderer;
			this.ANTI_ALIASING = p_settings.antiAliasing;
			this.INITIALLY_RESIZABLE = p_settings.canResize;
			this.STARTED_FULLSCREEN = p_settings.startedFullscreen;
			this.USES_OPENGL = PConstants.P3D.equals(this.RENDERER_NAME);
			this.INITIAL_WINDOW_TITLE = p_settings.name == null ? "Nerd Sketch" : p_settings.name;

			this.canFullscreen = !p_settings.cannotFullscreen;
			this.f11Fullscreen = !p_settings.cannotF11Fullscreen;
			this.closeOnEscape = !p_settings.preventCloseOnEscape;
			this.closeOnEscape = !p_settings.preventCloseOnEscape;
			this.altEnterFullscreen = !p_settings.cannotAltEnterFullscreen;

			// region Non-fullscreen window's dimensions when starting fullscreen.
			if (this.STARTED_FULLSCREEN) {
				this.INIT_WIDTH = 800;
				this.INIT_HEIGHT = 600;
			} else {
				this.INIT_WIDTH = p_settings.width;
				this.INIT_HEIGHT = p_settings.height;
			}

			// `INIT_WIDTH` ratios:
			this.INIT_WIDTH_HALF = this.INIT_WIDTH / 2;
			this.INIT_WIDTH_QUART = this.INIT_WIDTH / 2;

			this.INIT_HEIGHT_QUART = this.INIT_WIDTH / 4;
			this.INIT_HEIGHT_HALF = this.INIT_WIDTH / 4;

			this.INIT_SCR = (float) this.INIT_WIDTH / (float) this.INIT_HEIGHT;
			// endregion
		}

	}
	// endregion

	// region `public` fields.
	// region Constants.
	// region `static` constants.
	public static final File EXEC_DIR = new File("");
	public static final String EXEC_DIR_PATH = NerdSketch.EXEC_DIR.getAbsolutePath().concat(File.separator);

	public static final File DATA_DIR = new File("data");
	public static final String DATA_DIR_PATH = NerdSketch.DATA_DIR.getAbsolutePath().concat(File.separator);
	// endregion

	// region Instance constants.
	public final Robot ROBOT;
	public final NerdAssetsModule ASSETS;
	public final NerdSketch.NerdSketchSettings SKETCH_SETTINGS;

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

	// region `protected` fields.
	protected GL gl;
	protected GLU glu;
	protected PGL pgl;
	protected PGraphicsOpenGL glGraphics;

	protected final ArrayList<NerdModule> MODULES;
	protected final HashMap<Class<? extends NerdModule>, NerdModule> CLASSES_TO_MODULES_MAP;

	// Timers! (`millis()` returns `int`s!):
	protected int frameStartTime, pframeTime, frameTime;
	protected NerdGraphics nerdGraphics;
	protected NerdWindowModule window;
	protected NerdInputModule input;
	protected PFont defaultFont;
	// endregion

	// region Construction!
	public NerdSketch(final NerdSketchBuilderSettings p_settings) {
		Objects.requireNonNull(p_settings, String.format("""
				Please use an instance of some subclass of `%s`, or initialize a `%s` by hand, to make a `%s`.
				You seem to have provided, `null`, to its constructor.""",
				NerdCustomSketchBuilder.class.getSimpleName(),
				NerdSketchBuilderSettings.class.getSimpleName(),
				this.getClass().getSimpleName()));

		this.SKETCH_SETTINGS = new NerdSketch.NerdSketchSettings(p_settings);

		// region Stuff involving modules
		this.CLASSES_TO_MODULES_MAP = new HashMap<>(0);
		this.MODULES = this.sortModules(p_settings);

		for (final NerdModule m : this.MODULES) {
			this.CLASSES_TO_MODULES_MAP.put(m.getClass(), m);
			m.sketchConstructed(p_settings);

			// Assign `NerdModuleSettings` to all `NerdModule`s. ...`Theta(n^2)` style!:
			for (final var e : p_settings.nerdModulesSettings.entrySet())
				m.assignModuleSettings(e.getValue());
			// ...Doing it this way because otherwise some subclasses may be missed!
		}
		// endregion

		this.ASSETS = this.getNerdModule(NerdSketch.NerdSketchOnlyAssetsModule.class);

		// region Setting the icon of the OpenGL renderer's window.
		if (this.SKETCH_SETTINGS.USES_OPENGL)
			PJOGL.setIcon(this.SKETCH_SETTINGS.ICON_PATH);
		// endregion

		// region Setting up `this.ROBOT`.
		Robot toAssign = null;

		try {
			toAssign = new Robot();
			toAssign.setAutoDelay(0);
			toAssign.setAutoWaitForIdle(false);
		} catch (final AWTException e) {
			e.printStackTrace();
		}

		this.ROBOT = toAssign;
		// endregion
	}

	private ArrayList<NerdModule> sortModules(final NerdSketchBuilderSettings p_settings) {
		final LinkedHashSet<Function<NerdSketch, NerdModule>> nerdModulesToAssign = new LinkedHashSet<>(0);
		p_settings.nerdModulesInstantiator.accept(nerdModulesToAssign);

		final ArrayList<NerdModule> toRet = new ArrayList<>(nerdModulesToAssign.size());

		// Construct modules using the provided `Function`s, and add them to the map:
		for (final Function<NerdSketch, NerdModule> f : nerdModulesToAssign)
			try {

				Objects.requireNonNull(f,
						"Could not instantiate a `NerdModule` due to the supplying function being `null`.");

				final NerdModule module = f.apply(this);
				Objects.requireNonNull(module,
						"Could not include a `NerdModule` due to the it being `null`.");

				final Class<? extends NerdModule> moduleClass = module.getClass();

				// If we already have a certain `NerdModule`,
				if (this.CLASSES_TO_MODULES_MAP.get(moduleClass) != null) {
					// ..We replace the existing one,
					toRet.removeIf(m -> m.getClass().equals(moduleClass));

					// ..Add the new one at the end:
					toRet.add(module);

					// ..Update it in our map,
					this.CLASSES_TO_MODULES_MAP.put(moduleClass, module);

					// ..and continue the loop!:
					continue;
				}

				// Otherwise, just add as usual.

				toRet.add(module);
				this.CLASSES_TO_MODULES_MAP.put(moduleClass, module);

			} catch (final Exception e) {
				System.err.println("An exception occured when trying to instantiate `NerdModule`s:");
				e.printStackTrace();
			}

		return toRet;
	}
	// endregion

	// region Processing sketch workflow.
	@Override
	public void settings() {
		// Crazy effects (no un-decoration!), I guess:
		// if (this.STARTED_FULLSCREEN)
		// super.fullScreen(this.RENDERER);
		// else

		super.smooth(this.SKETCH_SETTINGS.ANTI_ALIASING);
		super.size(
				this.SKETCH_SETTINGS.INIT_WIDTH,
				this.SKETCH_SETTINGS.INIT_HEIGHT,
				this.SKETCH_SETTINGS.RENDERER_NAME);

		for (final NerdModule m : this.MODULES)
			m.settings();
	}

	@Override
	public void setup() {
		for (final NerdModule m : this.MODULES)
			m.preSetup();

		this.input = this.getNerdModule(NerdInputModule.class);
		this.window = this.getNerdModule(NerdWindowModule.class);

		if (this.SKETCH_SETTINGS.USES_OPENGL) {
			this.glu = new GLU();
			this.glGraphics = (PGraphicsOpenGL) super.getGraphics();
			this.gl = ((GLWindow) this.window.getNativeObject()).getGL();
		}

		super.registerMethod("pre", this);
		super.registerMethod("post", this);
		super.frameRate(this.DEFAULT_REFRESH_RATE);
		super.surface.setTitle(this.SKETCH_SETTINGS.INITIAL_WINDOW_TITLE);

		this.nerdGraphics = new NerdGraphics(this, super.getGraphics());
		this.defaultFont = super.createFont("SansSerif",
				72 * ((float) super.displayWidth / (float) super.displayHeight));

		// ...Also makes these changes to `this.nerdGraphics`, haha:
		super.background(0); // ..This, instead of `NerdAbstractCamera::clear()`.
		super.textFont(this.defaultFont);
		super.rectMode(PConstants.CENTER);
		super.imageMode(PConstants.CENTER);
		super.textAlign(PConstants.CENTER, PConstants.CENTER);

		if (this.SKETCH_SETTINGS.INITIALLY_RESIZABLE)
			this.window.setResizable(true);

		this.window.centerWindow();

		for (final NerdModule m : this.MODULES)
			m.postSetup();
	}

	public void pre() {
		if (this.SKETCH_SETTINGS.USES_OPENGL)
			this.pgl = super.beginPGL();

		for (final NerdModule m : this.MODULES)
			m.pre();

	}

	@Override
	public void draw() {
		this.pframeTime = this.frameStartTime;
		this.frameStartTime = super.millis(); // Timestamp!
		this.frameTime = this.frameStartTime - this.pframeTime;

		this.nerdGraphics.applyCameraIfCan();

		// Process all `NerdModule`s:
		for (final NerdModule m : this.MODULES) {
			m.preDraw();
			m.draw();
			m.postDraw();
		}
	}

	public void post() {
		// These help complete background work:
		for (final NerdModule m : this.MODULES)
			m.post();

		// THIS ACTUALLY WORKED! How does the JVM interpret pointers?:
		// super.image(this.nerdGraphics.getUnderlyingBuffer(), 0, 0);

		// ...Because apparently Processing allows rendering here!:
		if (this.SKETCH_SETTINGS.USES_OPENGL)
			super.endPGL();
	}

	@Override
	public void exit() {
		for (final NerdModule m : this.MODULES)
			m.exit();

		super.exit();
	}

	@Override
	public void dispose() {
		for (final NerdModule m : this.MODULES)
			m.dispose();

		super.dispose();
	}
	// endregion

	// region Processing's event callbacks! `NerdModule`s, go!!
	// region Mouse events.
	@Override
	public void mousePressed() {
		for (final NerdModule m : this.MODULES)
			m.mousePressed();

	}

	@Override
	public void mouseReleased() {
		for (final NerdModule m : this.MODULES)
			m.mouseReleased();

	}

	@Override
	public void mouseMoved() {
		for (final NerdModule m : this.MODULES)
			m.mouseMoved();

	}

	// JUST SO YA' KNOW!: On Android, `mouseClicked()` has been left unused.
	// AND, AND, ALSO!: On PC, it's called after `mouseReleased()`, AWT docs say.
	@Override
	public void mouseClicked() {
		for (final NerdModule m : this.MODULES)
			m.mouseClicked();

	}

	@Override
	public void mouseDragged() {
		for (final NerdModule m : this.MODULES)
			m.mouseDragged();

	}

	@Override
	public void mouseWheel(final MouseEvent p_mouseEvent) {
		this.MODULES.forEach(m -> m.mouseWheel(p_mouseEvent));
	}
	// endregion

	// region Keyboard events.
	@Override
	public void keyTyped() {
		for (final NerdModule m : this.MODULES)
			m.keyTyped();

	}

	@Override
	public void keyPressed() {
		if (!this.SKETCH_SETTINGS.closeOnEscape && this.keyCode == 27)
			this.key = '\0'; // Processing checks this to know what key was pressed.
		// By setting it to `\0`, we disallow exiting.

		if (this.SKETCH_SETTINGS.canFullscreen) {
			if (this.SKETCH_SETTINGS.altEnterFullscreen
					&& super.keyCode == KeyEvent.VK_ENTER
					&& this.input.anyGivenKeyIsPressed(KeyEvent.VK_ALT, 19))
				this.window.fullscreen = !this.window.fullscreen;
			else if (this.SKETCH_SETTINGS.f11Fullscreen) {
				// `KeyEvent.VK_ADD` is `107`, but here, it's actually `F11`!:
				if (this.SKETCH_SETTINGS.USES_OPENGL && super.keyCode == 107
						|| super.keyCode == KeyEvent.VK_F11)
					this.window.fullscreen = !this.window.fullscreen;
			}
		}

		for (final NerdModule m : this.MODULES)
			m.keyPressed();

	}

	@Override
	public void keyReleased() {
		for (final NerdModule m : this.MODULES)
			m.keyReleased();

	}
	// endregion

	// region Touch events.
	public void touchStarted() {
		for (final NerdModule m : this.MODULES)
			m.touchStarted();

	}

	public void touchMoved() {
		for (final NerdModule m : this.MODULES)
			m.touchMoved();

	}

	public void touchEnded() {
		for (final NerdModule m : this.MODULES)
			m.touchEnded();

	}
	// endregion

	// region Window and display events.
	public void monitorChanged() {
		for (final NerdModule m : this.MODULES)
			m.focusGained();
	}

	public void fullscreenChanged(final boolean p_state) {
		for (final NerdModule m : this.MODULES)
			m.fullscreenChanged(p_state);
	}

	@Override
	public void frameMoved(final int x, final int y) {
		System.out.println("`NerdSketch::frameMoved()`");
	}

	@Override
	public void frameResized(final int w, final int h) {
		System.out.println("`NerdSketch::frameResized()`");
	}

	@Override
	public void focusGained() {
		super.focusGained();
		super.focused = true;
		for (final NerdModule m : this.MODULES)
			m.focusGained();
	}

	@Override
	public void focusLost() {
		super.focusLost();
		super.focused = false;
		for (final NerdModule m : this.MODULES)
			m.focusLost();
	}
	// endregion
	// endregion

	// region Persistent asset operations.
	public void reloadGivenPersistentAsset(final NerdAsset p_asset) {
		this.ASSETS.remove(p_asset);
		this.ASSETS.addAsset(p_asset.getLoader());
	}

	public void reloadPersistentAssets() {
		final NerdAsset[] toReload = (NerdAsset[]) this.ASSETS.toArray();
		this.ASSETS.clear();

		for (final NerdAsset a : toReload)
			this.ASSETS.addAsset(a.getLoader());
	}
	// endregion

	// region Utilities!~
	public GL getGl() {
		return this.gl;
	}

	public GLU getGlu() {
		return this.glu;
	}

	public int getFrameTime() {
		return this.frameTime;
	}

	public int getPframeTime() {
		return this.pframeTime;
	}

	public PFont getDefaultFont() {
		return this.defaultFont;
	}

	public NerdAssetsModule getASSETS() {
		return this.ASSETS;
	}

	public final int getFrameStartTime() {
		return this.frameStartTime;
	}

	public NerdGraphics getNerdGraphics() {
		return this.nerdGraphics;
	}

	public PGraphicsOpenGL getGlGraphics() {
		return this.glGraphics;
	}

	@SuppressWarnings("unchecked")
	public <RetT extends NerdModule> RetT getNerdModule(final Class<RetT> p_moduleClass) {
		for (final var e : this.CLASSES_TO_MODULES_MAP.entrySet())
			if (p_moduleClass.isAssignableFrom(e.getKey()))
				return (RetT) e.getValue();
		return null;
	}

	// region Rendering utilities!
	public PImage svgToImage(final PShape p_shape, final float p_width, final float p_height) {
		if (p_shape == null)
			throw new NullPointerException("`svgToImage(null , p_width, p_height)` won't work.");

		final PGraphics buffer = super.createGraphics(
				PApplet.ceil(p_width), PApplet.ceil(p_height), PConstants.P3D);

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
	public PGraphics createGraphics(
			final int p_width, final int p_height, final String p_renderer, final String p_path) {
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

	// region File system utlities.
	public static String fromExecDir(final String p_path) {
		return NerdSketch.EXEC_DIR_PATH + p_path;
	}

	public static String fromDataDir(final String p_path) {
		return NerdSketch.DATA_DIR_PATH + p_path;
	}
	// endregion
	// endregion
	// endregion

}
