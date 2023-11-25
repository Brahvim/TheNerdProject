package com.brahvim.nerd.io.asset_loader.processing_loaders;

import com.brahvim.nerd.io.asset_loader.NerdAssetLoaderException;
import com.brahvim.nerd.io.asset_loader.NerdSinglePathAssetLoader;
import com.brahvim.nerd.processing_wrapper.NerdSketch;

import processing.data.XML;

public class NerdPXmlAsset extends NerdSinglePathAssetLoader<XML> {

	public NerdPXmlAsset(final String p_path) {
		super(p_path);
	}

	@Override
	protected XML fetchData(final NerdSketch<?> p_sketch)
			throws NerdAssetLoaderException, IllegalArgumentException {
		final XML markup = p_sketch.loadXML(super.path);
		if (markup == null)
			throw new NerdAssetLoaderException(this);
		return markup;
	}

}
