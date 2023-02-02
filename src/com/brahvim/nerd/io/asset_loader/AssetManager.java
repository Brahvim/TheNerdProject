package com.brahvim.nerd.io.asset_loader;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.brahvim.nerd.misc.NerdKey;
import com.brahvim.nerd.papplet_wrapper.Sketch;

public class AssetManager {

    // region Class `AssetManager.AssetKey`.
    public class AssetKey extends NerdKey {
        public final Sketch SKETCH;

        private AssetKey(Sketch p_sketch) {
            this.SKETCH = p_sketch;
        }

        public boolean isFor(Class<?> p_class) {
            // Putting `p_class` in the argument eliminates the need for a `null` check.
            return NerdAsset.class.equals(p_class);
        }
    }
    // endregion

    // region Fields.
    private final Sketch SKETCH;
    private final AssetManKey ASSET_MAN_KEY;
    // private final AssetManager.AssetKey ASSET_KEY;
    private final HashSet<NerdAsset<?>> ASSETS = new HashSet<>(0); // Start with LITERAL `0`!
    // Do we even *need* assets in any scene from the very beginning?
    // endregion

    // region Constructors.
    @SuppressWarnings("unused")
    private AssetManager() {
        // this.ASSET_KEY = null;
        this.SKETCH = null;
        this.ASSET_MAN_KEY = null;
    }

    public AssetManager(AssetManKey p_key) {
        this.verifyKey(p_key);

        this.SKETCH = p_key.SKETCH;
        this.ASSET_MAN_KEY = p_key;
        // this.ASSET_KEY = new AssetManager.AssetKey(p_key.SKETCH);
    }
    // endregion

    private boolean verifyKey(AssetManKey p_key) {
        if (this.ASSET_MAN_KEY != null)
            return p_key == this.ASSET_MAN_KEY;

        if (p_key == null) {
            throw new IllegalArgumentException("""
                    \"Keys cannot be `null`!\" - a `NerdAssetManager` instance.""");
        } else if (p_key.isUsed()) {
            throw new IllegalArgumentException("""
                    \"That is a used key!\" - a `NerdAssetManager` instance.""");
        } else if (!p_key.isFor(this.getClass()))
            throw new IllegalArgumentException("""
                    That key is not for a `NerdAssetManager`!""");

        p_key.use();
        return true;
    }

    /*
     * private NerdAssetManager add(NerdAsset p_asset) {
     * this.ASSETS.add(p_asset);
     * return this;
     * }
     * 
     * private NerdAssetManager add(NerdAsset... p_assets) {
     * for (NerdAsset a : p_assets)
     * this.ASSETS.add(a);
     * 
     * return this;
     * }
     */

    // region Asset operations.
    // region `makeAsset()` overloads.
    public <T> NerdAsset<T> makeAsset(AssetType<T> p_type, String p_path, Object... p_options) {
        if (p_type == null || p_path == null)
            throw new IllegalArgumentException("`NerdAsset`s need data!");

        return new NerdAsset<T>(this.SKETCH, p_type, p_path, p_options);
    }

    public <T> NerdAsset<T> makeAsset(AssetType<T> p_type, String p_path) {
        return this.makeAsset(p_type, p_path, (Object[]) null);
    }
    // endregion

    // region `add()` overloads.
    public <T> AssetManager add(AssetType<? extends T> p_type, String p_path, Runnable p_onLoad, Object... p_options) {
        this.ASSETS.add(
                this.makeAsset(p_type, p_path, p_onLoad, p_options)
        // .setLoadCallback(p_onLoad)
        );
        return this;
    }

    public <T> AssetManager add(AssetType<? extends T> p_type, String p_path, Object... p_options) {
        this.add(p_type, p_path, null, p_options);
        return this;
    }

    public <T> AssetManager add(AssetType<? extends T> p_type, String p_path, Runnable p_onLoad) {
        return this.add(p_type, p_path, p_onLoad, (Object[]) null);
    }

    public <T> AssetManager add(AssetType<? extends T> p_type, String p_path) {
        return this.add(p_type, p_path, (Object[]) null);
    }
    // endregion

    /**
     * @deprecated since using {@link AssetManager#get()} is better. In cases
     *             where you'd want to check for the availability of an asset, you
     *             probably also a want a reference to it, in which case, it is much
     *             better to use {@link AssetManager#get()} and check if the return
     *             value is {@code null}.
     */
    @Deprecated
    public boolean contains(String p_fileName) {
        for (NerdAsset<?> a : this.ASSETS)
            if (a.NAME.equals(p_fileName))
                return true;
        return false;
    }

    @SuppressWarnings("unchecked")
    public <T> NerdAsset<T> get(String p_fileName) {
        for (NerdAsset<?> a : this.ASSETS)
            if (a.NAME.equals(p_fileName))
                return (NerdAsset<T>) a;
        return null;
    }

    // region `remove()` overloads.
    public void remove(NerdAsset<?>... p_assets) {
        for (NerdAsset<?> a : p_assets)
            this.ASSETS.remove(a);
    }

    public void remove(NerdAsset<?> p_asset) {
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
    public boolean wasComplete() {
        for (NerdAsset<?> a : this.ASSETS)
            if (!a.wasLoaded())
                return false;
        return true;
    }

    public boolean hasCompleted() {
        for (NerdAsset<?> a : this.ASSETS)
            if (!a.hasLoaded())
                return false;
        return true;
    }

    /**
     * Wait till all assets are done loading!
     */
    public void ensureCompletion() {
        while (this.hasCompleted())
            ;
    }

    public void updatePreviousLoadState(AssetManKey p_key) {
        this.verifyKey(p_key);

        for (NerdAsset<?> a : this.ASSETS)
            a.updatePreviousLoadState(/* this.ASSET_KEY */);
    }
    // endregion

    // region From `HashSet<NerdAsset>`.
    public boolean isEmpty() {
        return this.ASSETS.isEmpty();
    }

    // Potential problem: Iterators allow you to remove elements!
    public Iterator<NerdAsset<?>> iterator() {
        return this.ASSETS.iterator();
    }

    public int size() {
        return this.ASSETS.size();
    }

    public Spliterator<NerdAsset<?>> spliterator() {
        return this.ASSETS.spliterator();
    }

    public Object[] toArray() {
        return this.ASSETS.toArray();
    }

    public <T> T[] toArray(T[] a) {
        return this.ASSETS.toArray(a);
    }

    public Stream<NerdAsset<?>> parallelStream() {
        return this.ASSETS.parallelStream();
    }

    public boolean removeIf(Predicate<? super NerdAsset<?>> p_filter) {
        return this.ASSETS.removeIf(p_filter);
    }

    public Stream<NerdAsset<?>> stream() {
        return this.ASSETS.stream();
    }

    public <T> T[] toArray(IntFunction<T[]> p_generator) {
        return this.ASSETS.toArray(p_generator);
    }

    public void forEach(Consumer<? super NerdAsset<?>> p_action) {
        this.ASSETS.forEach(p_action);
    }
    // endregion

}
