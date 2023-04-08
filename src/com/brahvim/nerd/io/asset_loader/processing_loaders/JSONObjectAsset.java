package com.brahvim.nerd.io.asset_loader.processing_loaders;

import com.brahvim.nerd.io.asset_loader.AssetLoaderFailedException;
import com.brahvim.nerd.io.asset_loader.AssetLoaderOptions;
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
	public JSONObject fetchData(final Sketch p_sketch, final String p_path, final AssetLoaderOptions... p_options)
			throws AssetLoaderFailedException, IllegalArgumentException {
		try {
			return p_sketch.loadJSONObject(p_path);
		} catch (final NullPointerException e) {
			throw new AssetLoaderFailedException();
		}
	}

}