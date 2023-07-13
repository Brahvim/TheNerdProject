package com.brahvim.nerd.processing_wrapper;

import java.awt.AWTException;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.function.Function;

import com.brahvim.nerd.framework.scene_api.NerdScene;
import com.brahvim.nerd.framework.scene_api.NerdScenesModule;
import com.brahvim.nerd.io.NerdStringTable;
import com.brahvim.nerd.io.asset_loader.NerdAsset;
import com.brahvim.nerd.io.asset_loader.NerdAssetLoader;
import com.brahvim.nerd.io.asset_loader.NerdAssetsModule;
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

	private final class NerdSketchOnlyAssetsModule extends NerdAssetsModule {

		private NerdSketchOnlyAssetsModule(final NerdSketch p_sketch) {
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

	// region Inner classes.
	/** Certain setting for the parent {@link NerdSketch}. */
	public class NerdSketchSettings {

		public final String NAME;
		public final String ICON_PATH;
		public final int ANTI_ALIASING;
		public final boolean USES_OPENGL;
		public final String RENDERER_NAME;
		public final Class<? extends NerdScene> FIRST_SCENE_CLASS;

		// Settings for the window:
		public final float INIT_SCR;
		public final int INIT_WIDTH, INIT_HEIGHT;
		public final int INIT_WIDTH_HALF, INIT_HEIGHT_HALF;
		public final int INIT_WIDTH_QUART, INIT_HEIGHT_QUART;

		public final boolean INITIALLY_RESIZABLE, STARTED_FULLSCREEN;

		public boolean closeOnEscape, canFullscreen, f11Fullscreen, altEnterFullscreen;

		protected final HashSet<Class<? extends NerdScene>> SCENES_TO_PRELOAD;

		protected NerdSketchSettings(final NerdSketchBuilderSettings p_settings) {
			this.ICON_PATH = p_settings.iconPath;
			this.RENDERER_NAME = p_settings.renderer;
			this.ANTI_ALIASING = p_settings.antiAliasing;
			this.INITIALLY_RESIZABLE = p_settings.canResize;
			this.SCENES_TO_PRELOAD = p_settings.scenesToPreload;
			this.FIRST_SCENE_CLASS = p_settings.firstSceneClass;
			this.STARTED_FULLSCREEN = p_settings.startedFullscreen;
			this.NAME = p_settings.name == null ? "TheNerdProject" : p_settings.name;

			this.USES_OPENGL = PConstants.P3D.equals(this.RENDERER_NAME);

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
	public final NerdStringTable STRINGS;
	public final NerdSketchBuilderSettings BUILDER_SETTINGS;
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

	protected NerdInputModule input;
	protected NerdScenesModule scenes;
	protected NerdWindowModule window;
	protected NerdDisplayModule display;
	protected final LinkedHashSet<NerdModule> MODULES;
	protected final HashMap<Class<? extends NerdModule>, NerdModule> CLASS_TO_MODULES_MAP;

	// Timers! (`millis()` returns `int`s!):
	protected int frameStartTime, pframeTime, frameTime;
	protected NerdGraphics nerdGraphics;
	protected PFont defaultFont;
	// endregion

	protected NerdSketch(final NerdSketchBuilderSettings p_settings) {
		Objects.requireNonNull(p_settings,
				"Please use an instance of some subclass of `NerdCustomSketchBuilder` to make a `NerdSketch`!");

		// Settings, straight from `NerdSketchBuilderSettings`!:
		this.BUILDER_SETTINGS = p_settings; // This objects just, ...lives a life. Don't ask me why!
		this.MODULES = p_settings.nerdModulesSupplier.apply(this);
		this.SKETCH_SETTINGS = new NerdSketch.NerdSketchSettings(p_settings);

		// region Storing and mapping `NerdModule`s!
		this.input = this.getNerdModule(NerdInputModule.class);
		this.scenes = this.getNerdModule(NerdScenesModule.class);
		this.window = this.getNerdModule(NerdWindowModule.class);
		this.display = this.getNerdModule(NerdDisplayModule.class);
		this.ASSETS = this.getNerdModule(NerdSketch.NerdSketchOnlyAssetsModule.class);

		this.CLASS_TO_MODULES_MAP = new HashMap<>(this.MODULES.size());

		for (final NerdModule m : this.MODULES)
			this.CLASS_TO_MODULES_MAP.put(m.getClass(), m);

		// Preloading assets from scenes the user wishes to, in advance:
		for (final Class<? extends NerdScene> c : this.SKETCH_SETTINGS.SCENES_TO_PRELOAD)
			this.scenes.loadSceneAssetsAsync(c);
		// endregion

		// region Setting OpenGL renderer icon.
		if (this.SKETCH_SETTINGS.USES_OPENGL)
			PJOGL.setIcon(this.SKETCH_SETTINGS.ICON_PATH);
		// endregion

		// region Loading the string table.
		NerdStringTable loadedTable = null;

		try {
			loadedTable = new NerdStringTable(p_settings.stringTablePath);
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			this.STRINGS = loadedTable;
		}
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

		p_settings.sketchConstructedListeners.forEach(l -> l.accept(this));
	}

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

		// for (final Consumer<NerdSketch> c : this.SETTINGS_LISTENERS)
		// if (c != null)
		// c.accept(this);
	}

	@Override
	public void setup() {
		if (this.SKETCH_SETTINGS.USES_OPENGL) {
			this.glu = new GLU();
			this.glGraphics = (PGraphicsOpenGL) super.getGraphics();
			this.gl = ((GLWindow) this.window.getNativeObject()).getGL();
		}

		this.MODULES.forEach(NerdModule::preSetup);

		super.registerMethod("pre", this);
		super.registerMethod("post", this);
		super.frameRate(this.DEFAULT_REFRESH_RATE);
		super.surface.setTitle(this.SKETCH_SETTINGS.NAME);

		this.nerdGraphics = new NerdGraphics(this, super.getGraphics());
		this.defaultFont = super.createFont("SansSerif", this.display.displayScr * 72);

		// ...Also makes these changes to `NerdSketch::nerdGraphics`, haha:
		super.background(0); // ..This, instead of `NerdAbstractCamera::clear()`.
		super.textFont(this.defaultFont);
		super.rectMode(PConstants.CENTER);
		super.imageMode(PConstants.CENTER);
		super.textAlign(PConstants.CENTER, PConstants.CENTER);

		// TODO: Move these calls into the window object itself!
		if (this.SKETCH_SETTINGS.INITIALLY_RESIZABLE)
			this.window.setResizable(true);

		this.window.centerWindow();
		// ..ENDTODO!

		this.MODULES.forEach(NerdModule::postSetup);
	}

	public void pre() {
		if (this.SKETCH_SETTINGS.USES_OPENGL)
			this.pgl = super.beginPGL();

		this.MODULES.forEach(NerdModule::pre);
	}

	@Override
	public void draw() {
		this.pframeTime = this.frameStartTime;
		this.frameStartTime = super.millis(); // Timestamp.
		this.frameTime = this.frameStartTime - this.pframeTime;

		// Call all pre-render listeners:
		// this.values.forEach(NerdModule::preDraw);

		this.nerdGraphics.applyCameraIfCan();

		// Call all draw listeners:
		this.MODULES.forEach(NerdModule::preDraw);
		this.MODULES.forEach(NerdModule::draw);
		this.MODULES.forEach(NerdModule::postDraw);

		// region If it doesn't yet exist, construct the scene!
		if (super.frameCount == 1 && this.scenes.getCurrentScene() == null) {
			if (this.SKETCH_SETTINGS.FIRST_SCENE_CLASS == null) {
				System.err.println("There is no initial `NerdScene` to show!");
				// System.exit(0);
			} else
				this.scenes.startScene(this.SKETCH_SETTINGS.FIRST_SCENE_CLASS);
		}
		// endregion

		// Call all post-render listeners:
		// this.values.forEach(NerdModule::postDraw);
	}

	public void post() {
		// this.sceneGraphics.endDraw();
		// // if (this.sceneGraphics.hasRendered())
		// this.image(this.sceneGraphics.getUnderlyingBuffer());
		// this.sceneGraphics.beginDraw();

		// These help complete background work:
		this.MODULES.forEach(NerdModule::post);

		// THIS ACTUALLY WORKED! How does the JVM interpret pointers?:
		// this.image(this.sceneGraphics);

		// ...Because apparently Processing allows rendering here!:
		if (this.SKETCH_SETTINGS.USES_OPENGL)
			super.endPGL();
	}

	@Override
	public void exit() {
		this.MODULES.forEach(NerdModule::exit);
		super.exit();
	}

	@Override
	public void dispose() {
		this.MODULES.forEach(NerdModule::dispose);
		super.dispose();
	}
	// endregion

	// region Processing's event callbacks! REMEMBER `this.sceneMan`! :joy:
	// region Mouse events.
	@Override
	public void mousePressed() {
		this.MODULES.forEach(NerdModule::mousePressed);
	}

	@Override
	public void mouseReleased() {
		this.MODULES.forEach(NerdModule::mouseReleased);
	}

	@Override
	public void mouseMoved() {
		this.MODULES.forEach(NerdModule::mouseMoved);
	}

	// JUST SO YA' KNOW!: On Android, `mouseClicked()` has been left unused.
	// AND, AND, ALSO!: On PC, it's called after `mouseReleased()`, AWT docs say.
	@Override
	public void mouseClicked() {
		this.MODULES.forEach(NerdModule::mouseClicked);
	}

	@Override
	public void mouseDragged() {
		this.MODULES.forEach(NerdModule::mouseDragged);
	}

	@Override
	public void mouseWheel(final MouseEvent p_mouseEvent) {
		this.MODULES.forEach(m -> m.mouseWheel(p_mouseEvent));
	}
	// endregion

	// region Keyboard events.
	@Override
	public void keyTyped() {
		this.MODULES.forEach(NerdModule::keyTyped);
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

		this.MODULES.forEach(NerdModule::keyPressed);
	}

	@Override
	public void keyReleased() {
		this.MODULES.forEach(NerdModule::keyReleased);
	}
	// endregion

	// region Touch events.
	public void touchStarted() {
		this.MODULES.forEach(NerdModule::touchStarted);
	}

	public void touchMoved() {
		this.MODULES.forEach(NerdModule::touchMoved);
	}

	public void touchEnded() {
		this.MODULES.forEach(NerdModule::touchEnded);
	}
	// endregion

	// region Window focus events.
	@Override
	public void focusGained() {
		super.focusGained();
		super.focused = true;
		this.MODULES.forEach(NerdModule::focusGained);

	}

	@Override
	public void focusLost() {
		super.focusLost();
		super.focused = false;
		this.MODULES.forEach(NerdModule::focusLost);
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

	@SuppressWarnings("unchecked")
	public <RetT extends NerdModule> RetT getNerdModule(final Class<RetT> p_moduleClass) {
		return (RetT) this.CLASS_TO_MODULES_MAP.get(p_moduleClass);
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

	// region [DEPRECATED] Overloads for `getPathToRootFrom()`.
	/**
	 * @deprecated Ineffective when using with {@link PApplet}'s "{@code load*()}"
	 *             methods. Also, all Processing-based assets can be
	 *             loaded from files outside the sketch's data
	 *             folder using <i>some</i> method! <b>Please also see
	 *             {@link PApplet}'s {@code static} methods.</b>
	 */
	@Deprecated
	public static String getPathToRootFrom(final File p_path) {
		return NerdSketch.getPathToRootFrom(p_path.getAbsolutePath());
	}

	/**
	 * @deprecated Ineffective when using with {@link PApplet}'s "{@code load*()}"
	 *             methods. Also, all Processing-based assets can be
	 *             loaded from files outside the sketch's data
	 *             folder using <i>some</i> method! <b>Please also see
	 *             {@link PApplet}'s {@code static} methods.</b>
	 */
	@Deprecated
	public static String getPathToRootFrom(final String p_path) {
		final int pathLen = p_path.length(), lastCharId = pathLen - 1;
		final StringBuilder toRetBuilder = new StringBuilder();

		if (p_path.charAt(lastCharId) != File.separatorChar)
			toRetBuilder.append(File.separator);

		for (int i = 0; i < pathLen; i++) {
			final char C = p_path.charAt(i);

			if (C == File.separatorChar) {
				toRetBuilder.append("..");
				toRetBuilder.append(File.separatorChar);
			}
		}

		return toRetBuilder.toString();
	}
	// endregion
	// endregion
	// endregion
	// endregion

}
