package com.brahvim.nerd.processing_wrapper;

import java.awt.AWTException;
import java.awt.DisplayMode;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.function.Consumer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import com.brahvim.nerd.framework.scene_api.NerdScene;
import com.brahvim.nerd.framework.scene_api.NerdSceneManager;
import com.brahvim.nerd.io.NerdStringTable;
import com.brahvim.nerd.io.asset_loader.NerdAsset;
import com.brahvim.nerd.io.asset_loader.NerdAssetLoader;
import com.brahvim.nerd.io.asset_loader.NerdAssetManager;
import com.brahvim.nerd.math.NerdUnprojector;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.glu.GLU;

import processing.awt.PSurfaceAWT;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PShape;
import processing.opengl.PGL;
import processing.opengl.PGraphicsOpenGL;
import processing.opengl.PJOGL;

/**
 * <h1>¯\_(ツ)_/¯</h1>
 */
public class NerdSketch extends PApplet {

	// region Inner classes.
	// Used abstract classes instead of interfaces for these (two) reasons:
	/*
	 * - No security for a `static` field containing a collection of all references
	 * from the user! It'll be `public`!
	 *
	 * - For registering a reference into the collection of all of them,
	 * a default method may be used, since interfaces do not have them. However,
	 * even default methods are overrideable, meaning that the registering code
	 * becomes modifiable, letting the user change what they weren't supposed to!
	 *
	 * Abstract classes do not do so. Their constructors are called by the
	 * implementing subclass regardless of being overriden.
	 */

	// region Input listeners.
	public abstract class NerdSketchMouseListener {

		protected NerdSketchMouseListener() {
			NerdSketch.this.MOUSE_LISTENERS.add(this);
		}

		// region Mouse events.
		public void mousePressed() {
		}

		public void mouseReleased() {
		}

		public void mouseMoved() {
		}

		public void mouseClicked() {
		}

		public void mouseDragged() {
		}

		public void mouseWheel(final processing.event.MouseEvent p_mouseEvent) {
		}
		// endregion

	}

	public abstract class NerdSketchTouchListener {

		protected NerdSketchTouchListener() {
			NerdSketch.this.TOUCH_LISTENERS.add(this);
		}

		// region Touch events.
		public void touchStarted() {
		}

		public void touchMoved() {
		}

		public void touchEnded() {
		}
		// endregion

	}

	public abstract class NerdSketchWindowListener {

		protected NerdSketchWindowListener() {
			NerdSketch.this.WINDOW_LISTENERS.add(this);
		}

		// region Window focus events.
		public void focusLost() {
		}

		public void focusGained() {
		}
		// endregion

		// region Other window events.
		public void resized() {
		}

		public void monitorChanged() {
		}

		public void fullscreenChanged(final boolean p_state) {
		}
		// endregion

	}

	public abstract class NerdSketchKeyboardListener {

		protected NerdSketchKeyboardListener() {
			NerdSketch.this.KEYBOARD_LISTENERS.add(this);
		}

		// region Keyboard events.
		public void keyTyped() {
		}

		public void keyPressed() {
		}

		public void keyReleased() {
		}
		// endregion

	}
	// endregion

	/** Certain setting for the parent {@link NerdSketch}. */
	public class NerdSketchSettings {

		public final String NAME;
		public final String ICON_PATH;
		public final int ANTI_ALIASING;
		public final boolean USES_OPENGL;
		public final String RENDERER_NAME;
		public final Class<? extends NerdScene> FIRST_SCENE_CLASS;

		// Dimensions of the inital size of the window:
		public final float INIT_SCR;
		public final int INIT_WIDTH, INIT_HEIGHT;
		public final int INIT_WIDTH_HALF, INIT_HEIGHT_HALF;
		public final int INIT_WIDTH_QUART, INIT_HEIGHT_QUART;

		public final boolean INITIALLY_RESIZABLE, STARTED_FULLSCREEN;

		public boolean closeOnEscape, canFullscreen, f11Fullscreen, altEnterFullscreen;

		protected NerdSketchSettings(final NerdSketchBuilderSettings p_settings) {
			this.ICON_PATH = p_settings.iconPath;
			this.ANTI_ALIASING = p_settings.antiAliasing;
			this.INITIALLY_RESIZABLE = p_settings.canResize;
			this.FIRST_SCENE_CLASS = p_settings.firstSceneClass;
			this.STARTED_FULLSCREEN = p_settings.startedFullscreen;
			this.NAME = p_settings.name == null ? "TheNerdProject" : p_settings.name;

			this.canFullscreen = !p_settings.cannotFullscreen;
			this.f11Fullscreen = !p_settings.cannotF11Fullscreen;
			this.closeOnEscape = !p_settings.preventCloseOnEscape;
			this.closeOnEscape = !p_settings.preventCloseOnEscape;
			this.altEnterFullscreen = !p_settings.cannotAltEnterFullscreen;

			this.RENDERER_NAME = p_settings.renderer;
			this.USES_OPENGL = PConstants.P3D.equals(this.RENDERER_NAME);

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
	public final NerdStringTable STRINGS;
	public final NerdAssetManager ASSETS;
	public final NerdUnprojector UNPROJECTOR;
	public final HashMap<String, Object> EXTENSIONS;
	public final NerdSketch.NerdSketchSettings SKETCH_SETTINGS;
	// `Object`s instead of a custom interface because you can't do
	// that to libraries you didn't write! (...or you'd be writing subclasses of the
	// library classes. Manual work. Uhh...)

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

	// `NerdSceneManager` is a `protected` field.
	public final NerdDisplayManager DISPLAYS;
	public final NerdWindowManager WINDOW;
	public final NerdInputManager INPUT;
	// endregion
	// endregion

	// Time (`millis()` returns `int`!):
	protected int frameStartTime, pframeTime, frameTime;
	// endregion

	// region `protected` fields.
	protected static final String NULL_LISTENER_ERROR_MESSAGE = "An object passed to `Sketch::add*Listener()` cannot be `null`.";

	// region Window object and native renderer references ("hacky stuff").
	// ("Why check for `null` at all? You know what renderer you used!")
	protected JFrame sketchFrame;

	// OpenGL context:
	protected GL gl;
	protected GLU glu;
	protected PGL pgl;
	protected GLWindow glWindow;
	protected PGraphicsOpenGL glGraphics;
	// endregion

	protected final NerdBridgedSceneManager SCENES; // This is a bridged object, thus, `protected`.

	protected NerdGraphics nerdGraphics;
	protected PFont defaultFont;
	protected PImage iconImage;

	// region Callback listeners,
	// LAMBDAS ARE EXPENSIVVVVVE! Allocate only this one!:
	protected final Consumer<Consumer<NerdSketch>> DEFAULT_CALLBACK_ITR_LAMBDA = l -> l.accept(this);

	protected final LinkedHashSet<NerdSketch.NerdSketchMouseListener> MOUSE_LISTENERS = new LinkedHashSet<>(1);
	protected final LinkedHashSet<NerdSketch.NerdSketchTouchListener> TOUCH_LISTENERS = new LinkedHashSet<>(1);
	protected final LinkedHashSet<NerdSketch.NerdSketchWindowListener> WINDOW_LISTENERS = new LinkedHashSet<>(1);
	protected final LinkedHashSet<NerdSketch.NerdSketchKeyboardListener> KEYBOARD_LISTENERS = new LinkedHashSet<>(1);
	// ...to remove!:
	protected final HashSet<Consumer<NerdSketch>> CALLBACK_LISTENERS_TO_REMOVE = new HashSet<>(1);

	// Adding a new callbacks list to `Sketch`, or a subclass? REGISTER IT IN THIS!:
	protected final Collection<?>[] LIST_OF_CALLBACK_LISTS;
	// See the end of the constructor!

	protected final LinkedHashSet<Consumer<NerdSketch>> DRAW_LISTENERS, PRE_DRAW_LISTENERS, POST_DRAW_LISTENERS;
	protected final LinkedHashSet<Consumer<NerdSketch>> SETTINGS_LISTENERS, SETUP_LISTENERS;
	protected final LinkedHashSet<Consumer<NerdSketch>> EXIT_LISTENERS, DISPOSAL_LISTENERS;
	protected final LinkedHashSet<Consumer<NerdSketch>> PRE_LISTENERS, POST_LISTENERS;
	// endregion
	// endregion

	// region Construction, `settings()`...
	public NerdSketch(final NerdSketchBuilderSettings p_key) {
		Objects.requireNonNull(p_key, "Please use a `SketchKey` or `CustomSketchBuilder` to make a `Sketch`!");

		// region Key settings.
		// region Listeners!...
		this.SETTINGS_LISTENERS = p_key.settingsListeners;
		this.SETUP_LISTENERS = p_key.setupListeners;

		this.PRE_LISTENERS = p_key.preListeners;

		this.PRE_DRAW_LISTENERS = p_key.preDrawListeners;
		this.DRAW_LISTENERS = p_key.drawListeners;
		this.POST_DRAW_LISTENERS = p_key.postDrawListeners;

		this.POST_LISTENERS = p_key.postListeners;

		this.EXIT_LISTENERS = p_key.exitListeners;
		this.DISPOSAL_LISTENERS = p_key.disposalListeners;
		// endregion

		this.EXTENSIONS = p_key.nerdExtensions;
		this.SKETCH_SETTINGS = new NerdSketch.NerdSketchSettings(p_key);
		// endregion

		// region Non-key settings.
		this.INPUT = new NerdInputManager(this);
		this.UNPROJECTOR = new NerdUnprojector();
		this.ASSETS = new NerdAssetManager(this) {
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
		};

		this.DISPLAYS = new NerdDisplayManager(this);
		this.WINDOW = NerdWindowManager.createWindowMan(this);
		this.SCENES = new NerdBridgedSceneManager(
				this, p_key.sceneManagerSettings, p_key.sceneChangeListeners, p_key.ecsSystemOrder);
		// endregion

		// region Setting OpenGL renderer icons.
		if (PConstants.P2D.equals(this.SKETCH_SETTINGS.RENDERER_NAME)
				|| PConstants.P3D.equals(this.SKETCH_SETTINGS.RENDERER_NAME))
			PJOGL.setIcon(this.SKETCH_SETTINGS.ICON_PATH);
		// endregion

		// region Loading the string table.
		NerdStringTable loadedTable = null;

		try {
			loadedTable = new NerdStringTable(p_key.stringTablePath);
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			this.STRINGS = loadedTable;
		}
		// endregion

		// region Preloading assets from scenes we want to!
		for (final Class<? extends NerdScene> c : p_key.scenesToPreload)
			this.SCENES.loadSceneAssetsAsync(c);
		// endregion

		// region Setting up `this.ROBOT`.
		Robot toAssign = null;

		try {
			toAssign = new Robot();
			toAssign.setAutoWaitForIdle(false);
		} catch (final AWTException e) {
			e.printStackTrace();
		}

		this.ROBOT = toAssign;
		// endregion

		this.LIST_OF_CALLBACK_LISTS = new Collection<?>[] {
				this.DRAW_LISTENERS, this.PRE_DRAW_LISTENERS, this.POST_DRAW_LISTENERS,
				this.SETTINGS_LISTENERS, this.SETUP_LISTENERS, this.EXIT_LISTENERS, this.DISPOSAL_LISTENERS,
				this.PRE_LISTENERS, this.POST_LISTENERS, };

		p_key.sketchConstructedListeners.forEach(this.DEFAULT_CALLBACK_ITR_LAMBDA);
	}

	@Override
	public void settings() {
		// Crazy effects (no un-decoration!), I guess:
		// if (this.STARTED_FULLSCREEN)
		// super.fullScreen(this.RENDERER);
		// else

		super.smooth(this.SKETCH_SETTINGS.ANTI_ALIASING);
		super.size(this.SKETCH_SETTINGS.INIT_WIDTH, this.SKETCH_SETTINGS.INIT_HEIGHT,
				this.SKETCH_SETTINGS.RENDERER_NAME);

		for (final Consumer<NerdSketch> c : this.SETTINGS_LISTENERS)
			if (c != null)
				c.accept(this);
	}
	// endregion

	// region Processing sketch workflow.
	@Override
	public void setup() {
		this.iconImage = super.loadImage(this.SKETCH_SETTINGS.ICON_PATH);

		this.WINDOW.width = super.width;
		this.WINDOW.height = super.height;

		this.DISPLAYS.displayWidth = super.displayWidth;
		this.DISPLAYS.displayHeight = super.displayHeight;

		this.DISPLAYS.updateDisplayRatios();
		this.WINDOW.updateWindowRatios();
		this.WINDOW.init();

		super.surface.setTitle(this.SKETCH_SETTINGS.NAME);
		super.registerMethod("pre", this);
		super.registerMethod("post", this);
		super.frameRate(this.DEFAULT_REFRESH_RATE);

		this.nerdGraphics = new NerdGraphics(this, super.getGraphics());
		this.defaultFont = super.createFont("SansSerif", this.WINDOW.scr * 72);

		// ...Also makes these changes to `NerdSketch::nerdGraphics`, haha:
		super.textFont(this.defaultFont);
		super.rectMode(PConstants.CENTER);
		super.imageMode(PConstants.CENTER);
		super.textAlign(PConstants.CENTER, PConstants.CENTER);

		// I should make a super slow "convenience" method to perform this
		// `switch (this.SKETCH_SETTINGS.RENDeRER_NAME)` using `Runnable`s!
		// :joy:!

		// Renderer-specific object initialization and settings!:
		switch (this.SKETCH_SETTINGS.RENDERER_NAME) {
			case PConstants.P2D, PConstants.P3D -> {
				this.glWindow = (GLWindow) this.WINDOW.getNativeObject();
				this.glGraphics = (PGraphicsOpenGL) super.getGraphics();
				this.gl = this.glWindow.getGL();
				this.glu = new GLU();

				if (this.SKETCH_SETTINGS.INITIALLY_RESIZABLE)
					this.glWindow.setResizable(true);

				// Done in the constructor! `setup()`'s too late for this!:
				// PJOGL.setIcon(this.iconPath);
			}

			case PConstants.JAVA2D -> this.sketchFrame = (JFrame) this.WINDOW.getNativeObject();
		}

		this.SETUP_LISTENERS.forEach(this.DEFAULT_CALLBACK_ITR_LAMBDA);
	}

	public void pre() {
		if (this.SKETCH_SETTINGS.USES_OPENGL)
			this.pgl = super.beginPGL();

		// Cheap removal strategy, LOL. I'm fed of boilerplate!:
		for (final Collection<?> c : this.LIST_OF_CALLBACK_LISTS)
			// ..Don't use `HashSet::contains()` to check here. Ugh.
			c.removeAll(this.CALLBACK_LISTENERS_TO_REMOVE);

		this.INPUT.preCallback();
		this.WINDOW.preCallback(this.WINDOW_LISTENERS);
		this.DISPLAYS.preCallback(this.WINDOW_LISTENERS);

		this.PRE_LISTENERS.forEach(this.DEFAULT_CALLBACK_ITR_LAMBDA);
		this.SCENES.runPre();
	}

	@Override
	public void draw() {
		this.pframeTime = this.frameStartTime;
		this.frameStartTime = super.millis(); // Timestamp.
		this.frameTime = this.frameStartTime - this.pframeTime;

		// Call all pre-render listeners:
		this.PRE_DRAW_LISTENERS.forEach(this.DEFAULT_CALLBACK_ITR_LAMBDA);

		this.nerdGraphics.applyCameraIfCan();

		// Call all draw listeners:
		this.DRAW_LISTENERS.forEach(this.DEFAULT_CALLBACK_ITR_LAMBDA);
		this.SCENES.runDraw();

		// region If it doesn't yet exist, construct the scene!
		if (super.frameCount == 1 && this.SCENES.getCurrentScene() == null) {
			if (this.SKETCH_SETTINGS.FIRST_SCENE_CLASS == null) {
				System.err.println("There is no initial `NerdScene` to show!");
				// System.exit(0);
			} else
				this.SCENES.startScene(this.SKETCH_SETTINGS.FIRST_SCENE_CLASS);
		}
		// endregion

		// Call all post-render listeners:
		this.POST_DRAW_LISTENERS.forEach(this.DEFAULT_CALLBACK_ITR_LAMBDA);
	}

	public void post() {
		// this.sceneGraphics.endDraw();
		// // if (this.sceneGraphics.hasRendered())
		// this.image(this.sceneGraphics.getUnderlyingBuffer());
		// this.sceneGraphics.beginDraw();

		// These help complete background work:
		this.INPUT.postCallback();
		this.WINDOW.postCallback(this.WINDOW_LISTENERS);
		this.POST_LISTENERS.forEach(this.DEFAULT_CALLBACK_ITR_LAMBDA);
		this.SCENES.runPost();

		// THIS ACTUALLY WORKED! How does the JVM interpret pointers?:
		// this.image(this.sceneGraphics);

		// ...Because apparently Processing allows rendering here!:
		if (this.SKETCH_SETTINGS.USES_OPENGL)
			super.endPGL();
	}

	@Override
	public void exit() {
		this.EXIT_LISTENERS.forEach(this.DEFAULT_CALLBACK_ITR_LAMBDA);
		this.SCENES.runExit();
		super.exit();
	}

	@Override
	public void dispose() {
		this.DISPOSAL_LISTENERS.forEach(this.DEFAULT_CALLBACK_ITR_LAMBDA);
		this.SCENES.runDispose();
		super.dispose();
	}
	// endregion

	// region Processing's event callbacks! REMEMBER `this.sceneMan`! :joy:
	// region Mouse events.
	@Override
	public void mousePressed() {
		this.INPUT.mousePressed();
		this.MOUSE_LISTENERS.forEach(NerdSketch.NerdSketchMouseListener::mousePressed);
		this.SCENES.mousePressed();
	}

	@Override
	public void mouseReleased() {
		this.INPUT.mouseReleased();
		this.MOUSE_LISTENERS.forEach(NerdSketch.NerdSketchMouseListener::mouseReleased);
		this.SCENES.mouseReleased();
	}

	@Override
	public void mouseMoved() {
		this.INPUT.mouseMoved();
		this.MOUSE_LISTENERS.forEach(NerdSketch.NerdSketchMouseListener::mouseMoved);
		this.SCENES.mouseMoved();
	}

	// JUST SO YA' KNOW!: On Android, `mouseClicked()` has been left unused.
	// AND, AND, ALSO!: On PC, it's called after `mouseReleased()`, AWT docs say.
	@Override
	public void mouseClicked() {
		this.INPUT.mouseClicked();
		this.MOUSE_LISTENERS.forEach(NerdSketch.NerdSketchMouseListener::mouseClicked);
		this.SCENES.mouseClicked();
	}

	@Override
	public void mouseDragged() {
		this.INPUT.mouseDragged();
		this.MOUSE_LISTENERS.forEach(NerdSketch.NerdSketchMouseListener::mouseDragged);
		this.SCENES.mouseDragged();
	}

	@Override
	public void mouseWheel(final processing.event.MouseEvent p_mouseEvent) {
		this.INPUT.mouseWheel(p_mouseEvent);
		this.MOUSE_LISTENERS.forEach(l -> l.mouseWheel(p_mouseEvent));
		this.SCENES.mouseWheel(p_mouseEvent);
	}
	// endregion

	// region Keyboard events.
	@Override
	public void keyTyped() {
		this.INPUT.keyTyped();

		for (final NerdSketchKeyboardListener l : this.KEYBOARD_LISTENERS)
			// ...could call that callback here directly, but I decided this!:
			// "Filter these keys using the utility method[s]?"
			//
			// ...and thus-this check was born!:
			if (NerdInputManager.isTypeable(super.key))
				l.keyTyped();

		this.SCENES.keyTyped();
	}

	@Override
	public void keyPressed() {
		if (!this.SKETCH_SETTINGS.closeOnEscape && this.keyCode == 27)
			this.key = '\0'; // Processing checks this to know what key was pressed.
		// By setting it to `\0`, we disallow exiting.

		if (this.SKETCH_SETTINGS.canFullscreen) {
			if (this.SKETCH_SETTINGS.altEnterFullscreen
					&& super.keyCode == KeyEvent.VK_ENTER
					&& this.INPUT.anyGivenKeyIsPressed(KeyEvent.VK_ALT, 19))
				this.WINDOW.fullscreen = !this.WINDOW.fullscreen;
			else if (this.SKETCH_SETTINGS.f11Fullscreen)
				switch (this.SKETCH_SETTINGS.RENDERER_NAME) {
					case PConstants.P2D, PConstants.P3D -> {
						if (super.keyCode == 107) // `KeyEvent.VK_ADD` is `107`, but here, it's `F11`!
							this.WINDOW.fullscreen = !this.WINDOW.fullscreen;
					}
					case PConstants.JAVA2D -> {
						if (super.keyCode == KeyEvent.VK_F11)
							this.WINDOW.fullscreen = !this.WINDOW.fullscreen;
					}
				}
		}

		this.INPUT.keyPressed();
		this.SCENES.keyPressed();
	}

	@Override
	public void keyReleased() {
		this.INPUT.keyReleased();
		this.KEYBOARD_LISTENERS.forEach(NerdSketch.NerdSketchKeyboardListener::keyReleased);
		this.SCENES.keyReleased();
	}
	// endregion

	// region Touch events.
	public void touchStarted() {
		for (final NerdSketchTouchListener l : this.TOUCH_LISTENERS)
			l.touchStarted();

		this.SCENES.touchStarted();
	}

	public void touchMoved() {
		for (final NerdSketchTouchListener l : this.TOUCH_LISTENERS)
			l.touchMoved();

		this.SCENES.touchMoved();
	}

	public void touchEnded() {
		for (final NerdSketchTouchListener l : this.TOUCH_LISTENERS)
			l.touchEnded();

		this.SCENES.touchEnded();
	}
	// endregion

	// region Window focus events.
	@Override
	public void focusGained() {
		// For compatibility with newer versions of Processing, I guess:
		super.focusGained();
		super.focused = true;
		this.WINDOW.focusGained();

		// I guess this works because `looping` is `false` for sometime after
		// `handleDraw()`, which is probably when events are handled:
		if (!super.isLooping())
			this.WINDOW_LISTENERS.forEach(NerdSketch.NerdSketchWindowListener::focusGained);

		this.SCENES.focusGained();
	}

	@Override
	public void focusLost() {
		// For compatibility with newer versions of Processing, I guess:
		super.focusLost();
		super.focused = false;
		this.WINDOW.focusLost();

		// I guess this works because `looping` is `false` for sometime after
		// `handleDraw()`, which is probably when events are handled:
		if (!super.isLooping())
			this.WINDOW_LISTENERS.forEach(NerdSketch.NerdSketchWindowListener::focusLost);

		this.SCENES.focusLost();
	}
	// endregion
	// endregion

	// region Persistent asset operations.
	public void reloadGivenPersistentAsset(final NerdAsset p_asset) {
		this.ASSETS.remove(p_asset);
		this.ASSETS.addAsset(p_asset.getLoader());
	}

	public void reloadPersistentAssets() {
		final NerdAsset[] assets = (NerdAsset[]) this.ASSETS.toArray();
		this.ASSETS.clear();

		for (final NerdAsset a : assets)
			this.ASSETS.addAsset(a.getLoader());
	}
	// endregion

	// region Callback and extension management.
	// region Adding listeners.
	public NerdSketch addPreListener(final Consumer<NerdSketch> p_preListener) {
		this.PRE_LISTENERS.add(Objects.requireNonNull(
				p_preListener, NerdSketch.NULL_LISTENER_ERROR_MESSAGE));
		return this;
	}

	public NerdSketch addPostListener(final Consumer<NerdSketch> p_postListener) {
		this.POST_LISTENERS.add(Objects.requireNonNull(
				p_postListener, NerdSketch.NULL_LISTENER_ERROR_MESSAGE));
		return this;
	}

	public NerdSketch addPreDrawListener(final Consumer<NerdSketch> p_preDrawListener) {
		this.PRE_DRAW_LISTENERS.add(Objects.requireNonNull(
				p_preDrawListener, NerdSketch.NULL_LISTENER_ERROR_MESSAGE));
		return this;
	}

	public NerdSketch addDrawListener(final Consumer<NerdSketch> p_drawListener) {
		this.DRAW_LISTENERS.add(Objects.requireNonNull(
				p_drawListener, NerdSketch.NULL_LISTENER_ERROR_MESSAGE));
		return this;
	}

	public NerdSketch addPostDrawListener(final Consumer<NerdSketch> p_postDrawListener) {
		this.POST_DRAW_LISTENERS.add(Objects.requireNonNull(
				p_postDrawListener, NerdSketch.NULL_LISTENER_ERROR_MESSAGE));
		return this;
	}

	public NerdSketch addSketchExitListener(final Consumer<NerdSketch> p_exitListener) {
		this.EXIT_LISTENERS.add(Objects.requireNonNull(
				p_exitListener, NerdSketch.NULL_LISTENER_ERROR_MESSAGE));
		return this;
	}

	public NerdSketch addSketchDisposalListener(final Consumer<NerdSketch> p_disposalListener) {
		this.DISPOSAL_LISTENERS.add(Objects.requireNonNull(
				p_disposalListener, NerdSketch.NULL_LISTENER_ERROR_MESSAGE));
		return this;
	}
	// endregion

	// region Removing listeners.
	// Don't need all of these, but still will have them around in case internal
	// workings change!
	public void removePreListener(final Consumer<NerdSketch> p_listener) {
		this.CALLBACK_LISTENERS_TO_REMOVE.add(p_listener);
	}

	public void removePostListener(final Consumer<NerdSketch> p_listener) {
		this.CALLBACK_LISTENERS_TO_REMOVE.add(p_listener);
	}

	public void removePreDrawListener(final Consumer<NerdSketch> p_listener) {
		this.CALLBACK_LISTENERS_TO_REMOVE.add(p_listener);
	}

	public void removeDrawListener(final Consumer<NerdSketch> p_listener) {
		this.CALLBACK_LISTENERS_TO_REMOVE.add(p_listener);
	}

	public void removePostDrawListener(final Consumer<NerdSketch> p_listener) {
		this.CALLBACK_LISTENERS_TO_REMOVE.add(p_listener);
	}

	public void removeSketchExitListener(final Consumer<NerdSketch> p_listener) {
		this.CALLBACK_LISTENERS_TO_REMOVE.add(p_listener);
	}

	public void removeSketchDisposalListener(final Consumer<NerdSketch> p_listener) {
		this.CALLBACK_LISTENERS_TO_REMOVE.add(p_listener);
	}
	// endregion

	@SuppressWarnings("unchecked")
	public <RetT> RetT getNerdExt(final String p_extName) {
		return (RetT) this.EXTENSIONS.get(p_extName);
	}
	// endregion

	// region Utilities!~
	// region Ah yes, GETTERS AND SETTERS. Even here!:
	// protected NerdBasicCamera getDefaultCameraByRef() {
	// return this.defaultCamera;
	// }

	public int getFrameStartTime() {
		return this.frameStartTime;
	}

	public int getFrameTime() {
		return this.frameTime;
	}

	public int getPframeTime() {
		return this.pframeTime;
	}

	public PImage getIconImage() {
		return this.iconImage;
	}

	public PFont getDefaultFont() {
		return this.defaultFont;
	}

	public NerdGraphics getNerdGraphics() {
		return this.nerdGraphics;
	}

	public NerdSceneManager getSceneManager() {
		return this.SCENES; // Actually a `NerdBridgedSceneManager`!
	}

	public NerdWindowManager getWindowManager() {
		return this.WINDOW;
	}

	public NerdDisplayManager getDisplayManager() {
		return this.DISPLAYS;
	}
	// endregion

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

	// region 2D rendering.
	/**
	 * Pushes the graphics buffer, disables depth testing and resets all current
	 * transformations (they're restored by a call to `Sketch::pop()` later!).
	 */
	public void begin2d() {
		this.push();
		super.hint(PConstants.DISABLE_DEPTH_TEST);
		super.perspective();
		super.camera();
	}

	/**
	 * Pops back transformations and enables depth testing.
	 */
	public void end2d() {
		super.hint(PConstants.ENABLE_DEPTH_TEST);
		this.pop(); // #JIT_FTW!
	}

	/**
	 * Pushes the graphics buffer, disables depth testing, resets all current
	 * transformations, calls your {@link Runnable} {@code p_toDraw}, and
	 * finally, pops back the transformations and enables depth testing!
	 * 
	 * @see {@link NerdSketch#end2d()}
	 * @see {@link NerdSketch#begin2d()}
	 */
	public void in2d(final Runnable p_toDraw) {
		// #JIT_FTW!
		this.begin2d();
		p_toDraw.run();
		this.end2d();
	}
	// endregion

	// region `Sketch::alphaBg()` overloads.
	public void alphaBg(final int p_color) {
		this.begin2d();
		super.fill(p_color);
		this.alphaBgImplRect();
	}

	public void alphaBg(final int p_color, final float p_alpha) {
		this.begin2d();
		super.fill(p_color, p_alpha);
		this.alphaBgImplRect();
	}

	public void alphaBg(final float p_grey, final float p_alpha) {
		this.begin2d();
		super.fill(p_grey, p_alpha);
		this.alphaBgImplRect();
	}

	public void alphaBg(final float p_v1, final float p_v2, final float p_v3) {
		this.begin2d();
		super.fill(p_v1, p_v2, p_v3);
		this.alphaBgImplRect();
	}

	public void alphaBg(final float p_v1, final float p_v2, final float p_v3, final float p_alpha) {
		this.begin2d();
		super.fill(p_v1, p_v2, p_v3, p_alpha);
		this.alphaBgImplRect();
	}

	protected void alphaBgImplRect() {
		// Removing this will not display the previous camera's view,
		// but still show clipping:
		super.camera();
		super.noStroke();
		super.rectMode(PConstants.CORNER);
		super.rect(0, 0, super.width, super.height);
		this.end2d();
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
		final int PATH_LEN = p_path.length(), LAST_CHAR_ID = PATH_LEN - 1;
		final StringBuilder toRetBuilder = new StringBuilder();

		if (p_path.charAt(LAST_CHAR_ID) != File.separatorChar)
			toRetBuilder.append(File.separator);

		for (int i = 0; i < PATH_LEN; i++) {
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

	// region Start a `JAVA2D` sketch with an undecorated window.
	public JFrame createSketchPanel(final Runnable p_exitTask, final NerdSketch p_sketch,
			final PGraphics p_sketchGraphics) {
		// This is what `PApplet::frame` used to contain:
		super.frame = null;
		final JFrame toRet = (JFrame) ((PSurfaceAWT.SmoothCanvas) p_sketch
				.getSurface().getNative()).getFrame();

		// region More stuff wth the `JFrame` (such as adding a `JPanel`!).
		toRet.removeNotify();
		toRet.setUndecorated(true);
		toRet.setLayout(null);
		toRet.addNotify();

		toRet.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent p_event) {
				System.out.println("Window closing...");
				p_sketch.exit();
			}
		});

		// region The `JPanel`, and input-event handling.
		final JPanel panel = new JPanel() {
			@Override
			protected void paintComponent(final Graphics p_javaGaphics) {
				if (p_javaGaphics instanceof Graphics2D) {
					((Graphics2D) p_javaGaphics).drawImage(
							p_sketchGraphics.image, 0, 0, null);
				}
			}
		};

		// Let the `JFrame` be visible and request for `OS` permissions:
		toRet.setContentPane(panel); // This is the dummy variable from Processing.
		panel.setFocusable(true);
		panel.setFocusTraversalKeysEnabled(false);
		panel.requestFocus();
		panel.requestFocusInWindow();

		// region Listeners for handling events :+1::
		// Listener for `PApplet::mousePressed()`, `PApplet::mouseReleased()`
		// and `PApplet::mouseClicked()`:
		panel.addMouseListener(new MouseListener() {
			@Override
			public void mousePressed(final MouseEvent p_mouseEvent) {
				p_sketch.updateSketchMouse();
				p_sketch.mousePressed = true;
				p_sketch.mouseButton = p_mouseEvent.getButton();
				p_sketch.mousePressed();
			}

			@Override
			public void mouseReleased(final MouseEvent p_mouseEvent) {
				p_sketch.updateSketchMouse();
				p_sketch.mousePressed = false;
				p_sketch.mouseButton = p_mouseEvent.getButton();
				p_sketch.mouseReleased();
			}

			@Override
			public void mouseClicked(final MouseEvent p_mouseEvent) {
				p_sketch.updateSketchMouse();
				p_sketch.mouseButton = p_mouseEvent.getButton();
				p_sketch.mouseClicked();
			}

			@Override
			public void mouseEntered(final MouseEvent p_mouseEvent) {
				p_sketch.focused = true;
			}

			@Override
			public void mouseExited(final MouseEvent p_mouseEvent) {
				p_sketch.focused = false;
			}
		});

		// Listener for `PApplet::mouseDragged()` and `PApplet::mouseMoved()`:
		panel.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseDragged(final MouseEvent p_mouseEvent) {
				p_sketch.updateSketchMouse();
				p_sketch.mouseDragged();
			}

			@Override
			public void mouseMoved(final MouseEvent p_mouseEvent) {
				p_sketch.updateSketchMouse();
				p_sketch.mouseMoved();
			}
		});

		// Listener for `PApplet::mouseWheel()`:
		panel.addMouseWheelListener(new MouseWheelListener() {
			@Override
			@SuppressWarnings("deprecation") // `deprecation` and not `deprecated`!
			public void mouseWheelMoved(final MouseWheelEvent p_mouseEvent) {
				p_sketch.mouseEvent = new processing.event.MouseEvent(
						p_mouseEvent, System.currentTimeMillis(),
						processing.event.MouseEvent.CLICK,
						p_mouseEvent.getModifiersEx(),
						p_mouseEvent.getX(),
						p_mouseEvent.getY(),
						p_mouseEvent.getButton(),
						p_mouseEvent.getClickCount());
				p_sketch.mouseWheel(p_sketch.mouseEvent);
			}
		});

		// Listener for `PApplet::keyPressed()`, `PApplet::keyReleased()`
		// and `PApplet::keyTyped()`:
		panel.addKeyListener(new KeyAdapter() {
			protected boolean sketchExited;

			@Override
			public void keyTyped(final KeyEvent p_keyEvent) {
				p_sketch.key = p_keyEvent.getKeyChar();
				p_sketch.keyCode = p_keyEvent.getKeyCode();
				p_sketch.keyTyped();
			}

			@Override
			public void keyPressed(final KeyEvent p_keyEvent) {
				// Handle `Alt + F4` closes ourselves!:

				if (!(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, InputEvent.ALT_DOWN_MASK) == null
						&& p_sketch.exitCalled())
						&& p_keyEvent.getKeyCode() == KeyEvent.VK_F4) {
					if (!p_sketch.exitCalled()) {
						if (!this.sketchExited)
							p_exitTask.run();
						this.sketchExited = true;
						p_keyEvent.consume();
					}
				}

				p_sketch.key = p_keyEvent.getKeyChar();
				p_sketch.keyCode = p_keyEvent.getKeyCode();
				// System.out.println("Heard a keypress!");
				p_sketch.keyPressed();
			}

			@Override
			public void keyReleased(final KeyEvent p_keyEvent) {
				p_sketch.key = p_keyEvent.getKeyChar();
				p_sketch.keyCode = p_keyEvent.getKeyCode();
				p_sketch.keyReleased();
			}

		});
		// endregion
		// endregion
		// endregion

		return toRet;
	}

	// Used by `Sketch::createSketchPanel()`:
	// ~~Should've made a method-class for this.~~
	protected void updateSketchMouse() {
		final Point mousePoint = MouseInfo.getPointerInfo().getLocation(),
				sketchFramePoint = this.sketchFrame.getLocation();
		super.mouseX = mousePoint.x - sketchFramePoint.x;
		super.mouseY = mousePoint.y - sketchFramePoint.y;
	}
	// endregion
	// endregion

}
