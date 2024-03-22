package com.brahvim.nerd.io.asset_loader;

import java.util.concurrent.atomic.AtomicBoolean;

import com.brahvim.nerd.processing_wrapper.NerdSketch;

import processing.core.PGraphics;

public class NerdAsset<SketchPGraphicsT extends PGraphics, AssetT> {

	// region Fields!
	public final String NAME;

	private final NerdSketch<SketchPGraphicsT> SKETCH;
	private final NerdAssetsModule<SketchPGraphicsT> MANAGER;
	private final NerdAssetLoader<SketchPGraphicsT, AssetT> LOADER;

	private int frame;
	private AssetT data;
	private Runnable onLoad;
	private long millis = -1;
	protected AtomicBoolean
	/*   */ loaded = new AtomicBoolean(false),
			ploaded = new AtomicBoolean(false),
			failure = new AtomicBoolean(false);
	// endregion

	// region Construction.
	public NerdAsset(
			final NerdAssetsModule<SketchPGraphicsT> p_assetsModule,
			final NerdAssetLoader<SketchPGraphicsT, AssetT> p_loader) {
		if (p_loader == null)
			throw new IllegalArgumentException("`NerdAsset`s need to know their type!");

		this.LOADER = p_loader;
		this.MANAGER = p_assetsModule;
		this.SKETCH = this.MANAGER.getSketch();
		this.NAME = this.LOADER.getAssetName();
	}

	public NerdAsset(
			final NerdAssetsModule<SketchPGraphicsT> p_assetsModule,
			final NerdAssetLoader<SketchPGraphicsT, AssetT> p_loader,
			final Runnable p_onLoad) {
		this(p_assetsModule, p_loader);
		this.onLoad = p_onLoad;
	}

	public NerdAsset(
			final NerdAsset<SketchPGraphicsT, AssetT> p_asset,
			final NerdSketch<SketchPGraphicsT> p_sketch,
			final NerdAssetsModule<SketchPGraphicsT> p_assetsModule,
			final NerdAssetLoader<SketchPGraphicsT, AssetT> p_loader) {
		this.LOADER = p_loader;
		this.SKETCH = p_sketch;
		this.NAME = p_asset.NAME;
		this.MANAGER = p_assetsModule;

		this.data = p_asset.data;
		this.frame = p_asset.frame;
		this.onLoad = p_asset.onLoad;
		this.millis = p_asset.millis;
		this.loaded = p_asset.loaded;
		this.ploaded = p_asset.ploaded;
		this.failure = p_asset.failure;
	}
	// endregion

	// region Load status requests.
	public NerdAsset<SketchPGraphicsT, AssetT> setLoadCallback(final Runnable p_onLoad) {
		this.onLoad = p_onLoad;
		return this;
	}

	// ...will cause a surge in CPU usage! Careful!...
	public NerdAsset<SketchPGraphicsT, AssetT> completeLoad() throws InterruptedException {
		while (!this.loaded.get()) {
			System.out.println("Waiting for `" + this.NAME + "` to load...");

			// Don't let the CPU go crazy!:
			try {
				Thread.sleep(50);
			} catch (final InterruptedException e) {
				// e.printStackTrace();
				Thread.currentThread().interrupt();
			}
		}

		return this;
	}

	public NerdAsset<SketchPGraphicsT, AssetT> startLoading() {
		if (this.loaded.get())
			return this;

		this.fetchData();
		this.loaded.set(true);

		if (this.onLoad != null)
			this.onLoad.run();

		return this;
	}

	// region "Yes/No" questions.
	public boolean wasLoaded() {
		return this.ploaded.get();
	}

	public boolean hasLoaded() {
		return this.loaded.get();
	}

	public boolean hasFailed() {
		return this.failure.get();
	}
	// endregion

	// region Getters.
	public int getLoadFrame() {
		return this.frame;
	}

	public long getLoadMillis() {
		return this.millis;
	}

	/**
	 * Ensures that the asset has loaded, then returns its data,
	 * given the name of a file (without the extension!).
	 * <p>
	 * Usage example!:
	 *
	 * <pre>
	 * final PImage image = ASSETS.get("my_sprite_image").getData();
	 * </pre>
	 */
	public AssetT getData() {
		try {
			this.completeLoad();
		} catch (final InterruptedException e) {
			// e.printStackTrace();
			// TODO: Investigate this and `NerdAsset::completeLoad()`'s interrupt behavior.
			Thread.currentThread().interrupt();
		}
		return this.data;
	}

	public NerdAssetLoader<SketchPGraphicsT, ?> getLoader() {
		return this.LOADER;
	}
	// endregion
	// endregion

	public <RetSketchPGraphicsT extends PGraphics> NerdAsset<RetSketchPGraphicsT, AssetT> castForAnotherSketch(
			final NerdAsset<RetSketchPGraphicsT, AssetT> p_asset,
			final NerdSketch<RetSketchPGraphicsT> p_sketch,
			final NerdAssetsModule<RetSketchPGraphicsT> p_module,
			final NerdAssetLoader<RetSketchPGraphicsT, AssetT> p_assetLoader,
			final Runnable p_immediatelyRunOnLoadCallback) {
		final NerdAsset<RetSketchPGraphicsT, AssetT> a = this
				.castForAnotherSketch(p_asset, p_sketch, p_module, p_assetLoader);
		a.onLoad = p_immediatelyRunOnLoadCallback;
		p_immediatelyRunOnLoadCallback.run();
		return a;
	}

	public <RetSketchPGraphicsT extends PGraphics> NerdAsset<RetSketchPGraphicsT, AssetT> castForAnotherSketch(
			final NerdAsset<RetSketchPGraphicsT, AssetT> p_asset,
			final NerdSketch<RetSketchPGraphicsT> p_sketch,
			final NerdAssetsModule<RetSketchPGraphicsT> p_module,
			final NerdAssetLoader<RetSketchPGraphicsT, AssetT> p_assetLoader) {
		return new NerdAsset<>(p_asset, p_sketch, p_module, p_assetLoader);
	}

	private void fetchData() {
		try {
			this.data = this.LOADER.fetchData(this.SKETCH);
			this.millis = this.SKETCH.millis();
			this.frame = this.SKETCH.frameCount;
		} catch (final NerdAssetLoaderException e) {
			this.data = null;
			this.failure.set(true);
			e.printStackTrace();
		}
	}

}
