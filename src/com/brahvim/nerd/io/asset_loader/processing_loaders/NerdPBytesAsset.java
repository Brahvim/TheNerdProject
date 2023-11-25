package com.brahvim.nerd.io.asset_loader.processing_loaders;

import com.brahvim.nerd.io.asset_loader.NerdAssetLoaderException;
import com.brahvim.nerd.io.asset_loader.NerdSinglePathAssetLoader;
import com.brahvim.nerd.processing_wrapper.NerdSketch;

public class NerdPBytesAsset extends NerdSinglePathAssetLoader<byte[]> {

	public NerdPBytesAsset(final String p_path) {
		super(p_path);
	}

	@Override
	protected byte[] fetchData(final NerdSketch<?> p_sketch)
			throws NerdAssetLoaderException, IllegalArgumentException {
		final byte[] bytes = p_sketch.loadBytes(super.path);

		if (bytes == null)
			throw new NerdAssetLoaderException(this);

		return bytes;
	}

}