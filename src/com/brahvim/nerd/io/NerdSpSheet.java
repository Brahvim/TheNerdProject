package com.brahvim.nerd.io;

import java.util.ArrayList;

import com.brahvim.nerd.papplet_wrapper.Sketch;

import processing.core.PImage;

public class NerdSpSheet implements Cloneable {

	private class SpPos {
		public int x, y;
	}

	// region Fields.
	private final Sketch SKETCH;

	private PImage sheet; // Not `final` so the user can dispose off sheets after one use.
	private ArrayList<PImage> sprites = new ArrayList<>(2);

	/** Position of a sprite, as well as its ID in {@code NerdSpSheet::sprites}. */
	private ArrayList<SpPos> poses = new ArrayList<>(2);
	// endregion

	public NerdSpSheet(Sketch p_sketch, PImage p_sheet) {
		this.sheet = p_sheet;
		this.SKETCH = p_sketch;
	}

	/**
	 * You can also choose to simply call this method, without using its return
	 * value, to cache the sprite, and have to worry less about performance!
	 */
	public PImage getSprite(int p_x, int p_y, int p_width, int p_height) {
		for (int i = poses.size() - 1; i != -1; i--) {
			final SpPos p = poses.get(i);
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

	@Override
	public Object clone() throws CloneNotSupportedException {
		return this.clone();
	}

	public NerdSpSheet clone(Sketch p_sketch) {
		final NerdSpSheet toRet = new NerdSpSheet(this.SKETCH, this.sheet);

		toRet.poses = new ArrayList<>(this.poses);
		toRet.sprites = new ArrayList<>(this.sprites);

		for (PImage i : toRet.sprites)
			i.parent = toRet.SKETCH;

		return toRet;
	}

}
