package com.brahvim.nerd.io.asset_loader.processing_loaders;

import com.brahvim.nerd.io.asset_loader.NerdAssetLoaderException;
import com.brahvim.nerd.processing_wrapper.NerdSketch;
import com.brahvim.nerd.io.asset_loader.NerdAssetLoader;

import processing.core.PImage;

public class PImageAsset extends NerdAssetLoader<PImage> {

	@Override
	public PImage fetchData(final NerdSketch p_sketch, final String p_path)
			throws NerdAssetLoaderException, IllegalArgumentException {
		final PImage img = p_sketch.loadImage(p_path);

		// Oh, it failed?
		boolean failure = img == null;

		if (!failure)
			failure = img.width == -1;

		if (failure)
			throw new NerdAssetLoaderException();

		return img;
	}

}
