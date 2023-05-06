package com.brahvim.nerd.io.asset_loader.processing_loaders;

import com.brahvim.nerd.io.asset_loader.AssetLoaderFailedException;
import com.brahvim.nerd.io.asset_loader.AssetLoader;
import com.brahvim.nerd.papplet_wrapper.NerdSketch;

import processing.core.PFont;

public class PFontAsset extends AssetLoader<PFont> {

	@Override
	public PFont fetchData(final NerdSketch p_sketch, final String p_path)
			throws AssetLoaderFailedException, IllegalArgumentException {
		final PFont font = p_sketch.loadFont(p_path);

		if (font == null)
			throw new AssetLoaderFailedException();

		return font;
	}

}