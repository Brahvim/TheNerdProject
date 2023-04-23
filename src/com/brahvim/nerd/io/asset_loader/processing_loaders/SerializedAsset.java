package com.brahvim.nerd.io.asset_loader.processing_loaders;

import com.brahvim.nerd.io.ByteSerial;
import com.brahvim.nerd.io.asset_loader.AssetLoaderFailedException;
import com.brahvim.nerd.io.asset_loader.AssetLoader;
import com.brahvim.nerd.papplet_wrapper.Sketch;

public class SerializedAsset extends AssetLoader<Object> {

	@Override
	public Object fetchData(final Sketch p_sketch, final String p_path)
			throws AssetLoaderFailedException, IllegalArgumentException {
		try {
			return ByteSerial.fromFile(p_path);
		} catch (final Exception e) {
			throw new AssetLoaderFailedException();
		}
	}

}