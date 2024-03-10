package com.brahvim.nerd.framework.graphics_backends;

import java.util.Objects;

import com.brahvim.nerd.processing_wrapper.NerdSketch;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import processing.opengl.PGraphics2D;

public class NerdP2dGraphics extends NerdOpenGlGraphics<PGraphics2D> {

    // region Utilitarian constructors.
    protected NerdP2dGraphics(final NerdSketch<PGraphics2D> p_sketch, final int p_width, final int p_height,
            final String p_renderer) {
        this(p_sketch, p_sketch.createGraphics(p_width, p_height, p_renderer));
    }

    protected NerdP2dGraphics(final NerdSketch<PGraphics2D> p_sketch, final int p_width, final int p_height) {
        this(p_sketch, p_sketch.createGraphics(p_width, p_height));
    }

    protected NerdP2dGraphics(
            final NerdSketch<PGraphics2D> p_sketch,
            final float p_width,
            final float p_height) {
        this(p_sketch, p_sketch.createGraphics(p_width, p_height));
    }

    protected NerdP2dGraphics(final NerdSketch<PGraphics2D> p_sketch, final float p_size) {
        this(p_sketch, p_sketch.createGraphics(p_size));
    }

    protected NerdP2dGraphics(final NerdSketch<PGraphics2D> p_sketch, final int p_size) {
        this(p_sketch, p_sketch.createGraphics(p_size));
    }

    protected NerdP2dGraphics(final NerdSketch<PGraphics2D> p_sketch) {
        this(p_sketch, p_sketch.createGraphics());
    }
    // endregion

    public NerdP2dGraphics(final NerdSketch<PGraphics2D> p_sketch, final PGraphics2D p_graphics) {
        super(p_sketch, p_graphics);
    }

    // region `modelVec()` and `screenVec()`.
    public PVector modelVec() {
        return new PVector(
                // "I passed these `0`s in myself, yeah. Let's not rely on the JIT too much!"
                // - Me before re-thinking that.
                this.modelX(), this.modelY(), 0); // The `z`-value is `0` in 2D, of course!
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
        return new PVector(this.screenX(), this.screenY(), 0); // The `z`-value is `0` in 2D, of course!
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
        return this.GRAPHICS.screenX(0, 0);
    }

    public float screenY() {
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

    // region Not in `SVG`/`PDF`, but are here.
    @Override
    public boolean save(final String filename) {
        return this.GRAPHICS.save(filename);
    }

    @Override
    public void blend(final int sx, final int sy, final int sw, final int sh,
            final int dx, final int dy, final int dw,
            final int dh, final int mode) {
        this.GRAPHICS.blend(sx, sy, sw, sh, dx, dy, dw, dh, mode);
    }

    @Override
    public void blend(final PImage src, final int sx, final int sy, final int sw,
            final int sh, final int dx,
            final int dy, final int dw, final int dh, final int mode) {
        this.GRAPHICS.blend(src, sx, sy, sw, sh, dx, dy, dw, dh, mode);
    }

    @Override
    public void copy(final int sx, final int sy, final int sw, final int sh,
            final int dx, final int dy, final int dw,
            final int dh) {
        this.GRAPHICS.copy(sx, sy, sw, sh, dx, dy, dw, dh);
    }

    @Override
    public void copy(final PImage src, final int sx, final int sy, final int sw,
            final int sh, final int dx,
            final int dy, final int dw, final int dh) {
        this.GRAPHICS.copy(src, sx, sy, sw, sh, dx, dy, dw, dh);
    }

    @Override
    public void filter(final int kind) {
        this.GRAPHICS.filter(kind);
    }

    @Override
    public void filter(final int kind, final float param) {
        this.GRAPHICS.filter(kind, param);
    }

    @Override
    public PImage get() {
        return this.GRAPHICS.get();
    }

    @Override
    public int get(final int x, final int y) {
        return this.GRAPHICS.get(x, y);
    }

    @Override
    public PImage get(final int x, final int y, final int w, final int h) {
        return this.GRAPHICS.get(x, y, w, h);

    }

    @Override
    public void mask(final int[] maskArray) {
        this.GRAPHICS.mask(maskArray);
    }

    @Override
    public void mask(final PImage img) {
        this.GRAPHICS.mask(img);
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

}
