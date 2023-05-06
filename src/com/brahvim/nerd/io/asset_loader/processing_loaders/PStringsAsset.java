package com.brahvim.nerd.io.asset_loader.processing_loaders;

import com.brahvim.nerd.io.asset_loader.NerdAssetLoaderException;
import com.brahvim.nerd.io.asset_loader.NerdAssetLoader;
import com.brahvim.nerd.papplet_wrapper.NerdSketch;

public class PStringsAsset extends NerdAssetLoader<String[]> {

	@Override
	public String[] fetchData(final NerdSketch p_sketch, final String p_path)
			throws NerdAssetLoaderException, IllegalArgumentException {
		final String[] strings = p_sketch.loadStrings(p_path);

		if (strings == null)
			throw new NerdAssetLoaderException();

		return strings;
	}

}