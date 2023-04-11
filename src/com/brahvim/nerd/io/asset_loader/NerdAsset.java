package com.brahvim.nerd.io.asset_loader;

import java.io.File;
import java.util.function.Consumer;

import com.brahvim.nerd.papplet_wrapper.Sketch;

public class NerdAsset {

    // region Fields!
    public final String NAME;

    private final String PATH;
    private final Sketch SKETCH;
    private final AssetLoader<?> LOADER;

    private int frame;
    private Object data;
    private Runnable onLoad;
    private long millis = -1;
    private AssetLoaderOptions[] options;
    private boolean loaded, ploaded, failure;
    // endregion

    // region Construction.
    public NerdAsset(final Sketch p_sketch, final AssetLoader<?> p_type, final String p_path) {
        // this.verifyKey(p_key);
        if (p_type == null || p_path == null)
            throw new IllegalArgumentException("`NerdAsset`s need data!");

        // this.KEY = p_key;
        this.PATH = p_path;
        this.LOADER = p_type;
        this.SKETCH = p_sketch;
        this.NAME = this.findName();
        this.startLoading();
    }

    public NerdAsset(final Sketch p_sketch, final AssetLoader<?> p_type, final String p_path,
            final Runnable p_onLoad) {
        this(p_sketch, p_type, p_path);
        this.onLoad = p_onLoad;
    }

    public NerdAsset(final Sketch p_sketch, final AssetLoader<?> p_type, final String p_path,
            final AssetLoaderOptions... p_options) {
        this(p_sketch, p_type, p_path);
        this.options = p_options;
    }

    public NerdAsset(final Sketch p_sketch, final AssetLoader<?> p_type, final String p_path,
            final Runnable p_onLoad, final AssetLoaderOptions... p_options) {
        this(p_sketch, p_type, p_path);
        this.onLoad = p_onLoad;
        this.options = p_options;
    }

    private String findName() {
        String toRet = new File(this.PATH).getName(); // Parses wth `/`s too!
        // Paths.get("").getFileName().toString(); // Parses with `File.separator`.

        int lastCharId = toRet.lastIndexOf('.');

        if (lastCharId == -1)
            lastCharId = toRet.length();

        toRet = toRet.substring(0, lastCharId);
        return toRet;
    }
    // endregion

    // region Load status requests.
    public NerdAsset setLoadCallback(final Runnable p_onLoad) {
        this.onLoad = p_onLoad;
        return this;
    }

    public NerdAsset completeLoad() {
        while (!this.loaded)
            System.out.println("Waiting for `" + this.NAME + "` to load...");
        return this;
    }

    public void startLoading() {
        // Adding callbacks for each asset since `AssetManager`s don't handle loading.
        final Consumer<Sketch> postCallback = s -> this.ploaded = this.loaded;
        this.SKETCH.addPostListener(postCallback);
        this.fetchData();
        this.loaded = true;

        if (this.onLoad != null)
            this.onLoad.run();

        // Once the asset has loaded, `loaded` is set to `true` and the `postCallback`
        // is no longer necessary.
        // However, we need to update `ploaded` for one last frame.
        // To do so, we add a "self-removing" callback!:

        final NerdAsset ASSET = this;
        final Consumer<Sketch> whenLoaded = new Consumer<Sketch>() {
            @Override
            public void accept(final Sketch p_sketch) {
                ASSET.ploaded = true;
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
    public String getPath() {
        return this.PATH;
    }

    /**
     * Ensures that the asset has loaded, then returns its data,
     * given the name of a file (without the extension!).<br>
     * <br>
     * Usage example:<br>
     * <br>
     * {@code PImage image = SCENE.ASSETS.get("my_image").getData();}
     */
    @SuppressWarnings("unchecked")
    public <RetT> RetT getData() {
        this.completeLoad();
        return (RetT) this.data;
    }

    public int getLoadFrame() {
        return this.frame;
    }

    public long getLoadMillis() {
        return this.millis;
    }

    public AssetLoader<?> getLoader() {
        return this.LOADER;
    }
    // endregion
    // endregion

    private void fetchData() {
        try {
            this.data = this.LOADER.fetchData(this.SKETCH, this.PATH, this.options);
            this.millis = this.SKETCH.millis();
            this.frame = this.SKETCH.frameCount;
        } catch (final AssetLoaderFailedException e) {
            this.data = null;
            this.failure = true;
            e.printStackTrace();
        }
    }

}
