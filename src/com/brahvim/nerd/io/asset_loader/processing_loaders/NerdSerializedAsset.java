package com.brahvim.nerd.io.asset_loader.processing_loaders;

import com.brahvim.nerd.io.asset_loader.NerdAssetLoaderException;
import com.brahvim.nerd.io.asset_loader.NerdSinglePathAssetLoader;
import com.brahvim.nerd.processing_wrapper.NerdSketch;
import com.brahvim.nerd.utils.NerdByteSerialUtils;

public class NerdSerializedAsset extends NerdSinglePathAssetLoader<Object> {

	public NerdSerializedAsset(final String p_path) {
		super(p_path);
	}

	@Override
	protected Object fetchData(final NerdSketch<?> p_sketch)
			throws NerdAssetLoaderException, IllegalArgumentException {
		try {
			return NerdByteSerialUtils.fromFile(super.path);
		} catch (final Exception e) {
			throw new NerdAssetLoaderException(this);
		}
	}

}