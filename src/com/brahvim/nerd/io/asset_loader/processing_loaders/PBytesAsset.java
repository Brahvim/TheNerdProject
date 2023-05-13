package com.brahvim.nerd.io.asset_loader.processing_loaders;

import com.brahvim.nerd.io.asset_loader.NerdAssetLoaderException;
import com.brahvim.nerd.processing_wrapper.NerdSketch;
import com.brahvim.nerd.io.asset_loader.NerdAssetLoader;

public class PBytesAsset extends NerdAssetLoader<byte[]> {

	@Override
	public byte[] fetchData(final NerdSketch p_sketch, final String p_path)
			throws NerdAssetLoaderException, IllegalArgumentException {
		final byte[] bytes = p_sketch.loadBytes(p_path);

		if (bytes == null)
			throw new NerdAssetLoaderException();

		return bytes;
	}

}