package com.brahvim.nerd.framework.graphics_backends;

import com.brahvim.nerd.processing_wrapper.NerdAbstractGraphics;
import com.brahvim.nerd.processing_wrapper.NerdSketch;

import processing.core.PGraphics;
import processing.core.PImage;

public abstract class NerdFramebufferBackedGraphics<SketchPGraphicsT extends PGraphics>
		extends NerdAbstractGraphics<SketchPGraphicsT> {

	public class PixelOperation implements AutoCloseable {

		public final int[] PIXELS = NerdFramebufferBackedGraphics.this.loadPixels();

		@Override
		public void close() throws Exception {
			// NerdFramebufferBackedGraphics.this.GRAPHICS.pixels = this.PIXELS;
			NerdFramebufferBackedGraphics.this.updatePixels();
		}

	}

	// region Utilitarian constructors.
	protected NerdFramebufferBackedGraphics(
			final NerdSketch<SketchPGraphicsT> p_sketch) {
		super(p_sketch);
	}

	protected NerdFramebufferBackedGraphics(
			final NerdSketch<SketchPGraphicsT> p_sketch, final float p_size) {
		super(p_sketch, p_size);
	}

	protected NerdFramebufferBackedGraphics(
			final NerdSketch<SketchPGraphicsT> p_sketch, final float p_width,
			final float p_height) {
		super(p_sketch, p_width, p_height);
	}

	protected NerdFramebufferBackedGraphics(
			final NerdSketch<SketchPGraphicsT> p_sketch, final int p_size) {
		super(p_sketch, p_size);
	}

	protected NerdFramebufferBackedGraphics(
			final NerdSketch<SketchPGraphicsT> p_sketch, final int p_width,
			final int p_height) {
		super(p_sketch, p_width, p_height);
	}

	protected NerdFramebufferBackedGraphics(
			final NerdSketch<SketchPGraphicsT> p_sketch, final int p_width, final int p_height,
			final String p_renderer) {
		super(p_sketch, p_width, p_height, p_renderer);
	}

	protected NerdFramebufferBackedGraphics(
			final NerdSketch<SketchPGraphicsT> p_sketch, final int p_width, final int p_height,
			final String p_renderer,
			final String p_path) {
		super(p_sketch, p_width, p_height, p_renderer, p_path);
	}

	protected NerdFramebufferBackedGraphics(
			final NerdSketch<SketchPGraphicsT> p_sketch,
			final SketchPGraphicsT p_pGraphicsToWrap) {
		super(p_sketch, p_pGraphicsToWrap);
	}
	// endregion

	// region Not in `SVG`/`PDF`, but here!
	public void set(final int x, final int y, final int c) {
		super.GRAPHICS.set(x, y, c);
	}

	// TODO: Write a `NerdColor` version for `set()`!
	// public void set(final int x, final int y, final NerdNoAlphaColor
	// noAlphaColor) {
	// super.GRAPHICS.set(x, y, super.color(noAlphaColor));
	// }

	public void set(final int x, final int y, final PImage img) {
		super.GRAPHICS.set(x, y, img);
	}

	public boolean save(final String filename) {
		return super.GRAPHICS.save(filename);
	}

	public void blend(final int sx, final int sy, final int sw, final int sh,
			final int dx, final int dy, final int dw,
			final int dh, final int mode) {
		super.GRAPHICS.blend(sx, sy, sw, sh, dx, dy, dw, dh, mode);
	}

	public void blend(final PImage src, final int sx, final int sy, final int sw,
			final int sh, final int dx,
			final int dy, final int dw, final int dh, final int mode) {
		super.GRAPHICS.blend(src, sx, sy, sw, sh, dx, dy, dw, dh, mode);
	}

	public void copy(final int sx, final int sy, final int sw, final int sh,
			final int dx, final int dy, final int dw,
			final int dh) {
		super.GRAPHICS.copy(sx, sy, sw, sh, dx, dy, dw, dh);
	}

	public void copy(final PImage src, final int sx, final int sy, final int sw,
			final int sh, final int dx,
			final int dy, final int dw, final int dh) {
		super.GRAPHICS.copy(src, sx, sy, sw, sh, dx, dy, dw, dh);
	}

	public void filter(final int kind) {
		super.GRAPHICS.filter(kind);
	}

	public void filter(final int kind, final float param) {
		super.GRAPHICS.filter(kind, param);
	}

	public PImage get() {
		return super.GRAPHICS.get();
	}

	public int get(final int x, final int y) {
		return super.GRAPHICS.get(x, y);
	}

	public PImage get(final int x, final int y, final int w, final int h) {
		return super.GRAPHICS.get(x, y, w, h);

	}

	public void mask(final int[] maskArray) {
		super.GRAPHICS.mask(maskArray);
	}

	public void mask(final PImage img) {
		super.GRAPHICS.mask(img);
	}

	public int[] loadPixels() {
		super.GRAPHICS.loadPixels();
		return super.GRAPHICS.pixels;
	}

	public void updatePixels() {
		super.GRAPHICS.updatePixels();
	}

	public void updatePixels(final int x, final int y, final int w, final int h) {
		super.GRAPHICS.updatePixels(x, y, w, h);
	}
	// endregion

}
