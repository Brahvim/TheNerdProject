package com.brahvim.nerd.io.asset_loader.processing_loaders;

import com.brahvim.nerd.io.asset_loader.NerdAssetLoaderException;
import com.brahvim.nerd.processing_wrapper.NerdSketch;
import com.brahvim.nerd.io.asset_loader.NerdAssetLoader;

import processing.data.JSONObject;

public class PJsonObjectAsset extends NerdAssetLoader<JSONObject> {

	@Override
	public JSONObject fetchData(final NerdSketch p_sketch, final String p_path)
			throws NerdAssetLoaderException, IllegalArgumentException {
		try {
			return p_sketch.loadJSONObject(p_path);
		} catch (final NullPointerException e) {
			throw new NerdAssetLoaderException();
		}
	}

}