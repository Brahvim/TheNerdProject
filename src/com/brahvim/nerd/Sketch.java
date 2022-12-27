package com.brahvim.nerd;

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
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import com.brahvim.nerd.scene_api.SceneManager;
import com.brahvim.nerd.scenes.test_scene.TestScene1;
import com.jogamp.newt.opengl.GLWindow;

import processing.awt.PSurfaceAWT;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

// TODO: Bring stuff from Nerd - let's make a Game Engine! ":D!~

public class Sketch extends PApplet {
    // region `public` fields.
    public static final int REFRESH_RATE = GraphicsEnvironment.getLocalGraphicsEnvironment()
            .getScreenDevices()[0].getDisplayMode().getRefreshRate();
    public static final boolean CLOSE_ON_ESCAPE = Boolean.TRUE;

    // Window object references::
    public GLWindow glWindow;
    public JFrame sketchFrame;
    // (Why check for errors at all? You know what renderer you used!)

    // region "Dimensions".
    public int frameStartTime, pframeTime, frameTime;
    public float cx, cy, qx, qy, q3x, q3y;
    public int pwidth, pheight;
    // endregion

    // Directories:
    public static final File EXEC_DIR = new File("");
    public static final File DATA_DIR = new File("data");
    // endregion

    // region `private` ~~/ `protected`~~ fields.
    private SceneManager sceneMan = new SceneManager(this);
    private final ArrayList<Integer> keysHeld = new ArrayList<>(5); // `final` to avoid concurrency issues.
    // endregion

    // region Constructors, `main()`, `settings()`...
    public static void main(String[] p_args) {
        Sketch constructedSketch = new Sketch();
        String[] args = new String[] { constructedSketch.getClass().getName() };

        if (p_args == null || p_args.length == 0)
            PApplet.runSketch(args, constructedSketch);
        else
            PApplet.runSketch(PApplet.concat(p_args, args), constructedSketch);
    }

    @Override
    public void settings() {
        super.size(400, 400, PConstants.P3D);
    }
    // endregion

    // region Processing sketch workflow.
    @Override
    public void setup() {
        super.frameRate(Sketch.REFRESH_RATE);
        super.registerMethod("pre", this);
        super.registerMethod("post", this);

        this.sketchSetup();
    }

    private void sketchSetup() {
        this.updateRatios();
        this.glWindow = ((GLWindow) super.surface.getNative());
        this.sceneMan.setScene(TestScene1.class);
    }

    public void pre() {
        if (!(this.pwidth == super.width || this.pheight == super.height))
            this.updateRatios();

        this.sceneMan.pre();
    }

    @Override
    public void draw() {
        this.frameStartTime = super.millis(); // Timestamp.
        this.frameTime = this.frameStartTime - this.pframeTime;
        this.pframeTime = this.frameStartTime;

        this.sceneMan.draw();
    }

    public void post() {
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
        if (!Sketch.CLOSE_ON_ESCAPE) {
            if (super.keyCode == 27)
                super.key = ' ';
        }

        this.keysHeld.add(this.keysHeld.indexOf(super.keyCode));
        this.sceneMan.keyPressed();
    }

    public void keyReleased() {
        try {
            this.keysHeld.remove(this.keysHeld.indexOf(super.keyCode));
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
    // endregion

    // region Boilerplate-y extras.
    public void updateRatios() {
        this.cx = super.width * 0.5f;
        this.cy = super.height * 0.5f;
        this.qx = this.cx * 0.5f;
        this.qy = this.cy * 0.5f;
        this.q3x = this.cx + this.qx;
        this.q3y = this.cy + this.qy;
    }

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

    public void updateSketchMouse() {
        Point mousePoint = MouseInfo.getPointerInfo().getLocation();
        super.mouseX = mousePoint.x - this.sketchFrame.getLocation().x;
        super.mouseY = mousePoint.y - this.sketchFrame.getLocation().y;
    }
    // endregion

}
