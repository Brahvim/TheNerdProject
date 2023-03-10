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
	public byte[] fetchData(Sketch p_sketch, String p_path, AssetLoaderOptions... p_options)
			throws AssetLoaderFailedException, IllegalArgumentException {
		byte[] bytes = p_sketch.loadBytes(p_path);

		if (bytes == null)
			throw new AssetLoaderFailedException();

		return bytes;
	}

}