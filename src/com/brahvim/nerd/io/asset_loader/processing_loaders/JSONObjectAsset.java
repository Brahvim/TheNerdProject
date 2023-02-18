package com.brahvim.nerd.io.asset_loader.processing_loaders;

import com.brahvim.nerd.io.asset_loader.AssetLoaderFailedException;
import com.brahvim.nerd.io.asset_loader.AssetType;
import com.brahvim.nerd.papplet_wrapper.Sketch;

import processing.data.JSONObject;

public class JSONObjectAsset extends AssetType<JSONObject> {

	// region SINGLETON STUFF.
	private static JSONObjectAsset LOADER = new JSONObjectAsset();

	public static AssetType<?> getLoader() {
		return JSONObjectAsset.LOADER;
	}
	// endregion

	@Override
	public JSONObject fetchData(Sketch SKETCH, String p_path, Object... p_options)
			throws AssetLoaderFailedException, IllegalArgumentException {
		try {
			return SKETCH.loadJSONObject(p_path);
		} catch (NullPointerException e) {
			throw new AssetLoaderFailedException();
		}
	}

}