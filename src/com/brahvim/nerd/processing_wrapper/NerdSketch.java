package com.brahvim.nerd.processing_wrapper;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.brahvim.nerd.io.NerdStringsTable;
import com.brahvim.nerd.io.asset_loader.NerdAsset;
import com.brahvim.nerd.io.asset_loader.NerdAssetLoader;
import com.brahvim.nerd.io.asset_loader.NerdAssetsModule;
import com.brahvim.nerd.useless_callback_interfaces.workflow.NerdSketchAllWorkflowsListener;
import com.brahvim.nerd.utils.NerdAwtUtils;
import com.brahvim.nerd.utils.NerdSingleAssignmentBox;
import com.brahvim.nerd.window_management.NerdDisplayModule;
import com.brahvim.nerd.window_management.NerdInputModule;
import com.brahvim.nerd.window_management.NerdWindowModule;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PShape;
import processing.opengl.PJOGL;

public class NerdSketch<SketchPGraphicsT extends PGraphics> extends PApplet implements NerdSketchAllWorkflowsListener {

	// region Inner classes.
	protected static final class NerdSketchOnlyAssetsModule<SketchPGraphicsT extends PGraphics>
			extends NerdAssetsModule<SketchPGraphicsT> {

		public NerdSketchOnlyAssetsModule(final NerdSketch<SketchPGraphicsT> p_sketch) {
			super(p_sketch);
		}

		public <AssetT> NerdAsset<SketchPGraphicsT, AssetT> addAssetAndStartLoading(
				final NerdAssetLoader<SketchPGraphicsT, AssetT> p_type) {
			return super.addAsset(p_type).startLoading();
		}

		public <AssetT> NerdAsset<SketchPGraphicsT, AssetT> addAssetAndStartLoading(
				final NerdAssetLoader<SketchPGraphicsT, AssetT> p_type,
				final Runnable p_onLoad) {
			return super.addAsset(p_type, p_onLoad).startLoading();
		}

	}
	// endregion

	// region Fields.
	// region `public` fields.
	// region Constants.
	// region `static` constants.
	public static final File EXEC_DIR = new File("");
	public static final String EXEC_DIR_PATH = NerdSketch.EXEC_DIR.getAbsolutePath(); // .concat(File.separator);

	public static final File DATA_DIR = new File("data");
	public static final String DATA_DIR_PATH = NerdSketch.DATA_DIR.getAbsolutePath(); // .concat(File.separator);
	// endregion

	// region Instance constants.
	public final Robot ROBOT;
	public final boolean USES_OPENGL;

	// region Necessary `NerdModule`s:
	public final NerdInputModule<SketchPGraphicsT> INPUT;
	public final NerdDisplayModule<SketchPGraphicsT> DISPLAY;
	public final NerdWindowModule<SketchPGraphicsT> GENERIC_WINDOW;
	public final NerdSketchSettings<SketchPGraphicsT> SKETCH_SETTINGS;
	public final NerdSketch.NerdSketchOnlyAssetsModule<SketchPGraphicsT> ASSETS;
	// endregion

	// region `java.awt` objects (some not constant).
	public final GraphicsEnvironment LOCAL_GRAPHICS_ENVIRONMENT = GraphicsEnvironment.getLocalGraphicsEnvironment();
	public GraphicsDevice DEFAULT_JAVA_SCREEN = this.LOCAL_GRAPHICS_ENVIRONMENT.getDefaultScreenDevice();
	public final GraphicsDevice[] JAVA_SCREENS = this.LOCAL_GRAPHICS_ENVIRONMENT.getScreenDevices();
	public DisplayMode DEFAULT_JAVA_SCREEN_MODE = this.DEFAULT_JAVA_SCREEN.getDisplayMode();
	public int DEFAULT_REFRESH_RATE = this.DEFAULT_JAVA_SCREEN_MODE.getRefreshRate();
	// endregion
	// endregion
	// endregion
	// endregion

	// region `protected` fields.
	protected final List<NerdModule<SketchPGraphicsT>> MODULES;
	protected final Map<Class<? extends NerdModule<SketchPGraphicsT>>, NerdModule<SketchPGraphicsT>>
	/* */ CLASSES_TO_MODULES_MAP = new HashMap<>(5);
	public final Map<NerdAbstractGraphics<SketchPGraphicsT>, SketchPGraphicsT>
	/* */ NERD_GRAPHICS_TO_PGRAPHICS_MAP = new HashMap<>(1);

	protected NerdStringsTable globalStringTable;
	protected NerdSingleAssignmentBox<PFont> defaultFont = new NerdSingleAssignmentBox<>();
	protected NerdAbstractGraphics<SketchPGraphicsT> nerdGenericGraphics;

	// Timers! (`millis()` returns `int`s!):
	protected int frameStartTime, pframeStartTime, frameTime, pframeTime;
	protected float inverseFrameRate, pframeRate, pinverseFrameRate;
	// endregion
	// endregion

	// region Construction.
	@SuppressWarnings("unchecked")
	public NerdSketch(final NerdSketchSettings<SketchPGraphicsT> p_settings) {
		Objects.requireNonNull(this.SKETCH_SETTINGS = p_settings, String.format("""
				Please use an instance of some subclass of `%s`, or initialize a `%s` by hand, to make a `%s`.
				You seem to have provided, `null`, to its constructor.""",
				NerdSketchBuilder.class.getSimpleName(),
				NerdSketchSettings.class.getSimpleName(),
				this.getClass().getSimpleName()));

		this.USES_OPENGL = PConstants.P3D.equals(p_settings.renderer);

		// region Stuff involving `NerdModule`s.
		this.MODULES = this.createAndSortModules(p_settings);
		this.ASSETS = this.getNerdModule(NerdSketch.NerdSketchOnlyAssetsModule.class);
		this.GENERIC_WINDOW = this.getNerdModule(NerdWindowModule.class);
		this.DISPLAY = this.getNerdModule(NerdDisplayModule.class);
		this.INPUT = this.getNerdModule(NerdInputModule.class);
		// endregion

		// region Setting the icon of the OpenGL renderer's window.
		if (this.USES_OPENGL)
			PJOGL.setIcon(this.SKETCH_SETTINGS.windowIconPath);
		// endregion

		this.ROBOT = NerdAwtUtils.createAwtRobot();
	}

	// This method will break *any second* Nerd decides to not use "modules".
	// Oh God, do I hope that never happens! (Phew! It likely never will!)
	// (The entire engine is built on this *evolutionary* idea, Brahvim! You idiot!)
	private List<NerdModule<SketchPGraphicsT>> createAndSortModules(
			final NerdSketchSettings<SketchPGraphicsT> p_settings) {
		final NerdSketchSettings<SketchPGraphicsT>.NerdModulesBuilderRegistry nerdModulesAndSettings
		/*   */ = p_settings.nerdModulesBuilderRegistry;
		final List<NerdModule<SketchPGraphicsT>> toRet = new ArrayList<>(
				nerdModulesAndSettings.CLASSES_TO_CONSTRUCTORS_MAP.size());

		// Construct `NerdModule`s using the provided constructors, then add them:
		for (final var entry : nerdModulesAndSettings.CLASSES_TO_CONSTRUCTORS_MAP.entrySet())
			try {
				if (entry == null) {
					System.out.println(
							"`null` entry in the `NerdModulesBuilderRegistry::CLASSES_TO_CONSTRUCTORS_MAP`!");
					continue;
				}

				Objects.requireNonNull(entry,
						"Could not instantiate a `NerdModule` due to the supplying function being `null`.");

				// "Classes to constructors map", remember? The key is the class:
				final Class<? extends NerdModule<SketchPGraphicsT>> moduleClass = entry.getKey();

				// ...and the value? The constructor:
				final NerdModule<SketchPGraphicsT> module = entry.getValue().apply(this,
						nerdModulesAndSettings.CLASSES_TO_SETTINGS_MAP.get(moduleClass));

				if (module == null) {
					System.out.println("Could not include a `NerdModule` due to it being `null`.");
					continue;
				}

				// If we already have a certain `NerdModule`,
				if (this.CLASSES_TO_MODULES_MAP.get(moduleClass) != null) {
					// ...We replace the existing one,
					toRet.removeIf(m -> m.getClass().equals(moduleClass));

					// ..Add the new one at the end:
					toRet.add(module);

					// ..Update it in our map,
					this.CLASSES_TO_MODULES_MAP.put(moduleClass, module);

					// ..and continue the loop!:
					continue;
				}

				// Otherwise, just add as usual:
				toRet.add(module);
				this.CLASSES_TO_MODULES_MAP.put(moduleClass, module);
			} catch (final Exception e) {
				System.err.println("An exception occurred when trying to instantiate `NerdModule`s:");
				e.printStackTrace();
			}

		return toRet;
	}
	// endregion

	// region Processing sketch workflow.
	@Override
	public void settings() {
		// TODO: Move these to `NerdWindowModule`.

		// Placed first, since this restarts the sketch:
		super.orientation(this.SKETCH_SETTINGS.shouldStartPortraitMode
				? PConstants.PORTRAIT
				: PConstants.LANDSCAPE);

		// TODO: Should get this via an `enum` w/ a method via an interface.
		// TODO: See [ https://processing.org/reference/smooth_.html ].
		super.smooth(this.SKETCH_SETTINGS.antiAliasing);

		if (this.SKETCH_SETTINGS.shouldStartFullscreen)
			super.fullScreen(this.SKETCH_SETTINGS.renderer);
		else
			super.size(this.SKETCH_SETTINGS.width, this.SKETCH_SETTINGS.height, this.SKETCH_SETTINGS.renderer);

		this.forEachNerdModule(NerdModule::settings);
	}

	@Override
	public void preSetup() {
		this.forEachNerdModule(NerdModule::preSetup);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void setup() {
		this.nerdGenericGraphics = (NerdAbstractGraphics<SketchPGraphicsT>)
		/*   */ NerdAbstractGraphics.createNerdGenericGraphicsWrapperForSketch(this);

		super.surface.setResizable(this.SKETCH_SETTINGS.canResize);
		this.preSetup();

		super.registerMethod("pre", this);
		super.registerMethod("post", this);
		super.frameRate(this.SKETCH_SETTINGS.frameRateLimit < 1
				? this.DEFAULT_REFRESH_RATE
				: this.SKETCH_SETTINGS.frameRateLimit);
		super.surface.setTitle(this.SKETCH_SETTINGS.initialWindowTitle);

		this.defaultFont.assign(
				super.createFont("SansSerif", 72 * this.GENERIC_WINDOW.scr));

		// ...Also makes these changes to `this.NerdGenericGraphics`, haha:
		super.background(0); // ..This, instead of `NerdAbstractCamera::clear()`.
		// ...Also makes these changes to `this.NerdGenericGraphics`, haha:
		super.background(0); // ..This, instead of `NerdAbstractCamera::clear()`.
		super.textFont(this.defaultFont.getValue());
		super.rectMode(PConstants.CENTER);
		super.imageMode(PConstants.CENTER);
		super.textAlign(PConstants.CENTER, PConstants.CENTER);

		if (this.SKETCH_SETTINGS.canResize)
			this.GENERIC_WINDOW.setResizable(true);
		this.GENERIC_WINDOW.centerWindow();

		this.postSetup();
	}

	@Override
	public void postSetup() {
		this.forEachNerdModule(NerdModule::postSetup);
	}

	@Override
	public void pre() {
		final int timestamp = super.millis(); // Timestamp!

		this.pinverseFrameRate = this.inverseFrameRate;
		this.inverseFrameRate = 1.0f / super.frameRate;

		this.pframeStartTime = this.frameStartTime;
		this.frameStartTime = timestamp;

		this.pframeTime = this.frameTime;
		this.frameTime = this.frameStartTime - this.pframeStartTime;

		this.forEachNerdModule(NerdModule::pre);
	}

	@Override
	public void preDraw() {
		this.NERD_GRAPHICS_TO_PGRAPHICS_MAP.keySet().forEach(NerdAbstractGraphics::preDraw);
		this.forEachNerdModule(NerdModule::preDraw);
	}

	@Override
	public void draw() {
		// try (NerdMillisTimer millisTimer = new
		// NerdMillisTimer("`NerdSketch::draw()`")) {

		this.preDraw();
		this.NERD_GRAPHICS_TO_PGRAPHICS_MAP.keySet().forEach(NerdAbstractGraphics::draw);
		this.forEachNerdModule(NerdModule::draw);
		this.postDraw();

		// } catch (final Exception e) {
		// this.e.printStackTrace();
		// }
	}

	@Override
	public void postDraw() {
		this.NERD_GRAPHICS_TO_PGRAPHICS_MAP.keySet().forEach(NerdAbstractGraphics::postDraw);
		this.forEachNerdModule(NerdModule::postDraw);
	}

	@Override
	public void post() {
		this.forEachNerdModule(NerdModule::post);
	}

	@Override
	public void exit() {
		this.forEachNerdModule(NerdModule::exit);
		super.exit();
	}

	@Override
	public void dispose() {
		this.forEachNerdModule(NerdModule::dispose);
		super.dispose();
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

	// Keyboard:

	@Override
	public void keyPressed() {
		if (this.keyCode == 27)
			if (this.SKETCH_SETTINGS.preventCloseOnEscape)
				super.key = '\0';
		// .....^^^^^^^^^^^^^^^^^.....
		// Processing checks this field, `PApplet::key` to observe what key was pressed.
		// By setting it to `\0`, we disallow exiting.

		if (this.SKETCH_SETTINGS.canFullscreen) {

			if (this.SKETCH_SETTINGS.canAltEnterFullscreen
					&& super.keyCode == KeyEvent.VK_ENTER
					&& this.INPUT.keysPressedIsEither(KeyEvent.VK_ALT, 19))
				this.GENERIC_WINDOW.fullscreen = !this.GENERIC_WINDOW.fullscreen;

			else if (this.SKETCH_SETTINGS.canF11Fullscreen) {
				// `KeyEvent.VK_ADD` is `107`, but here, it's actually `F11`!:
				if (this.USES_OPENGL) {
					if (super.keyCode == 107)
						this.GENERIC_WINDOW.fullscreen = !this.GENERIC_WINDOW.fullscreen;
				} else {
					if (super.keyCode == KeyEvent.VK_F11)
						this.GENERIC_WINDOW.fullscreen = !this.GENERIC_WINDOW.fullscreen;
				}
			}

		}

		// for (final NerdModule m : this.MODULES)
		// m.keyPressed(); // If things slow down too much, this guy's your bud.

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

	// Touchscreen:

	public void touchStarted() {
		this.forEachNerdModule(NerdModule::touchStarted);
	}

	public void touchMoved() {
		this.forEachNerdModule(NerdModule::touchMoved);
	}

	public void touchEnded() {
		this.forEachNerdModule(NerdModule::touchEnded);
	}

	// Window:

	public void monitorChanged() {
		this.forEachNerdModule(NerdModule::focusGained);
	}

	public void fullscreenChanged(final boolean p_state) {
		this.forEachNerdModule(NerdModule::fullscreenChanged, p_state);
	}

	// @Override
	// public void frameMoved(final int x, final int y) {
	// System.out.println("`NerdSketch::frameMoved()`");
	// }

	// @Override
	// public void frameResized(final int w, final int h) {
	// System.out.println("`NerdSketch::frameResized()`");
	// }

	@Override
	public void focusLost() {
		super.focusLost();
		super.focused = false;
		this.forEachNerdModule(NerdModule::focusLost);
	}

	@Override
	public void focusGained() {
		super.focusGained();
		super.focused = true;
		this.forEachNerdModule(NerdModule::focusGained);
	}
	// endregion

	// region `NerdModule` management.
	// // @SuppressWarnings("unchecked")
	// public <RetModulePGraphicsT extends PGraphics, RetModuleT extends
	// NerdModule<RetModulePGraphicsT>>
	// /* */ RetModuleT getNerdModule(final Class<? extends RetModuleT>
	// p_moduleClass) {
	// for (final NerdModule<SketchPGraphicsT> m : this.MODULES)
	// if (p_moduleClass.isInstance(m))
	// return (RetModuleT) p_moduleClass.cast(m);
	//
	// throw new NoSuchElementException(String.format(
	// "No `%s` of type `%s` was found.",
	// NerdModule.class.getSimpleName(),
	// p_moduleClass.getSimpleName()));
	// }

	// @SuppressWarnings("unchecked")
	public <RetModuleT extends NerdModule<SketchPGraphicsT>>
	/*   */ RetModuleT getNerdModule(
			final Class<? extends RetModuleT> p_moduleClass) {
		for (final NerdModule<SketchPGraphicsT> m : this.MODULES)
			if (p_moduleClass.isInstance(m))
				return p_moduleClass.cast(m);

		throw new NoSuchElementException(String.format(
				"No `%s` of type `%s` was found.",
				NerdModule.class.getSimpleName(),
				p_moduleClass.getSimpleName()));
	}

	public void forEachNerdModule(final Consumer<NerdModule<SketchPGraphicsT>> p_task) {
		if (p_task == null)
			return;

		this.MODULES.forEach(p_task);
	}

	public <OtherArgT> void forEachNerdModule(
			final BiConsumer<NerdModule<SketchPGraphicsT>, OtherArgT> p_task, final OtherArgT p_otherArg) {
		if (p_task == null)
			return;

		for (final NerdModule<SketchPGraphicsT> m : this.MODULES)
			p_task.accept(m, p_otherArg);
	}

	public NerdAssetsModule<SketchPGraphicsT> getGlobalNerdAssetsModule() {
		return this.ASSETS;
	}

	public NerdDisplayModule<SketchPGraphicsT> getNerdDisplayModule() {
		return this.DISPLAY;
	}

	public NerdWindowModule<SketchPGraphicsT> getNerdWindowModule() {
		return this.GENERIC_WINDOW;
	}

	public NerdInputModule<SketchPGraphicsT> getNerdInputModule() {
		return this.INPUT;
	}
	// endregion

	// region Utilities!
	// region Getters.
	public int getFrameTime() {
		return this.frameTime;
	}

	public PFont getDefaultFont() {
		return this.defaultFont.getValue();
	}

	public int getPframeStartTime() {
		return this.pframeStartTime;
	}

	public int getPreviousFrameTime() {
		return this.pframeTime;
	}

	public float getInverseFrameRate() {
		return this.inverseFrameRate;
	}

	public float getPreviousFrameRate() {
		return this.pframeRate;
	}

	public final int getFrameStartTime() {
		return this.frameStartTime;
	}

	public float getPreviousDeltaTimeFactor() {
		return this.pinverseFrameRate;
	}

	public float getPreviousInverseFrameRate() {
		return this.pinverseFrameRate;
	}

	public String getRendererPConstantString() {
		return this.SKETCH_SETTINGS.renderer;
	}

	public NerdStringsTable getGlobalStringsTable() {
		return this.globalStringTable;
	}

	public NerdAbstractGraphics<SketchPGraphicsT> getNerdGenericGraphics() {
		return this.nerdGenericGraphics;
	}
	// endregion

	// region `frameRate()` overloads.
	/**
	 * Sets the framerate to the maximum possible.
	 * <p>
	 * Warning: this can cause issues!
	 */
	public void frameRateMaximize() {
		super.frameRate(1000);
	}

	/** Sets the framerate to the current display's refresh rate. */
	public void frameRateMinimize() {
		super.frameRate(this.DISPLAY.displayRefreshRate);
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
	@SuppressWarnings("unchecked")
	public SketchPGraphicsT createGraphics(
			final int p_width, final int p_height, final String p_renderer, final String p_path) {
		return (SketchPGraphicsT) super.makeGraphics(p_width, p_height, p_renderer, p_path, false);
	}

	@Override
	public SketchPGraphicsT createGraphics(final int p_width, final int p_height, final String p_renderer) {
		return this.createGraphics(p_width, p_height, p_renderer, NerdSketch.EXEC_DIR_PATH);
	}

	@Override
	public SketchPGraphicsT createGraphics(final int p_width, final int p_height) {
		return this.createGraphics(p_width, p_height, this.getRendererPConstantString());
	}
	// endregion

	// region Utilitarian overloads.
	public SketchPGraphicsT createGraphics(final float p_width, final float p_height) {
		return this.createGraphics((int) p_width, (int) p_height, this.getRendererPConstantString());
	}

	public SketchPGraphicsT createGraphics(final float p_size) {
		final int size = (int) p_size;
		return this.createGraphics(size, size, this.getRendererPConstantString());
	}

	public SketchPGraphicsT createGraphics(final int p_size) {
		return this.createGraphics(p_size, p_size, this.getRendererPConstantString());
	}

	public SketchPGraphicsT createGraphics() {
		return this.createGraphics(super.width, super.height, this.getRendererPConstantString());
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
	// endregion

}
