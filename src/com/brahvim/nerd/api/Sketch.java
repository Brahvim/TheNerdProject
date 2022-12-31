package com.brahvim.nerd.api;

import java.awt.Graphics;
import java.awt.Graphics2D;
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
import java.util.LinkedHashSet;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import com.brahvim.nerd.scene_api.SceneManager;
import com.jogamp.newt.opengl.GLWindow;

import processing.awt.PSurfaceAWT;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PVector;
import processing.opengl.PGraphics3D;

// TODO: Bring stuff from Nerd - let's make a "Game Engine"! ":D!~

public class Sketch extends PApplet {
    // region `public` fields.
    // region Constants.
    public final static File EXEC_DIR = new File("");
    public final static File DATA_DIR = new File("data");

    public final static int REFRESH_RATE = GraphicsEnvironment.getLocalGraphicsEnvironment()
            .getScreenDevices()[0].getDisplayMode().getRefreshRate();

    public final static char[] VALID_SYMBOLS = {
            '\'', '\"', '-', '=', '`', '~', '!', '@', '#', '$',
            '%', '^', '&', '*', '(', ')', '{', '}', '[',
            ']', ';', ',', '.', '/', '\\', ':', '|', '<',
            '>', '_', '+', '?'
    };

    public final Sketch SKETCH;

    public final String RENDERER;
    public final int INIT_WIDTH, INIT_HEIGHT;
    public final boolean CLOSE_ON_ESCAPE, STARTED_FULLSCREEN, INITIALLY_RESIZABLE,
            CAN_FULLSCREEN, F11_FULLSCREEN, ALT_ENTER_FULLSCREEN;
    // endregion

    // Window object references::
    public GLWindow glWindow;
    public JFrame sketchFrame;
    // (Why check for errors at all? You know what renderer you used!)

    // region Frame-wise states, Processing style (modifiable!).
    public char pkey; // Previous fraaaaame!...
    public boolean pfocused;
    public int pmouseButton, pkeyCode;
    public boolean pkeyPressed, pmousePressed; // Previous frame...

    // Current frame!
    public float mouseScroll, mouseScrollDelta;
    public boolean mouseLeft, mouseMid, mouseRight;

    public boolean fullscreen, pfullscreen;
    public boolean cursorConfined, cursorVisible; // nO previoS versiuN!11!!
    public Camera previousCamera, currentCamera; // CAM! (wher lite?)
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
    private SceneManager sceneMan;
    private final Unprojector unprojector;
    private final LinkedHashSet<Integer> keysHeld = new LinkedHashSet<>(5); // `final` to avoid concurrency issues.
    // endregion

    // region Constructors, `main()`, `settings()`...
    public Sketch(SketchBuilder.SketchInitializer p_sketchInitializer) {
        if (p_sketchInitializer == null) {
            throw new IllegalArgumentException("""
                    Please use a `SketchBuilder` instance to make a `Sketch`!""");
        }

        this.SKETCH = this;

        this.unprojector = new Unprojector();
        this.sceneMan = new SceneManager(this);
        this.currentCamera = new CameraBuilder(this).build();
        this.sceneMan.setScene(p_sketchInitializer.firstScene);

        this.RENDERER = p_sketchInitializer.renderer;
        this.CAN_FULLSCREEN = !p_sketchInitializer.cannotFullscreen;
        this.CLOSE_ON_ESCAPE = p_sketchInitializer.closeOnEscape;
        this.INITIALLY_RESIZABLE = p_sketchInitializer.canResize;
        this.STARTED_FULLSCREEN = p_sketchInitializer.startedFullscreen;
        this.F11_FULLSCREEN = !p_sketchInitializer.cannotF11Fullscreen;
        this.ALT_ENTER_FULLSCREEN = !p_sketchInitializer.cannotAltEnterFullscreen;

        this.fullscreen = this.STARTED_FULLSCREEN;
        if (this.STARTED_FULLSCREEN) {
            this.INIT_WIDTH = super.displayWidth / 8;
            this.INIT_HEIGHT = super.displayHeight / 8;
        } else {
            this.INIT_WIDTH = p_sketchInitializer.width;
            this.INIT_HEIGHT = p_sketchInitializer.height;
        }
    }

    @Override
    public void settings() {
        // if (this.STARTED_FULLSCREEN)
        // super.fullScreen(this.RENDERER);
        // else
        super.size(this.INIT_WIDTH, this.INIT_HEIGHT, this.RENDERER);
    }
    // endregion

    // region Processing sketch workflow.
    @Override
    public void setup() {
        super.frameRate(Sketch.REFRESH_RATE);
        super.registerMethod("pre", this);
        super.registerMethod("post", this);

        this.updateRatios();

        switch (this.RENDERER) {
            case PConstants.P3D -> this.glWindow = (GLWindow) super.surface.getNative();
            case PConstants.JAVA2D -> this.sketchFrame = (JFrame) super.surface.getNative();
        }

        if (this.INITIALLY_RESIZABLE)
            switch (this.RENDERER) {
                case PConstants.P3D -> this.glWindow.setResizable(true);
                case PConstants.JAVA2D -> super.surface.setResizable(true);
            }

        super.rectMode(PConstants.CENTER);
        super.imageMode(PConstants.CENTER);
        super.textAlign(PConstants.CENTER, PConstants.CENTER);
    }

    public void pre() {
        if (!(this.pwidth == super.width || this.pheight == super.height))
            this.updateRatios();

        this.mouseScrollDelta = this.mouseScroll - this.pmouseScroll;
        this.mouse.set(super.mouseX, super.mouseY);
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

        if (this.currentCamera != null)
            this.currentCamera.apply();

        this.sceneMan.draw();
    }

    public void post() {
        this.pwidth = this.width;
        this.pheight = this.height;
        this.pfocused = this.focused;

        this.pmouse.set(this.mouse);
        this.pmouseMid = this.mouseMid;
        this.pmouseLeft = this.mouseLeft;
        this.pmouseRight = this.mouseRight;
        this.pmouseButton = this.mouseButton;
        this.pmouseScroll = this.mouseScroll;
        this.pmouseScrollDelta = this.mouseScrollDelta;

        this.pkey = super.key;
        this.pkeyCode = this.keyCode;
        this.pkeyPressed = super.keyPressed;
        this.pmousePressed = super.mousePressed;

        this.previousCamera = this.currentCamera;

        if (this.RENDERER == PConstants.P3D) {
            // region `glWindow` stuff.
            this.pfullscreen = this.fullscreen;

            // if (this.pfullscreen != this.fullscreen) {
            this.glWindow.setFullscreen(this.fullscreen);
            while (this.fullscreen ? !this.glWindow.isFullscreen() : this.glWindow.isFullscreen())
                ;
            // }

            this.glWindow.confinePointer(this.cursorConfined);
            while (this.cursorConfined ? !this.glWindow.isPointerConfined() : this.glWindow.isPointerConfined())
                ;

            this.glWindow.setPointerVisible(this.cursorVisible);
            while (this.cursorVisible ? !this.glWindow.isPointerVisible() : this.glWindow.isPointerVisible())
                ;
            // endregion
        } else {
            if (cursorVisible)
                super.cursor();
            else
                super.noCursor();
        }

        this.sceneMan.post();
    }
    // endregion

    // region Processing's event callbacks! REMEMBER `this.sceneMan`! :joy:
    // region Mouse events.
    public void mousePressed() {
        this.sceneMan.mousePressed();
    }

    public void mouseReleased() {
        this.sceneMan.mouseReleased();
    }

    public void mouseMoved() {
        this.sceneMan.mouseMoved();
    }

    public void mouseClicked() {
        this.sceneMan.mouseClicked();
    }

    public void mouseDragged() {
        this.sceneMan.mouseDragged();
    }

    // @SuppressWarnings("unused")
    public void mouseWheel(processing.event.MouseEvent p_mouseEvent) {
        this.sceneMan.mouseWheel(p_mouseEvent);
    }
    // endregion

    // region Keyboard events.
    public void keyTyped() {
        this.sceneMan.keyTyped();
    }

    public void keyPressed() {
        if (!this.CLOSE_ON_ESCAPE) {
            if (super.keyCode == 27)
                super.key = ' ';
        }

        if (this.CAN_FULLSCREEN) {
            if (this.ALT_ENTER_FULLSCREEN) {
                if (super.keyCode == KeyEvent.VK_ENTER &&
                        this.keyIsPressed(KeyEvent.VK_ALT)) {
                    System.out.println("`Alt`-`Enter` fullscreen!");
                    this.fullscreen = !this.fullscreen;
                }
            }
            if (this.F11_FULLSCREEN) {
                if (super.keyCode == 107) { // `KeyEvent.VK_ADD` is `107`, but here, it's `F11`!
                    System.out.println("`F11` fullscreen!");
                    this.fullscreen = !this.fullscreen;
                }
            }
        }

        this.keysHeld.add(super.keyCode);
        this.sceneMan.keyPressed();
    }

    public void keyReleased() {
        try {
            this.keysHeld.remove(super.keyCode);
        } catch (IndexOutOfBoundsException e) {
        }
        this.sceneMan.keyReleased();
    }
    // endregion

    // region Touch events.
    public void touchStarted() {
        this.sceneMan.touchStarted();
    }

    public void touchMoved() {
        this.sceneMan.touchMoved();
    }

    public void touchEnded() {
        this.sceneMan.touchEnded();
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
            this.sceneMan.focusGained();
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
            this.sceneMan.focusLost();
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

    public void centerWindow() {
        this.updateRatios(); // You called this function when the window changed its size or position, right?
        // Remember: computers with multiple displays exist! We shouldn't cache this:

        int winX = (int) (super.displayWidth * 0.5f - this.cx),
                winY = (int) (super.displayHeight * 0.5f - this.cy);

        // switch (this.RENDERER) {
        // case PConstants.P3D -> this.glWindow.setPosition(winX, winY);
        // default -> super.surface.setLocation(winX, winY);
        // }

        super.surface.setLocation(winX, winY);
        // (Well, changing the display does NOT effect those variables in any way :|)
    }

    // region Key-press and key-type helper methods.
    public boolean keyIsPressed(int p_keyCode) {
        return this.keysHeld.contains(p_keyCode);
    }

    public boolean keysPressed(int... p_keyCodes) {
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

    public static boolean isValidSymbol(char p_char) {
        // boolean is = false;
        for (char ch : VALID_SYMBOLS)
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
                Sketch.isValidSymbol(p_char);
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
         * else if (keyCode == RETURN || keyCode == ENTER)
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
                p_keyCode == 10 || // Both `Enter`s/`Return`s.
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
    public void in2d(Runnable p_toDraw) {
        this.begin2d();
        p_toDraw.run();
        this.end2d();
    }

    public void begin2d() {
        super.hint(PConstants.DISABLE_DEPTH_TEST);
        super.pushMatrix();
        super.pushStyle();
    }

    public void end2d() {
        super.popStyle();
        super.popMatrix();
        super.hint(PConstants.ENABLE_DEPTH_TEST);
    }
    // endregion

    // region Start a `JAVA2D` sketch with an undecorated window.
    public JFrame createSketchPanel(
            Runnable p_exitTask, Sketch p_sketch,
            PGraphics p_sketchGraphics) {
        // This is what the dummy variable from Processing used to contain.
        JFrame ret = (JFrame) ((PSurfaceAWT.SmoothCanvas) p_sketch
                .getSurface().getNative()).getFrame();

        // region More stuff wth the `JFrame` (such as adding a `JPanel`!).
        ret.removeNotify();
        ret.setUndecorated(true);
        ret.setLayout(null);
        ret.addNotify();

        ret.addWindowListener(new WindowAdapter() {
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
        ((JFrame) ret).setContentPane(panel); // This is the dummy variable from Processing.
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
            }

            @Override
            public void mouseExited(MouseEvent p_mouseEvent) {
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

        return ret;
    }

    // Used by `Sketch::createSketchPanel()`:
    public void updateSketchMouse() {
        Point mousePoint = MouseInfo.getPointerInfo().getLocation();
        super.mouseX = mousePoint.x - this.sketchFrame.getLocation().x;
        super.mouseY = mousePoint.y - this.sketchFrame.getLocation().y;
    }
    // endregion
    // endregion

}
