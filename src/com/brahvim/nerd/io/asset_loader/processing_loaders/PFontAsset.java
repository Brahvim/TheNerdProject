package com.brahvim.nerd.io.asset_loader.processing_loaders;

import com.brahvim.nerd.io.asset_loader.AssetLoaderFailedException;
import com.brahvim.nerd.io.asset_loader.AssetLoaderOptions;
import com.brahvim.nerd.io.asset_loader.AssetLoader;
import com.brahvim.nerd.papplet_wrapper.Sketch;

import processing.core.PFont;

public class PFontAsset extends AssetLoader<PFont> {

	// region SINGLETON STUFF.
	private static PFontAsset LOADER = new PFontAsset();

	public static AssetLoader<PFont> getLoader() {
		return PFontAsset.LOADER;
	}
	// endregion

	@Override
	public PFont fetchData(final Sketch p_sketch, final String p_path, final AssetLoaderOptions... p_options)
			throws AssetLoaderFailedException, IllegalArgumentException {
		final PFont font = p_sketch.loadFont(p_path);

		if (font == null)
			throw new AssetLoaderFailedException();

		return font;
	}

}