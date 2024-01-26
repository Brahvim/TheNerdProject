package com.brahvim.nerd.processing_wrapper.graphics_backends;

import java.util.Objects;

import com.brahvim.nerd.framework.cameras.NerdAbstractCamera;
import com.brahvim.nerd.framework.cameras.NerdBasicCamera;
import com.brahvim.nerd.framework.cameras.NerdFlyCamera;
import com.brahvim.nerd.math.NerdUnprojector;
import com.brahvim.nerd.processing_wrapper.NerdSketch;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import processing.opengl.PGraphics2D;

public class NerdP2dGraphics extends NerdGlGenericGraphics<PGraphics2D> {

    protected final NerdUnprojector UNPROJECTOR;

    // region Utilitarian constructors.
    protected NerdP2dGraphics(final NerdSketch<PGraphics2D> p_sketch, final int p_width, final int p_height,
            final String p_renderer) {
        this(p_sketch, (PGraphics2D) p_sketch.createGraphics(p_width, p_height, p_renderer));
    }

    protected NerdP2dGraphics(final NerdSketch<PGraphics2D> p_sketch, final int p_width, final int p_height) {
        this(p_sketch, (PGraphics2D) p_sketch.createGraphics(p_width, p_height));
    }

    protected NerdP2dGraphics(
            final NerdSketch<PGraphics2D> p_sketch,
            final float p_width,
            final float p_height) {
        this(p_sketch, (PGraphics2D) p_sketch.createGraphics(p_width, p_height));
    }

    protected NerdP2dGraphics(final NerdSketch<PGraphics2D> p_sketch, final float p_size) {
        this(p_sketch, (PGraphics2D) p_sketch.createGraphics(p_size));
    }

    protected NerdP2dGraphics(final NerdSketch<PGraphics2D> p_sketch, final int p_size) {
        this(p_sketch, (PGraphics2D) p_sketch.createGraphics(p_size));
    }

    protected NerdP2dGraphics(final NerdSketch<PGraphics2D> p_sketch) {
        this(p_sketch, (PGraphics2D) p_sketch.createGraphics());
    }
    // endregion

    public NerdP2dGraphics(final NerdSketch<PGraphics2D> p_sketch, final PGraphics2D p_graphics) {
        super(p_sketch, p_graphics);
        this.UNPROJECTOR = new NerdUnprojector();
    }

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
        return new PVector(this.screenX(p_vec.x, p_vec.y), this.screenY(p_vec.x, p_vec.y));
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
    // ...how about `p_modelMatInvMulter`? :rofl:!
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
        return this.GRAPHICS.screenX(0, 0);
    }

    public float screenY() {
        return this.GRAPHICS.screenY(0, 0);
    }

    public float screenZ() {
        return this.GRAPHICS.screenY(0, 0);
    }
    // endregion

    @Override
    public float screenX(final float x, final float y) {
        return this.GRAPHICS.screenX(x, y);
    }

    @Override
    public float screenY(final float x, final float y) {
        return this.GRAPHICS.screenY(x, y);
    }

    // region `p_vec`!
    // The following two were going to exclude the `z` if it was `0`.
    // And later, I felt this was risky.
    // This two-`float` overload ain't in the docs, that scares me!

    // ...ACTUALLY,
    // https://github.com/SKETCHssing/SKETCHssing/blob/459853d0dcdf1e1648b1049d3fdbb4bf233fded8/core/src/SKETCHssing/opengl/PGraphicsOpenGL.java#L4611
    // ..."they rely on the JIT too!" (no, they don't optimize this at all. They
    // just put the `0` themselves, LOL.) :joy:

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
        this.GRAPHICS.camera(p_cam.getPos().x, p_cam.getPos().y, p_cam.getPos().z, p_cam.getCenter().x,
                p_cam.getCenter().y, p_cam.getCenter().z, p_cam.getUp().x, p_cam.getUp().y, p_cam.getUp().z);
    }

    public void camera(final NerdFlyCamera p_cam) {
        this.GRAPHICS.camera(p_cam.getPos().x, p_cam.getPos().y, p_cam.getPos().z,
                p_cam.getPos().x + p_cam.front.x, p_cam.getPos().y + p_cam.front.y, p_cam.getPos().z + p_cam.front.z,
                p_cam.getUp().x, p_cam.getUp().y, p_cam.getUp().z);
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
     * graphics anyway! :joy:
     */
    public PVector getMouseInWorld() {
        return this.getMouseInWorldFromFarPlane(this.currentCamera.mouseZ);
    }

    public PVector getMouseInWorldFromFarPlane(final float p_distanceFromFarPlane) {
        return this.worldVec(this.INPUT.mouseX, this.INPUT.mouseY,
                this.currentCamera.far - p_distanceFromFarPlane + this.currentCamera.near);
    }

    public PVector getMouseInWorldAtZ(final float p_distanceFromCamera) {
        return this.worldVec(this.INPUT.mouseX, this.INPUT.mouseY, p_distanceFromCamera);
    }
    // endregion

    // region Touches.
    // /**
    // * Caching this vector never works. Call this method everytime!~
    // * People recalculate things framely in computer graphics anyway! :joy:
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

    // region Not in `SVG`/`PDF`, but are here.
    public boolean save(final String filename) {
        return this.GRAPHICS.save(filename);
    }

    public void blend(final int sx, final int sy, final int sw, final int sh,
            final int dx, final int dy, final int dw,
            final int dh, final int mode) {
        this.GRAPHICS.blend(sx, sy, sw, sh, dx, dy, dw, dh, mode);
    }

    public void blend(final PImage src, final int sx, final int sy, final int sw,
            final int sh, final int dx,
            final int dy, final int dw, final int dh, final int mode) {
        this.GRAPHICS.blend(src, sx, sy, sw, sh, dx, dy, dw, dh, mode);
    }

    public void copy(final int sx, final int sy, final int sw, final int sh,
            final int dx, final int dy, final int dw,
            final int dh) {
        this.GRAPHICS.copy(sx, sy, sw, sh, dx, dy, dw, dh);
    }

    public void copy(final PImage src, final int sx, final int sy, final int sw,
            final int sh, final int dx,
            final int dy, final int dw, final int dh) {
        this.GRAPHICS.copy(src, sx, sy, sw, sh, dx, dy, dw, dh);
    }

    public void filter(final int kind) {
        this.GRAPHICS.filter(kind);
    }

    public void filter(final int kind, final float param) {
        this.GRAPHICS.filter(kind, param);
    }

    public PImage get() {
        return this.GRAPHICS.get();
    }

    public int get(final int x, final int y) {
        return this.GRAPHICS.get(x, y);
    }

    public PImage get(final int x, final int y, final int w, final int h) {
        return this.GRAPHICS.get(x, y, w, h);

    }

    public void loadPixels() {
        this.GRAPHICS.loadPixels();
    }

    public void mask(final int[] maskArray) {
        this.GRAPHICS.mask(maskArray);
    }

    public void mask(final PImage img) {
        this.GRAPHICS.mask(img);
    }

    public void updatePixels() {
        this.GRAPHICS.updatePixels();
    }

    public void updatePixels(final int x, final int y, final int w, final int h) {
        this.GRAPHICS.updatePixels(x, y, w, h);
    }

    // endregion

    /**
     * Draws the {@code p_bgImage} as if it was a background. You may even choose to
     * call one of the
     * {@linkplain PApplet#tint() PApplet::tint()} overloads before calling this!
     */
    @Override
    public void background(final PImage p_bgImage) {
        Objects.requireNonNull(p_bgImage);
        super.push();
        this.GRAPHICS.image(p_bgImage, this.WINDOW.cx, this.WINDOW.cy, this.WINDOW.width,
                this.WINDOW.height);
        super.pop();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((this.UNPROJECTOR == null) ? 0 : this.UNPROJECTOR.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final NerdP2dGraphics other = (NerdP2dGraphics) obj;
        if (this.UNPROJECTOR == null) {
            if (other.UNPROJECTOR != null) {
                return false;
            }
        } else if (!this.UNPROJECTOR.equals(other.UNPROJECTOR)) {
            return false;
        }
        return true;
    }

}
