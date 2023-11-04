package com.brahvim.nerd.io.asset_loader;

public class NerdAssetLoaderException extends RuntimeException {

	public NerdAssetLoaderException(final NerdAssetLoader<?> p_assetLoader) {
		super(String.format("Failed to load asset `%s`!", p_assetLoader.getAssetName()));
	}

	public NerdAssetLoaderException(final NerdAssetLoader<?> p_assetLoader, final String p_reason) {
		super(String.format("Failed to load asset `%s`! Reason: %s", p_assetLoader.getAssetName(), p_reason));
	}

	public NerdAssetLoaderException(
			final NerdAssetLoader<?> p_assetLoader, final Exception p_cause) {
		super(String.format("""
				Failed to load asset `%s`!
				Stacktrace:
				""",
				p_assetLoader.getAssetName(), p_cause));
	}

	public NerdAssetLoaderException(
			final NerdAssetLoader<?> p_assetLoader, final String p_reason, final Exception p_cause) {
		super(String.format("""
				Failed to load asset `%s`!
				Reason: %s
				Stacktrace:
				""",
				p_assetLoader.getAssetName(), p_reason, p_cause));
	}

}
