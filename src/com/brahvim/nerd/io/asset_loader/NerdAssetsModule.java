package com.brahvim.nerd.io.asset_loader;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.brahvim.nerd.processing_wrapper.NerdModule;
import com.brahvim.nerd.processing_wrapper.NerdSketch;

public class NerdAssetsModule extends NerdModule {

	// The ONLY field!:
	private final HashSet<NerdAsset> ASSETS = new HashSet<>(0); // Start with LITERAL `0`!
	// Do we even *need* assets in any scene from the very beginning?

	public NerdAssetsModule(final NerdSketch p_sketch) {
		super(p_sketch);
		super.getSketchModulesMap().put(NerdAssetsModule.class, this);
	}

	// region `NerdAsset`-operations!
	private <AssetT> NerdAsset makeAsset(final NerdAssetLoader<AssetT> p_type) {
		return new NerdAsset(this, p_type);
	}

	// region `add()` overloads.
	public <AssetT> NerdAsset addAsset(final NerdAssetLoader<AssetT> p_type) {
		final NerdAsset toRet = this.makeAsset(p_type);
		this.ASSETS.add(toRet);
		return toRet;
	}

	public <AssetT> NerdAsset addAsset(final NerdAssetLoader<AssetT> p_type, final Runnable p_onLoad) {
		final NerdAsset toRet = this.addAsset(p_type);
		toRet.setLoadCallback(p_onLoad);
		return toRet;
	}
	// endregion

	/**
	 * @deprecated Since using {@link NerdAssetsModule#get()} is better. In cases
	 *             where you'd want to check for the availability of an asset, you
	 *             probably also a want a reference to it, in which case, it is much
	 *             better to use {@link NerdAssetsModule#get()} and check if the
	 *             return
	 *             value is {@code null}.
	 */
	@Deprecated
	public boolean contains(final String p_fileName) {
		return this.get(p_fileName) != null;

		// for (final NerdAsset a : this.ASSETS)
		// if (a.NAME.equals(p_fileName))
		// return true;
		// return false;
	}

	public NerdAsset get(final String p_fileName) {
		for (final NerdAsset a : this.ASSETS)
			if (a.NAME.equals(p_fileName))
				return a;
		return null;
	}

	// region `remove()` overloads.
	public void remove(final NerdAsset... p_assets) {
		for (final NerdAsset a : p_assets)
			this.ASSETS.remove(a);
	}

	public void remove(final NerdAsset p_asset) {
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
		for (final NerdAsset a : this.ASSETS)
			if (!a.wasLoaded())
				return false;
		return true;
	}

	public boolean hasCompleted() {
		for (final NerdAsset a : this.ASSETS)
			if (!a.hasLoaded())
				return false;
		return true;
	}

	/**
	 * Load assets that haven't been loaded yet.
	 */
	public void forceLoading() {
		for (final NerdAsset a : this.ASSETS)
			if (!a.hasLoaded())
				a.startLoading();
	}
	// endregion

	// region From `HashSet<NerdAsset>`.
	public boolean isEmpty() {
		return this.ASSETS.isEmpty();
	}

	// Potential problem: Iterators allow you to remove elements!
	public Iterator<NerdAsset> iterator() {
		return this.ASSETS.iterator();
	}

	public int size() {
		return this.ASSETS.size();
	}

	public Spliterator<NerdAsset> spliterator() {
		return this.ASSETS.spliterator();
	}

	public Object[] toArray() {
		return this.ASSETS.toArray();
	}

	public <T> T[] toArray(final T[] a) {
		return this.ASSETS.toArray(a);
	}

	public Stream<NerdAsset> parallelStream() {
		return this.ASSETS.parallelStream();
	}

	public boolean removeIf(final Predicate<? super NerdAsset> p_filter) {
		return this.ASSETS.removeIf(p_filter);
	}

	public Stream<NerdAsset> stream() {
		return this.ASSETS.stream();
	}

	public <T> T[] toArray(final IntFunction<T[]> p_generator) {
		return this.ASSETS.toArray(p_generator);
	}

	public void forEach(final Consumer<? super NerdAsset> p_action) {
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