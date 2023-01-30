package com.brahvim.nerd.papplet_wrapper;

import java.awt.DisplayMode;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.MouseInfo;
import java.awt.Point;
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
import java.util.LinkedHashSet;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import com.brahvim.nerd.io.StringTable;
import com.brahvim.nerd.math.Unprojector;
import com.brahvim.nerd.processing_wrappers.NerdCam;
import com.brahvim.nerd.processing_wrappers.NerdCameraBuilder;
import com.brahvim.nerd.scene_api.NerdScene;
import com.brahvim.nerd.scene_api.SceneManager;
import com.jogamp.newt.opengl.GLWindow;

import processing.awt.PSurfaceAWT;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PShape;
import processing.core.PVector;
import processing.opengl.PGraphics3D;
import processing.opengl.PJOGL;

public class Sketch extends PApplet {

    // region Listener interfaces and abstract classes.
    @FunctionalInterface
    public interface SketchInsideListener {
        public void listen(Sketch p_sketch);
    }

    // region Input events.
    // Used classes instead of interfaces for these (two) reasons:
    /*
     * - No security for `ALL_REFERENCES` from the user! It'll be `public`!
     * - For registering the reference into the `ALL_REFERENCES` collection,
     * a `default` method may be used. However, the method is overriable,
     * meaning that registering code becomes modifiable, letting the user
     * accidentally change what they weren't supposed to!
     */

    public abstract class SketchMouseListener {
        public SketchMouseListener() {
            SKETCH.MOUSE_LISTENERS.add(this);
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

        // @SuppressWarnings("unused")
        public void mouseWheel(processing.event.MouseEvent p_mouseEvent) {
        }
        // endregion

    }

    public abstract class SketchTouchListener {
        public SketchTouchListener() {
            SKETCH.TOUCH_LISTENERS.add(this);
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

    public abstract class SketchWindowListener {
        public SketchWindowListener() {
            SKETCH.WINDOW_LISTENERS.add(this);
        }

        // region Window focus events.
        public void resized() {
        }

        public void focusLost() {
        }

        public void focusGained() {
        }
        // endregion

    }

    public abstract class SketchKeyboardListener {
        public SketchKeyboardListener() {
            SKETCH.KEYBOARD_LISTENERS.add(this);
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
    // region Callback orders.
    public static enum CallbackOrder {
        SCENE(), LAYER();
    }

    public CallbackOrder PRE_CALLBACK_ORDER = CallbackOrder.SCENE;

    /**
     * Ignored when using {@link PConstants.P2D} or {@link PConstants.P3D}
     * as the renderer.
     */
    public CallbackOrder DRAW_CALLBACK_ORDER = CallbackOrder.LAYER;

    public CallbackOrder POST_CALLBACK_ORDER = CallbackOrder.LAYER;
    // endregion

    // region Constants.
    // region `static` constants.
    public final static File EXEC_DIR = new File("");
    public final static String EXEC_DIR_PATH = Sketch.EXEC_DIR.getAbsolutePath().concat(File.separator);

    public final static File DATA_DIR = new File("data");
    public final static String DATA_DIR_PATH = Sketch.DATA_DIR.getAbsolutePath().concat(File.separator);
    public final static String DATA_DIR_PATH_TO_DRIVE_ROOT_SUFFIX = Sketch.getPathToRootFrom(Sketch.DATA_DIR_PATH);

    public final static GraphicsDevice[] JAVA_SCREENS = GraphicsEnvironment
            .getLocalGraphicsEnvironment().getScreenDevices();

    public final static DisplayMode DEFAULT_JAVA_SCREEN_MODE = GraphicsEnvironment
            .getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();

    public final static int DEFAULT_REFRESH_RATE = GraphicsEnvironment.getLocalGraphicsEnvironment()
            .getDefaultScreenDevice().getDisplayMode().getRefreshRate();

    public final static char[] STANDARD_KEYBOARD_SYMBOLS = {
            '\'', '\"', '-', '=', '`', '~', '!', '@', '#', '$',
            '%', '^', '&', '*', '(', ')', '{', '}', '[',
            ']', ';', ',', '.', '/', '\\', ':', '|', '<',
            '>', '_', '+', '?'
    };
    // endregion

    public final String NAME;
    public final Sketch SKETCH;
    public final String RENDERER;
    public final StringTable STRINGS;
    public final NerdCam DEFAULT_CAMERA;
    public final int INIT_WIDTH, INIT_HEIGHT;
    public final Class<? extends NerdScene> FIRST_SCENE_CLASS;
    public final boolean CLOSE_ON_ESCAPE, STARTED_FULLSCREEN, INITIALLY_RESIZABLE,
            CAN_FULLSCREEN, F11_FULLSCREEN, ALT_ENTER_FULLSCREEN, DO_FAKE_2D_CAMERA = false;
    // endregion

    // region Window object references.
    public GLWindow glWindow;
    public JFrame sketchFrame;
    // (Why check for errors at all? You know what renderer you used!)
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
    public boolean cursorConfined, cursorVisible = true; // nO previoS versiuN!11!!
    public NerdCam previousCamera, currentCamera; // CAMERA! (wher lite?! wher accsunn?!)
    public PVector mouse = new PVector(), pmouse = new PVector(); // MOUS!

    public boolean pmouseLeft, pmouseMid, pmouseRight; // Previous frame...
    public float pmouseScroll, pmouseScrollDelta;
    // endregion

    // region "Dimensions".
    public int frameStartTime, pframeTime, frameTime;
    public float cx, cy, qx, qy, q3x, q3y;
    public int pwidth, pheight;
    // endregion
    // endregion

    // region `private` ~~/ `protected`~~ fields.
    private SceneManager sceneMan; // Don't use static initialization for this..?
    public final String ICON_PATH;
    private final Unprojector unprojector;
    // `LinkedHashSet`s preserve order (and also disallow element repetition)!
    private final LinkedHashSet<Integer> keysHeld = new LinkedHashSet<>(5); // `final` to avoid concurrency issues.
    // endregion

    // region Listeners!
    private final SketchInsideListener EXIT_LISTENER, DISPOSAL_LISTENER, SETUP_LISTENER;
    private final LinkedHashSet<SketchMouseListener> MOUSE_LISTENERS = new LinkedHashSet<>(1);
    private final LinkedHashSet<SketchTouchListener> TOUCH_LISTENERS = new LinkedHashSet<>(1);
    private final LinkedHashSet<SketchWindowListener> WINDOW_LISTENERS = new LinkedHashSet<>(1);
    private final LinkedHashSet<SketchKeyboardListener> KEYBOARD_LISTENERS = new LinkedHashSet<>(1);
    // endregion

    // region Constructor[s], `settings()`...
    public Sketch(SketchBuilder.SketchKey p_key) {
        // region Verify and 'use' key.
        if (p_key == null) {
            throw new IllegalArgumentException("""
                    Please use a `SketchBuilder` instance to make a `Sketch`!""");
        } else if (p_key.isUsed()) {
            throw new IllegalArgumentException("""
                    Please use a `SketchBuilder` instance to make a `Sketch`! That is a used key!""");
        } else if (!p_key.isFor(this.getClass()))
            throw new IllegalArgumentException("""
                    Please use a `SketchBuilder` instance to make a `Sketch`! That key is not for me!""");

        p_key.use();
        // endregion

        // region Key settings.
        // region Setting `Sketch.CallbackOrder`s.
        if (p_key.preCallOrder != null)
            this.PRE_CALLBACK_ORDER = p_key.preCallOrder;

        if (p_key.drawCallOrder != null)
            this.DRAW_CALLBACK_ORDER = p_key.drawCallOrder;

        if (p_key.postCallOrder != null)
            this.POST_CALLBACK_ORDER = p_key.postCallOrder;
        // endregion

        this.SKETCH = this;
        this.NAME = p_key.name;
        this.ICON_PATH = p_key.iconPath;
        this.RENDERER = p_key.renderer;
        this.DEFAULT_CAMERA = new NerdCameraBuilder(this).build();
        this.FIRST_SCENE_CLASS = p_key.firstScene;
        this.INITIALLY_RESIZABLE = p_key.canResize;
        this.CAN_FULLSCREEN = !p_key.cannotFullscreen;
        this.CLOSE_ON_ESCAPE = !p_key.dontCloseOnEscape;
        this.F11_FULLSCREEN = !p_key.cannotF11Fullscreen;
        this.STARTED_FULLSCREEN = p_key.startedFullscreen;
        this.ALT_ENTER_FULLSCREEN = !p_key.cannotAltEnterFullscreen;
        // endregion

        // region ...of course, more key settings!
        this.pfullscreen = !this.fullscreen;
        this.unprojector = new Unprojector();
        this.sceneMan = new SceneManager(this);
        this.currentCamera = this.DEFAULT_CAMERA;
        this.fullscreen = this.STARTED_FULLSCREEN;

        this.EXIT_LISTENER = p_key.exitListener;
        this.SETUP_LISTENER = p_key.setupListener;
        this.DISPOSAL_LISTENER = p_key.disposalListener;
        // endregion

        // region Setting icons.
        if (this.RENDERER == PConstants.P2D || this.RENDERER == PConstants.P3D)
            PJOGL.setIcon(this.ICON_PATH);
        // endregion

        // region Loading the string table.
        StringTable loadedTable = null;

        try {
            loadedTable = new StringTable(p_key.stringTablePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            this.STRINGS = loadedTable;
        }
        // endregion

        // region Loading scenes to preload.
        for (Class<? extends NerdScene> c : p_key.scenesToPreload) {
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
        // endregion

        p_key.sketchconstructedListener.listen(this);
    }

    @Override
    public void settings() {
        // Crazy effects (no un-decoration!), I guess:
        // if (this.STARTED_FULLSCREEN)
        // super.fullScreen(this.RENDERER);
        // else

        super.size(this.INIT_WIDTH, this.INIT_HEIGHT, this.RENDERER);
    }
    // endregion

    // region Processing sketch workflow.
    @Override
    public void setup() {
        this.updateRatios();
        super.surface.setTitle(this.NAME);
        super.registerMethod("pre", this);
        super.registerMethod("post", this);
        super.frameRate(Sketch.DEFAULT_REFRESH_RATE);

        // TODO: Deal with multiple monitors!
        // [https://stackoverflow.com/questions/21585121/java-getting-mouse-location-on-multiple-monitor-environment]
        // TODO: Find app monitor. AWT tells what mouse's on. Check if app tracks mouse.
        {
            // DisplayMode mode = GraphicsEnvironment.getLocalGraphicsEnvironment()
            // .getScreenDevices()[0].getDisplayMode();

            // super.displayWidth = mode.getWidth();
            // super.displayHeight = mode.getHeight();
        }

        // I should make a super slow "convenience" method to do this with `Runnable`s!
        // :joy:!

        // Renderer-specific object initialization and settings!:
        switch (this.RENDERER) {
            case PConstants.P2D, PConstants.P3D:
                this.glWindow = (GLWindow) super.surface.getNative();

                if (this.INITIALLY_RESIZABLE)
                    this.glWindow.setResizable(true);

                // Done in the constructor! `setup()`'s too late for this!:
                // PJOGL.setIcon(this.iconPath);
                break;

            case PConstants.JAVA2D:
                PSurfaceAWT.SmoothCanvas canvas = (PSurfaceAWT.SmoothCanvas) super.surface.getNative();
                this.sketchFrame = (JFrame) canvas.getFrame();

                if (this.INITIALLY_RESIZABLE)
                    super.surface.setResizable(true);

                super.surface.setIcon(super.loadImage(this.ICON_PATH));
                // "Loose" image loading is usually not a good idea, but I guess it's here...
                // super.surface.setIcon(super.loadImage(this.iconPath));
                break;
        }

        // Should I make a "default styling" `SketchInsideListener`?
        super.rectMode(PConstants.CENTER);
        super.imageMode(PConstants.CENTER);
        super.textAlign(PConstants.CENTER, PConstants.CENTER);

        if (this.SETUP_LISTENER != null)
            this.SETUP_LISTENER.listen(this);
    }

    public void pre() {
        // When the window is resized, do the following!:
        if (!(this.pwidth == super.width || this.pheight == super.height)) {
            this.updateRatios();
            for (SketchWindowListener l : this.WINDOW_LISTENERS)
                l.resized();
        }

        if (this.pfullscreen != this.fullscreen)
            this.centerWindow();

        this.mouseScrollDelta = this.mouseScroll - this.pmouseScroll;
        this.mouse.set(super.mouseX, super.mouseY);
        if (this.RENDERER == PConstants.P3D)
            this.unprojectMouse();

        this.sceneMan.pre();
    }

    @Override
    public void draw() {
        this.frameStartTime = super.millis(); // Timestamp.
        this.frameTime = this.frameStartTime - this.pframeTime;
        this.pframeTime = this.frameStartTime;

        this.mouseRight = super.mouseButton == PConstants.RIGHT && super.mousePressed;
        this.mouseMid = super.mouseButton == PConstants.CENTER && super.mousePressed;
        this.mouseLeft = super.mouseButton == PConstants.LEFT && super.mousePressed;

        // region Apply the camera!:
        if (this.currentCamera != null)
            switch (this.RENDERER) {
                // "ToDo: iMpLeMeNt a 2d cAmErA."
                // if (this.DO_FAKE_2D_CAMERA);

                case PConstants.JAVA2D: // If using the 2D renderer, don't do any of these!
                    this.currentCamera.runScript();
                    this.currentCamera.clear();
                    break;

                case PConstants.P3D, PConstants.P2D:
                    this.currentCamera.apply(); // Do all three tasks!
                    break;
            }
        // endregion

        // region If it doesn't yet exist, construct the scene!
        if (super.frameCount == 1 && this.sceneMan.getCurrentScene() == null) {
            this.sceneMan.startScene(this.FIRST_SCENE_CLASS);
        }
        // endregion

        this.sceneMan.draw();
    }

    public void post() {
        this.fullScreenCheck();

        // region Previous state updates!!!
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
        this.pmouseScrollDelta = this.mouseScrollDelta;
        // endregion

        for (PVector v : this.UNPROJ_TOUCHES)
            this.PREV_UNPROJ_TOUCHES.add(v);

        this.sceneMan.post();
    }

    @Override
    public void exit() {
        if (this.EXIT_LISTENER != null)
            this.EXIT_LISTENER.listen(this);

        super.exit();
    }

    @Override
    public void dispose() {
        if (this.DISPOSAL_LISTENER != null)
            this.DISPOSAL_LISTENER.listen(this);

        super.dispose();
    }
    // endregion

    // region Processing's event callbacks! REMEMBER `this.sceneMan`! :joy:
    // region Mouse events.
    public void mousePressed() {
        for (SketchMouseListener l : this.MOUSE_LISTENERS) {
            l.mousePressed();
        }
    }

    public void mouseReleased() {
        for (SketchMouseListener l : this.MOUSE_LISTENERS) {
            l.mouseReleased();
        }
    }

    public void mouseMoved() {
        for (SketchMouseListener l : this.MOUSE_LISTENERS) {
            l.mouseMoved();
        }
    }

    public void mouseClicked() {
        for (SketchMouseListener l : this.MOUSE_LISTENERS) {
            l.mouseClicked();
        }
    }

    public void mouseDragged() {
        for (SketchMouseListener l : this.MOUSE_LISTENERS) {
            l.mouseDragged();
        }
    }

    // @SuppressWarnings("unused")
    public void mouseWheel(processing.event.MouseEvent p_mouseEvent) {
        for (SketchMouseListener l : this.MOUSE_LISTENERS) {
            l.mouseWheel(p_mouseEvent);
        }
    }
    // endregion

    // region Keyboard events.
    public void keyTyped() {
        for (SketchKeyboardListener l : this.KEYBOARD_LISTENERS) {
            // ...could call that callback here directly, but I decided this!:
            // "Filter these keys using the utility method(s)?"

            // ...and thus-this check was born!:
            if (Sketch.isTypeable(super.key))
                l.keyTyped();
        }
    }

    public void keyPressed() {
        if (!this.CLOSE_ON_ESCAPE) {
            if (super.keyCode == 27)
                super.key = ' ';
            else
                System.out.println("`Esc` exit!");
        }

        if (this.CAN_FULLSCREEN) {
            if (this.ALT_ENTER_FULLSCREEN) {
                if (super.keyCode == KeyEvent.VK_ENTER &&
                        this.anyGivenKeyIsPressed(KeyEvent.VK_ALT, 19 /* Same as `VK_PAUSE`. */)) {
                    System.out.println("`Alt`-`Enter` fullscreen!");
                    this.fullscreen = !this.fullscreen;
                }
            }

            if (this.F11_FULLSCREEN) {
                switch (this.RENDERER) {
                    case PConstants.P2D, PConstants.P3D:
                        if (super.keyCode == 107) { // `KeyEvent.VK_ADD` is `107`, but here, it's `F11`!
                            System.out.println("`F11` fullscreen!");
                            this.fullscreen = !this.fullscreen;
                        }
                        break;

                    case PConstants.JAVA2D:
                        if (super.keyCode == KeyEvent.VK_F11) {
                            System.out.println("`F11` fullscreen!");
                            this.fullscreen = !this.fullscreen;
                        }
                        break;
                }
            }
        }

        this.keysHeld.add(super.keyCode);

        for (SketchKeyboardListener l : this.KEYBOARD_LISTENERS) {
            l.keyPressed();
        }
    }

    public void keyReleased() {
        try {
            this.keysHeld.remove(super.keyCode);
        } catch (IndexOutOfBoundsException e) {
        }

        for (SketchKeyboardListener l : this.KEYBOARD_LISTENERS) {
            l.keyReleased();
        }
    }
    // endregion

    // region Touch events.
    public void touchStarted() {
        for (SketchTouchListener l : this.TOUCH_LISTENERS) {
            l.touchStarted();
        }
    }

    public void touchMoved() {
        for (SketchTouchListener l : this.TOUCH_LISTENERS) {
            l.touchMoved();
        }
    }

    public void touchEnded() {
        for (SketchTouchListener l : this.TOUCH_LISTENERS) {
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
            for (SketchWindowListener l : this.WINDOW_LISTENERS) {
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
            for (SketchWindowListener l : this.WINDOW_LISTENERS) {
                l.focusLost();
            }
    }
    // endregion
    // endregion

    // region Utilities!~
    public void updateRatios() {
        this.cx = super.width * 0.5f;
        this.cy = super.height * 0.5f;
        this.qx = this.cx * 0.5f;
        this.qy = this.cy * 0.5f;
        this.q3x = this.cx + this.qx;
        this.q3y = this.cy + this.qy;
    }

    public void unprojectMouse() {
        if (this.currentCamera == null)
            return;

        float originalNear = this.currentCamera.near;
        this.currentCamera.near = this.currentCamera.mouseZ;
        this.currentCamera.applyMatrix();

        // Unproject:
        this.unprojector.captureViewMatrix((PGraphics3D) g);
        // `0.9f`: at the near clipping plane.
        // `0.9999f`: at the far clipping plane.
        this.unprojector.gluUnProject(
                super.mouseX, super.height - super.mouseY,
                // 0.9f + map(mouseY, height, 0, 0, 0.1f),
                0, this.mouse);

        this.currentCamera.near = originalNear;
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
         * // ^^^ (...that basically, this `u.z` modfication will go unchanged,
         * // no matter what un-projection method you use!:)
         * u.z = touches[i].pressure; // Should be accessed in some other way, but
         * whatever...
         * 
         * this.UNPROJ_TOUCHES.add(u);
         * }
         */
    }

    public void centerWindow() {
        this.updateRatios(); // You called this function when the window changed its size or position, right?
        // Remember: computers with multiple displays exist! We shouldn't cache this:

        final int WIDTH = Sketch.DEFAULT_JAVA_SCREEN_MODE.getWidth(),
                HEIGHT = Sketch.DEFAULT_JAVA_SCREEN_MODE.getHeight();

        int winX = (int) ((WIDTH * 0.5f) - this.cx),
                winY = (int) ((HEIGHT * 0.5f) - this.cy);

        switch (this.RENDERER) {
            case PConstants.P3D, PConstants.P2D -> this.glWindow.setPosition(winX, winY);
            default -> super.surface.setLocation(winX, winY);
        }

        // super.surface.setLocation(winX, winY);
        // (Well, changing the display does NOT effect those variables in any way :|)
    }

    public void fullScreenCheck() {
        switch (this.RENDERER) {
            case PConstants.JAVA2D:
                // Fullscreen?
                // https://stackoverflow.com/a/11570414/13951505

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

                if (cursorVisible)
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

                    long fsStartMillis = System.currentTimeMillis();

                    while (this.fullscreen != this.glWindow.isFullscreen())
                        if (System.currentTimeMillis() - fsStartMillis > 5000)
                            break; // Throw an exception instead?

                }

                this.glWindow.confinePointer(this.cursorConfined);
                while (this.cursorConfined ? !this.glWindow.isPointerConfined() : this.glWindow.isPointerConfined())
                    ;

                this.glWindow.setPointerVisible(this.cursorVisible);
                while (this.cursorVisible ? !this.glWindow.isPointerVisible() : this.glWindow.isPointerVisible())
                    ;
                break;
        }
    }

    // region Overloads for `getPathToRootFrom()`.
    public static String getPathToRootFrom(File p_path) {
        return getPathToRootFrom(p_path.getAbsolutePath());
    }

    public static String getPathToRootFrom(String p_path) {
        final int PATH_LEN = p_path.length(), LAST_CHAR_ID = PATH_LEN - 1;
        StringBuilder toRetBuilder = new StringBuilder();

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

    // region Drawing utilities!
    // region From `PGraphics`.
    public void translate(PVector p_translation) {
        super.translate(p_translation.x, p_translation.y, p_translation.z);
    }

    public void scale(PVector p_scalingVector) {
        super.scale(p_scalingVector.x, p_scalingVector.y, p_scalingVector.z);
    }

    // region `screenX()`, `screenY()` and `screenZ()`, `PVector` overloads.
    // "Oh! And when the `z` is `-1`, you just add this and sub that. Optimization!"
    // - That ONE Mathematician.
    public float screenX(PVector p_vec) {
        return p_vec.z == 0
                ? super.screenX(p_vec.x, p_vec.y)
                : super.screenX(p_vec.x, p_vec.y, p_vec.z);
    }

    public float screenY(PVector p_vec) {
        return p_vec.z == 0
                ? super.screenY(p_vec.x, p_vec.y)
                : super.screenY(p_vec.x, p_vec.y, p_vec.z);
    }

    public float screenZ(PVector p_vec) {
        // Hmmm...
        // ..so `z` cannot be `0` here.
        // ..and `x` and `y` cannot be ignored!
        // "No room for optimization here!"
        return super.screenZ(p_vec.x, p_vec.y, p_vec.z);
    }
    // endregion

    public void rotate(PVector p_rotationVector) {
        super.rotateX(p_rotationVector.x);
        super.rotateY(p_rotationVector.y);
        super.rotateZ(p_rotationVector.z);
    }

    public void camera(NerdCam p_cam) {
        super.camera(p_cam.pos.x, p_cam.pos.y, p_cam.pos.z,
                p_cam.center.x, p_cam.center.y, p_cam.center.z,
                p_cam.up.x, p_cam.up.y, p_cam.up.z);
    }

    public void perspective(NerdCam p_cam) {
        super.perspective(p_cam.fov, (float) super.width / (float) super.height, p_cam.near, p_cam.far);
    }

    public void ortho(NerdCam p_cam) {
        super.ortho(-this.cx, this.cx, -this.cy, this.cy, p_cam.near, p_cam.far);
    }

    // region The billion `image()` overloads.
    // region For `PImage`s.
    public void image(PImage p_image) {
        super.image(p_image, 0, 0);
    }

    public void image(PImage p_image, PVector p_pos) {
        super.pushMatrix();
        super.translate(p_pos.x, p_pos.y, p_pos.z);
        super.image(p_image, 0, 0);
        super.popMatrix();
    }

    public void image(PImage p_image, float p_scale) {
        super.image(p_image, 0, 0, p_scale, p_scale);
    }

    public void image(PImage p_image, PVector p_pos, float p_scaleMod) {
        super.pushMatrix();
        super.translate(p_pos.x, p_pos.y, p_pos.z);
        this.image(p_image, p_pos.x, p_pos.y, p_scaleMod, p_scaleMod);
        super.popMatrix();
    }

    public void image(PImage p_image, float p_x, float p_y, float p_scaleMod) {
        this.image(p_image, p_x, p_y, p_scaleMod, p_scaleMod);
    }
    // endregion

    // region For `PGraphics`.
    public void image(PGraphics p_graphics) {
        super.image(p_graphics, 0, 0);
    }

    public void image(PGraphics p_graphics, PVector p_pos) {
        super.pushMatrix();
        super.translate(p_pos.x, p_pos.y, p_pos.z);
        super.image(p_graphics, 0, 0);
        super.popMatrix();
    }

    public void image(PGraphics p_graphics, float p_scale) {
        super.image(p_graphics, 0, 0, p_scale, p_scale);
    }

    public void image(PGraphics p_graphics, PVector p_pos, float p_scale) {
        super.pushMatrix();
        super.translate(p_pos.x, p_pos.y, p_pos.z);
        this.image(p_graphics, 0, 0, p_scale, p_scale);
        super.popMatrix();
    }

    public void image(PGraphics p_graphics, float p_x, float p_y, float p_scaleMod) {
        this.image(p_graphics, p_x, p_y,
                p_graphics.width * p_scaleMod,
                p_graphics.height * p_scaleMod);
    }

    public void image(PGraphics p_graphics, PVector p_pos, float p_width, float p_height) {
        super.pushMatrix();
        super.translate(p_pos.x, p_pos.y, p_pos.z);
        this.image(p_graphics, p_pos.x, p_pos.y, p_width, p_height);
        super.popMatrix();
    }
    // endregion
    // endregion

    // These simply don't work in `PApplet`...:
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

    public PImage svgToImage(PShape p_shape, float p_width, float p_height) {
        if (p_shape == null)
            throw new NullPointerException("`svgToImage(null , p_width, p_height)` won't work.");

        PGraphics buffer = super.createGraphics(
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
    public PGraphics createGraphics(int w, int h, String renderer, String path) {
        return super.makeGraphics(w, h, renderer, path, false);
    }

    @Override
    public PGraphics createGraphics(int w, int h, String renderer) {
        return this.createGraphics(w, h, renderer, Sketch.EXEC_DIR_PATH);
    }

    @Override
    public PGraphics createGraphics(int p_width, int p_height) {
        return this.createGraphics(p_width, p_height, super.sketchRenderer());
    }
    // endregion

    // region Utilitarian overloads.
    public PGraphics createGraphics(float p_width, float p_height) {
        return this.createGraphics((int) p_width, (int) p_height, super.sketchRenderer());
    }

    public PGraphics createGraphics(float p_size) {
        int size = (int) p_size;
        return this.createGraphics(size, size, super.sketchRenderer());
    }

    public PGraphics createGraphics(int p_size) {
        return this.createGraphics(p_size, p_size, super.sketchRenderer());
    }

    public PGraphics createGraphics() {
        return this.createGraphics(super.width, super.height, super.sketchRenderer());
    }
    // endregion
    // endregion
    // endregion

    // region `Sketch::alphaBg()` overloads.
    public void alphaBg(int p_color) {
        super.pushStyle();
        super.fill(p_color);
        this.alphaBgRect();
    }

    public void alphaBg(float p_grey, float p_alpha) {
        super.pushStyle();
        super.fill(p_grey, p_alpha);
        this.alphaBgRect();
    }

    public void alphaBg(float p_red, float p_green, float p_blue, float p_alpha) {
        super.pushStyle();
        super.fill(p_red, p_green, p_blue, p_alpha);
        this.alphaBgRect();
    }

    private void alphaBgRect() {
        super.rectMode(PConstants.CORNER);
        super.rect(0, 0, super.width, super.height);
        super.popStyle();
    }
    // endregion

    // region Key-press and key-type helper methods.
    public boolean onlyKeyPressedIs(int p_keyCode) {
        return this.keysHeld.size() == 1 && this.keysHeld.contains(p_keyCode);
    }

    public boolean onlyKeysPressedAre(int... p_keyCodes) {
        boolean toRet = this.keysHeld.size() == p_keyCodes.length;

        if (!toRet)
            return false;

        for (int i : p_keyCodes)
            toRet &= this.keysHeld.contains(i);

        return toRet;
    }

    public boolean keysPressed(int... p_keyCodes) {
        // this.keysHeld.contains(p_keyCodes); // Causes a totally unique error :O

        for (int i : p_keyCodes)
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

    public boolean keyIsPressed(int p_keyCode) {
        return this.keysHeld.contains(p_keyCode);
    }

    public boolean anyGivenKeyIsPressed(int... p_keyCodes) {
        for (int i : p_keyCodes)
            if (this.keysHeld.contains(i))
                return true;
        return false;
    }

    public static boolean isStandardKeyboardSymbol(char p_char) {
        // boolean is = false;
        for (char ch : Sketch.STANDARD_KEYBOARD_SYMBOLS)
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

    public static boolean isTypeable(char p_char) {
        return Character.isDigit(p_char) ||
                Character.isLetter(p_char) ||
                Character.isWhitespace(p_char) ||
                Sketch.isStandardKeyboardSymbol(p_char);
    }

    public char getTypedKey() {
        if (Sketch.isTypeable(key))
            return key;

        // New way to do this in Java!:
        // (...as seen in [`java.lang.`]`Long.class`, on line 217, in OpenJDK `17`!)
        return switch (keyCode) {
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

    public void addTypedKeyTo(String p_str) {
        char typedChar = this.getTypedKey();
        int strLen = p_str.length();

        if (typedChar == '\b' && strLen > 0)
            p_str.substring(strLen - 1, strLen);
        else
            p_str.concat(Character.toString(typedChar));
    }

    public void addTypedKeyTo(StringBuilder p_str) {
        char typedChar = this.getTypedKey();
        int strLen = p_str.length();

        if (typedChar == '\b' && strLen > 0)
            p_str.substring(strLen - 1, strLen);
        else
            p_str.append(Character.toString(typedChar));
    }

    // To be used for checking if a certain key can be typed:
    public boolean isNotSpecialKey(int p_keyCode) {
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

    // region 2D rendering.
    public void begin2d() {
        super.hint(PConstants.DISABLE_DEPTH_TEST);
        super.pushMatrix();
        super.pushStyle();
    }

    public void end2d() {
        super.hint(PConstants.ENABLE_DEPTH_TEST);
        super.popMatrix();
        super.popStyle();
    }

    public void in2d(Runnable p_toDraw) {
        super.hint(PConstants.DISABLE_DEPTH_TEST);
        super.pushMatrix();
        super.pushStyle();
        // this.begin2d();

        p_toDraw.run();

        // this.end2d();
        super.hint(PConstants.ENABLE_DEPTH_TEST);
        super.popMatrix();
        super.popStyle();
    }

    // endregion

    // region Start a `JAVA2D` sketch with an undecorated window.
    public JFrame createSketchPanel(
            Runnable p_exitTask, Sketch p_sketch,
            PGraphics p_sketchGraphics) {
        // This is what the dummy variable from Processing used to contain.
        JFrame toRet = (JFrame) ((PSurfaceAWT.SmoothCanvas) p_sketch
                .getSurface().getNative()).getFrame();

        // region More stuff wth the `JFrame` (such as adding a `JPanel`!).
        toRet.removeNotify();
        toRet.setUndecorated(true);
        toRet.setLayout(null);
        toRet.addNotify();

        toRet.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent p_event) {
                System.out.println("Window closing...");
                p_sketch.exit();
            }
        });

        // region The `JPanel`, and input-event handling.
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics p_javaGaphics) {
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
            public void mousePressed(MouseEvent p_mouseEvent) {
                p_sketch.updateSketchMouse();
                p_sketch.mousePressed = true;
                p_sketch.mouseButton = p_mouseEvent.getButton();
                p_sketch.mousePressed();
            }

            @Override
            public void mouseReleased(MouseEvent p_mouseEvent) {
                p_sketch.updateSketchMouse();
                p_sketch.mousePressed = false;
                p_sketch.mouseButton = p_mouseEvent.getButton();
                p_sketch.mouseReleased();
            }

            @Override
            public void mouseClicked(MouseEvent p_mouseEvent) {
                p_sketch.updateSketchMouse();
                p_sketch.mouseButton = p_mouseEvent.getButton();
                p_sketch.mouseClicked();
            }

            @Override
            public void mouseEntered(MouseEvent p_mouseEvent) {
                // p_sketch.focused = true;
            }

            @Override
            public void mouseExited(MouseEvent p_mouseEvent) {
                // p_sketch.focused = false;
            }
        });

        // Listener for `PApplet::mouseDragged()` and `PApplet::mouseMoved()`:
        panel.addMouseMotionListener(new MouseMotionListener() {
            public void mouseDragged(MouseEvent p_mouseEvent) {
                p_sketch.updateSketchMouse();
                p_sketch.mouseDragged();
            }

            public void mouseMoved(MouseEvent p_mouseEvent) {
                p_sketch.updateSketchMouse();
                p_sketch.mouseMoved();
            }
        });

        // Listener for `PApplet::mouseWheel()`:
        panel.addMouseWheelListener(new MouseWheelListener() {
            @Override
            @SuppressWarnings("deprecation") // `deprecation` and not `deprecated`!
            public void mouseWheelMoved(MouseWheelEvent p_mouseEvent) {
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
            private boolean sketchExited;

            @Override
            public void keyTyped(KeyEvent p_keyEvent) {
                p_sketch.key = p_keyEvent.getKeyChar();
                p_sketch.keyCode = p_keyEvent.getKeyCode();
                p_sketch.keyTyped();
            }

            @Override
            public void keyPressed(KeyEvent p_keyEvent) {
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
            public void keyReleased(KeyEvent p_keyEvent) {
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
    private void updateSketchMouse() {
        Point mousePoint = MouseInfo.getPointerInfo().getLocation();
        super.mouseX = mousePoint.x - this.sketchFrame.getLocation().x;
        super.mouseY = mousePoint.y - this.sketchFrame.getLocation().y;
    }
    // endregion
    // endregion

}
