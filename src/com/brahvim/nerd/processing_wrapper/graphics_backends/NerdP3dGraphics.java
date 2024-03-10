package com.brahvim.nerd.processing_wrapper.graphics_backends;

import java.util.ArrayList;
import java.util.Objects;

import com.brahvim.nerd.framework.cameras.NerdAbstractCamera;
import com.brahvim.nerd.framework.cameras.NerdBasicCamera;
import com.brahvim.nerd.framework.cameras.NerdBasicCameraBuilder;
import com.brahvim.nerd.framework.cameras.NerdFlyCamera;
import com.brahvim.nerd.framework.lights.NerdAmbientLight;
import com.brahvim.nerd.framework.lights.NerdDirectionalLight;
import com.brahvim.nerd.framework.lights.NerdPointLight;
import com.brahvim.nerd.framework.lights.NerdSpotLight;
import com.brahvim.nerd.math.NerdUnprojector;
import com.brahvim.nerd.processing_wrapper.NerdSketch;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;
import processing.opengl.PGraphics3D;

public class NerdP3dGraphics extends NerdOpenGlGraphics<PGraphics3D> {

    // TODO: Processing! Max lights `8`! Cameras! HERE! Inner classes!
    // (Cameras are best implemented using DOD! Write a camera 'manager' here.
    // Use that guy! Let people extend 'im! He'll act in your best interest, trust!)

    public enum NerdLightSlot {

        ONE(),
        TWO(),
        THREE(),
        FOUR(),
        FIVE(),
        SIX(),
        SEVEN(),
        EIGHT();

    }

    // region Inner classes.
    public class TwoDimensionalPush implements AutoCloseable {

        public TwoDimensionalPush() {
            NerdP3dGraphics.this.begin2d();
        }

        @Override
        public void close() throws Exception {
            NerdP3dGraphics.this.end2d();
        }

    }

    public class CameraPush implements AutoCloseable {

        public CameraPush() {
            NerdP3dGraphics.this.beginCamera();
        }

        @Override
        public void close() throws Exception {
            NerdP3dGraphics.this.endCamera();
        }

    }
    // endregion

    // region Fields.
    public boolean autoApplyCameraMatrix = true;

    protected final NerdUnprojector UNPROJECTOR;
    protected final NerdAbstractCamera DEFAULT_CAMERA;
    protected final NerdBasicCamera DEFAULT_CAMERA_BASIC;

    /** To be assigned in the constructor. */
    protected NerdAbstractCamera currentCamera, previousCamera; // CAMERA! wher lite?! wher accsunn?!

    // region Light-slots API.
    // ...These two are NOT lazy-initialized.
    // Sometimes, DOD requires storing extras like this so everything is contiguous
    // and less dynamic:
    protected Object[] lightSlots = new Object[8];
    protected ArrayList<? extends Object>[] lightSlotsArrays = new ArrayList<?>[8];

    protected final ArrayList<ArrayList<NerdSpotLight>> SPOT_LIGHTS = new ArrayList<>(0);
    protected final ArrayList<ArrayList<NerdPointLight>> POINT_LIGHTS = new ArrayList<>(0);

    protected final ArrayList<NerdAmbientLight> AMBIENT_LIGHTS = new ArrayList<>(0);
    protected final ArrayList<NerdDirectionalLight> DIRECTIONAL_LIGHTS = new ArrayList<>(0);
    // endregion
    // endregion

    // region Utilitarian constructors.
    public NerdP3dGraphics(
            final NerdSketch<PGraphics3D> p_sketch, final int p_width, final int p_height,
            final String p_renderer, final String p_path) {
        this(p_sketch, p_sketch.createGraphics(p_width, p_height, p_renderer, p_path));
    }

    public NerdP3dGraphics(
            final NerdSketch<PGraphics3D> p_sketch, final int p_width, final int p_height,
            final String p_renderer) {
        this(p_sketch, p_sketch.createGraphics(p_width, p_height, p_renderer));
    }

    public NerdP3dGraphics(
            final NerdSketch<PGraphics3D> p_sketch, final int p_width, final int p_height) {
        this(p_sketch, p_sketch.createGraphics(p_width, p_height));
    }

    public NerdP3dGraphics(
            final NerdSketch<PGraphics3D> p_sketch, final float p_width,
            final float p_height) {
        this(p_sketch, p_sketch.createGraphics(p_width, p_height));
    }

    public NerdP3dGraphics(
            final NerdSketch<PGraphics3D> p_sketch, final float p_size) {
        this(p_sketch, p_sketch.createGraphics(p_size));
    }

    public NerdP3dGraphics(
            final NerdSketch<PGraphics3D> p_sketch, final int p_size) {
        this(p_sketch, p_sketch.createGraphics(p_size));
    }

    public NerdP3dGraphics(
            final NerdSketch<PGraphics3D> p_sketch) {
        this(p_sketch, p_sketch.createGraphics());
    }
    // endregion

    public NerdP3dGraphics(final NerdSketch<PGraphics3D> p_sketch, final PGraphics3D p_graphics) {
        super(p_sketch, p_graphics);

        this.DEFAULT_CAMERA_BASIC = new NerdBasicCameraBuilder(this).build();
        this.DEFAULT_CAMERA = this.DEFAULT_CAMERA_BASIC;
        this.UNPROJECTOR = new NerdUnprojector();

        this.currentCamera = this.DEFAULT_CAMERA;
        this.previousCamera = this.DEFAULT_CAMERA;
    }

    @Override
    protected void preDrawImpl() {
        if (this.autoApplyCameraMatrix)
            this.applyCurrentCamera();
    }

    // region Lighting methods.
    // I'm not going for `public`, `final` instances of `NerdLightSlot`s because
    // this style allows users to make new types of lights, and use them here
    // without having to worry that the `NerdLightSlot` instances already exist in
    // this class, and that access to them cannot be removed entirely.
    public void addLight(final NerdP3dGraphics.NerdLightSlot p_slot, final NerdDirectionalLight p_light) {
        // if (!this.lightApiObjectsExist)
        // this.initLightApiObjects();

        this.lightSlots[p_slot.ordinal()] = p_light;
        this.lightSlotsArrays[p_slot.ordinal()] = this.DIRECTIONAL_LIGHTS;
    }

    public void addLight(final NerdP3dGraphics.NerdLightSlot p_slot, final NerdAmbientLight p_light) {
        // if (!this.lightApiObjectsExist)
        // this.initLightApiObjects();

        this.lightSlots[p_slot.ordinal()] = p_light;
        this.lightSlotsArrays[p_slot.ordinal()] = this.AMBIENT_LIGHTS;
    }

    public void addLight(final NerdP3dGraphics.NerdLightSlot p_slot, final NerdPointLight p_light) {
        // if (!this.lightApiObjectsExist)
        // this.initLightApiObjects();

        this.lightSlots[p_slot.ordinal()] = p_light;
        this.lightSlotsArrays[p_slot.ordinal()] = this.POINT_LIGHTS;
    }

    public void addLight(final NerdP3dGraphics.NerdLightSlot p_slot, final NerdSpotLight p_light) {
        // if (!this.lightApiObjectsExist)
        // this.initLightApiObjects();

        this.lightSlots[p_slot.ordinal()] = p_light;
        this.lightSlotsArrays[p_slot.ordinal()] = this.SPOT_LIGHTS;
    }
    // endregion

    // region Dealing with `NerdAbstractCamera` subclasses.
    public void applyCurrentCamera() {
        // This was moved to the constructor:
        // if (!this.SKETCH.USES_OPENGL)
        // return;

        // If the current camera is `null`, use the default one instead:
        // if (this.currentCamera != null)
        // this.currentCamera.apply();
        // else {
        // this.DEFAULT_CAMERA.apply();
        //
        // // If the current camera is null, but wasn't, notify!:
        // if (this.currentCamera != this.previousCamera)
        // System.out.printf("`%s` has no camera! Consider adding one...?", this);
        // }

        // ...
        // - `NerdP3dGraphics::currentCamera`, and-
        // - `NerdP3dGraphics::previousCamera`,
        // ...can never be `null`. So...:

        this.currentCamera.apply();
    }

    /**
     * @param <RetT>   is the type of your {@link NerdAbstractCamera} subclass.
     * @param p_camera is the camera object you want to declare as the current one
     *                 used for rendering by the scene's {@link NerdP3dGraphics}.
     * @return The very same camera you pass.
     */
    public <RetT extends NerdAbstractCamera> RetT setCurrentCamera(final RetT p_camera) {
        this.currentCamera = Objects.requireNonNull(p_camera);
        return p_camera;
    }

    /**
     * @param <RetT> is the type of your {@link NerdAbstractCamera} subclass.
     * @return The default camera you passed to this method.
     */
    public NerdBasicCamera setCurrentCameraToDefault() {
        this.currentCamera = new NerdBasicCamera(this.DEFAULT_CAMERA_BASIC);
        return (NerdBasicCamera) this.currentCamera;
    }

    @SuppressWarnings("unchecked")
    public <RetT extends NerdAbstractCamera> RetT getCurrentCamera() {
        return (RetT) this.currentCamera;
    }

    @SuppressWarnings("unchecked")
    public <RetT extends NerdAbstractCamera> RetT getPreviousCamera() {
        return (RetT) this.previousCamera;
    }
    // endregion

    // region Translation transform.
    public void translateZ(final float p_value) {
        this.GRAPHICS.translate(0, 0, p_value);
    }

    public void translateFromCenterZ(final float p_value) {
        this.GRAPHICS.translate(this.cx, this.cy, p_value);
    }
    // endregion

    // region `modelVec()` and `screenVec()`.
    public PVector modelVec() {
        return new PVector(
                // "I passed these `0`s in myself, yeah. Let's not rely on the JIT too much!"
                // - Me before re-thinking that.
                this.modelX(), this.modelY(), this.modelZ());
    }

    public PVector modelVec(final PVector p_vec) {
        return new PVector(this.GRAPHICS.modelX(p_vec.x, p_vec.y, p_vec.z),
                this.GRAPHICS.modelY(p_vec.x, p_vec.y, p_vec.z), this.GRAPHICS.modelZ(p_vec.x, p_vec.y, p_vec.z));
    }

    public PVector modelVec(final float p_x, final float p_y, final float p_z) {
        return new PVector(this.GRAPHICS.modelX(p_x, p_y, p_z), this.GRAPHICS.modelY(p_x, p_y, p_z),
                this.GRAPHICS.modelZ(p_x, p_y, p_z));
    }

    public PVector screenVec() {
        return new PVector(this.screenX(), this.screenY(), this.screenZ());
    }

    public PVector screenVec(final PVector p_vec) {
        return new PVector(this.screenX(p_vec.x, p_vec.y, p_vec.z), this.screenY(p_vec.x, p_vec.y, p_vec.z),
                this.screenZ(p_vec.x, p_vec.y, p_vec.z));
    }

    public PVector screenVec(final float p_x, final float p_y, final float p_z) {
        return new PVector(this.screenX(p_x, p_y, p_z), this.screenY(p_x, p_y, p_z), this.screenZ(p_x, p_y, p_z));
    }
    // endregion

    // region `modelX()`-`modelY()`-`modelZ()` `PVector` and no-parameter overloads.
    // region Parameterless overloads.
    public float modelX() {
        return this.GRAPHICS.modelX(0, 0, 0);
    }

    public float modelY() {
        return this.GRAPHICS.modelY(0, 0, 0);
    }

    public float modelZ() {
        return this.GRAPHICS.modelZ(0, 0, 0);
    }
    // endregion

    // region `p_vec`?
    // ...how about `p_modelMatInvMulter`? 🤣!
    public float modelX(final PVector p_vec) {
        return this.GRAPHICS.modelX(p_vec.x, p_vec.y, p_vec.z);
    }

    public float modelY(final PVector p) {
        return this.GRAPHICS.modelY(p.x, p.y, p.z);
    }

    public float modelZ(final PVector p) {
        return this.GRAPHICS.modelZ(p.x, p.y, p.z);
    }
    // endregion
    // endregion

    // region `screenX()`-`screenY()`-`screenZ()`, `PVector`, plus no-arg overloads.
    // "Oh! And when the `z` is `-1`, you just add this and sub that. Optimization!"
    // - That ONE Mathematician.

    // region Parameterless overloads.
    public float screenX() {
        return this.GRAPHICS.screenX(0, 0, 0);
    }

    public float screenY() {
        return this.GRAPHICS.screenY(0, 0, 0);
    }

    public float screenZ() {
        return this.GRAPHICS.screenY(0, 0, 0);
    }
    // endregion

    @Override
    public float screenX(final float p_x, final float p_y, final float p_z) {
        return super.GRAPHICS.screenZ(p_x, p_y, p_z);
    }

    @Override
    public float screenY(final float p_x, final float p_y, final float p_z) {
        return super.GRAPHICS.screenZ(p_x, p_y, p_z);
    }

    @Override
    public float screenZ(final float p_x, final float p_y, final float p_z) {
        return super.GRAPHICS.screenZ(p_x, p_y, p_z);
    }

    // region `p_vec`!
    // The following two were going to exclude the `z` if it was `0`.
    // And later, I felt this was risky.
    // This two-`float` overload ain't in the docs, that scares me!

    // ...ACTUALLY,
    // https://github.com/processing/processing/blob/459853d0dcdf1e1648b1049d3fdbb4bf233fded8/core/src/processing/opengl/PGraphicsOpenGL.java#L4611
    // ..."they rely on the JIT too!" (no, they don't optimize this at all. They
    // just put the `0` themselves, LOL.) 😂

    public float screenX(final PVector p_vec) {
        return this.GRAPHICS.screenX(p_vec.x, p_vec.y, p_vec.z);

        // return p_vec.z == 0
        // ? this.GRAPHICS.screenX(p_vec.x, p_vec.y)
        // : this.GRAPHICS.screenX(p_vec.x, p_vec.y, p_vec.z);
    }

    public float screenY(final PVector p_vec) {
        return this.GRAPHICS.screenY(p_vec.x, p_vec.y, p_vec.z);

        // return p_vec.z == 0
        // ? this.GRAPHICS.screenY(p_vec.x, p_vec.y)
        // : this.GRAPHICS.screenY(p_vec.x, p_vec.y, p_vec.z);
    }

    public float screenZ(final PVector p_vec) {
        // Hmmm...
        // ..so `z` cannot be `0` here.
        // ..and `x` and `y` cannot be ignored!
        // "No room for optimization here!"
        return this.GRAPHICS.screenZ(p_vec.x, p_vec.y, p_vec.z);
    }
    // endregion
    // endregion

    // region Camera matrix configuration.
    public void camera(final NerdBasicCamera p_cam) {
        this.GRAPHICS.camera(
                p_cam.POSITION.x, p_cam.POSITION.y, p_cam.POSITION.z,
                p_cam.CENTER.x, p_cam.CENTER.y, p_cam.CENTER.z,
                p_cam.ORIENTATION.x, p_cam.ORIENTATION.y,
                p_cam.ORIENTATION.z);
    }

    public void camera(final NerdFlyCamera p_cam) {
        this.GRAPHICS.camera(
                p_cam.POSITION.x, p_cam.POSITION.y, p_cam.POSITION.z,

                p_cam.POSITION.x + p_cam.FRONT.x,
                p_cam.POSITION.y + p_cam.FRONT.y,
                p_cam.POSITION.z + p_cam.FRONT.z,

                p_cam.ORIENTATION.x, p_cam.ORIENTATION.y, p_cam.ORIENTATION.z);
    }

    public void camera(final PVector p_pos, final PVector p_center, final PVector p_up) {
        this.GRAPHICS.camera(p_pos.x, p_pos.y, p_pos.z, p_center.x, p_center.y, p_center.z, p_up.x, p_up.y, p_up.z);
    }
    // endregion

    // region Projection functions.
    public void perspective(final NerdAbstractCamera p_cam) {
        this.GRAPHICS.perspective(p_cam.fov, p_cam.aspect, p_cam.near, p_cam.far);
    }

    public void perspective(final float p_fov, final float p_near, final float p_far) {
        this.GRAPHICS.perspective(p_fov, this.WINDOW.scr, p_near, p_far);
    }

    public void ortho(final NerdAbstractCamera p_cam) {
        this.GRAPHICS.ortho(-this.WINDOW.cx, this.WINDOW.cx, -this.WINDOW.cy,
                this.WINDOW.cy, p_cam.near, p_cam.far);
    }

    public void ortho(final float p_near, final float p_far) {
        this.GRAPHICS.ortho(-this.WINDOW.cx, this.WINDOW.cx, -this.WINDOW.cy,
                this.WINDOW.cy, p_near, p_far);
    }

    /**
     * Expands to:
     * {@code PGraphics::ortho(-p_cx, p_cx, -p_cy, p_cy, p_near, p_far)}.
     *
     * @param p_cx   is the screen center on the `x`-axis.
     * @param p_cy   is the screen center on the `y`-axis.
     * @param p_near is the camera's distance from near plane.
     * @param p_far  is the camera's distance from far plane.
     */
    public void ortho(final float p_cx, final float p_cy, final float p_near, final float p_far) {
        this.GRAPHICS.ortho(-p_cx, p_cx, -p_cy, p_cy, p_near, p_far);
    }
    // endregion

    // region Unprojection functions.
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
        this.UNPROJECTOR.captureViewMatrix(this.GRAPHICS);
        this.UNPROJECTOR.gluUnProject(p_x, this.SKETCH.height - p_y,
                // `0.9f`: at the near clipping plane.
                // `0.9999f`: at the far clipping plane. (NO! Calculate EPSILON first! *Then-*)
                // 0.9f + map(mouseY, height, 0, 0, 0.1f),
                PApplet.map(p_z, this.currentCamera.near, this.currentCamera.far, 0, 1), toRet);

        return toRet;
    }
    // endregion

    // region Mouse!
    /**
     * Caching this vector never works. Call this method everytime!~ People
     * recalculate things framely in computer
     * graphics anyway! 😂
     */
    public PVector getMouseInWorld() {
        return // this.currentCamera == null ? new PVector() :
        this.getMouseInWorldFromFarPlane(this.currentCamera.mouseZ);
    }

    public PVector getMouseInWorldFromFarPlane(final float p_distanceFromFarPlane) {
        return // this.currentCamera == null ? new PVector() :
        this.worldVec(this.INPUT.mouseX, this.INPUT.mouseY,
                this.currentCamera.far - p_distanceFromFarPlane + this.currentCamera.near);
    }

    public PVector getMouseInWorldAtZ(final float p_distanceFromCamera) {
        return // this.currentCamera == null ? new PVector() :
        this.worldVec(this.INPUT.mouseX, this.INPUT.mouseY, p_distanceFromCamera);
    }
    // endregion

    // region Touches.
    // /**
    // * Caching this vector never works. Call this method everytime!~
    // * People recalculate things framely in computer graphics anyway! 😂
    // */
    // public PVector getTouchInWorld(final int p_touchId) {
    // return this.getTouchInWorldFromFarPlane(p_touchId,
    // this.camera.mouseZ);
    // }

    // public PVector getTouchInWorldFromFarPlane(final float p_touchId, final float
    // p_distanceFromFarPlane) {
    // final TouchEvent.Pointer touch = this.SKETCH.touches[p_touchId];
    // return this.worldVec(touch.x, touch.y,
    // this.camera.far - p_distanceFromFarPlane +
    // this.camera.near);
    // }

    // public PVector getTouchInWorldAtZ(final int p_touchId, final float
    // p_distanceFromCamera) {
    // final TouchEvent.Pointer touch = this.SKETCH.touches[p_touchId];
    // return this.worldVec(touch.x, touch.y, p_distanceFromCamera);
    // }
    // endregion
    // endregion

    // region 3D shapes (and shapes in general haha).
    // region `drawClosedShape()` and `drawOpenShape()`.
    public void drawClosedShape(final float p_x, final float p_y, final float p_z,
            final int p_shapeType, final Runnable p_shapingFxn) {
        this.GRAPHICS.pushMatrix();
        this.translate(p_x, p_y, p_z);
        this.SKETCH.beginShape(p_shapeType);
        p_shapingFxn.run();
        this.SKETCH.endShape(PConstants.CLOSE);
        this.GRAPHICS.popMatrix();
    }

    public void drawOpenShape(final float p_x, final float p_y, final float p_z,
            final int p_shapeType, final Runnable p_shapingFxn) {
        this.GRAPHICS.pushMatrix();
        this.translate(p_x, p_y, p_z);
        this.SKETCH.beginShape(p_shapeType);
        p_shapingFxn.run();
        this.SKETCH.endShape();
        this.GRAPHICS.popMatrix();
    }
    // endregion

    // region `box()` overloads.
    public void box(final float p_x, final float p_y, final float p_z, final float p_width, final float p_height,
            final float p_depth) {
        this.GRAPHICS.pushMatrix();
        this.GRAPHICS.translate(p_x, p_y, p_z);
        this.GRAPHICS.box(p_width, p_height, p_depth);
        this.GRAPHICS.popMatrix();
    }

    public void box(final PVector p_pos, final float p_width, final float p_height, final float p_depth) {
        this.GRAPHICS.pushMatrix();
        this.translate(p_pos);
        this.GRAPHICS.box(p_width, p_height, p_depth);
        this.GRAPHICS.popMatrix();
    }

    public void box(final float p_x, final float p_y, final float p_z, final PVector p_dimensions) {
        this.GRAPHICS.pushMatrix();
        this.GRAPHICS.translate(p_x, p_y, p_z);
        this.GRAPHICS.box(p_dimensions.x, p_dimensions.y, p_dimensions.z);
        this.GRAPHICS.popMatrix();
    }

    public void box(final float p_x, final float p_y, final float p_z, final float p_size) {
        this.GRAPHICS.pushMatrix();
        this.GRAPHICS.translate(p_x, p_y, p_z);
        this.GRAPHICS.box(p_size);
        this.GRAPHICS.popMatrix();
    }

    public void box(final PVector p_pos, final PVector p_dimensions) {
        this.GRAPHICS.pushMatrix();
        this.translate(p_pos);
        this.GRAPHICS.box(p_dimensions.x, p_dimensions.y, p_dimensions.z);
        this.GRAPHICS.popMatrix();
    }

    public void box(final PVector p_pos, final float p_size) {
        this.GRAPHICS.pushMatrix();
        this.translate(p_pos);
        this.GRAPHICS.box(p_size);
        this.GRAPHICS.popMatrix();
    }
    // endregion

    // region `sphere()` overloads (just a copy of the `box()` ones, hehehe.).
    public void sphere(final float p_x, final float p_y, final float p_z, final float p_width, final float p_height,
            final float p_depth) {
        this.GRAPHICS.pushMatrix();
        this.GRAPHICS.translate(p_x, p_y, p_z);
        this.GRAPHICS.scale(p_width, p_height, p_depth);
        this.GRAPHICS.sphere(1);
        this.GRAPHICS.popMatrix();
    }

    public void sphere(final PVector p_pos, final float p_width, final float p_height, final float p_depth) {
        this.GRAPHICS.pushMatrix();
        this.translate(p_pos);
        this.GRAPHICS.scale(p_width, p_height, p_depth);
        this.GRAPHICS.sphere(1);
        this.GRAPHICS.popMatrix();
    }

    public void sphere(final float p_x, final float p_y, final float p_z, final PVector p_dimensions) {
        this.GRAPHICS.pushMatrix();
        this.GRAPHICS.translate(p_x, p_y, p_z);
        this.GRAPHICS.scale(p_dimensions.x, p_dimensions.y, p_dimensions.z);
        this.GRAPHICS.sphere(1);
        this.GRAPHICS.popMatrix();
    }

    public void sphere(final float p_x, final float p_y, final float p_z, final float p_size) {
        this.GRAPHICS.pushMatrix();
        this.GRAPHICS.translate(p_x, p_y, p_z);
        this.GRAPHICS.sphere(p_size);
        this.GRAPHICS.popMatrix();
    }

    public void sphere(final PVector p_pos, final PVector p_dimensions) {
        this.GRAPHICS.pushMatrix();
        this.translate(p_pos);
        this.GRAPHICS.scale(p_dimensions.x, p_dimensions.y, p_dimensions.z);
        this.GRAPHICS.sphere(1);
        this.GRAPHICS.popMatrix();
    }

    public void sphere(final PVector p_pos, final float p_size) {
        this.GRAPHICS.pushMatrix();
        this.translate(p_pos);
        this.GRAPHICS.sphere(p_size);
        this.GRAPHICS.popMatrix();
    }
    // endregion
    // endregion

    // region 2D rendering.
    /**
     * Pushes the graphics buffer, disables depth testing and resets all current
     * transformations (they're restored by a call to {@linkplain NerdSketch#pop()
     * NerdSketch::pop()} later!).
     */
    public void begin2d() {
        this.push();
        this.GRAPHICS.hint(PConstants.DISABLE_DEPTH_TEST);
        this.GRAPHICS.perspective();
        this.GRAPHICS.camera();
    }

    /** Pops back transformations and enables depth testing. */
    public void end2d() {
        this.pop();
        this.GRAPHICS.hint(PConstants.ENABLE_DEPTH_TEST);
        if (this.currentCamera != null)
            this.currentCamera.applyMatrix();
    }

    // Best for these `*shape2d()` operations to be explicit!...
    // /**
    // * Pushes the graphics buffer, disables depth testing and resets all current
    // * transformations (they're restored by a call to {@linkplain NerdSketch#pop()
    // * NerdSketch::pop()} later!), then begins a shape.
    // */
    // public void beginShape2d() {
    // this.begin2d();
    // super.beginShape();
    // }
    //
    // /** Ends the shape, and pops back transformations and enables depth testing.
    // */
    // public void endShape2d() {
    // this.endShape();
    // this.end2d();
    // }

    /**
     * Pushes the graphics buffer, disables depth testing, resets all current
     * transformations, calls your {@link Runnable} {@code p_toDraw}, and finally,
     * pops back the transformations and enables depth testing!
     *
     * @see {@linkplain NerdSketch#end2d() NerdSketch::end2d()},
     * @see {@linkplain NerdSketch#begin2d() NerdSketch::begin2d()}.
     */
    public void in2d(final Runnable p_toDraw) {
        // #JIT_FTW!
        this.begin2d();
        p_toDraw.run();
        this.end2d();
    }
    // endregion

    // region `background(PImage)` and `background()`-with-alpha overrides.
    /**
     * Draws the {@code p_bgImage} as if it was a background. You may even choose to
     * call one of the {@linkplain PApplet#tint() PApplet::tint()} overloads before
     * calling this!
     */
    @Override
    public void background(final PImage p_bgImage) {
        Objects.requireNonNull(p_bgImage);
        this.begin2d();
        this.GRAPHICS.image(p_bgImage,
                this.WINDOW.cx, this.WINDOW.cy,
                this.WINDOW.width, this.WINDOW.height);
        this.end2d();
    }

    @Override
    protected void backgroundWithAlphaInitialPushMethod() {
        this.begin2d();
    }

    @Override
    protected void backgroundWithAlphaRectRenderingImpl() {
        // Removing this will not display the previous camera's view,
        // but still show clipping:

        this.GRAPHICS.camera();
        this.GRAPHICS.noStroke();
        this.GRAPHICS.rectMode(PConstants.CORNER);
        this.GRAPHICS.rect(0, 0, this.GRAPHICS.width, this.GRAPHICS.height);
        this.end2d();
    }
    // endregion

    // region `P3D`-only methods from `PGraphics`.
    public void beginCamera() {
        this.GRAPHICS.beginCamera();
    }

    public void endCamera() {
        this.GRAPHICS.endCamera();
    }

    public void bezier(final float x1, final float y1, final float z1, final float x2, final float y2, final float z2,
            final float x3, final float y3, final float z3, final float x4, final float y4, final float z4) {
        this.GRAPHICS.bezier(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4);
    }

    public void bezierVertex(final float x2, final float y2, final float z2, final float x3, final float y3,
            final float z3, final float x4, final float y4, final float z4) {
        this.GRAPHICS.bezierVertex(x2, y2, z2, x3, y3, z3, x4, y4, z4);
    }

    public void curve(final float x1, final float y1, final float z1, final float x2, final float y2, final float z2,
            final float x3, final float y3, final float z3, final float x4, final float y4, final float z4) {
        this.GRAPHICS.curve(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4);
    }
    // endregion

}
