package com.brahvim.nerd.processing_wrapper.graphics_backends;

import com.brahvim.nerd.processing_wrapper.NerdSketch;

import processing.core.PImage;
import processing.javafx.PGraphicsFX2D;

public class NerdFx2dGraphics extends NerdGenericGraphics<PGraphicsFX2D> {

    public NerdFx2dGraphics(final NerdSketch<PGraphicsFX2D> p_sketch, final PGraphicsFX2D p_pGraphicsToWrap) {
        super(p_sketch, p_pGraphicsToWrap);
    }

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

}
