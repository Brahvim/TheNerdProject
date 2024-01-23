package com.brahvim.nerd.io.asset_loader;

import java.io.File;

import com.brahvim.nerd.processing_wrapper.NerdSketch;

public abstract class NerdAssetLoader<AssetT> {

	/**
	 * @throws NerdAssetLoaderException when a failure occurs.
	 * @throws IllegalArgumentException if the options passed to the loader
	 *                                  weren't meant for it.
	 */
	protected abstract AssetT fetchData(final NerdSketch<?> p_sketch)
			throws NerdAssetLoaderException, IllegalArgumentException;

	/**
	 * This method tells the wrapping {@link NerdAsset} the name to use. For most
	 * implementations (that hold a string named {@code PATH}), just:
	 *
	 * <pre>
	 * return super.findNameFromPath(this.PATH);
	 * </pre>
	 *
	 * ...will suffice!
	 *
	 * @return The name of the {@link NerdAsset} <i>properly</i> derived from its
	 *         file's name, perhaps by using the parsing algorithm provided by
	 *         {@linkplain NerdAssetLoader#findNameFromPath(String)
	 *         NerdAssetLoader::findNameFromPath(String)}.
	 */
	protected abstract String getAssetName();

	protected String findNameFromPath(final String p_path) {
		if (p_path == null)
			return "";

		String toRet = new File(p_path).getName(); // Parses wth `/`s too!
		// Paths.get("").getFileName().toString(); // Parses with `File.separator`.

		int lastCharId = toRet.lastIndexOf('.');

		if (lastCharId == -1)
			lastCharId = toRet.length();

		toRet = toRet.substring(0, lastCharId);
		return toRet;
	}

}
