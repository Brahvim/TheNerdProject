package com.brahvim.nerd.openal.al_asset_loaders;

import java.io.File;
import java.nio.ShortBuffer;

import com.brahvim.nerd.io.asset_loader.AssetLoaderFailedException;
import com.brahvim.nerd.io.asset_loader.AssetType;
import com.brahvim.nerd.openal.al_buffers.AlBufferLoader;
import com.brahvim.nerd.papplet_wrapper.Sketch;

public class OggBufferAsset extends AssetType<ShortBuffer> {
	private final static OggBufferAsset LOADER = new OggBufferAsset();

	public static AssetType<?> getLoader() {
		return OggBufferAsset.LOADER;
	}

	@Override
	public ShortBuffer fetchData(Sketch p_sketch, String p_path, Object... p_options)
			throws AssetLoaderFailedException, IllegalArgumentException {
		try {
			return AlBufferLoader.loadOgg(new File(p_path));
		} catch (Exception e) {
			e.printStackTrace();
			throw new AssetLoaderFailedException();
			// return ShortBuffer.allocate(0);
		}
	}

}
