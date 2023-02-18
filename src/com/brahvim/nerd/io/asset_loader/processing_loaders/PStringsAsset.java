package com.brahvim.nerd.io.asset_loader.processing_loaders;

import com.brahvim.nerd.io.asset_loader.AssetLoaderFailedException;
import com.brahvim.nerd.io.asset_loader.AssetType;
import com.brahvim.nerd.papplet_wrapper.Sketch;

public class PStringsAsset extends AssetType<String[]> {

	// region SINGLETON STUFF.
	private static PStringsAsset LOADER = new PStringsAsset();

	public static PStringsAsset getLoader() {
		return PStringsAsset.LOADER;
	}
	// endregion SINGLETON STUFF.

	@Override
	public String[] fetchData(Sketch SKETCH, String p_path, Object... p_options)
			throws AssetLoaderFailedException, IllegalArgumentException {
		String[] strings = SKETCH.loadStrings(p_path);

		if (strings == null)
			throw new AssetLoaderFailedException();

		return strings;
	}

}