package com.brahvim.nerd.io.asset_loader.processing_loaders;

import com.brahvim.nerd.io.asset_loader.AssetLoaderFailedException;
import com.brahvim.nerd.io.asset_loader.AssetLoaderOptions;
import com.brahvim.nerd.io.asset_loader.AssetType;
import com.brahvim.nerd.papplet_wrapper.Sketch;

import processing.core.PImage;

public class PImageAsset extends AssetType<PImage> {

	// region SINGLETON STUFF.
	private static PImageAsset LOADER = new PImageAsset();

	public static AssetType<PImage> getLoader() {
		return PImageAsset.LOADER;
	}
	// endregion

	@Override
	public PImage fetchData(Sketch p_sketch, String p_path, AssetLoaderOptions... p_options)
			throws AssetLoaderFailedException, IllegalArgumentException {
		PImage img = p_sketch.loadImage(p_path);

		// Oh, it failed?
		boolean failure = img == null;

		if (!failure)
			failure = img.width == -1;

		if (failure)
			throw new AssetLoaderFailedException();

		return img;
	}

}
