package com.brahvim.nerd.openal.al_asset_loaders;

import com.brahvim.nerd.io.asset_loader.AssetLoaderFailedException;
import com.brahvim.nerd.io.asset_loader.AssetLoaderOptions;
import com.brahvim.nerd.io.asset_loader.AssetType;
import com.brahvim.nerd.openal.NerdAl;
import com.brahvim.nerd.openal.al_buffers.AlOggBuffer;
import com.brahvim.nerd.papplet_wrapper.Sketch;

public class OggBufferDataAsset extends AssetType<AlOggBuffer> {

	// region SINGLETION STUFF.
	private static final OggBufferDataAsset LOADER = new OggBufferDataAsset();

	public static AssetType<?> getLoader() {
		return OggBufferDataAsset.LOADER;
	}
	// endregion

	@Override
	public AlOggBuffer fetchData(Sketch p_sketch, String p_path, AssetLoaderOptions... p_options)
			throws AssetLoaderFailedException, IllegalArgumentException {
		try {
			final AlOggBuffer oggBuffer = new AlOggBuffer((NerdAl) p_sketch.getNerdExt("OpenAL"));
			oggBuffer.shouldDispose(false);
			oggBuffer.loadFrom(p_path);
			return oggBuffer;
		} catch (final Exception e) {
			e.printStackTrace();
			throw new AssetLoaderFailedException();
			// return ShortBuffer.allocate(0);
		}
	}

}
