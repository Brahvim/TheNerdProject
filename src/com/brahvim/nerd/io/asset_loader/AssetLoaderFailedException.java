package com.brahvim.nerd.io.asset_loader;

public class AssetLoaderFailedException extends RuntimeException {
	public AssetLoaderFailedException() {
		super();
	}

	public AssetLoaderFailedException(final String p_message) {
		super(p_message);
	}
}
