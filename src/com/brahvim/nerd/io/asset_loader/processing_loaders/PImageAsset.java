package com.brahvim.nerd.io.asset_loader.processing_loaders;

import com.brahvim.nerd.io.asset_loader.NerdAssetLoaderException;
import com.brahvim.nerd.io.asset_loader.NerdSinglePathAssetLoader;
import com.brahvim.nerd.processing_wrapper.NerdSketch;

import processing.core.PImage;

public class PImageAsset extends NerdSinglePathAssetLoader<PImage> {

	public PImageAsset(final String p_path) {
		super(p_path);
	}

	@Override
	protected PImage fetchData(final NerdSketch<?> p_sketch)
			throws NerdAssetLoaderException, IllegalArgumentException {
		final PImage img = p_sketch.loadImage(super.path);

		// Oh, it failed?
		boolean failure = img == null;

		if (!failure)
			failure = img.width == -1;

		if (failure)
			throw new NerdAssetLoaderException(this);

		return img;
	}

}
