package com.brahvim.nerd.io.asset_loader;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.brahvim.nerd.papplet_wrapper.Sketch;

public class AssetManager {

    // region Fields.
    private final Sketch SKETCH;
    // private final AssetManager.AssetKey ASSET_KEY;
    private final HashSet<NerdAsset> ASSETS = new HashSet<>(0); // Start with LITERAL `0`!
    // Do we even *need* assets in any scene from the very beginning?
    // endregion

    public AssetManager(Sketch p_key) {
        this.SKETCH = p_key;
    }

    // region `makeAsset()` overloads.
    public <T> NerdAsset makeAsset(AssetType<T> p_type, String p_path, AssetLoaderOptions... p_options) {
        if (p_type == null || p_path == null)
            throw new IllegalArgumentException("`NerdAssets` need data!");
        return new NerdAsset(this.SKETCH, p_type, p_path, p_options);
    }

    public <T> NerdAsset makeAsset(AssetType<T> p_type, String p_path) {
        return this.makeAsset(p_type, p_path, (AssetLoaderOptions[]) null);
    }
    // endregion

    // region `add()` overloads.
    public <T> AssetManager add(AssetType<T> p_type, String p_path, AssetLoaderOptions... p_options) {
        this.ASSETS.add(this.makeAsset(p_type, p_path, p_options));
        return this;
    }

    public <T> AssetManager add(AssetType<T> p_type, String p_path, Runnable p_onLoad) {
        return this.add(p_type, p_path, p_onLoad);
    }

    public <T> AssetManager add(AssetType<T> p_type, String p_path) {
        return this.add(p_type, p_path, (AssetLoaderOptions[]) null);
    }

    public <T> AssetManager add(NerdAsset p_asset) {
        this.ASSETS.add(p_asset);
        return this;
    }

    /**
     * @deprecated since using {@link AssetManager#get()} is better. In cases
     *             where you'd want to check for the availability of an asset, you
     *             probably also a want a reference to it, in which case, it is much
     *             better to use {@link AssetManager#get()} and check if the return
     *             value is {@code null}.
     */
    @Deprecated
    public boolean contains(String p_fileName) {
        for (NerdAsset a : this.ASSETS)
            if (a.NAME.equals(p_fileName))
                return true;
        return false;
    }

    public NerdAsset get(String p_fileName) {
        for (NerdAsset a : this.ASSETS)
            if (a.NAME.equals(p_fileName))
                return (NerdAsset) a;
        return null;
    }

    // region `remove()` overloads.
    public void remove(NerdAsset... p_assets) {
        for (NerdAsset a : p_assets)
            this.ASSETS.remove(a);
    }

    public void remove(NerdAsset p_asset) {
        this.ASSETS.remove(p_asset);
    }
    // endregion

    public void clear() {
        this.ASSETS.clear();
    }

    public void updatePreviousLoadState() {
        for (NerdAsset a : this.ASSETS)
            a.updatePreviousLoadState();
    }
    // endregion

    // region Load state queries.
    /**
     * Has every asset completed loading?
     */
    public boolean hadCompletedLastFrame() {
        for (NerdAsset a : this.ASSETS)
            if (!a.wasLoaded())
                return false;
        return true;
    }

    public boolean hasCompleted() {
        for (NerdAsset a : this.ASSETS)
            if (!a.hasLoaded())
                return false;
        return true;
    }

    /**
     * Wait till all assets are done loading!
     */
    // This increases CPU usage, right?!
    public void forceLoading() {
        while (!this.hasCompleted())
            ;
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

    public <T> T[] toArray(T[] a) {
        return this.ASSETS.toArray(a);
    }

    public Stream<NerdAsset> parallelStream() {
        return this.ASSETS.parallelStream();
    }

    public boolean removeIf(Predicate<? super NerdAsset> p_filter) {
        return this.ASSETS.removeIf(p_filter);
    }

    public Stream<NerdAsset> stream() {
        return this.ASSETS.stream();
    }

    public <T> T[] toArray(IntFunction<T[]> p_generator) {
        return this.ASSETS.toArray(p_generator);
    }

    public void forEach(Consumer<? super NerdAsset> p_action) {
        this.ASSETS.forEach(p_action);
    }
    // endregion

}
