package com.brahvim.nerd.io.asset_loader.processing_loaders;

import com.brahvim.nerd.io.asset_loader.AssetLoaderFailedException;
import com.brahvim.nerd.io.asset_loader.AssetLoaderOptions;
import com.brahvim.nerd.io.asset_loader.AssetLoader;
import com.brahvim.nerd.papplet_wrapper.Sketch;

public class PStringsAsset extends AssetLoader<String[]> {

	// region SINGLETON STUFF.
	private static PStringsAsset LOADER = new PStringsAsset();

	public static PStringsAsset getLoader() {
		return PStringsAsset.LOADER;
	}
	// endregion SINGLETON STUFF.

	@Override
	public String[] fetchData(final Sketch p_sketch, final String p_path, final AssetLoaderOptions... p_options)
			throws AssetLoaderFailedException, IllegalArgumentException {
		final String[] strings = p_sketch.loadStrings(p_path);

		if (strings == null)
			throw new AssetLoaderFailedException();

		return strings;
	}

}