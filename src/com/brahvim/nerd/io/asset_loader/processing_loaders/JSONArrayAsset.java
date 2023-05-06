package com.brahvim.nerd.io.asset_loader.processing_loaders;

import com.brahvim.nerd.io.asset_loader.AssetLoaderFailedException;
import com.brahvim.nerd.io.asset_loader.AssetLoader;
import com.brahvim.nerd.papplet_wrapper.NerdSketch;

import processing.data.JSONArray;

public class JSONArrayAsset extends AssetLoader<JSONArray> {

	@Override
	public JSONArray fetchData(final NerdSketch p_sketch, final String p_path)
			throws AssetLoaderFailedException, IllegalArgumentException {
		try {
			return p_sketch.loadJSONArray(p_path);
		} catch (final NullPointerException e) {
			throw new AssetLoaderFailedException();
		}
	}

}