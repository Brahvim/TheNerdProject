package com.brahvim.nerd.io.asset_loader.processing_loaders;

import com.brahvim.nerd.io.asset_loader.AssetLoaderFailedException;
import com.brahvim.nerd.io.asset_loader.AssetLoader;
import com.brahvim.nerd.papplet_wrapper.NerdSketch;

import processing.core.PImage;

public class PImageAsset extends AssetLoader<PImage> {

	@Override
	public PImage fetchData(final NerdSketch p_sketch, final String p_path)
			throws AssetLoaderFailedException, IllegalArgumentException {
		final PImage img = p_sketch.loadImage(p_path);

		// Oh, it failed?
		boolean failure = img == null;

		if (!failure)
			failure = img.width == -1;

		if (failure)
			throw new AssetLoaderFailedException();

		return img;
	}

}
