package com.brahvim.nerd.io.asset_loader.processing_loaders;

import com.brahvim.nerd.io.asset_loader.NerdAssetLoaderException;
import com.brahvim.nerd.io.asset_loader.NerdAssetLoader;
import com.brahvim.nerd.papplet_wrapper.NerdSketch;

import processing.core.PFont;

public class PFontAsset extends NerdAssetLoader<PFont> {

	@Override
	public PFont fetchData(final NerdSketch p_sketch, final String p_path)
			throws NerdAssetLoaderException, IllegalArgumentException {
		final PFont font = p_sketch.loadFont(p_path);

		if (font == null)
			throw new NerdAssetLoaderException();

		return font;
	}

}