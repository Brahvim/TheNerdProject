package com.brahvim.nerd.framework.graphics_backends;

import com.brahvim.nerd.processing_wrapper.NerdSketch;

import processing.core.PImage;
import processing.javafx.PGraphicsFX2D;

public class NerdFx2dGraphics extends NerdFramebufferBackedGraphics<PGraphicsFX2D> {

    // region Utilitarian constructors.
    protected NerdFx2dGraphics(final NerdSketch<PGraphicsFX2D> p_sketch, final int p_width, final int p_height,
            final String p_renderer) {
        this(p_sketch, p_sketch.createGraphics(p_width, p_height, p_renderer));
    }

    protected NerdFx2dGraphics(final NerdSketch<PGraphicsFX2D> p_sketch, final int p_width, final int p_height) {
        this(p_sketch, p_sketch.createGraphics(p_width, p_height));
    }

    protected NerdFx2dGraphics(
            final NerdSketch<PGraphicsFX2D> p_sketch,
            final float p_width,
            final float p_height) {
        this(p_sketch, p_sketch.createGraphics(p_width, p_height));
    }

    protected NerdFx2dGraphics(final NerdSketch<PGraphicsFX2D> p_sketch, final float p_size) {
        this(p_sketch, p_sketch.createGraphics(p_size));
    }

    protected NerdFx2dGraphics(final NerdSketch<PGraphicsFX2D> p_sketch, final int p_size) {
        this(p_sketch, p_sketch.createGraphics(p_size));
    }

    protected NerdFx2dGraphics(final NerdSketch<PGraphicsFX2D> p_sketch) {
        this(p_sketch, p_sketch.createGraphics());
    }
    // endregion

    public NerdFx2dGraphics(final NerdSketch<PGraphicsFX2D> p_sketch, final PGraphicsFX2D p_pGraphicsToWrap) {
        super(p_sketch, p_pGraphicsToWrap);
    }

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
    public int[] loadPixels() {
        this.GRAPHICS.loadPixels();
        return this.GRAPHICS.pixels;
    }

    @Override
    public void mask(final int[] maskArray) {
        this.GRAPHICS.mask(maskArray);
    }

    @Override
    public void mask(final PImage img) {
        this.GRAPHICS.mask(img);
    }

    @Override
    public void updatePixels() {
        this.GRAPHICS.updatePixels();
    }

    @Override
    public void updatePixels(final int x, final int y, final int w, final int h) {
        this.GRAPHICS.updatePixels(x, y, w, h);
    }
    // endregion

}
