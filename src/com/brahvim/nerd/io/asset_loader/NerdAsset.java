package com.brahvim.nerd.io.asset_loader;

import java.util.function.Consumer;

import com.brahvim.nerd.processing_wrapper.NerdSketch;

public class NerdAsset {

	// region Fields!
	public final String NAME;

	private final NerdSketch SKETCH;
	private final NerdAssetLoader<?> LOADER;

	private int frame;
	private Object data;
	private Runnable onLoad;
	private long millis = -1;
	private boolean loaded, ploaded, failure;
	// endregion

	// region Construction.
	/* `package` */ NerdAsset(final NerdSketch p_sketch, final NerdAssetLoader<?> p_type) {
		if (p_type == null)
			throw new IllegalArgumentException("`NerdAsset`s need to know their type!");

		this.LOADER = p_type;
		this.SKETCH = p_sketch;
		this.NAME = this.LOADER.getAssetName();
	}

	public NerdAsset(final NerdSketch p_sketch, final NerdAssetLoader<?> p_type, final Runnable p_onLoad) {
		this(p_sketch, p_type);
		this.onLoad = p_onLoad;
	}
	// endregion

	// region Load status requests.
	public NerdAsset setLoadCallback(final Runnable p_onLoad) {
		this.onLoad = p_onLoad;
		return this;
	}

	// ...will cause a surge in CPU usage! Careful!...
	public NerdAsset completeLoad() throws InterruptedException {
		while (!this.loaded) {
			System.out.println("Waiting for `" + this.NAME + "` to load...");

			// Don't let the CPU go crazy!:
			try {
				Thread.sleep(50);
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}

		return this;
	}

	public void startLoading() {
		// Adding callbacks for each asset since `AssetManager`s don't handle loading.
		final Consumer<NerdSketch> postCallback = s -> this.ploaded = this.loaded;
		this.SKETCH.addPostListener(postCallback);
		this.fetchData();
		this.loaded = true;

		if (this.onLoad != null)
			this.onLoad.run();

		// Once the asset has loaded, `loaded` is set to `true` and `postCallback`
		// is no longer necessary.
		// However, we need to update `ploaded` for one last frame.
		// To do so, we add a "self-removing" callback!:

		final Consumer<NerdSketch> whenLoaded = new Consumer<NerdSketch>() {
			@Override
			public void accept(final NerdSketch p_sketch) {
				NerdAsset.this.ploaded = true;
				p_sketch.removePostListener(this);
			}
		};

		this.SKETCH.addPostListener(whenLoaded);
		this.SKETCH.removePostListener(postCallback);
	}

	// region "Yes/No" questions.
	public boolean wasLoaded() {
		return this.ploaded;
	}

	public boolean hasLoaded() {
		return this.loaded;
	}

	public boolean hasFailed() {
		return this.failure;
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
	@SuppressWarnings("unchecked")
	public <RetT> RetT getData() {
		try {
			this.completeLoad();
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}

		return (RetT) this.data;
	}

	public NerdAssetLoader<?> getLoader() {
		return this.LOADER;
	}
	// endregion
	// endregion

	private void fetchData() {
		try {
			this.data = this.LOADER.fetchData(this.SKETCH);
			this.millis = this.SKETCH.millis();
			this.frame = this.SKETCH.frameCount;
		} catch (final NerdAssetLoaderException e) {
			this.data = null;
			this.failure = true;
			e.printStackTrace();
		}
	}

}
