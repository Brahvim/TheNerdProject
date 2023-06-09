package com.brahvim.nerd.io.asset_loader;

import java.io.File;

import com.brahvim.nerd.processing_wrapper.NerdSketch;

public abstract class NerdAssetLoader<AssetT> {

	/**
	 * @throws NerdAssetLoaderException when a failure occurs.
	 * @throws IllegalArgumentException if the options passed to the loader
	 *                                  weren't meant for it.
	 */
	protected abstract AssetT fetchData(final NerdSketch p_sketch)
			throws NerdAssetLoaderException, IllegalArgumentException;

	/**
	 * This method tells the wrapping {@code NerdAsset} the name to use. Usually
	 * just <code>return super.findNameFromPath(this.PATH);</code>
	 * 
	 * @return
	 */
	protected abstract String getAssetName();

	protected String findNameFromPath(final String p_path) {
		String toRet = new File(p_path).getName(); // Parses wth `/`s too!
		// Paths.get("").getFileName().toString(); // Parses with `File.separator`.

		int lastCharId = toRet.lastIndexOf('.');

		if (lastCharId == -1)
			lastCharId = toRet.length();

		toRet = toRet.substring(0, lastCharId);
		return toRet;
	}

}
