package com.brahvim.nerd.io.asset_loader.processing_loaders;

import com.brahvim.nerd.io.asset_loader.AssetLoaderFailedException;
import com.brahvim.nerd.io.asset_loader.AssetLoaderOptions;
import com.brahvim.nerd.io.asset_loader.AssetType;
import com.brahvim.nerd.papplet_wrapper.Sketch;

import processing.data.JSONArray;

public class JSONArrayAsset extends AssetType<JSONArray> {

	// region SINGLETON STUFF.
	private static JSONArrayAsset LOADER = new JSONArrayAsset();

	public static AssetType<?> getLoader() {
		return JSONArrayAsset.LOADER;
	}
	// endregion

	@Override
	public JSONArray fetchData(Sketch p_sketch, String p_path, AssetLoaderOptions... p_options)
			throws AssetLoaderFailedException, IllegalArgumentException {
		try {
			return p_sketch.loadJSONArray(p_path);
		} catch (NullPointerException e) {
			throw new AssetLoaderFailedException();
		}
	}

}