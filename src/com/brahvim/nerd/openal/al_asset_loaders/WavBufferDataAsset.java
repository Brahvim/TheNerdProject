package com.brahvim.nerd.openal.al_asset_loaders;

import com.brahvim.nerd.io.asset_loader.AssetLoaderFailedException;
import com.brahvim.nerd.io.asset_loader.AssetLoaderOptions;
import com.brahvim.nerd.io.asset_loader.AssetLoader;
import com.brahvim.nerd.openal.NerdAl;
import com.brahvim.nerd.openal.al_buffers.AlWavBuffer;
import com.brahvim.nerd.papplet_wrapper.Sketch;

@Deprecated
public class WavBufferDataAsset extends AssetLoader<AlWavBuffer> {

	// region SINGLETON STUFF.
	private static final WavBufferDataAsset LOADER = new WavBufferDataAsset();

	public static AssetLoader<?> getLoader() {
		return WavBufferDataAsset.LOADER;
	}
	// endregion

	@Override
	public AlWavBuffer fetchData(final Sketch p_sketch, final String p_path, final AssetLoaderOptions... p_options)
			throws AssetLoaderFailedException, IllegalArgumentException {
		try {
			final AlWavBuffer wavBuffer = new AlWavBuffer((NerdAl) p_sketch.getNerdExt("OpenAL"));
			wavBuffer.shouldDispose(false);
			wavBuffer.loadFrom(p_path);
			return wavBuffer;
		} catch (final Exception e) {
			e.printStackTrace();
			throw new AssetLoaderFailedException();
			// return ByteBuffer.allocate(0);
		}
	}

}
