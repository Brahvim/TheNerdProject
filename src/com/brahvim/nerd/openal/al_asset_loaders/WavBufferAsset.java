package com.brahvim.nerd.openal.al_asset_loaders;

import java.io.File;
import java.nio.ByteBuffer;

import com.brahvim.nerd.io.asset_loader.AssetLoaderFailedException;
import com.brahvim.nerd.io.asset_loader.AssetType;
import com.brahvim.nerd.openal.al_buffers.AlBufferLoader;
import com.brahvim.nerd.papplet_wrapper.Sketch;

public class WavBufferAsset extends AssetType<ByteBuffer> {
	private final static WavBufferAsset LOADER = new WavBufferAsset();

	public static AssetType<?> getLoader() {
		return WavBufferAsset.LOADER;
	}

	@Override
	public ByteBuffer fetchData(Sketch p_sketch, String p_path, Object... p_options)
			throws AssetLoaderFailedException, IllegalArgumentException {
		try {
			return AlBufferLoader.loadWav(new File(p_path));
		} catch (Exception e) {
			e.printStackTrace();
			throw new AssetLoaderFailedException();
			// return ByteBuffer.allocate(0);
		}
	}

}
