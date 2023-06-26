package com.brahvim.nerd.io;

import java.util.ArrayList;

import com.brahvim.nerd.processing_wrapper.NerdSketch;

import processing.core.PImage;

public class NerdSpriteSheet {

	private class SpritePos {
		public int x, y;
	}

	// region Fields.
	private final PImage SHEET;
	private final NerdSketch SKETCH;

	private ArrayList<PImage> sprites = new ArrayList<>(2);
	// If you literally have only one image, why're you even using a 'sheet'?!

	/**
	 * Position of a sprite, as well as its ID in {@link NerdSpriteSheet#sprites}.
	 */
	private ArrayList<SpritePos> poses = new ArrayList<>(2);
	// endregion

	public NerdSpriteSheet(final NerdSketch p_sketch, final PImage p_sheet) {
		this.SHEET = p_sheet;
		this.SKETCH = p_sketch;
	}

	/**
	 * You can also choose to simply call this method, without using its return
	 * value, to cache the sprite, and have to worry less about performance!
	 */
	public PImage getSprite(final int p_x, final int p_y, final int p_width, final int p_height) {
		for (int i = this.poses.size() - 1; i != -1; i--) {
			final SpritePos p = this.poses.get(i);
			if (p.x == p_x && p.y == p_y)
				return this.sprites.get(i);
		}

		final PImage toCache = this.SKETCH.createImage(p_width, p_height, this.SHEET.format);

		this.SHEET.copy(toCache,
				p_x, p_y,
				p_width, p_height,
				0, 0,
				p_width, p_height);

		this.sprites.add(toCache);

		return toCache;
	}

	@Override
	public NerdSpriteSheet clone() {
		final NerdSpriteSheet toRet = new NerdSpriteSheet(this.SKETCH, this.SHEET);

		toRet.poses = new ArrayList<>(this.poses);
		toRet.sprites = new ArrayList<>(this.sprites);

		for (final PImage i : toRet.sprites)
			i.parent = toRet.SKETCH;

		return toRet;
	}

}
