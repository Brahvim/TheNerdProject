package com.brahvim.nerd.io.asset_loader.processing_loaders;

import com.brahvim.nerd.io.asset_loader.AssetLoaderFailedException;
import com.brahvim.nerd.io.asset_loader.AssetType;
import com.brahvim.nerd.papplet_wrapper.Sketch;

import processing.data.XML;

public class XMLAsset extends AssetType<XML> {
	private static XMLAsset LOADER = new XMLAsset();

	public static AssetType<XML> getLoader() {
		return XMLAsset.LOADER;
	}

	@Override
	public XML fetchData(Sketch SKETCH, String p_path, Object... p_options)
			throws AssetLoaderFailedException {
		XML markup = SKETCH.loadXML(p_path);
		if (markup == null)
			throw new AssetLoaderFailedException();
		return markup;
	}

}
