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

import com.brahvim.nerd.framework.cameras.NerdAbstractCamera;
import com.brahvim.nerd.framework.cameras.NerdBasicCamera;
import com.brahvim.nerd.framework.cameras.NerdBasicCameraBuilder;
import com.brahvim.nerd.framework.cameras.NerdFlyCamera;
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
import processing.core.PVector;
import processing.opengl.PGL;
import processing.opengl.PGraphics3D;
import processing.opengl.PGraphicsOpenGL;
import processing.opengl.PJOGL;

/**
 * <h1>¯\_(ツ)_/¯</h1>
 * 
 * @author Brahvim Bhaktvatsal
 */
public class NerdSketch extends PApplet {

	// region Abstract inner classes.
	// Used abstract classes instead of interfaces for these (two) reasons:
	/*
	 * - No security for `ALL_REFERENCES` from the user! It'll be `public`!
	 * - For registering the reference into the `ALL_REFERENCES` collection,
	 * a `default` method may be used. However, the method is overrideable,
	 * meaning that registering code becomes modifiable, letting the user
	 * change what they weren't supposed to!
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
	public final String NAME;
	public final String ICON_PATH;
	public final boolean USES_OPENGL;
	public final String RENDERER_NAME;
	public final NerdStringTable STRINGS;
	public final NerdAssetManager ASSETS;
	public final NerdUnprojector UNPROJECTOR;
	public final HashMap<String, Object> EXTENSIONS;
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

	public final Class<? extends NerdScene> FIRST_SCENE_CLASS;

	// Dimensions of the inital size of the window:
	public final float INIT_SCR;
	public final int INIT_WIDTH, INIT_HEIGHT;
	public final int INIT_WIDTH_HALF, INIT_HEIGHT_HALF;
	public final int INIT_WIDTH_QUART, INIT_HEIGHT_QUART;

	/** Certain setting for the sketch. */
	public final boolean CLOSE_ON_ESCAPE, STARTED_FULLSCREEN, INITIALLY_RESIZABLE,
			CAN_FULLSCREEN, F11_FULLSCREEN, ALT_ENTER_FULLSCREEN;

	// `NerdSceneManager` is a `protected` field.
	public final NerdDisplayManager DISPLAYS;
	public final NerdWindowManager WINDOW;
	public final NerdInputManager INPUT;
	// endregion
	// endregion

	// Time (`millis()` returns `int`!):
	public int frameStartTime, pframeTime, frameTime;
	// endregion

	// region `protected` fields.
	protected static final String NULL_LISTENER_ERROR_MESSAGE = "An object passed to `Sketch::add*Listener()` cannot be `null`.";

	protected final NerdSketch SKETCH = this;

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

	protected final int ANTI_ALIASING;
	protected final NerdBridgedSceneManager SCENES; // This is a bridged object, thus, `protected`.

	// `LinkedHashSet`s preserve order (and also disallow element repetition)!

	protected NerdAbstractCamera previousCamera, currentCamera; // CAMERA! (wher lite?! wher accsunn?!)
	protected NerdBasicCamera defaultCamera;
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

		this.RENDERER_NAME = p_key.renderer;
		this.ICON_PATH = p_key.iconPath;
		this.EXTENSIONS = p_key.nerdExtensions;
		this.ANTI_ALIASING = p_key.antiAliasing;
		this.FIRST_SCENE_CLASS = p_key.firstScene;
		this.INITIALLY_RESIZABLE = p_key.canResize;
		this.CAN_FULLSCREEN = !p_key.cannotFullscreen;
		this.CLOSE_ON_ESCAPE = !p_key.preventCloseOnEscape;
		this.F11_FULLSCREEN = !p_key.cannotF11Fullscreen;
		this.STARTED_FULLSCREEN = p_key.startedFullscreen;
		this.ALT_ENTER_FULLSCREEN = !p_key.cannotAltEnterFullscreen;
		this.NAME = p_key.name == null ? "TheNerdProject" : p_key.name;
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
		this.USES_OPENGL = PConstants.P2D.equals(this.RENDERER_NAME) || PConstants.P3D.equals(this.RENDERER_NAME);
		this.SCENES = new NerdBridgedSceneManager(
				this, p_key.sceneManagerSettings, p_key.sceneChangeListeners, p_key.ecsSystemOrder);
		// endregion

		// region Setting OpenGL renderer icons.
		if (PConstants.P2D.equals(this.RENDERER_NAME) || PConstants.P3D.equals(this.RENDERER_NAME))
			PJOGL.setIcon(this.ICON_PATH);
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

		super.smooth(this.ANTI_ALIASING);
		super.size(this.INIT_WIDTH, this.INIT_HEIGHT, this.RENDERER_NAME);

		for (final Consumer<NerdSketch> c : this.SETTINGS_LISTENERS)
			if (c != null)
				c.accept(this);
	}
	// endregion

	// region Processing sketch workflow.
	@Override
	public void setup() {
		this.iconImage = super.loadImage(this.ICON_PATH);

		this.WINDOW.width = super.width;
		this.WINDOW.height = super.height;

		this.DISPLAYS.displayWidth = super.displayWidth;
		this.DISPLAYS.displayHeight = super.displayHeight;

		this.DISPLAYS.updateDisplayRatios();
		this.WINDOW.updateWindowRatios();
		this.WINDOW.init();

		super.surface.setTitle(this.NAME);
		super.registerMethod("pre", this);
		super.registerMethod("post", this);
		super.frameRate(this.DEFAULT_REFRESH_RATE);

		this.nerdGraphics = new NerdGraphics(this, super.getGraphics());
		this.defaultFont = super.createFont("SansSerif", this.WINDOW.scr * 72);

		// I should make a super slow "convenience" method to perform this
		// `switch (this.RENDERER)` using `Runnable`s!
		// :joy:!

		// Renderer-specific object initialization and settings!:
		switch (this.RENDERER_NAME) {
			case PConstants.P2D, PConstants.P3D -> {
				this.glWindow = (GLWindow) this.WINDOW.getNativeObject();
				this.glGraphics = (PGraphicsOpenGL) super.getGraphics();
				this.gl = this.glWindow.getGL();
				this.glu = new GLU();

				this.defaultCamera = new NerdBasicCameraBuilder(this).build();
				this.currentCamera = this.defaultCamera;

				if (this.INITIALLY_RESIZABLE)
					this.glWindow.setResizable(true);

				// Done in the constructor! `setup()`'s too late for this!:
				// PJOGL.setIcon(this.iconPath);
			}

			case PConstants.JAVA2D -> {
				this.sketchFrame = (JFrame) this.WINDOW.getNativeObject();
			}

		}

		super.textFont(this.defaultFont);
		super.rectMode(PConstants.CENTER);
		super.imageMode(PConstants.CENTER);
		super.textAlign(PConstants.CENTER, PConstants.CENTER);

		this.SETUP_LISTENERS.forEach(this.DEFAULT_CALLBACK_ITR_LAMBDA);
	}

	public void pre() {
		if (this.USES_OPENGL)
			this.pgl = super.beginPGL();

		// Cheap removal strategy, LOL. I'm fed of boilerplate!:
		for (final Collection<?> c : this.LIST_OF_CALLBACK_LISTS)
			// ..Don't use `HashSet::contains()` to check here. Ugh.
			c.removeAll(this.CALLBACK_LISTENERS_TO_REMOVE);

		this.WINDOW.preCallback(this.WINDOW_LISTENERS);
		this.DISPLAYS.preCallback(this.WINDOW_LISTENERS);

		// this.INPUT.framelyMouseScrollDelta = this.INPUT.totalMouseScroll -
		// this.INPUT.pframeTotalMouseScroll;
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

		// region Update frame-ly mouse settings.
		// region "`GLOBAL_MOUSE`".
		this.INPUT.PREV_FRAME_GLOBAL_MOUSE_POINT.setLocation(this.INPUT.GLOBAL_MOUSE_POINT);
		this.INPUT.PREV_FRAME_GLOBAL_MOUSE_VECTOR.set(this.INPUT.GLOBAL_MOUSE_VECTOR);

		this.INPUT.GLOBAL_MOUSE_POINT.setLocation(MouseInfo.getPointerInfo().getLocation());
		this.INPUT.GLOBAL_MOUSE_VECTOR.set(this.INPUT.GLOBAL_MOUSE_POINT.x, this.INPUT.GLOBAL_MOUSE_POINT.y);
		// endregion
		// endregion

		// region Apply the camera when using OpenGL!
		if (this.USES_OPENGL) {
			if (this.currentCamera != null)
				this.currentCamera.apply(super.getGraphics()); // Do all three tasks using the current camera.
			else { // ..."Here we go again."
				this.defaultCamera.apply(super.getGraphics());

				// If `this.currentCamera` is `null`, but wasn't,
				if (this.currentCamera != this.previousCamera)
					System.out.printf(
							"Sketch \"%s\" has no camera! Consider adding one...?",
							this.NAME);
			}
		}
		// endregion

		// Call all draw listeners:
		this.DRAW_LISTENERS.forEach(this.DEFAULT_CALLBACK_ITR_LAMBDA);
		this.SCENES.runDraw();

		// region If it doesn't yet exist, construct the scene!
		if (super.frameCount == 1 && this.SCENES.getCurrentScene() == null) {
			if (this.FIRST_SCENE_CLASS == null)
				System.err.println("There is no initial `NerdScene`! It's `null`!");
			else
				this.SCENES.startScene(this.FIRST_SCENE_CLASS);
		}
		// endregion

		// Call all post-render listeners:
		this.POST_DRAW_LISTENERS.forEach(this.DEFAULT_CALLBACK_ITR_LAMBDA);
	}

	public void post() {
		this.previousCamera = this.currentCamera;

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
		if (this.USES_OPENGL)
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
		if (!this.CLOSE_ON_ESCAPE && this.keyCode == 27)
			this.key = '\0'; // Processing checks this to know what key was pressed.
		// By setting it to `\0`, we disallow exiting.

		if (this.CAN_FULLSCREEN) {
			if (this.ALT_ENTER_FULLSCREEN
					&& super.keyCode == KeyEvent.VK_ENTER
					&& this.INPUT.anyGivenKeyIsPressed(KeyEvent.VK_ALT, 19))
				this.WINDOW.fullscreen = !this.WINDOW.fullscreen;
			else if (this.F11_FULLSCREEN)
				switch (this.RENDERER_NAME) {
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
	// region From `PGraphics`.
	// region Shapes.
	// region `drawShape()` overloads.
	public void drawShape(final float p_x, final float p_y, final float p_z, final int p_shapeType,
			final Runnable p_shapingFxn) {
		super.pushMatrix();
		this.translate(p_x, p_y, p_z);
		super.beginShape(p_shapeType);
		p_shapingFxn.run();
		super.endShape(PConstants.CLOSE);
		super.popMatrix();
	}

	public void drawShape(final float p_x, final float p_y, final int p_shapeType, final Runnable p_shapingFxn) {
		super.pushMatrix();
		this.translate(p_x, p_y);
		super.beginShape(p_shapeType);
		p_shapingFxn.run();
		super.endShape(PConstants.CLOSE);
		super.popMatrix();
	}

	public void drawShape(final PVector p_pos, final int p_shapeType, final Runnable p_shapingFxn) {
		super.pushMatrix();
		this.translate(p_pos);
		super.beginShape(p_shapeType);
		p_shapingFxn.run();
		super.endShape(PConstants.CLOSE);
		super.popMatrix();
	}

	public void drawShape(final int p_shapeType, final Runnable p_shapingFxn) {
		super.beginShape(p_shapeType);
		p_shapingFxn.run();
		super.endShape(PConstants.CLOSE);
	}
	// endregion

	// region `drawOpenShape()` overloads.
	public void drawOpenShape(final float p_x, final float p_y, final float p_z, final int p_shapeType,
			final Runnable p_shapingFxn) {
		super.pushMatrix();
		this.translate(p_x, p_y, p_z);
		super.beginShape(p_shapeType);
		p_shapingFxn.run();
		super.endShape();
		super.popMatrix();
	}

	public void drawOpenShape(final float p_x, final float p_y, final int p_shapeType, final Runnable p_shapingFxn) {
		super.pushMatrix();
		this.translate(p_x, p_y);
		super.beginShape(p_shapeType);
		p_shapingFxn.run();
		super.endShape();
		super.popMatrix();
	}

	public void drawOpenShape(final PVector p_pos, final int p_shapeType, final Runnable p_shapingFxn) {
		super.pushMatrix();
		this.translate(p_pos);
		super.beginShape(p_shapeType);
		p_shapingFxn.run();
		super.endShape();
		super.popMatrix();
	}

	public void drawOpenShape(final int p_shapeType, final Runnable p_shapingFxn) {
		super.beginShape(p_shapeType);
		p_shapingFxn.run();
		super.endShape();
	}
	// endregion

	public void drawContour(final Runnable p_countouringFxn) {
		super.beginContour();
		p_countouringFxn.run();
		super.endContour();
	}

	// region `PVector` overloads.
	// region 3D shapes.
	// region `box()` overloads.
	public void box(final float p_x, final float p_y, final float p_z,
			final float p_width, final float p_height, final float p_depth) {
		super.pushMatrix();
		super.translate(p_x, p_y, p_z);
		super.box(p_width, p_height, p_depth);
		super.popMatrix();
	}

	public void box(final PVector p_pos, final float p_width, final float p_height, final float p_depth) {
		super.pushMatrix();
		this.translate(p_pos);
		super.box(p_width, p_height, p_depth);
		super.popMatrix();
	}

	public void box(final float p_x, final float p_y, final float p_z, final PVector p_dimensions) {
		super.pushMatrix();
		super.translate(p_x, p_y, p_z);
		super.box(p_dimensions.x, p_dimensions.y, p_dimensions.z);
		super.popMatrix();
	}

	public void box(final float p_x, final float p_y, final float p_z, final float p_size) {
		super.pushMatrix();
		super.translate(p_x, p_y, p_z);
		super.box(p_size);
		super.popMatrix();
	}

	public void box(final PVector p_pos, final PVector p_dimensions) {
		super.pushMatrix();
		this.translate(p_pos);
		super.box(p_dimensions.x, p_dimensions.y, p_dimensions.z);
		super.popMatrix();
	}

	public void box(final PVector p_pos, final float p_size) {
		super.pushMatrix();
		this.translate(p_pos);
		super.box(p_size);
		super.popMatrix();
	}
	// endregion

	// region `sphere()` overloads (just a copy of the `box()` ones, hehehe.).
	public void sphere(final float p_x, final float p_y, final float p_z,
			final float p_width, final float p_height, final float p_depth) {
		super.pushMatrix();
		super.translate(p_x, p_y, p_z);
		super.scale(p_width, p_height, p_depth);
		super.sphere(1);
		super.popMatrix();
	}

	public void sphere(final PVector p_pos, final float p_width, final float p_height, final float p_depth) {
		super.pushMatrix();
		this.translate(p_pos);
		super.scale(p_width, p_height, p_depth);
		super.sphere(1);
		super.popMatrix();
	}

	public void sphere(final float p_x, final float p_y, final float p_z, final PVector p_dimensions) {
		super.pushMatrix();
		super.translate(p_x, p_y, p_z);
		super.scale(p_dimensions.x, p_dimensions.y, p_dimensions.z);
		super.sphere(1);
		super.popMatrix();
	}

	public void sphere(final float p_x, final float p_y, final float p_z, final float p_size) {
		super.pushMatrix();
		super.translate(p_x, p_y, p_z);
		super.sphere(p_size);
		super.popMatrix();
	}

	public void sphere(final PVector p_pos, final PVector p_dimensions) {
		super.pushMatrix();
		this.translate(p_pos);
		super.scale(p_dimensions.x, p_dimensions.y, p_dimensions.z);
		super.sphere(1);
		super.popMatrix();
	}

	public void sphere(final PVector p_pos, final float p_size) {
		super.pushMatrix();
		this.translate(p_pos);
		super.sphere(p_size);
		super.popMatrix();
	}
	// endregion
	// endregion

	public void arc(final PVector p_translation, final PVector p_size,
			final float p_startAngle, final float p_endAngle) {
		super.pushMatrix();
		this.translate(p_translation);
		super.arc(0, 0, p_size.x, p_size.y, p_startAngle, p_endAngle);
		super.popMatrix();
	}

	// Perhaps I should figure out the default arc mode and make the upper one call
	// this one?:
	public void arc(final PVector p_translation, final PVector p_size,
			final float p_startAngle, final float p_endAngle, final int p_mode) {
		super.pushMatrix();
		this.translate(p_translation);
		super.arc(0, 0, p_size.x, p_size.y, p_startAngle, p_endAngle, p_mode);
		super.popMatrix();
	}

	public void circle(final PVector p_pos, final float p_size) {
		super.pushMatrix();
		this.translate(p_pos);
		super.circle(0, 0, p_size);
		super.popMatrix();
	}

	// region E L L I P S E S.
	// ...For when you want to use an ellipse like a circle:
	public void ellipse(final float p_x, final float p_y, final PVector p_dimensions) {
		super.ellipse(p_x, p_y, p_dimensions.x, p_dimensions.y);
	}

	public void ellipse(final float p_x, final float p_y, final float p_z, final float p_width, final float p_height) {
		super.pushMatrix();
		this.translate(p_x, p_y, p_z);
		super.ellipse(0, 0, p_width, p_height);
		super.popMatrix();
	}

	public void ellipse(final float p_x, final float p_y, final float p_z, final PVector p_dimensions) {
		super.pushMatrix();
		this.translate(p_x, p_y, p_z);
		super.ellipse(0, 0, p_dimensions.x, p_dimensions.y);
		super.popMatrix();
	}

	public void ellipse(final PVector p_pos, final float p_size) {
		super.pushMatrix();
		this.translate(p_pos);
		super.ellipse(0, 0, p_size, p_size);
		super.popMatrix();
	}

	public void ellipse(final PVector p_pos, final float p_width, final float p_height) {
		super.pushMatrix();
		this.translate(p_pos);
		super.ellipse(0, 0, p_width, p_height);
		super.popMatrix();
	}

	public void ellipse(final PVector p_pos, final PVector p_dimensions) {
		super.pushMatrix();
		this.translate(p_pos);
		super.ellipse(0, 0, p_dimensions.x, p_dimensions.y);
		super.popMatrix();
	}
	// endregion

	public void line(final PVector p_from, final PVector p_to) {
		// `z`-coordinate of first and THEN the second point!:
		super.line(p_from.x, p_from.y, p_to.x, p_to.y, p_from.z, p_to.y);
	}

	public void lineInDir/* `lineInDirOfLength` */(
			final PVector p_start, final PVector p_dir, final float p_length) {
		// `z`-coordinate of first and THEN the second point!:
		super.line(p_start.x, p_start.y,
				p_start.x + p_dir.x * p_length,
				p_start.y + p_dir.y * p_length,
				// `z` stuff!:
				p_start.z, p_start.z + p_dir.z * p_length);
	}

	public void line2d(final PVector p_from, final PVector p_to) {
		super.line(p_from.x, p_from.y, p_to.x, p_to.y);
	}

	// region `radialLine*d()`!
	public void radialLine2d(final PVector p_from, final float p_angle) {
		super.line(p_from.x, p_from.y, PApplet.sin(p_angle), PApplet.cos(p_angle));
	}

	public void radialLine2d(final PVector p_from, final float p_x, final float p_y, final float p_length) {
		super.line(p_from.x, p_from.y,
				p_from.x + PApplet.sin(p_x) * p_length,
				p_from.y + PApplet.cos(p_y) * p_length);
	}

	public void radialLine2d(final PVector p_from, final float p_x, final float p_y,
			final float p_xLen, final float p_yLen) {
		super.line(p_from.x, p_from.y,
				p_from.x + PApplet.sin(p_x) * p_xLen,
				p_from.y + PApplet.cos(p_y) * p_yLen);
	}

	public void radialLine2d(final PVector p_from, final PVector p_trigVals, final float p_size) {
		this.line(p_from.x, p_from.y, p_trigVals.x * p_size, p_trigVals.y * p_size);
	}

	public void radialLine2d(final PVector p_from, final PVector p_values) {
		this.radialLine2d(p_from, p_values, p_values.z);
	}

	public void radialLine3d(final PVector p_from, final float p_theta, final float p_phi, final float p_length) {
		final float sineOfPitch = PApplet.sin(p_theta);
		super.line(p_from.x, p_from.y,
				p_from.x + sineOfPitch * PApplet.cos(p_phi) * p_length,
				p_from.x + sineOfPitch * PApplet.sin(p_phi) * p_length,
				p_from.z, p_from.x + PApplet.cos(p_theta) * p_length);
	}

	public void radialLine3d(final PVector p_from, final float p_theta, final float p_phi,
			final float p_xLen, final float p_yLen, final float p_zLen) {
		final float sineOfPitch = PApplet.sin(p_theta);
		super.line(p_from.x, p_from.y,
				p_from.x + sineOfPitch * PApplet.cos(p_phi) * p_xLen,
				p_from.x + sineOfPitch * PApplet.sin(p_phi) * p_yLen,
				p_from.z, p_from.x + PApplet.cos(p_theta) * p_zLen);
	}
	// endregion

	public void point(final PVector p_pos) {
		super.point(p_pos.x, p_pos.y, p_pos.z);
	}

	public void point2d(final PVector p_pos) {
		super.point(p_pos.x, p_pos.y, 0);
	}

	public void quad(final PVector p_first, final PVector p_second, final PVector p_third, final PVector p_fourth) {
		super.quad(
				p_first.x, p_first.y,
				p_second.x, p_second.y,
				p_third.x, p_third.y,
				p_fourth.x, p_first.y);
	}

	// region `rect()` overloads, ;)!
	@Override
	public void rect(final float p_x, final float p_y, final float p_z, final float p_width, final float p_height) {
		super.pushMatrix();
		this.translate(p_x, p_y, p_z);
		super.rect(0, 0, p_width, p_height);
		super.popMatrix();
	}

	public void rect(final float p_x, final float p_y, final float p_z, final float p_width, final float p_height,
			final float p_radius) {
		super.pushMatrix();
		this.translate(p_x, p_y, p_z);
		super.rect(0, 0, p_width, p_height, p_radius);
		super.popMatrix();
	}

	public void rect(final float p_x, final float p_y, final float p_z,
			final float p_width, final float p_height,
			final float p_topLeftRadius, final float p_topRightRadius,
			final float p_bottomRightRadius, final float p_bottomLeftRadius) {
		super.pushMatrix();
		super.translate(p_x, p_y, p_z);
		super.rect(0, 0, p_width, p_height,
				p_topLeftRadius, p_topRightRadius,
				p_bottomRightRadius, p_bottomLeftRadius);
		super.popMatrix();
	}

	public void rect(final float p_x, final float p_y, final float p_z, final float p_width, final float p_height,
			final PVector p_topRadii, final PVector p_bottomRadii) {
		super.pushMatrix();
		super.translate(p_x, p_y, p_z);
		super.rect(0, 0, p_width, p_height,
				p_topRadii.x, p_topRadii.y,
				p_bottomRadii.x, p_bottomRadii.y);
		super.popMatrix();
	}

	public void rect(final float p_x, final float p_y, final PVector p_dimensions) {
		super.rect(p_x, p_y, p_dimensions.x, p_dimensions.y);
	}

	public void rect(final float p_x, final float p_y, final PVector p_dimensions, final float p_radius) {
		super.rect(p_x, p_y, p_dimensions.x, p_dimensions.y, p_radius);
	}

	public void rect(final float p_x, final float p_y, final float p_z, final PVector p_dimensions,
			final float p_topLeftRadius, final float p_topRightRadius,
			final float p_bottomRightRadius, final float p_bottomLeftRadius) {
		super.pushMatrix();
		super.translate(p_x, p_y, p_z);
		super.rect(0, 0,
				p_dimensions.x, p_dimensions.y,
				p_topLeftRadius, p_topRightRadius,
				p_bottomRightRadius, p_bottomLeftRadius);
		super.popMatrix();
	}

	public void rect(final float p_x, final float p_y, final float p_z, final PVector p_dimensions,
			final PVector p_topRadii, final PVector p_bottomRadii) {
		super.pushMatrix();
		super.translate(p_x, p_y, p_z);
		super.rect(0, 0,
				p_dimensions.x, p_dimensions.y,
				p_topRadii.x, p_topRadii.y,
				p_bottomRadii.x, p_bottomRadii.y);
		super.popMatrix();
	}

	public void rect(final PVector p_pos, final float p_width, final float p_height) {
		super.pushMatrix();
		this.translate(p_pos);
		super.rect(0, 0, p_width, p_height);
		super.popMatrix();
	}

	public void rect(final PVector p_pos, final float p_width, final float p_height, final float p_radius) {
		super.pushMatrix();
		this.translate(p_pos);
		super.rect(0, 0, p_width, p_height, p_radius);
		super.popMatrix();
	}

	public void rect(final PVector p_pos, final float p_width, final float p_height,
			final float p_topLeftRadius, final float p_topRightRadius,
			final float p_bottomRightRadius, final float p_bottomLeftRadius) {
		super.pushMatrix();
		this.translate(p_pos);
		super.rect(0, 0, p_width, p_height,
				p_topLeftRadius, p_topRightRadius,
				p_bottomRightRadius, p_bottomLeftRadius);
		super.popMatrix();
	}

	public void rect(final PVector p_pos, final float p_width, final float p_height,
			final PVector p_topRadii, final PVector p_bottomRadii) {
		super.pushMatrix();
		this.translate(p_pos);
		super.rect(0, 0, p_width, p_height,
				p_topRadii.x, p_topRadii.y,
				p_bottomRadii.x, p_bottomRadii.y);
		super.popMatrix();
	}

	public void rect(final PVector p_pos, final PVector p_dimensions) {
		super.pushMatrix();
		this.translate(p_pos);
		super.rect(0, 0, p_dimensions.x, p_dimensions.y);
		super.popMatrix();
	}

	public void rect(final PVector p_pos, final PVector p_dimensions, final float p_radius) {
		super.pushMatrix();
		this.translate(p_pos);
		super.rect(0, 0, p_dimensions.x, p_dimensions.y, p_radius);
		super.popMatrix();
	}

	public void rect(final PVector p_pos, final PVector p_dimensions,
			final float p_topLeftRadius, final float p_topRightRadius,
			final float p_bottomRightRadius, final float p_bottomLeftRadius) {
		super.pushMatrix();
		this.translate(p_pos);
		super.rect(0, 0, p_dimensions.x, p_dimensions.y,
				p_topLeftRadius, p_topRightRadius,
				p_bottomRightRadius, p_bottomLeftRadius);
		super.popMatrix();
	}

	public void rect(final PVector p_pos, final PVector p_dimensions,
			final PVector p_topRadii, final PVector p_bottomRadii) {
		super.pushMatrix();
		this.translate(p_pos);
		super.rect(0, 0, p_dimensions.x, p_dimensions.y,
				p_topRadii.x, p_topRadii.y,
				p_bottomRadii.x, p_bottomRadii.y);
		super.popMatrix();
	}
	// endregion

	public void square(final PVector p_pos, final float p_size) {
		super.pushMatrix();
		super.translate(p_pos.x, p_pos.y, p_pos.z);
		super.square(0, 0, p_size);
		super.popMatrix();
	}

	// region `triangle()` overload!
	// region ...Thoughts about the-uhh, `triangle()` overloads!
	// I wanted to go crazy, writing out (BY HAND!) versions like...
	// (shown here without the `final` keyword, of course!):
	// `triangle(PVector p_v1, float x2, float y2, float x3, float y3)`
	// `triangle(float x1, float y1, PVector p_v2, float x3, float y3)`
	// ...
	// ...AND EVEN:
	// `triangle(PVector p_v2, float x2, float y2, PVector v3)`.
	// ...Yeah. You get the point. I'm crazy ;)
	// Yes, I was going to use more generator code (JavaScript!) for this.
	// I might be crazy, but am also lazy! :joy:
	// endregion

	public void triangle(final PVector p_v1, final PVector p_v2, final PVector p_v3) {
		super.triangle(
				p_v1.x, p_v1.y,
				p_v2.x, p_v2.y,
				p_v3.x, p_v3.y);
	}
	// endregion
	// endregion
	// endregion

	/**
	 * Draws the {@code p_bgImage} as if it was a background. You may even choose to
	 * call one of the {@link PApplet#tint()} overloads before calling this!
	 */
	@Override
	public void background(final PImage p_bgImage) {
		Objects.requireNonNull(p_bgImage);

		super.pushMatrix();
		super.hint(PConstants.DISABLE_DEPTH_TEST);
		super.perspective();
		super.camera();
		super.image(p_bgImage,
				this.WINDOW.cx, this.WINDOW.cy,
				this.WINDOW.width, this.WINDOW.height);
		super.hint(PConstants.ENABLE_DEPTH_TEST);
		super.popMatrix();
	}

	// region Transformations!
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
	// endregion

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

	// region `screenX()`-`screenY()`-`screenZ()`, `PVector`, plus no-arg overloads.
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
	public void camera(final NerdBasicCamera p_cam) {
		super.camera(
				p_cam.getPos().x, p_cam.getPos().y, p_cam.getPos().z,
				p_cam.getCenter().x, p_cam.getCenter().y, p_cam.getCenter().z,
				p_cam.getUp().x, p_cam.getUp().y, p_cam.getUp().z);
	}

	public void camera(final NerdFlyCamera p_cam) {
		super.camera(
				p_cam.getPos().x, p_cam.getPos().y, p_cam.getPos().z,

				p_cam.getPos().x + p_cam.front.x,
				p_cam.getPos().y + p_cam.front.y,
				p_cam.getPos().z + p_cam.front.z,

				p_cam.getUp().x, p_cam.getUp().y, p_cam.getUp().z);
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
		super.perspective(p_fov, this.WINDOW.scr, p_near, p_far);
	}

	public void ortho(final NerdAbstractCamera p_cam) {
		super.ortho(-this.WINDOW.cx, this.WINDOW.cx, -this.WINDOW.cy, this.WINDOW.cy, p_cam.near, p_cam.far);
	}

	public void ortho(final float p_near, final float p_far) {
		super.ortho(-this.WINDOW.cx, this.WINDOW.cx, -this.WINDOW.cy, this.WINDOW.cy, p_near, p_far);
	}

	/**
	 * Expands to: {@code PApplet::ortho(-p_cx, p_cx, -p_cy, p_cy, p_near, p_far)}.
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

	// region `push()` and `pop()` simply don't work in `PApplet`,
	// ...so I re-wrote them myself!
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

	public float textHeight() {
		return super.textAscent() - super.textDescent();
	}

	/**
	 * Translates by the width of {@code p_text} halved, and the current text
	 * height, halved, before actually rendering the text.
	 * 
	 * @see NerdSketch#textHeight()
	 * @see PApplet#textWidth(String)
	 */
	public void centeredText(final String p_text) {
		super.text(p_text, super.textWidth(p_text) * 0.5f, this.textHeight() * 0.5f);
	}

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

	// region Camera and unprojection.
	// region Camera!
	public NerdAbstractCamera getCamera() {
		return this.currentCamera;
	}

	public NerdAbstractCamera getPreviousCamera() {
		return this.previousCamera;
	}

	public NerdBasicCamera getDefaultCameraClone() {
		return this.defaultCamera.clone();
	}

	public NerdAbstractCamera setCameraToDefault() {
		if (!this.USES_OPENGL)
			return null;

		final NerdAbstractCamera toRet = this.getDefaultCameraClone();
		this.setCamera(toRet);
		return toRet;
	}

	/**
	 * @return The previous camera the {@link NerdSketch} had access to.
	 */
	public NerdAbstractCamera setCamera(final NerdAbstractCamera p_camera) {
		final NerdAbstractCamera toRet = this.previousCamera;
		this.previousCamera = this.currentCamera;
		this.currentCamera = p_camera;
		return toRet;
	}
	// endregion

	// region Unprojection via `world*()` and `getMouseInWorld*()`!
	// region 2D versions!
	public float worldX(final float p_x, final float p_y) {
		return this.worldVec(p_x, p_y, 0).x;
	}

	public float worldY(final float p_x, final float p_y) {
		return this.worldVec(p_x, p_y, 0).y;
	}

	public float worldZ(final float p_x, final float p_y) {
		return this.worldVec(p_x, p_y, 0).z;
	}
	// endregion

	// region 3D versions (should use!).
	public float worldX(final float p_x, final float p_y, final float p_z) {
		return this.worldVec(p_x, p_y, p_z).x;
	}

	public float worldY(final float p_x, final float p_y, final float p_z) {
		return this.worldVec(p_x, p_y, p_z).y;
	}

	public float worldZ(final float p_x, final float p_y, final float p_z) {
		return this.worldVec(p_x, p_y, p_z).z;
	}
	// endregion

	// region `worldVec()`!
	public PVector worldVec(final PVector p_vec) {
		return this.worldVec(p_vec.x, p_vec.y, p_vec.z);
	}

	public PVector worldVec(final float p_x, final float p_y) {
		return this.worldVec(p_x, p_y, 0);
	}

	public PVector worldVec(final float p_x, final float p_y, final float p_z) {
		final PVector toRet = new PVector();
		// Unproject:
		this.UNPROJECTOR.captureViewMatrix((PGraphics3D) super.getGraphics());
		this.UNPROJECTOR.gluUnProject(
				p_x, super.height - p_y,
				// `0.9f`: at the near clipping plane.
				// `0.9999f`: at the far clipping plane. (NO! Calculate EPSILON first! *Then-*)
				// 0.9f + map(mouseY, height, 0, 0, 0.1f),
				PApplet.map(p_z, this.currentCamera.near, this.currentCamera.far, 0, 1),
				toRet);

		return toRet;
	}
	// endregion

	// region Mouse!
	/**
	 * Caching this vector never works. Call this method everytime!~
	 * People recalculate things framely in computer graphics anyway! :joy:
	 */
	public PVector getMouseInWorld() {
		return this.getMouseInWorldFromFarPlane(this.currentCamera.mouseZ);
	}

	public PVector getMouseInWorldFromFarPlane(final float p_distanceFromFarPlane) {
		return this.worldVec(super.mouseX, super.mouseY,
				this.currentCamera.far - p_distanceFromFarPlane + this.currentCamera.near);
	}

	public PVector getMouseInWorldAtZ(final float p_distanceFromCamera) {
		return this.worldVec(super.mouseX, super.mouseY, p_distanceFromCamera);
	}
	// endregion

	// region Touches.
	// /**
	// * Caching this vector never works. Call this method everytime!~
	// * People recalculate things framely in computer graphics anyway! :joy:
	// */
	// public PVector getTouchInWorld(final int p_touchId) {
	// return this.getTouchInWorldFromFarPlane(p_touchId,
	// this.currentCamera.mouseZ);
	// }

	// public PVector getTouchInWorldFromFarPlane(final float p_touchId, final float
	// p_distanceFromFarPlane) {
	// final TouchEvent.Pointer touch = super.touches[p_touchId];
	// return this.worldVec(touch.x, touch.y,
	// this.currentCamera.far - p_distanceFromFarPlane + this.currentCamera.near);
	// }

	// public PVector getTouchInWorldAtZ(final int p_touchId, final float
	// p_distanceFromCamera) {
	// final TouchEvent.Pointer touch = super.touches[p_touchId];
	// return this.worldVec(touch.x, touch.y, p_distanceFromCamera);
	// }
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
