package com.brahvim.nerd.openal.al_asset_loaders;

import com.brahvim.nerd.io.asset_loader.AssetLoaderFailedException;
import com.brahvim.nerd.io.asset_loader.AssetLoaderOptions;
import com.brahvim.nerd.io.asset_loader.AssetType;
import com.brahvim.nerd.openal.al_buffers.AlWavBuffer;
import com.brahvim.nerd.papplet_wrapper.Sketch;

@Deprecated
public class WavBufferDataAsset extends AssetType<AlWavBuffer> {

	// region SINGLETON STUFF.
	private static final WavBufferDataAsset LOADER = new WavBufferDataAsset();

	public static AssetType<?> getLoader() {
		return WavBufferDataAsset.LOADER;
	}
	// endregion

	@Override
	public AlWavBuffer fetchData(Sketch p_sketch, String p_path, AssetLoaderOptions... p_options)
			throws AssetLoaderFailedException, IllegalArgumentException {
		try {
			AlWavBuffer wavBuffer = new AlWavBuffer(p_sketch.AL);
			wavBuffer.shouldDispose(false);
			wavBuffer.loadFrom(p_path);
			return wavBuffer;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AssetLoaderFailedException();
			// return ByteBuffer.allocate(0);
		}
	}

}
