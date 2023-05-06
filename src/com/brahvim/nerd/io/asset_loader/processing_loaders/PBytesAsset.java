package com.brahvim.nerd.io.asset_loader.processing_loaders;

import com.brahvim.nerd.io.asset_loader.AssetLoaderFailedException;
import com.brahvim.nerd.io.asset_loader.AssetLoader;
import com.brahvim.nerd.papplet_wrapper.NerdSketch;

public class PBytesAsset extends AssetLoader<byte[]> {

	@Override
	public byte[] fetchData(final NerdSketch p_sketch, final String p_path)
			throws AssetLoaderFailedException, IllegalArgumentException {
		final byte[] bytes = p_sketch.loadBytes(p_path);

		if (bytes == null)
			throw new AssetLoaderFailedException();

		return bytes;
	}

}