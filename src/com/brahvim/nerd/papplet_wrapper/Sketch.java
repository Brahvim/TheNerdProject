package com.brahvim.nerd.papplet_wrapper;

import java.awt.AWTException;
import java.awt.DisplayMode;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.function.Consumer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import com.brahvim.nerd.io.StringTable;
import com.brahvim.nerd.math.Unprojector;
import com.brahvim.nerd.rendering.cameras.BasicCamera;
import com.brahvim.nerd.rendering.cameras.BasicCameraBuilder;
import com.brahvim.nerd.rendering.cameras.FlyCamera;
import com.brahvim.nerd.rendering.cameras.NerdAbstractCamera;
import com.brahvim.nerd.scene_api.NerdLayer;
import com.brahvim.nerd.scene_api.NerdScene;
import com.brahvim.nerd.scene_api.SceneManager;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.glu.GLU;

import processing.awt.PSurfaceAWT;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PShape;
import processing.core.PVector;
import processing.opengl.PGL;
import processing.opengl.PGraphics3D;
import processing.opengl.PGraphicsOpenGL;
import processing.opengl.PJOGL;

public class Sketch extends PApplet {

	// region Abstract inner classes.
	// Used abstract classes instead of interfaces for these (two) reasons:
	/*
	 * - No security for `ALL_REFERENCES` from the user! It'll be `public`!
	 * - For registering the reference into the `ALL_REFERENCES` collection,
	 * a `default` method may be used. However, the method is overriable,
	 * meaning that registering code becomes modifiable, letting the user
	 * accidentally change what they weren't supposed to!
	 */

	// region Input listeners.
	public /* `abstract` */ class SketchMouseListener {

		public SketchMouseListener() {
			Sketch.this.SKETCH.MOUSE_LISTENERS.add(this);
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

	public /* `abstract` */ class SketchTouchListener {

		public SketchTouchListener() {
			Sketch.this.SKETCH.TOUCH_LISTENERS.add(this);
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

	public /* `abstract` */ class SketchDisplayListener {

		public SketchDisplayListener() {
			Sketch.this.SKETCH.WINDOW_LISTENERS.add(this);
		}

		// region Window focus events.
		public void resized() {
		}

		public void focusLost() {
		}

		public void focusGained() {
		}
		// endregion

		public void monitorChanged() {
		}

	}

	public /* `abstract` */ class SketchKeyboardListener {

		public SketchKeyboardListener() {
			Sketch.this.SKETCH.KEYBOARD_LISTENERS.add(this);
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
	// endregion

	// region `public` fields.
	// region Constants.
	// region `static` constants.
	public static final File EXEC_DIR = new File("");
	public static final String EXEC_DIR_PATH = Sketch.EXEC_DIR.getAbsolutePath().concat(File.separator);

	public static final File DATA_DIR = new File("data");
	public static final String DATA_DIR_PATH = Sketch.DATA_DIR.getAbsolutePath().concat(File.separator);
	// public static final String DATA_DIR_PATH_TO_DRIVE_ROOT_SUFFIX =
	// Sketch.getPathToRootFrom(Sketch.DATA_DIR_PATH);

	public static final char[] STANDARD_KEYBOARD_SYMBOLS = {
			'\'', '\"', '-', '=', '`', '~', '!', '@', '#', '$',
			'%', '^', '&', '*', '(', ')', '{', '}', '[',
			']', ';', ',', '.', '/', '\\', ':', '|', '<',
			'>', '_', '+', '?'
	};

	// region `java.awt` constants.
	public static final GraphicsEnvironment LOCAL_GRAPHICS_ENVIRONMENT = GraphicsEnvironment
			.getLocalGraphicsEnvironment();

	public static GraphicsDevice[] JAVA_SCREENS = Sketch.LOCAL_GRAPHICS_ENVIRONMENT
			.getScreenDevices();

	public static GraphicsDevice DEFAULT_JAVA_SCREEN = Sketch.LOCAL_GRAPHICS_ENVIRONMENT
			.getDefaultScreenDevice();

	public static DisplayMode DEFAULT_JAVA_SCREEN_MODE = Sketch.DEFAULT_JAVA_SCREEN
			.getDisplayMode();

	public static int DEFAULT_REFRESH_RATE = Sketch.DEFAULT_JAVA_SCREEN_MODE
			.getRefreshRate();
	// endregion
	// endregion

	// region Instance constants.
	public final String NAME;
	public final Robot ROBOT;
	public final String RENDERER;
	public final String ICON_PATH;
	public final boolean USES_OPENGL;
	public final StringTable STRINGS;
	public final HashMap<String, Object> EXTENSIONS;
	public final Class<? extends NerdScene> FIRST_SCENE_CLASS;

	// Dimensions of the inital size of the window:
	public final float INIT_SCR;
	public final int INIT_WIDTH, INIT_HEIGHT;
	public final int INIT_WIDTH_HALF, INIT_HEIGHT_HALF;
	public final int INIT_WIDTH_QUART, INIT_HEIGHT_QUART;

	/** Position of the mouse relative to the monitor. */
	public final Point GLOBAL_MOUSE_POINT = new Point(),
			PREV_GLOBAL_MOUSE_POINT = new Point();

	/** Position of the mouse relative to the monitor. */
	public final PVector GLOBAL_MOUSE_VECTOR = new PVector();
	public final PVector PREV_GLOBAL_MOUSE_VECTOR = new PVector();

	/** Position of the window relative to the monitor. */
	public final PVector WINDOW_POSITION = new PVector(),
			PREV_WINDOW_POSITION = new PVector();

	/** Certain setting for the sketch. */
	public final boolean CLOSE_ON_ESCAPE, STARTED_FULLSCREEN, INITIALLY_RESIZABLE,
			CAN_FULLSCREEN, F11_FULLSCREEN, ALT_ENTER_FULLSCREEN;

	private final Sketch SKETCH = this;
	// endregion
	// endregion

	// region App workflow callbacks scene-or-layer order.
	/**
	 * Dictates to every {@link Sketch} instance, the order in which a
	 * {@link NerdScene} or {@link NerdLayer} is allowed to call certain "workflow
	 * events" ({@code pre()}, {@code draw()} and {@code post()}) from Processing
	 * 
	 * @see {@link Sketch#PRE_FIRST_CALLER} - {@link CallbackOrder#SCENE} by
	 *      default.
	 * @see {@link Sketch#DRAW_FIRST_CALLER} - {@link CallbackOrder#LAYER} by
	 *      default.
	 * @see {@link Sketch#POST_FIRST_CALLER} - {@link CallbackOrder#LAYER} by
	 *      default.
	 */
	public static enum CallbackOrder {
		SCENE(), LAYER();
	}

	/**
	 * Controls whether {@link NerdScene#pre()} or {@link NerdLayer#pre()} is
	 * called first by the sketch. {@link Sketch.CallbackOrder#SCENE} by default.
	 */
	public CallbackOrder PRE_FIRST_CALLER = CallbackOrder.SCENE;

	/**
	 * Controls whether {@link NerdScene#draw()} or {@link NerdLayer#draw()} is
	 * called first by the sketch. {@link Sketch.CallbackOrder#LAYER} by default.
	 */
	public CallbackOrder DRAW_FIRST_CALLER = CallbackOrder.LAYER;

	/**
	 * Controls whether {@link NerdScene#post()} or {@link NerdLayer#post()} is
	 * called first by the sketch. {@link Sketch.CallbackOrder#LAYER} by default.
	 */
	public CallbackOrder POST_FIRST_CALLER = CallbackOrder.LAYER;
	// endregion

	// region Window object and native renderer references ("hacky stuff").
	// (Why check for errors at all? You know what renderer you used!)
	public JFrame sketchFrame;

	// OpenGL context:
	public GL gl;
	public GLU glu;
	public PGL pgl;
	public GLWindow glWindow;
	public PGraphicsOpenGL glGraphics;
	// endregion

	// region Frame-wise states, Processing style (modifiable!).
	public char pkey; // Previous fraaaaame!...
	public boolean pfocused;
	public int pmouseButton, pkeyCode;
	public boolean pkeyPressed, pmousePressed;
	public final ArrayList<PVector> UNPROJ_TOUCHES = new ArrayList<>(10); // Previous frame...

	// Current frame!
	public float mouseScroll, mouseScrollDelta;
	public boolean mouseLeft, mouseMid, mouseRight;
	public final ArrayList<PVector> PREV_UNPROJ_TOUCHES = new ArrayList<>(10);

	public boolean fullscreen, pfullscreen;
	public boolean cursorConfined, cursorVisible = true; // "nO previoS versiuN!11!!"
	public boolean pcursorConfined, pcursorVisible = true; // onU previoS versiuN!11!!1!!11!
	public PVector mouse = new PVector(), pmouse = new PVector(); // MOUS!

	public boolean pmouseLeft, pmouseMid, pmouseRight; // Previous frame...
	public float pmouseScroll, pmouseScrollDelta;
	// endregion

	// region "Dimensions".
	// Time (`millis()` returns `int`!):
	public int frameStartTime, pframeTime, frameTime;

	// Display dimensions:
	public float displayScr;
	public int displayRefreshRate;
	public int displayWidthHalf, displayHeightHalf;
	public int displayWidthQuart, displayHeightQuart;
	public int displayWidthTwice, displayHeightTwice; // <-- Twice!
	public int displayWidthThirdQuart, displayHeightThirdQuart;

	// Previous frame display dimensions:
	public float pdisplayScr;
	public int pdisplayRefreshRate;
	public int pdisplayWidth, pdisplayHeight; // <-- Not included with Processing!
	public int pdisplayWidthHalf, pdisplayHeightHalf;
	public int pdisplayWidthQuart, pdisplayHeightQuart;
	public int pdisplayWidthTwice, pdisplayHeightTwice; // <-- Twice!
	public int ppixelDensity, ppixelWidth, ppixelHeight; // <-- Also not included with Processing!
	public int pdisplayWidthThirdQuart, pdisplayHeightThirdQuart;

	// Windows dimensions for the current and previous frames:
	public float pdbx, pdby, pcx, pcy, pqx, pqy, pq3x, pq3y, pscr;
	public float dbx, dby, cx, cy, qx, qy, q3x, q3y, scr;
	public int pwidth, pheight;
	// endregion
	// endregion

	// region `protected` fields.
	protected final int ANTI_ALIASING;
	protected final Unprojector UNPROJECTOR;
	// `LinkedHashSet`s preserve order (and also disallow element repetition)!
	protected final LinkedHashSet<Integer> keysHeld = new LinkedHashSet<>(5); // `final` to avoid concurrency issues.

	protected GraphicsDevice previousMonitor, currentMonitor;
	protected NerdAbstractCamera previousCamera, currentCamera; // CAMERA! (wher lite?! wher accsunn?!)
	protected BasicCamera defaultCamera;
	protected NerdScene currentScene;
	protected SceneManager sceneMan; // Don't use static initialization for this..?

	// region Callback listeners!
	protected final LinkedHashSet<SketchMouseListener> MOUSE_LISTENERS = new LinkedHashSet<>(1);
	protected final LinkedHashSet<SketchTouchListener> TOUCH_LISTENERS = new LinkedHashSet<>(1);
	protected final LinkedHashSet<SketchDisplayListener> WINDOW_LISTENERS = new LinkedHashSet<>(1);
	protected final LinkedHashSet<SketchKeyboardListener> KEYBOARD_LISTENERS = new LinkedHashSet<>(1);

	protected LinkedHashSet<Consumer<Sketch>> SETTINGS_LISTENERS, SETUP_LISTENERS;
	protected LinkedHashSet<Consumer<Sketch>> EXIT_LISTENERS, DISPOSAL_LISTENERS;

	protected LinkedHashSet<Consumer<Sketch>> PRE_LISTENERS, POST_LISTENERS;
	protected LinkedHashSet<Consumer<Sketch>> DRAW_LISTENERS, PRE_DRAW_LISTENERS, POST_DRAW_LISTENERS;
	// endregion
	// endregion

	// region Construction, `settings()`...
	public Sketch(final SketchKey p_key) {
		Objects.requireNonNull(p_key, "Please use a `SketchKey` instance to make a `Sketch`!");

		// region Key settings.
		// region Setting `Sketch.CallbackOrder`s.
		if (p_key.preCallOrder != null)
			this.PRE_FIRST_CALLER = p_key.preCallOrder;

		if (p_key.drawCallOrder != null)
			this.DRAW_FIRST_CALLER = p_key.drawCallOrder;

		if (p_key.postCallOrder != null)
			this.POST_FIRST_CALLER = p_key.postCallOrder;
		// endregion

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

		this.sceneMan = new SceneManager(this, p_key.sceneChangeListeners); // Before `Sketch::AL`!

		this.RENDERER = p_key.renderer;
		this.ICON_PATH = p_key.iconPath;
		this.EXTENSIONS = p_key.nerdExtensions;
		this.ANTI_ALIASING = p_key.antiAliasing;
		this.FIRST_SCENE_CLASS = p_key.firstScene;
		this.INITIALLY_RESIZABLE = p_key.canResize;
		this.CAN_FULLSCREEN = !p_key.cannotFullscreen;
		this.CLOSE_ON_ESCAPE = !p_key.dontCloseOnEscape;
		this.F11_FULLSCREEN = !p_key.cannotF11Fullscreen;
		this.STARTED_FULLSCREEN = p_key.startedFullscreen;
		this.NAME = p_key.name == null ? "TheNerdProject" : p_key.name;
		this.ALT_ENTER_FULLSCREEN = !p_key.cannotAltEnterFullscreen;
		// endregion

		// region Non-key settings.
		this.UNPROJECTOR = new Unprojector();
		this.fullscreen = this.STARTED_FULLSCREEN;
		this.USES_OPENGL = this.RENDERER == PConstants.P2D || this.RENDERER == PConstants.P3D;
		// endregion

		// region Setting OpenGL renderer icons.
		if (this.RENDERER == PConstants.P2D || this.RENDERER == PConstants.P3D)
			PJOGL.setIcon(this.ICON_PATH);
		// endregion

		// region Loading the string table.
		StringTable loadedTable = null;

		try {
			loadedTable = new StringTable(p_key.stringTablePath);
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			this.STRINGS = loadedTable;
		}
		// endregion

		// region Preloading assets from scenes we want to!
		for (final Class<? extends NerdScene> c : p_key.scenesToPreload) {
			this.sceneMan.loadSceneAssetsAsync(c);
		}
		// endregion

		// region Non-fullscreen window's dimensions when starting fullscreen.
		if (this.STARTED_FULLSCREEN) {
			this.INIT_WIDTH = 800;
			this.INIT_HEIGHT = 600;
		} else {
			this.INIT_WIDTH = p_key.width;
			this.INIT_HEIGHT = p_key.height;
		}

		// `INIT_WIDTH` ratios:
		this.INIT_WIDTH_HALF = this.INIT_WIDTH / 2;
		this.INIT_WIDTH_QUART = this.INIT_WIDTH / 2;

		this.INIT_HEIGHT_QUART = this.INIT_WIDTH / 4;
		this.INIT_HEIGHT_HALF = this.INIT_WIDTH / 4;

		this.INIT_SCR = (float) this.INIT_WIDTH / (float) this.INIT_HEIGHT;
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

		for (final Consumer<Sketch> c : p_key.sketchConstructedListeners)
			if (c != null)
				c.accept(this);
	}

	@Override
	public void settings() {
		// Crazy effects (no un-decoration!), I guess:
		// if (this.STARTED_FULLSCREEN)
		// super.fullScreen(this.RENDERER);
		// else

		super.smooth(this.ANTI_ALIASING);
		super.size(this.INIT_WIDTH, this.INIT_HEIGHT, this.RENDERER);

		for (final Consumer<Sketch> c : this.SETTINGS_LISTENERS)
			if (c != null)
				c.accept(this);
	}
	// endregion

	// region Processing sketch workflow.
	@Override
	public void setup() {
		// Causes a NPE in LWJGL. Yes, 'a':
		// Runtime.getRuntime().addShutdownHook(new Thread(() -> {
		// this.dispose();
		// }));

		this.updateWindowRatios();
		super.surface.setTitle(this.NAME);
		super.registerMethod("pre", this);
		super.registerMethod("post", this);
		super.frameRate(Sketch.DEFAULT_REFRESH_RATE);

		// I should make a super slow "convenience" method to perform this
		// `switch (this.RENDERER)` using `Runnable`s!
		// :joy:!

		// Renderer-specific object initialization and settings!:
		switch (this.RENDERER) {
			case PConstants.P2D, PConstants.P3D:
				this.glWindow = (GLWindow) super.surface.getNative();
				this.glGraphics = (PGraphicsOpenGL) super.g;
				this.gl = this.glWindow.getGL();
				this.glu = new GLU();

				this.defaultCamera = new BasicCameraBuilder(this).build();
				this.currentCamera = this.defaultCamera;

				if (this.INITIALLY_RESIZABLE)
					this.glWindow.setResizable(true);

				// Done in the constructor! `setup()`'s too late for this!:
				// PJOGL.setIcon(this.iconPath);
				break;

			case PConstants.JAVA2D:
				final PSurfaceAWT.SmoothCanvas canvas = (PSurfaceAWT.SmoothCanvas) super.surface.getNative();
				this.sketchFrame = (JFrame) canvas.getFrame();

				if (this.INITIALLY_RESIZABLE)
					super.surface.setResizable(true);

				super.surface.setIcon(super.loadImage(this.ICON_PATH));
				// "Loose" image loading is usually not a good idea, but I guess it's here...
				// super.surface.setIcon(super.loadImage(this.iconPath));
				break;
		}

		this.centerWindow();

		super.rectMode(PConstants.CENTER);
		super.imageMode(PConstants.CENTER);
		super.textAlign(PConstants.CENTER, PConstants.CENTER);

		for (final Consumer<Sketch> c : this.SETUP_LISTENERS)
			if (c != null)
				c.accept(this);
	}

	public void pre() {
		this.currentScene = this.sceneMan.getCurrentScene();
		this.PREV_WINDOW_POSITION.set(this.WINDOW_POSITION);
		if (this.USES_OPENGL) {
			this.pgl = super.beginPGL();
			this.WINDOW_POSITION.set(this.SKETCH.glWindow.getX(), this.SKETCH.glWindow.getY());
		} else
			this.WINDOW_POSITION.set(this.sketchFrame.getX(), this.sketchFrame.getY());

		// When the window is resized, do the following!:
		if (!(this.pwidth == super.width || this.pheight == super.height)) {
			this.updateWindowRatios();
			for (final SketchDisplayListener l : this.WINDOW_LISTENERS)
				l.resized();
		}

		// region Current and previous frame monitor settings, plus callback!.
		final GraphicsDevice[] updatedList = Sketch.LOCAL_GRAPHICS_ENVIRONMENT.getScreenDevices();
		if (Sketch.JAVA_SCREENS != updatedList) {
			Sketch.DEFAULT_JAVA_SCREEN = Sketch.LOCAL_GRAPHICS_ENVIRONMENT.getDefaultScreenDevice();
			Sketch.DEFAULT_JAVA_SCREEN_MODE = Sketch.DEFAULT_JAVA_SCREEN.getDisplayMode();
			Sketch.DEFAULT_REFRESH_RATE = Sketch.DEFAULT_JAVA_SCREEN_MODE.getRefreshRate();
		}

		if (this.previousMonitor != this.currentMonitor) {
			this.previousMonitor = this.currentMonitor;
			this.updateDisplayRatios();
			for (final SketchDisplayListener l : this.WINDOW_LISTENERS)
				l.monitorChanged();
		}

		if (super.focused)
			this.currentMonitor = Sketch.getGraphicsDeviceAt(this.GLOBAL_MOUSE_POINT);

		// Update `super.displayWidth` and `super.displayHeight`:
		if (this.currentMonitor != null) {
			final DisplayMode CURRENT_MON_MODE = this.currentMonitor.getDisplayMode();
			super.displayWidth = CURRENT_MON_MODE.getWidth();
			super.displayHeight = CURRENT_MON_MODE.getHeight();
		}
		// endregion

		this.mouseScrollDelta = this.mouseScroll - this.pmouseScroll;

		for (final Consumer<Sketch> c : this.PRE_LISTENERS)
			if (c != null)
				c.accept(this);
	}

	@Override
	public void draw() {
		this.frameStartTime = super.millis(); // Timestamp.
		this.frameTime = this.frameStartTime - this.pframeTime;
		this.pframeTime = this.frameStartTime;

		// region Call all pre-render listeners.
		for (final Consumer<Sketch> c : this.PRE_DRAW_LISTENERS)
			if (c != null)
				c.accept(this);
		// endregion

		// region Update frame-ly mouse settings.
		this.mouseRight = super.mouseButton == PConstants.RIGHT && super.mousePressed;
		this.mouseMid = super.mouseButton == PConstants.CENTER && super.mousePressed;
		this.mouseLeft = super.mouseButton == PConstants.LEFT && super.mousePressed;

		// region "`GLOBAL_MOUSE`".
		this.PREV_GLOBAL_MOUSE_POINT.setLocation(this.GLOBAL_MOUSE_POINT);
		this.PREV_GLOBAL_MOUSE_VECTOR.set(this.GLOBAL_MOUSE_VECTOR);

		this.GLOBAL_MOUSE_POINT.setLocation(MouseInfo.getPointerInfo().getLocation());
		this.GLOBAL_MOUSE_VECTOR.set(this.GLOBAL_MOUSE_POINT.x, this.GLOBAL_MOUSE_POINT.y);
		// endregion
		// endregion

		// region Apply the camera and unprojection when using OpenGL:
		// Needed by `this.unprojectMouse()`:
		this.mouse.set(super.mouseX, super.mouseY);
		if (this.RENDERER == PConstants.P3D) {
			this.unprojectMouse();

			if (this.currentCamera != null)
				this.currentCamera.apply(); // Do all three tasks!
			// If `this.currentCamera` is `null`, but wasn't,

			// else if (this.currentCamera != this.previousCamera)
			// System.out.printf(
			// "Sketch \"%s\" has no camera! Consider adding one...?", this.NAME);
		}
		// endregion

		// region Call all draw listeners.
		for (final Consumer<Sketch> c : this.DRAW_LISTENERS)
			if (c != null)
				c.accept(this);
		// endregion

		// region If it doesn't yet exist, construct the scene!
		if (super.frameCount == 1 && this.currentScene == null) {
			if (this.FIRST_SCENE_CLASS == null)
				System.err.println("There is no first `NerdScene`! It's `null`!");
			else
				this.sceneMan.startScene(this.FIRST_SCENE_CLASS);
		}
		// endregion

		// region Call all post-render listeners.
		for (final Consumer<Sketch> c : this.POST_DRAW_LISTENERS)
			if (c != null)
				c.accept(this);
		// endregion

	}

	public void post() {
		for (final Consumer<Sketch> c : this.POST_LISTENERS)
			if (c != null)
				c.accept(this);

		if (this.USES_OPENGL)
			super.endPGL();

		this.framelyWindowSetup();

		// region Previous state updates!!!
		for (final PVector v : this.UNPROJ_TOUCHES)
			this.PREV_UNPROJ_TOUCHES.add(v);

		if (this.currentScene != null)
			this.currentScene.ASSETS.updatePreviousLoadState();

		this.pkey = super.key;
		this.pwidth = this.width;
		this.pheight = this.height;
		this.pmouse.set(this.mouse);
		this.pfocused = this.focused;
		this.pkeyCode = this.keyCode;
		this.pmouseMid = this.mouseMid;
		this.pmouseLeft = this.mouseLeft;
		this.pmouseRight = this.mouseRight;
		this.pfullscreen = this.fullscreen;
		this.pkeyPressed = super.keyPressed;
		this.pmouseButton = this.mouseButton;
		this.pmouseScroll = this.mouseScroll;
		this.pmousePressed = super.mousePressed;
		this.previousCamera = this.currentCamera;
		this.pcursorVisible = this.cursorVisible;
		this.pcursorConfined = this.cursorConfined;
		this.pmouseScrollDelta = this.mouseScrollDelta;
		// endregion

	}

	@Override
	public void exit() {
		for (final Consumer<Sketch> c : this.EXIT_LISTENERS)
			if (c != null)
				c.accept(this);

		super.exit();
	}

	@Override
	public void dispose() {
		for (final Consumer<Sketch> c : this.DISPOSAL_LISTENERS)
			if (c != null)
				c.accept(this);

		super.dispose();
	}
	// endregion

	// region Processing's event callbacks! REMEMBER `this.sceneMan`! :joy:
	// region Mouse events.
	@Override
	public void mousePressed() {
		for (final SketchMouseListener l : this.MOUSE_LISTENERS) {
			l.mousePressed();
		}
	}

	@Override
	public void mouseReleased() {
		for (final SketchMouseListener l : this.MOUSE_LISTENERS) {
			l.mouseReleased();
		}
	}

	@Override
	public void mouseMoved() {
		for (final SketchMouseListener l : this.MOUSE_LISTENERS) {
			l.mouseMoved();
		}
	}

	@Override
	public void mouseClicked() {
		for (final SketchMouseListener l : this.MOUSE_LISTENERS) {
			l.mouseClicked();
		}
	}

	@Override
	public void mouseDragged() {
		for (final SketchMouseListener l : this.MOUSE_LISTENERS) {
			l.mouseDragged();
		}
	}

	// @SuppressWarnings("unused")
	@Override
	public void mouseWheel(final processing.event.MouseEvent p_mouseEvent) {
		this.mouseScroll += p_mouseEvent.getCount();
		for (final SketchMouseListener l : this.MOUSE_LISTENERS) {
			l.mouseWheel(p_mouseEvent);
		}
	}
	// endregion

	// region Keyboard events.
	@Override
	public void keyTyped() {
		for (final SketchKeyboardListener l : this.KEYBOARD_LISTENERS) {
			// ...could call that callback here directly, but I decided this!:
			// "Filter these keys using the utility method[s]?"

			// ...and thus-this check was born!:
			if (Sketch.isTypeable(super.key))
				l.keyTyped();
		}
	}

	@Override
	public void keyPressed() {
		if (!this.CLOSE_ON_ESCAPE) {
			if (super.keyCode == 27)
				super.key = ' ';
		}

		if (this.CAN_FULLSCREEN) {
			if (this.ALT_ENTER_FULLSCREEN) {
				if (super.keyCode == KeyEvent.VK_ENTER &&
						this.anyGivenKeyIsPressed(KeyEvent.VK_ALT, 19 /* Same as `VK_PAUSE`. */)) {
					// System.out.println("`Alt`-`Enter` fullscreen!");
					this.fullscreen = !this.fullscreen;
				}
			}

			if (this.F11_FULLSCREEN) {
				switch (this.RENDERER) {
					case PConstants.P2D, PConstants.P3D:
						if (super.keyCode == 107) { // `KeyEvent.VK_ADD` is `107`, but here, it's `F11`!
							// System.out.println("`F11` fullscreen!");
							this.fullscreen = !this.fullscreen;
						}
						break;

					case PConstants.JAVA2D:
						if (super.keyCode == KeyEvent.VK_F11) {
							// System.out.println("`F11` fullscreen!");
							this.fullscreen = !this.fullscreen;
						}
						break;
				}
			}
		}

		synchronized (this.keysHeld) {
			this.keysHeld.add(super.keyCode);
		}

		for (final SketchKeyboardListener l : this.KEYBOARD_LISTENERS) {
			l.keyPressed();
		}
	}

	@Override
	public void keyReleased() {
		try {
			synchronized (this.keysHeld) {
				this.keysHeld.remove(super.keyCode);
			}
		} catch (final IndexOutOfBoundsException e) {
		}

		for (final SketchKeyboardListener l : this.KEYBOARD_LISTENERS) {
			l.keyReleased();
		}
	}
	// endregion

	// region Touch events.
	public void touchStarted() {
		for (final SketchTouchListener l : this.TOUCH_LISTENERS) {
			l.touchStarted();
		}
	}

	public void touchMoved() {
		for (final SketchTouchListener l : this.TOUCH_LISTENERS) {
			l.touchMoved();
		}
	}

	public void touchEnded() {
		for (final SketchTouchListener l : this.TOUCH_LISTENERS) {
			l.touchEnded();
		}
	}
	// endregion

	// region Window focus events.
	@Override
	public void focusGained() {
		// For compatibility with newer versions of Processing, I guess:
		super.focusGained();
		super.focused = true;

		// I guess this works because `looping` is `false` for sometime after
		// `handleDraw()`,
		// which is probably when events are handled:
		if (!super.isLooping())
			for (final SketchDisplayListener l : this.WINDOW_LISTENERS) {
				l.focusGained();
			}
	}

	@Override
	public void focusLost() {
		// For compatibility with newer versions of Processing, I guess:
		super.focusLost();
		super.focused = false;

		// I guess this works because `looping` is `false` for sometime after
		// `handleDraw()`,
		// which is probably when events are handled:
		if (!super.isLooping())
			for (final SketchDisplayListener l : this.WINDOW_LISTENERS) {
				l.focusLost();
			}
	}
	// endregion
	// endregion

	// region Callback and extension management.
	// region Adding listeners.
	public void addPreListener(final Consumer<Sketch> p_preListener) {
		this.PRE_LISTENERS.add(p_preListener);
	}

	public void addPostListener(final Consumer<Sketch> p_postListener) {
		this.POST_LISTENERS.add(p_postListener);
	}

	public void addPreDrawListener(final Consumer<Sketch> p_preDrawListener) {
		this.PRE_DRAW_LISTENERS.add(p_preDrawListener);
	}

	public void addDrawListener(final Consumer<Sketch> p_drawListener) {
		this.DRAW_LISTENERS.add(p_drawListener);
	}

	public void addPostDrawListener(final Consumer<Sketch> p_postDrawListener) {
		this.POST_DRAW_LISTENERS.add(p_postDrawListener);
	}

	public void addSketchExitListener(final Consumer<Sketch> p_exitListener) {
		this.EXIT_LISTENERS.add(p_exitListener);
	}

	public void addSketchDisposalListener(final Consumer<Sketch> p_disposalListener) {
		this.DISPOSAL_LISTENERS.add(p_disposalListener);
	}
	// endregion

	// region Removing listeners.
	public void removePreListener(final Consumer<Sketch> p_preListener) {
		this.PRE_LISTENERS.remove(p_preListener);
	}

	public void removePostListener(final Consumer<Sketch> p_postListener) {
		this.POST_LISTENERS.remove(p_postListener);
	}

	public void removePreDrawListener(final Consumer<Sketch> p_preDrawListener) {
		this.PRE_DRAW_LISTENERS.remove(p_preDrawListener);
	}

	public void removeDrawListener(final Consumer<Sketch> p_drawListener) {
		this.DRAW_LISTENERS.remove(p_drawListener);
	}

	public void removePostDrawListener(final Consumer<Sketch> p_postDrawListener) {
		this.POST_DRAW_LISTENERS.remove(p_postDrawListener);
	}

	public void removeSketchExitListener(final Consumer<Sketch> p_exitListener) {
		this.EXIT_LISTENERS.remove(p_exitListener);
	}

	public void removeSketchDisposalListener(final Consumer<Sketch> p_disposalListener) {
		this.DISPOSAL_LISTENERS.remove(p_disposalListener);
	}

	// endregion

	@SuppressWarnings("unchecked")
	public <RetT> RetT getNerdExt(final String p_extName) {
		return (RetT) this.EXTENSIONS.get(p_extName);
	}
	// endregion

	// region Utilities!~
	// region Ah yes, GETTERS AND SETTERS. Even here!
	public SceneManager getSceneManager() {
		return this.sceneMan;
	}

	public SceneManager setSceneManager(final SceneManager p_sceneMan) {
		Objects.requireNonNull(p_sceneMan,
				"`Sketch::setSceneManager()` cannot take in a `null`!");
		return this.sceneMan = p_sceneMan;
	}
	// endregion

	// region Window queries.
	public void centerWindow() {
		// You called this function when the window changed its size or position, right?
		// Remember: Computers with multiple displays exist! We shouldn't cache this:
		this.updateWindowRatios();

		final DisplayMode CURRENT_DISPLAY_MODE = this.currentMonitor == null
				? Sketch.DEFAULT_JAVA_SCREEN_MODE
				: this.currentMonitor.getDisplayMode();

		final int WIDTH = CURRENT_DISPLAY_MODE.getWidth(),
				HEIGHT = CURRENT_DISPLAY_MODE.getHeight();

		switch (this.RENDERER) {
			case PConstants.P3D, PConstants.P2D:
				this.glWindow.setPosition(
						(int) ((WIDTH * 0.5f) - this.cx),
						(int) ((HEIGHT * 0.5f) - this.cy));
				break;

			default:
				super.surface.setLocation(
						(int) (WIDTH / 2 - this.width),
						(int) (HEIGHT / 2 - this.q3y));
				break;
		}

		// super.surface.setLocation(winX, winY);
		// (Well, changing the display does NOT effect those variables in any way :|)
	}

	protected void framelyWindowSetup() {
		switch (this.RENDERER) {
			case PConstants.JAVA2D:
				// Fullscreen?
				// https://stackoverflow.com/a/11570414/

				if (this.pfullscreen != this.fullscreen) {
					this.sketchFrame.removeNotify();

					this.sketchFrame.setVisible(false);
					while (this.sketchFrame.isDisplayable())
						;

					if (this.fullscreen) {
						// If `super.displayDim` are set to the actual (AWT) ones:

						// this.sketchFrame.setLocation(-7, -30);
						// this.sketchFrame.setSize(super.displayWidth - 354,
						// super.displayHeight - 270);

						// this.sketchFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
						this.sketchFrame.setLocation(-8, -30);
						this.sketchFrame.setSize(super.displayWidth + 15, super.displayHeight);
						this.sketchFrame.setUndecorated(true);
					} else {
						this.centerWindow();
						this.sketchFrame.setSize(this.INIT_WIDTH, this.INIT_HEIGHT);
						this.sketchFrame.setUndecorated(false);
					}

					this.sketchFrame.setVisible(true);
					this.sketchFrame.addNotify();
				}

				if (this.cursorVisible)
					super.cursor();
				else
					super.noCursor();
				break;

			case PConstants.P3D, PConstants.P2D:
				if (this.pfullscreen != this.fullscreen) {
					this.glWindow.setFullscreen(this.fullscreen);

					// Wait for the window to change its mode.
					// Don't wait for more than `5000` milliseconds!:
					// ...yes, that should crash the program :|
					// (It didn't, during my tests, surprisingly :O
					// The window just... waited there and didn't change states O_O
					// ...and then Processing began rendering again :D
					// Apparently `setFullscreen()` returns `boolean`, meaning that it does
					// error-checking! Kind of JogAmp!)

					// region Older logic (no time checking!).
					// while (this.fullscreen ? !this.glWindow.isFullscreen() :
					// this.glWindow.isFullscreen())
					// ;
					// endregion

					final long fsStartMillis = System.currentTimeMillis();

					while (this.fullscreen != this.glWindow.isFullscreen())
						if (System.currentTimeMillis() - fsStartMillis > 5000)
							break; // Throw an exception instead?

				}

				// I knew this already, but you may want to check out:
				// http://twicetwo.com/blog/processing/2016/03/01/processing-locking-the-mouse.html

				if (this.cursorConfined != this.pcursorConfined)
					this.glWindow.confinePointer(this.cursorConfined);
				while (this.cursorConfined ? !this.glWindow.isPointerConfined() : this.glWindow.isPointerConfined())
					;

				if (this.cursorVisible != this.pcursorVisible) {
					this.glWindow.setPointerVisible(this.cursorVisible);
					while (this.cursorVisible ? !this.glWindow.isPointerVisible() : this.glWindow.isPointerVisible())
						;
				}
				break;

		}
	}

	// I tried the 3rd-to-last method in
	// https://stackoverflow.com/a/21592711/,
	// but https://stackoverflow.com/a/1248865/ was what worked.
	// And yes, I modified it.
	public static GraphicsDevice getGraphicsDeviceAt(final Point p_pos) {
		for (final GraphicsDevice d : Sketch.JAVA_SCREENS)
			for (final GraphicsConfiguration c : d.getConfigurations())
				if (c.getBounds().contains(p_pos))
					return d;

		// If the point is outside all monitors, default to the default monitor!:
		return Sketch.DEFAULT_JAVA_SCREEN;
	}
	// endregion

	// region Get monitor info.
	public GraphicsDevice getCurrentMonitor() {
		return this.currentMonitor;
	}

	public GraphicsDevice getPreviousMonitor() {
		return this.previousMonitor;
	}
	// endregion

	// region Rendering utilities!
	public void updateWindowRatios() {
		this.dbx = super.width * 2;
		this.dby = super.height * 2;

		this.cx = super.width * 0.5f;
		this.cy = super.height * 0.5f;

		this.qx = this.cx * 0.5f;
		this.qy = this.cy * 0.5f;

		this.q3x = this.cx + this.qx;
		this.q3y = this.cy + this.qy;

		this.scr = (float) super.width / (float) super.height;
	}

	public void updateDisplayRatios() {
		this.displayWidthTwice = super.displayWidth * 2;
		this.displayHeightTwice = super.displayHeight * 2;
		this.displayScr = (float) super.displayWidth / (float) super.displayHeight;
		this.displayRefreshRate = this.currentMonitor.getDisplayMode().getRefreshRate();

		this.displayWidthHalf = super.displayWidth / 2;
		this.displayHeightHalf = super.displayHeight / 2;

		// Dividing directly by `4` for more precision...?
		this.displayWidthQuart = super.displayWidth / 4;
		this.displayHeightQuart = super.displayHeight / 4;

		this.displayWidthThirdQuart = this.displayWidthHalf + this.displayWidthQuart;
		this.displayHeightThirdQuart = this.displayWidthHalf + this.displayWidthQuart;
	}

	public void recordCurrentWindowRatios() {
		this.pdbx = this.dbx;
		this.pdby = this.dby;

		this.pcx = this.cx;
		this.pcy = this.cy;

		this.pqx = this.qx;
		this.pqy = this.qy;

		this.pq3x = this.q3x;
		this.pq3y = this.q3y;

		this.pscr = this.scr;
	}

	public void recordCurrentDisplayRatios() {
		this.pdisplayScr = this.displayScr;
		this.ppixelWidth = super.pixelWidth;
		this.ppixelHeight = super.pixelHeight;
		this.ppixelDensity = super.pixelDensity;
		this.pdisplayRefreshRate = this.displayRefreshRate;

		this.pdisplayWidthTwice = this.displayWidthTwice;
		this.pdisplayHeightTwice = this.displayHeightTwice;

		this.pdisplayWidth = super.displayWidth;
		this.pdisplayHeight = super.displayHeight;

		this.pdisplayWidthHalf = this.displayWidthHalf;
		this.pdisplayHeightHalf = this.displayHeightHalf;

		this.pdisplayWidthQuart = this.displayWidthQuart;
		this.pdisplayHeightQuart = this.displayHeightQuart;

		this.pdisplayWidthThirdQuart = this.displayWidthThirdQuart;
		this.pdisplayHeightThirdQuart = this.displayHeightThirdQuart;
	}

	// region From `PGraphics`.
	@Override
	public void background(final PImage p_image) {
		try {
			super.background(p_image);
		} catch (final Exception e) {
			//// Do nothing with the exception. Don't even READ it.
			p_image.resize(super.width, super.height);
			super.background(p_image);

			// super.clear(); // ...apparently `PImage::resize()` is fast enough!
			// System.out.println("`PApplet::background(PImage)` encountered the size
			// issue!");
		}
	}

	// "Hah! Gott'em with the name alignment!"
	public void translate(final PVector p_vec) {
		super.translate(p_vec.x, p_vec.y, p_vec.z);
	}

	public void scale(final PVector p_scaling) {
		super.scale(p_scaling.x, p_scaling.y, p_scaling.z);
	}

	public void rotate(final PVector p_rotVec) {
		super.rotateX(p_rotVec.x);
		super.rotateY(p_rotVec.y);
		super.rotateZ(p_rotVec.z);
	}

	public void rotate(final float p_x, final float p_y, final float p_z) {
		super.rotateX(p_x);
		super.rotateY(p_y);
		super.rotateZ(p_z);
	}

	// region `modelVec()` and `screenVec()`.
	public PVector modelVec() {
		return new PVector(
				// "I passed these `0`s in myself, yeah. Let's not rely on the JIT too much!"
				// - Me before re-thinking that.
				this.modelX(),
				this.modelY(),
				this.modelZ());
	}

	public PVector modelVec(final PVector p_vec) {
		return new PVector(
				super.modelX(p_vec.x, p_vec.y, p_vec.z),
				super.modelY(p_vec.x, p_vec.y, p_vec.z),
				super.modelZ(p_vec.x, p_vec.y, p_vec.z));
	}

	public PVector modelVec(final float p_x, final float p_y, final float p_z) {
		return new PVector(
				super.modelX(p_x, p_y, p_z),
				super.modelY(p_x, p_y, p_z),
				super.modelZ(p_x, p_y, p_z));
	}

	public PVector screenVec() {
		return new PVector(
				this.screenX(),
				this.screenY(),
				this.screenZ());
	}

	public PVector screenVec(final PVector p_vec) {
		return new PVector(
				this.screenX(p_vec.x, p_vec.y, p_vec.z),
				this.screenY(p_vec.x, p_vec.y, p_vec.z),
				this.screenZ(p_vec.x, p_vec.y, p_vec.z));
	}

	public PVector screenVec(final float p_x, final float p_y, final float p_z) {
		return new PVector(
				this.screenX(p_x, p_y, p_z),
				this.screenY(p_x, p_y, p_z),
				this.screenZ(p_x, p_y, p_z));
	}
	// endregion

	// region `modelX()`-`modelY()`-`modelZ()` `PVector` and no-parameter overloads.
	// region Parameterless overloads.
	public float modelX() {
		return super.modelX(0, 0, 0);
	}

	public float modelY() {
		return super.modelY(0, 0, 0);
	}

	public float modelZ() {
		return super.modelZ(0, 0, 0);
	}
	// endregion

	// region `p_vec`?
	// ...how about `p_modelMatInvMulter`? :rofl:!
	public float modelX(final PVector p_vec) {
		return super.modelX(p_vec.x, p_vec.y, p_vec.z);
	}

	public float modelY(final PVector p) {
		return super.modelY(p.x, p.y, p.z);
	}

	public float modelZ(final PVector p) {
		return super.modelZ(p.x, p.y, p.z);
	}
	// endregion
	// endregion

	// region `screenX()`-`screenY()`-`screenZ()`, `PVector` + no-arg overloads.
	// "Oh! And when the `z` is `-1`, you just add this and sub that. Optimization!"
	// - That ONE Mathematician.

	// region Parameterless overloads.
	public float screenX() {
		return super.screenX(0, 0, 0);
	}

	public float screenY() {
		return super.screenY(0, 0, 0);
	}

	public float screenZ() {
		return super.screenY(0, 0, 0);
	}
	// endregion

	// region `p_vec`!
	// The following two were going to disclude the `z` if it was `0`.
	// And later, I felt this was risky.
	// This two-`float` overload ain't in the docs, that scares me!

	// ...ACTUALLY,
	// https://github.com/processing/processing/blob/459853d0dcdf1e1648b1049d3fdbb4bf233fded8/core/src/processing/opengl/PGraphicsOpenGL.java#L4611
	// ..."they rely on the JIT too!" (no, they don't optimize this at all. They
	// just put the `0` themselves, LOL.) :joy:

	public float screenX(final PVector p_vec) {
		return super.screenX(p_vec.x, p_vec.y, p_vec.z);

		// return p_vec.z == 0
		// ? super.screenX(p_vec.x, p_vec.y)
		// : super.screenX(p_vec.x, p_vec.y, p_vec.z);
	}

	public float screenY(final PVector p_vec) {
		return super.screenY(p_vec.x, p_vec.y, p_vec.z);

		// return p_vec.z == 0
		// ? super.screenY(p_vec.x, p_vec.y)
		// : super.screenY(p_vec.x, p_vec.y, p_vec.z);
	}

	public float screenZ(final PVector p_vec) {
		// Hmmm...
		// ..so `z` cannot be `0` here.
		// ..and `x` and `y` cannot be ignored!
		// "No room for optimization here!"
		return super.screenZ(p_vec.x, p_vec.y, p_vec.z);
	}
	// endregion
	// endregion

	// region Camera matrix configuration.
	public void camera(final BasicCamera p_cam) {
		super.camera(
				p_cam.pos.x, p_cam.pos.y, p_cam.pos.z,
				p_cam.center.x, p_cam.center.y, p_cam.center.z,
				p_cam.up.x, p_cam.up.y, p_cam.up.z);
	}

	public void camera(final FlyCamera p_cam) {
		super.camera(
				p_cam.pos.x, p_cam.pos.y, p_cam.pos.z,

				p_cam.pos.x + p_cam.front.x,
				p_cam.pos.y + p_cam.front.y,
				p_cam.pos.z + p_cam.front.z,

				p_cam.up.x, p_cam.up.y, p_cam.up.z);
	}

	public void camera(final PVector p_pos, final PVector p_center, final PVector p_up) {
		super.camera(
				p_pos.x, p_pos.y, p_pos.z,
				p_center.x, p_center.y, p_center.z,
				p_up.x, p_up.y, p_up.z);
	}
	// endregion

	// region Projection functions.
	public void perspective(final NerdAbstractCamera p_cam) {
		super.perspective(p_cam.fov, p_cam.aspect, p_cam.near, p_cam.far);
	}

	public void perspective(final float p_fov, final float p_near, final float p_far) {
		super.perspective(p_fov, this.scr, p_near, p_far);
	}

	public void ortho(final NerdAbstractCamera p_cam) {
		super.ortho(-this.cx, this.cx, -this.cy, this.cy, p_cam.near, p_cam.far);
	}

	public void ortho(final float p_near, final float p_far) {
		super.ortho(-this.cx, this.cx, -this.cy, this.cy, p_near, p_far);
	}

	/**
	 * Expands to {@code PApplet::ortho(-p_cx, p_cx, -p_cy, p_cy, p_near, p_far)}.
	 */
	@Override
	public void ortho(final float p_cx, final float p_cy, final float p_near, final float p_far) {
		super.ortho(-p_cx, p_cx, -p_cy, p_cy, p_near, p_far);
	}
	// endregion

	// region The billion `image()` overloads. Help me make "standards"?
	// region For `PImage`s.
	public void image(final PImage p_image) {
		// https://processing.org/reference/set_.html.
		// Faster than `image()`!:
		// `super.set(0, 0, p_image);`
		// However, we also need to remember that it doesn't render the image on to a
		// quad, meaning that transformations won't apply.
		super.image(p_image, 0, 0);
	}

	/**
	 * @param p_side The length of the side of the square.
	 */
	public void image(final PImage p_image, final float p_side) {
		super.image(p_image, 0, 0, p_side, p_side);
	}

	public void image(final PImage p_image, final PVector p_pos) {
		super.pushMatrix();
		super.translate(p_pos.x, p_pos.y, p_pos.z);
		super.image(p_image, 0, 0);
		super.popMatrix();
	}

	public void image(final PImage p_image, final PVector p_pos, final float p_size) {
		super.pushMatrix();
		super.translate(p_pos.x, p_pos.y, p_pos.z);
		this.image(p_image, p_pos.x, p_pos.y, p_size, p_size);
		super.popMatrix();
	}

	public void image(final PImage p_image, final float p_x, final float p_y, final float p_z) {
		super.pushMatrix();
		super.translate(p_x, p_y, p_z);
		super.image(p_image, 0, 0);
		super.popMatrix();
	}
	// endregion

	// region For `PGraphics`.
	public void image(final PGraphics p_graphics) {
		super.image(p_graphics, 0, 0);
	}

	public void image(final PGraphics p_graphics, final PVector p_pos) {
		super.pushMatrix();
		super.translate(p_pos.x, p_pos.y, p_pos.z);
		super.image(p_graphics, 0, 0);
		super.popMatrix();
	}

	public void image(final PGraphics p_graphics, final float p_scale) {
		super.image(p_graphics, 0, 0, p_scale, p_scale);
	}

	public void image(final PGraphics p_graphics, final PVector p_pos, final float p_scale) {
		super.pushMatrix();
		super.translate(p_pos.x, p_pos.y, p_pos.z);
		this.image(p_graphics, 0, 0, p_scale, p_scale);
		super.popMatrix();
	}

	public void image(final PGraphics p_graphics, final float p_x, final float p_y, final float p_z) {
		this.image((PImage) p_graphics, p_x, p_y, p_z);
	}

	public void image(final PGraphics p_graphics, final PVector p_pos, final float p_width, final float p_height) {
		super.pushMatrix();
		super.translate(p_pos.x, p_pos.y, p_pos.z);
		this.image(p_graphics, p_pos.x, p_pos.y, p_width, p_height);
		super.popMatrix();
	}
	// endregion
	// endregion

	// region `push()` and `pop()` simply don't work in `PApplet`...:
	// ..so I made them work myself!
	@Override
	public void push() {
		super.pushMatrix();
		super.pushStyle();
	}

	@Override
	public void pop() {
		super.popStyle();
		super.popMatrix();
	}
	// endregion
	// endregion

	public PImage svgToImage(final PShape p_shape, final float p_width, final float p_height) {
		if (p_shape == null)
			throw new NullPointerException("`svgToImage(null , p_width, p_height)` won't work.");

		final PGraphics buffer = super.createGraphics(
				(int) PApplet.ceil(p_width),
				(int) PApplet.ceil(p_height),
				PConstants.P3D);

		if (buffer == null) {
			throw new NullPointerException("`svgToImage()`'s `buffer` is `null`!");
		}

		buffer.beginDraw();
		buffer.shape(p_shape, 0, 0, p_width, p_height);
		buffer.endDraw();
		return buffer;
	}

	// region `createGraphics()` overrides and overloads.
	// region Actual overrides.
	@Override
	public PGraphics createGraphics(final int w, final int h, final String renderer, final String path) {
		return super.makeGraphics(w, h, renderer, path, false);
	}

	@Override
	public PGraphics createGraphics(final int w, final int h, final String renderer) {
		return this.createGraphics(w, h, renderer, Sketch.EXEC_DIR_PATH);
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
	public void begin2d() {
		super.hint(PConstants.DISABLE_DEPTH_TEST);
		this.push(); // #JIT_FTW!
	}

	public void end2d() {
		super.hint(PConstants.ENABLE_DEPTH_TEST);
		this.pop(); // #JIT_FTW!
	}

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
		return Sketch.EXEC_DIR_PATH + p_path;
	}

	public static String fromDataDir(final String p_path) {
		return Sketch.DATA_DIR_PATH + p_path;
	}

	// region [DEPRECATED] Overloads for `getPathToRootFrom()`.
	/**
	 * @deprecated Ineffective when using with {@link PApplet}'s "{@code load()}"
	 *             methods. Also, all of these methods have some method
	 *             of accessing files from outside the sketch's data
	 *             folder! <b>Please also see {@code PApplet}'s
	 *             {@code static} methods</b>
	 */
	@Deprecated
	public static String getPathToRootFrom(final File p_path) {
		return Sketch.getPathToRootFrom(p_path.getAbsolutePath());
	}

	/**
	 * @deprecated Ineffective when using with {@link PApplet}'s "{@code load()}"
	 *             methods. Also, all of these methods have some method
	 *             of accessing files from outside the sketch's data
	 *             folder! <b>Please also see {@code PApplet}'s
	 *             {@code static} methods</b>
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

	// region Camera and unprojection.
	// region Camera!
	public NerdAbstractCamera getCamera() {
		return this.currentCamera;
	}

	public NerdAbstractCamera getPreviousCamera() {
		return this.previousCamera;
	}

	public BasicCamera getDefaultCameraClone() {
		return this.defaultCamera.clone();
	}

	/**
	 * @return The previous camera the {@link Sketch} had access to.
	 */
	public NerdAbstractCamera setCamera(final NerdAbstractCamera p_camera) {
		final NerdAbstractCamera toRet = this.previousCamera;
		this.previousCamera = this.currentCamera;
		this.currentCamera = p_camera;
		return toRet;
	}
	// endregion

	public void unprojectMouse() {
		//// [WORKS, NOT CHEAP + STILL INACCURATE]
		//// Unprojection of my own:
		// PVector u = new PVector(super.mouseX, super.mouseY);
		// u = this.glGraphics.projection.mult(u, null);
		// u = this.glGraphics.cameraInv.mult(u, null);
		// u.x -= this.qx;
		//// u.sub(super.width, super.height);
		//// u.add(this.cx, this.cy);
		//// u.add(this.cx, this.cy);
		// this.mouse.set(u);

		// System.out.println(this.mouse);

		final float originalNear;
		final boolean camNotNull = this.currentCamera != null;

		if (camNotNull) {
			originalNear = this.currentCamera.near;
			this.currentCamera.near = this.currentCamera.mouseZ;
			this.currentCamera.applyMatrix();
		} else
			originalNear = 0;

		// Unproject:
		this.UNPROJECTOR.captureViewMatrix((PGraphics3D) this.g);
		// this.mouse.set(0, 0, 0); // Does not help!

		this.UNPROJECTOR.gluUnProject(super.mouseX, super.height - super.mouseY,
				// `0.9f`: at the near clipping plane.
				// `0.9999f`: at the far clipping plane. (NO! Calculate epsilon first, then-)
				// 0.9f + map(mouseY, height, 0, 0, 0.1f),
				0, this.mouse);

		if (camNotNull)
			this.currentCamera.near = originalNear;

		/*
		 * if (this.currentCamera == null)
		 * return;
		 * 
		 * float originalNear = this.currentCamera.near;
		 * this.currentCamera.near = this.currentCamera.mouseZ;
		 * this.currentCamera.applyMatrix();
		 * 
		 * // Unproject:
		 * this.UNPROJECTOR.captureViewMatrix((PGraphics3D) g);
		 * // `0.9f`: at the near clipping plane.
		 * // `0.9999f`: at the far clipping plane.
		 * this.UNPROJECTOR.gluUnProject(
		 * super.mouseX, super.height - super.mouseY,
		 * // 0.9f + map(mouseY, height, 0, 0, 0.1f),
		 * 0, this.mouse);
		 * 
		 * this.currentCamera.near = originalNear;
		 */

	}

	public void unprojectTouches() {
		// Left empty, used on Android! (Implementation for Android, below:)
		/*
		 * Sketch.UNPROJ_TOUCHES.clear();
		 * TouchEvent.Pointer[] touches = super.touches;
		 * 
		 * for (int i = 0; i < touches.length; i++) {
		 * // [WORKS, CHEAPEST, SAME LEVEL OF ACCURACY]
		 * // My own 'mapping' method!:
		 * /*
		 * PVector u = new PVector(
		 * PApplet.map(touches[i].x, 0, super.displayWidth,
		 * 0, super.width),
		 * PApplet.map(touches[i].y, 0, super.displayHeight,
		 * 0, super.height));
		 * <asterisk>/
		 * //u.add(super.cameraPos);
		 * 
		 * // [WORKS, NOT CHEAP + STILL INACCURATE]
		 * // Unprojection of my own:
		 * PVector u = new PVector(touches[i].x, touches[i].y);
		 * u = super.glGraphics.modelviewInv.mult(u, null);
		 * //u = super.glGraphics.cameraInv.mult(u, null);
		 * u.sub(super.width, super.height);
		 * u.add(super.cx, super.cy);
		 * 
		 * // [FAILURE] Unprojection using the `Unprojector` class:
		 * /*
		 * PVector u = new PVector(touches[i].x, touches[i].y);
		 * // Believe in the JIT!~
		 * Unprojector.captureViewMatrix(((PGraphics3D)super.getGraphics()
		 * ));
		 * System.out.printf("Was unprojection successful? %s\n",
		 * Unprojector.gluUnProject(u.x, u.y, u.z, u) // Yes, you can do that. Passing
		 * by value.
		 * ? "Yes!" : "No...");
		 * u.x *= 1.2f; //super.qx;
		 * u.y *= 1.2f; //super.qy;
		 * <asterisk>/
		 * 
		 * // (...here's longer text explaining that.. :)
		 * /*
		 * // [sic] "As different streams having their sources in different places all
		 * mingles
		 * // their water in the sea, so, O Lord, the different paths which men take
		 * through
		 * // tendencies, various touch as they appear, crooked or straight, all lead to
		 * Thee."
		 * // - Swami Vivekananda, quoting a hymn in his `1893` Chicago convention
		 * speech.
		 * // (((I do not guarantee complete correctness in the copying of that
		 * quote.)))
		 * <asterisk>/
		 * // ^^^ be accessed in some other way, but
		 * whatever... (...that basically, this `u.z` modfication will go unchanged,
		 * // no matter what un-projection method you use!:)
		 * u.z = touches[i].pressure; // Should
		 * 
		 * this.UNPROJ_TOUCHES.add(u);
		 * }
		 */
	}
	// endregion

	// region Key-press and key-type helper methods.
	public boolean onlyKeyPressedIs(final int p_keyCode) {
		return this.keysHeld.size() == 1 && this.keysHeld.contains(p_keyCode);
	}

	public boolean onlyKeysPressedAre(final int... p_keyCodes) {
		boolean toRet = this.keysHeld.size() == p_keyCodes.length;

		if (!toRet)
			return false;

		for (final int i : p_keyCodes)
			toRet &= this.keysHeld.contains(i);

		return toRet;
	}

	public boolean keysPressed(final int... p_keyCodes) {
		// this.keysHeld.contains(p_keyCodes); // Causes a totally unique error :O

		for (final int i : p_keyCodes)
			if (!this.keysHeld.contains(i))
				return false;
		return true;

		// I have no idea why Nerd still uses this. Didn't I change that..?:
		/*
		 * boolean flag = true;
		 * for (int i : p_keyCodes)
		 * flag &= this.keysHeld.contains(i); // ...yeah, `|=` and not `&=`...
		 * return flag;
		 */
		// An article once said: `boolean` flags are bad.
	}

	public boolean keyIsPressed(final int p_keyCode) {
		return this.keysHeld.contains(p_keyCode);
	}

	public boolean anyGivenKeyIsPressed(final int... p_keyCodes) {
		for (final int i : p_keyCodes)
			if (this.keysHeld.contains(i))
				return true;
		return false;
	}

	public static boolean isStandardKeyboardSymbol(final char p_char) {
		// boolean is = false;
		for (final char ch : Sketch.STANDARD_KEYBOARD_SYMBOLS)
			// Can't use this!:
			// return ch == p_char;
			// What if the array being examined is empty?!

			if (ch == p_char)
				return true;

		// These used to be in the loop:
		// is = ch == p_char;
		// is |= ch == p_char;
		// return is;

		return false;
	}

	public static boolean isTypeable(final char p_char) {
		return Character.isDigit(p_char) ||
				Character.isLetter(p_char) ||
				Character.isWhitespace(p_char) ||
				Sketch.isStandardKeyboardSymbol(p_char);
	}

	public char getTypedKey() {
		if (Sketch.isTypeable(this.key))
			return this.key;

		// New way to do this in Java!:
		// (...as seen in [`java.lang.`]`Long.class`, on line 217, in OpenJDK `17`!)
		return switch (this.keyCode) {
			case PConstants.BACKSPACE -> '\b';
			case PConstants.RETURN -> '\n';
			case PConstants.ENTER -> '\n';
			default -> '\0';
		};

		// """"""""Slow"""""""":
		/*
		 * if (keyCode == BACKSPACE)
		 * return '\b';
		 * else if (keyCode == retURN || keyCode == ENTER)
		 * return '\n';
		 * else if (isTypeable(key))
		 * return key;
		 * else return '\0';
		 */

	}

	public void addTypedKeyTo(final String p_str) {
		final char typedChar = this.getTypedKey();
		final int strLen = p_str.length();

		if (typedChar == '\b' && strLen > 0)
			p_str.substring(strLen - 1, strLen);
		else
			p_str.concat(Character.toString(typedChar));
	}

	public void addTypedKeyTo(final StringBuilder p_str) {
		final char typedChar = this.getTypedKey();
		final int strLen = p_str.length();

		if (typedChar == '\b' && strLen > 0)
			p_str.substring(strLen - 1, strLen);
		else
			p_str.append(Character.toString(typedChar));
	}

	// To be used for checking if a certain key can be typed:
	public boolean isNotSpecialKey(final int p_keyCode) {
		// I just didn't want to make an array :joy::
		return !(
		// For all function keys [regardless of whether `Shift` or `Ctrl` are pressed]:
		p_keyCode > 96 && p_keyCode < 109 ||
				p_keyCode == 0 || // `Fn`, plus a function key.
				p_keyCode == 2 || // `Home`,
				p_keyCode == 3 || // `End`,
				p_keyCode == 8 || // `Backspace`,
				p_keyCode == 10 || // Both `Enter`s/`return`s.
				p_keyCode == 11 || // `PageDown`,
				p_keyCode == 12 || // Resistered when a button is pressed on the numpad with `NumLock` off.
				p_keyCode == 16 || // `PageUp`,
				p_keyCode == 19 || // "`Alt`-Graph',
				p_keyCode == 20 || // `CapsLock`,
				p_keyCode == 23 || // `ScrollLock`,
				p_keyCode == 26 || // `Insert`,
				p_keyCode == 147 || // Both `Delete` keys,
				p_keyCode == 148 || // `Pause`/`Break` and also `NumLock`,
				p_keyCode == 153 || // `Menu`/`Application` AKA "RightClick" key.
				p_keyCode == 157 // "Meta", AKA the "OS key".
		);
	}
	// endregion

	// region Start a `JAVA2D` sketch with an undecorated window.
	public JFrame createSketchPanel(final Runnable p_exitTask, final Sketch p_sketch,
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
		((JFrame) toRet).setContentPane(panel); // This is the dummy variable from Processing.
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
		final Point mousePoint = MouseInfo.getPointerInfo().getLocation();
		super.mouseX = mousePoint.x - this.sketchFrame.getLocation().x;
		super.mouseY = mousePoint.y - this.sketchFrame.getLocation().y;
	}
	// endregion
	// endregion

}
