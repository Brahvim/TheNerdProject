package com.brahvim.nerd.io.asset_loader.processing_loaders;

import com.brahvim.nerd.io.asset_loader.NerdAssetLoaderException;
import com.brahvim.nerd.io.asset_loader.NerdSinglePathAssetLoader;
import com.brahvim.nerd.processing_wrapper.NerdSketch;

import processing.core.PFont;
import processing.core.PGraphics;

public class NerdPFontAsset<SketchPGraphicsT extends PGraphics>
		extends NerdSinglePathAssetLoader<SketchPGraphicsT, PFont> implements NerdProcessingAsset {

	public NerdPFontAsset(final String p_path) {
		super(p_path);
	}

	@Override
	protected PFont fetchData(final NerdSketch<SketchPGraphicsT> p_sketch)
			throws NerdAssetLoaderException, IllegalArgumentException {
		final PFont font = p_sketch.loadFont(super.path);

		if (font == null)
			throw new NerdAssetLoaderException(this);

		return font;
	}

}