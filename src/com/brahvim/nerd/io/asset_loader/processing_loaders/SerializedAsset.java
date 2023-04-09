package com.brahvim.nerd.io.asset_loader.processing_loaders;

import com.brahvim.nerd.io.ByteSerial;
import com.brahvim.nerd.io.asset_loader.AssetLoaderFailedException;
import com.brahvim.nerd.io.asset_loader.AssetLoaderOptions;
import com.brahvim.nerd.io.asset_loader.AssetLoader;
import com.brahvim.nerd.papplet_wrapper.Sketch;

public class SerializedAsset extends AssetLoader<Object> {

	// region SINGLETON STUFF.
	private static SerializedAsset LOADER = new SerializedAsset();

	public static AssetLoader<?> getLoader() {
		return SerializedAsset.LOADER;
	}
	// endregion

	@Override
	public Object fetchData(final Sketch p_sketch, final String p_path, final AssetLoaderOptions... p_options)
			throws AssetLoaderFailedException, IllegalArgumentException {
		try {
			return ByteSerial.fromFile(p_path);
		} catch (final Exception e) {
			throw new AssetLoaderFailedException();
		}
	}

}