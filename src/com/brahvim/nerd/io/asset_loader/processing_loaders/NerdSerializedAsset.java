package com.brahvim.nerd.io.asset_loader.processing_loaders;

import java.io.Serializable;

import com.brahvim.nerd.io.asset_loader.NerdAssetLoaderException;
import com.brahvim.nerd.io.asset_loader.NerdSinglePathAssetLoader;
import com.brahvim.nerd.processing_wrapper.NerdSketch;
import com.brahvim.nerd.utils.NerdByteSerialUtils;

import processing.core.PGraphics;

public class NerdSerializedAsset<SketchPGraphicsT extends PGraphics>
		extends NerdSinglePathAssetLoader<SketchPGraphicsT, Serializable> {

	public NerdSerializedAsset(final String p_path) {
		super(p_path);
	}

	@Override
	protected Serializable fetchData(final NerdSketch<SketchPGraphicsT> p_sketch)
			throws NerdAssetLoaderException, IllegalArgumentException {
		try {
			return NerdByteSerialUtils.fromFile(super.path);
		} catch (final Exception e) {
			throw new NerdAssetLoaderException(this);
		}
	}

}