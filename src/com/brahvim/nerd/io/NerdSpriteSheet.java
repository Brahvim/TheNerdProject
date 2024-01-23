package com.brahvim.nerd.io;

import java.util.ArrayList;
import java.util.List;

import com.brahvim.nerd.processing_wrapper.NerdSketch;

import processing.core.PImage;

public class NerdSpriteSheet {

	private class SpritePos {
		public int x, y;
	}

	// region Fields.
	private final PImage SHEET;
	private final NerdSketch<?> SKETCH;

	private List<PImage> sprites = new ArrayList<>(2);
	// If you literally have only one image, why're you even using a 'sheet'?!
	// ...Oh, preparing in advance? Well then, nevermind.

	/**
	 * Position and ID of a sprite in {@linkplain NerdSpriteSheet#sprites
	 * NerdSpriteSheet::sprites}.
	 */
	private List<SpritePos> poses = new ArrayList<>(2);
	// endregion

	public NerdSpriteSheet(final NerdSketch<?> p_sketch, final PImage p_sheet) {
		this.SHEET = p_sheet;
		this.SKETCH = p_sketch;
	}

	public NerdSpriteSheet(final NerdSpriteSheet p_sheet) {
		this.SHEET = p_sheet.SHEET;
		this.SKETCH = p_sheet.SKETCH;

		// Non-`final` fields:
		this.poses = p_sheet.poses;
		this.sprites = p_sheet.sprites;
	}

	/**
	 * You can also choose to simply call this method, without using its return
	 * value, to cache the sprite, and have to
	 * worry less about performance!
	 */
	public PImage getSprite(final int p_x, final int p_y, final int p_width, final int p_height) {
		for (int i = this.poses.size() - 1; i != -1; i--) {
			final SpritePos p = this.poses.get(i);
			if (p.x == p_x && p.y == p_y)
				return this.sprites.get(i);
		}

		final PImage toCache = this.SKETCH.createImage(p_width, p_height, this.SHEET.format);

		this.SHEET.copy(toCache, p_x, p_y, p_width, p_height, 0, 0, p_width, p_height);

		this.sprites.add(toCache);

		return toCache;
	}

}
