package com.brahvim.nerd.io.asset_loader.processing_loaders;

import com.brahvim.nerd.io.asset_loader.NerdAssetLoaderException;
import com.brahvim.nerd.processing_wrapper.NerdSketch;
import com.brahvim.nerd.io.asset_loader.NerdAssetLoader;

import processing.data.XML;

public class PXmlAsset extends NerdAssetLoader<XML> {

	@Override
	public XML fetchData(final NerdSketch p_sketch, final String p_path)
			throws NerdAssetLoaderException, IllegalArgumentException {
		final XML markup = p_sketch.loadXML(p_path);
		if (markup == null)
			throw new NerdAssetLoaderException();
		return markup;
	}

}
