package com.brahvim.nerd.io.asset_loader.processing_loaders;

import com.brahvim.nerd.io.asset_loader.NerdAssetLoaderException;
import com.brahvim.nerd.io.asset_loader.NerdSinglePathAssetLoader;
import com.brahvim.nerd.processing_wrapper.NerdSketch;

import processing.core.PGraphics;
import processing.data.JSONArray;

public class NerdPJsonArrayAsset<SketchPGraphicsT extends PGraphics>
		extends NerdSinglePathAssetLoader<SketchPGraphicsT, JSONArray> implements NerdProcessingAsset {

	public NerdPJsonArrayAsset(final String p_path) {
		super(p_path);
	}

	@Override
	protected JSONArray fetchData(final NerdSketch<SketchPGraphicsT> p_sketch)
			throws NerdAssetLoaderException, IllegalArgumentException {
		try {
			return p_sketch.loadJSONArray(super.path);
		} catch (final NullPointerException e) {
			throw new NerdAssetLoaderException(this);
		}
	}

}