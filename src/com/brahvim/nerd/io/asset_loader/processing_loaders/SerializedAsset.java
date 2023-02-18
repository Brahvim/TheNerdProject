package com.brahvim.nerd.io.asset_loader.processing_loaders;

import com.brahvim.nerd.io.ByteSerial;
import com.brahvim.nerd.io.asset_loader.AssetLoaderFailedException;
import com.brahvim.nerd.io.asset_loader.AssetType;
import com.brahvim.nerd.papplet_wrapper.Sketch;

public class SerializedAsset extends AssetType<Object> {

	// region SINGLETON STUFF.
	private static SerializedAsset LOADER = new SerializedAsset();

	public static AssetType<?> getLoader() {
		return SerializedAsset.LOADER;
	}
	// endregion

	@Override
	public Object fetchData(Sketch SKETCH, String p_path, Object... p_options)
			throws AssetLoaderFailedException, IllegalArgumentException {
		try {
			return ByteSerial.fromFile(p_path);
		} catch (Exception e) {
			throw new AssetLoaderFailedException();
		}
	}

}