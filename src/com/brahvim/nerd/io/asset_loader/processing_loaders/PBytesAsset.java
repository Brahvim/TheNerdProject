package com.brahvim.nerd.io.asset_loader.processing_loaders;

import com.brahvim.nerd.io.asset_loader.AssetLoaderFailedException;
import com.brahvim.nerd.io.asset_loader.AssetLoaderOptions;
import com.brahvim.nerd.io.asset_loader.AssetType;
import com.brahvim.nerd.papplet_wrapper.Sketch;

public class PBytesAsset extends AssetType<byte[]> {

	// region SINGLETON STUFF.
	private static PBytesAsset LOADER = new PBytesAsset();

	public static AssetType<?> getLoader() {
		return PBytesAsset.LOADER;
	}
	// endregion

	@Override
	public byte[] fetchData(final Sketch p_sketch, final String p_path, final AssetLoaderOptions... p_options)
			throws AssetLoaderFailedException, IllegalArgumentException {
		final byte[] bytes = p_sketch.loadBytes(p_path);

		if (bytes == null)
			throw new AssetLoaderFailedException();

		return bytes;
	}

}