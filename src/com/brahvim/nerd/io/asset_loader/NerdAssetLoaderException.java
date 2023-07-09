package com.brahvim.nerd.io.asset_loader;

public class NerdAssetLoaderException extends RuntimeException {
	public NerdAssetLoaderException(final NerdAssetLoader<?> p_assetLoader) {
		super(String.format("Failed to load asset `%s`!", p_assetLoader.getAssetName()));
	}

	public NerdAssetLoaderException(final String p_message) {
		super(p_message);
	}
}
