package com.brahvim.nerd.io;

import java.util.ArrayList;

import com.brahvim.nerd.papplet_wrapper.Sketch;

import processing.core.PImage;

public class NerdSpriteSheet implements Cloneable {

	private class SpritePos {
		public int x, y;
	}

	// region Fields.
	private final Sketch SKETCH;

	private final PImage sheet; // Not `final` so the user can dispose off sheets after one use.
	private ArrayList<PImage> sprites = new ArrayList<>(2);

	/** Position of a sprite, as well as its ID in {@code NerdSpSheet::sprites}. */
	private ArrayList<SpritePos> poses = new ArrayList<>(2);
	// endregion

	public NerdSpriteSheet(final Sketch p_sketch, final PImage p_sheet) {
		this.sheet = p_sheet;
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

		final PImage toCache = this.SKETCH.createImage(p_width, p_height, this.sheet.format);

		this.sheet.copy(toCache,
				p_x, p_y,
				p_width, p_height,
				0, 0,
				p_width, p_height);

		this.sprites.add(toCache);

		return toCache;
	}

	public NerdSpriteSheet clone(final Sketch p_sketch) {
		final NerdSpriteSheet toRet = new NerdSpriteSheet(this.SKETCH, this.sheet);

		toRet.poses = new ArrayList<>(this.poses);
		toRet.sprites = new ArrayList<>(this.sprites);

		for (final PImage i : toRet.sprites)
			i.parent = toRet.SKETCH;

		return toRet;
	}

}
