package com.brahvim.nerd.io.asset_loader;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.brahvim.nerd.processing_wrapper.NerdModule;
import com.brahvim.nerd.processing_wrapper.NerdSketch;

import processing.core.PGraphics;

public class NerdAssetsModule<SketchPGraphicsT extends PGraphics> extends NerdModule<SketchPGraphicsT> {

	// The ONLY field!:
	private final Set<NerdAsset<SketchPGraphicsT, ?>> ASSETS = new HashSet<>(0); // Start with LITERAL `0`!
	// Do we even *need* assets in any scene from the very beginning?

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

		// for (final NerdAsset<SketchPGraphicsT, ?> a : this.ASSETS)
		// if (a.NAME.equals(p_fileName))
		// return true;
		// return false;
	}

	@SuppressWarnings("unchecked")
	public <AssetT> NerdAsset<SketchPGraphicsT, AssetT> get(final String p_fileName) {
		for (final NerdAsset<SketchPGraphicsT, ?> a : this.ASSETS)
			if (a.NAME.equals(p_fileName))
				return (NerdAsset<SketchPGraphicsT, AssetT>) a;
		return null;
	}

	// region `remove()` overloads.
	@SuppressWarnings("unchecked")
	public void remove(final NerdAsset<SketchPGraphicsT, ?>... p_assets) {
		for (final NerdAsset<SketchPGraphicsT, ?> a : p_assets)
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
		for (final NerdAsset<SketchPGraphicsT, ?> a : this.ASSETS)
			if (!a.wasLoaded())
				return false;
		return true;
	}

	public boolean hasCompleted() {
		for (final NerdAsset<SketchPGraphicsT, ?> a : this.ASSETS)
			if (!a.hasLoaded())
				return false;
		return true;
	}

	/**
	 * Load assets that haven't been loaded yet.
	 */
	public void forceLoading() {
		for (final NerdAsset<SketchPGraphicsT, ?> a : this.ASSETS)
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
