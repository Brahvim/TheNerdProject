package com.brahvim.nerd.io.asset_loader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.brahvim.nerd.processing_wrapper.NerdModule;
import com.brahvim.nerd.processing_wrapper.NerdSketch;

import processing.core.PGraphics;

// TODO: Make asset-loading independent of the sketch, then use this class to wrap that & DI it in!

public class NerdAssetsModule<SketchPGraphicsT extends PGraphics> extends NerdModule<SketchPGraphicsT> {

	public static class NerdAsset<SketchPGraphicsT extends PGraphics, AssetT> {

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

	// region The ONLY field!
	private final List<NerdAsset<SketchPGraphicsT, ?>> ASSETS = new ArrayList<>(0) { // Start with LITERAL `0`!
		// Do we even *need* assets in any scene from the very beginning?

		@Override
		public boolean add(final NerdAsset<SketchPGraphicsT, ?> p_element) {
			if (super.contains(p_element))
				return false;

			super.add(p_element);
			return true;
		}
	};
	// endregion

	@SuppressWarnings("unchecked")
	public NerdAssetsModule(final NerdSketch<SketchPGraphicsT> p_sketch) {
		super(p_sketch);
		super.getSketchModulesMap().put((Class<? extends NerdModule<SketchPGraphicsT>>) NerdAssetsModule.class, this);
	}

	public void addAllAssetsFrom(final NerdAssetsModule<SketchPGraphicsT> p_assetsModule) {
		this.ASSETS.addAll(p_assetsModule.ASSETS);
	}

	// region `NerdAsset`-operations!
	protected final <AssetT> NerdAsset<SketchPGraphicsT, AssetT> makeAsset(
			final NerdAssetLoader<SketchPGraphicsT, AssetT> p_type) {
		return new NerdAsset<>(this, p_type);
	}

	// region `add()` overloads.
	public <AssetT> NerdAsset<SketchPGraphicsT, AssetT> addAsset(
			final NerdAssetLoader<SketchPGraphicsT, AssetT> p_type) {
		final NerdAsset<SketchPGraphicsT, AssetT> toRet = this.makeAsset(p_type);
		this.ASSETS.add(toRet);
		return toRet;
	}

	public <AssetT> NerdAsset<SketchPGraphicsT, AssetT> addAsset(
			final NerdAssetLoader<SketchPGraphicsT, AssetT> p_type,
			final Runnable p_onLoad) {
		final NerdAsset<SketchPGraphicsT, AssetT> toRet = this.addAsset(p_type);
		toRet.setLoadCallback(p_onLoad);
		return toRet;
	}
	// endregion

	/**
	 * @apiNote Using {@linkplain NerdAssetsModule#get() NerdAssetsModule::get()}
	 *          could be better. In cases where you'd want to check for the
	 *          availability of an asset, you probably also a want a reference to
	 *          it, in which case, it is much better to use
	 *          {@linkplain NerdAssetsModule#get() NerdAssetsModule::get()} and
	 *          check if the return value is {@code null}.
	 *          <p>
	 *          ...<i>Feel free to use this method otherwise!</i>
	 */
	@Deprecated
	public boolean contains(final String p_fileName) {
		return this.get(p_fileName) != null;

		// for (final var a : this.ASSETS)
		// if (a.NAME.equals(p_fileName))
		// return true;
		// return false;
	}

	@SuppressWarnings("unchecked")
	public <AssetT> NerdAsset<SketchPGraphicsT, AssetT> get(final String p_fileName) {
		for (final var a : this.ASSETS)
			if (a.NAME.equals(p_fileName))
				return (NerdAsset<SketchPGraphicsT, AssetT>) a;
		return null;
	}

	// region `remove()` overloads.
	@SuppressWarnings("unchecked")
	public void remove(final NerdAsset<SketchPGraphicsT, ?>... p_assets) {
		for (final var a : p_assets)
			this.ASSETS.remove(a);
	}

	public void remove(final NerdAsset<SketchPGraphicsT, ?> p_asset) {
		this.ASSETS.remove(p_asset);
	}
	// endregion

	public void clear() {
		this.ASSETS.clear();
	}
	// endregion

	// region Load state queries.
	/**
	 * Has every asset completed loading?
	 */
	public boolean hadCompletedLastFrame() {
		for (final var a : this.ASSETS)
			if (!a.wasLoaded())
				return false;
		return true;
	}

	public boolean hasCompleted() {
		for (final var a : this.ASSETS)
			if (!a.hasLoaded())
				return false;
		return true;
	}

	/**
	 * Load assets that haven't been loaded yet.
	 */
	public void forceLoading() {
		for (final var a : this.ASSETS)
			if (!a.hasLoaded())
				a.startLoading();
	}
	// endregion

	// region From `HashSet<NerdAsset<SketchPGraphicsT, AssetT>>`.
	public boolean isEmpty() {
		return this.ASSETS.isEmpty();
	}

	// Potential problem: Iterators allow you to remove elements!
	public Iterator<NerdAsset<SketchPGraphicsT, ?>> iterator() {
		return this.ASSETS.iterator();
	}

	public int size() {
		return this.ASSETS.size();
	}

	public Spliterator<NerdAsset<SketchPGraphicsT, ?>> spliterator() {
		return this.ASSETS.spliterator();
	}

	public Object[] toArray() {
		return this.ASSETS.toArray();
	}

	public <T> T[] toArray(final T[] a) {
		return this.ASSETS.toArray(a);
	}

	public Stream<NerdAsset<SketchPGraphicsT, ?>> parallelStream() {
		return this.ASSETS.parallelStream();
	}

	public boolean removeIf(final Predicate<? super NerdAsset<SketchPGraphicsT, ?>> p_filter) {
		return this.ASSETS.removeIf(p_filter);
	}

	public Stream<NerdAsset<SketchPGraphicsT, ?>> stream() {
		return this.ASSETS.stream();
	}

	public <T> T[] toArray(final IntFunction<T[]> p_generator) {
		return this.ASSETS.toArray(p_generator);
	}

	public void forEach(final Consumer<? super NerdAsset<SketchPGraphicsT, ?>> p_action) {
		this.ASSETS.forEach(p_action);
	}
	// endregion

	private void updateAssetsLoadingStatus() {
		this.ASSETS.forEach(a -> a.ploaded = a.loaded);
	}

	// region `NerdModule` events.
	@Override
	protected void pre() {
		this.updateAssetsLoadingStatus();
	}

	@Override
	protected void preDraw() {
		this.updateAssetsLoadingStatus();
	}

	@Override
	protected void draw() {
		this.updateAssetsLoadingStatus();
	}

	@Override
	protected void postDraw() {
		this.updateAssetsLoadingStatus();
	}
	// endregion

}
