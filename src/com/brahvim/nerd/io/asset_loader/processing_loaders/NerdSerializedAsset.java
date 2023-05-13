package com.brahvim.nerd.io.asset_loader.processing_loaders;

import com.brahvim.nerd.io.NerdByteSerial;
import com.brahvim.nerd.io.asset_loader.NerdAssetLoaderException;
import com.brahvim.nerd.processing_wrapper.NerdSketch;
import com.brahvim.nerd.io.asset_loader.NerdAssetLoader;

public class NerdSerializedAsset extends NerdAssetLoader<Object> {

	@Override
	public Object fetchData(final NerdSketch p_sketch, final String p_path)
			throws NerdAssetLoaderException, IllegalArgumentException {
		try {
			return NerdByteSerial.fromFile(p_path);
		} catch (final Exception e) {
			throw new NerdAssetLoaderException();
		}
	}

}