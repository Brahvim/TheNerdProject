package com.brahvim.nerd.io.asset_loader.processing_loaders;

import com.brahvim.nerd.io.asset_loader.NerdAssetLoaderException;
import com.brahvim.nerd.io.asset_loader.NerdSinglePathAssetLoader;
import com.brahvim.nerd.processing_wrapper.NerdSketch;

import processing.core.PGraphics;

public class NerdPStringsAsset<SketchPGraphicsT extends PGraphics>
		extends NerdSinglePathAssetLoader<SketchPGraphicsT, String[]> implements NerdProcessingAsset {

	public NerdPStringsAsset(final String p_path) {
		super(p_path);
	}

	@Override
	protected String[] fetchData(final NerdSketch<SketchPGraphicsT> p_sketch)
			throws NerdAssetLoaderException, IllegalArgumentException {
		final String[] strings = p_sketch.loadStrings(super.path);

		if (strings == null)
			throw new NerdAssetLoaderException(this);

		return strings;
	}

}