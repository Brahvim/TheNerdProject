package com.brahvim.nerd.openal.al_asset_loaders;

import com.brahvim.nerd.io.asset_loader.AssetLoaderFailedException;
import com.brahvim.nerd.io.asset_loader.AssetType;
import com.brahvim.nerd.openal.al_buffers.AlOggBuffer;
import com.brahvim.nerd.papplet_wrapper.Sketch;

public class OggBufferDataAsset extends AssetType<AlOggBuffer> {
	private final static OggBufferDataAsset LOADER = new OggBufferDataAsset();

	public static AssetType<?> getLoader() {
		return OggBufferDataAsset.LOADER;
	}

	@Override
	public AlOggBuffer fetchData(Sketch p_sketch, String p_path, Object... p_options)
			throws AssetLoaderFailedException, IllegalArgumentException {
		try {
			AlOggBuffer oggBuffer = new AlOggBuffer(p_sketch.AL);
			oggBuffer.shouldDispose(false);
			oggBuffer.loadFrom(p_path);
			return oggBuffer;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AssetLoaderFailedException();
			// return ShortBuffer.allocate(0);
		}
	}

}
