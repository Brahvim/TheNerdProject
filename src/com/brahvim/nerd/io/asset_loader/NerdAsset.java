package com.brahvim.nerd.io.asset_loader;

import java.io.File;
import com.brahvim.nerd.papplet_wrapper.Sketch;

public class NerdAsset<AssetT> {

    // region Fields!
    public static boolean CACHE_SOUNDFILES = false;
    public final String NAME;

    private AssetT data;
    private Runnable onLoad;
    private int frame;
    private long millis = -1;
    private boolean loaded, ploaded, failure;

    private final String PATH;
    private final Sketch SKETCH;
    private final AssetType<AssetT> TYPE;
    // private final AssetManager.AssetKey KEY;

    private Object[] loaderArgs;
    // endregion

    // region Constructors!
    public NerdAsset(Sketch p_sketch, AssetType<AssetT> p_type, String p_path) {
        // this.verifyKey(p_key);
        if (p_type == null || p_path == null)
            throw new IllegalArgumentException("`NerdAsset`s need data!");

        // this.KEY = p_key;
        this.TYPE = p_type;
        this.SKETCH = p_sketch;
        this.loaderArgs = null;

        this.PATH = p_path;
        this.NAME = this.findName();
        this.startLoading();
    }

    public NerdAsset(Sketch p_sketch, AssetType<AssetT> p_type, String p_path, Runnable p_onLoad) {
        this(p_sketch, p_type, p_path);
        this.onLoad = p_onLoad;
    }

    public NerdAsset(Sketch p_sketch, AssetType<AssetT> p_type, String p_path, Object... p_loaderArgs) {
        this(p_sketch, p_type, p_path);
        this.loaderArgs = p_loaderArgs;
    }

    public NerdAsset(Sketch p_sketch, AssetType<AssetT> p_type, String p_path,
            Runnable p_onLoad, Object... p_loaderArgs) {
        this(p_sketch, p_type, p_path);
        this.onLoad = p_onLoad;
        this.loaderArgs = p_loaderArgs;
    }
    // endregion

    private String findName() {
        String toRet = new File(this.PATH).getName(); // Parses wth `/`s too!
        // Paths.get("").getFileName().toString(); // Parses with `File.separator`.

        int lastCharId = toRet.lastIndexOf('.');

        if (lastCharId == -1)
            lastCharId = toRet.length();

        toRet = toRet.substring(0, lastCharId);
        return toRet;
    }

    /*
     * private boolean verifyKey(AssetManager.AssetKey p_key) {
     * // * if (this.KEY != null)
     * // * return p_key == this.KEY;
     * 
     * if (p_key == null) {
     * throw new IllegalArgumentException("""
     * Please use an `AssetManager` instance to make a `NerdAsset`!""");
     * }
     * // else if (p_key.isUsed()) {
     * // throw new IllegalArgumentException("""
     * // Please use a `NerdSceneManager` instance to make a `NerdScene`! That is a
     * // used key!""");
     * // }
     * else if (!p_key.isFor(this.getClass()))
     * throw new IllegalArgumentException("""
     * Please use a `AssetManager` instance to make a `NerdAsset`! That key is not
     * for me!""");
     * 
     * p_key.use();
     * return true;
     * 
     * }
     */

    // region Load status requests.
    public NerdAsset<AssetT> setLoadCallback(Runnable p_onLoad) {
        this.onLoad = p_onLoad;
        return this;
    }

    public NerdAsset<AssetT> completeLoad() {
        while (!this.loaded)
            System.out.println("Waiting for `" + this.NAME + "` to load...");

        return this;
    }

    public void startLoading() {
        this.fetchData();
        this.loaded = true;

        if (this.onLoad != null) {
            this.onLoad.run();
        }
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

    // Tends to return `Object`s instead :|
    // public AssetDataT getData() {
    // this.completeLoad();
    // return this.data;
    // }

    public int getLoadFrame() {
        return this.frame;
    }

    public long getLoadMillis() {
        return this.millis;
    }
    // endregion
    // endregion

    private void fetchData() {
        try {
            this.data = this.TYPE.fetchData(this.SKETCH, this.PATH, this.loaderArgs);
        } catch (AssetLoaderFailedException e) {
            this.data = null;
            this.failure = true;
            e.printStackTrace();
        }

        this.frame = this.SKETCH.frameCount;
        this.millis = this.SKETCH.millis();

        /*
         * switch (this.TYPE) {{
         * 
         * case PAUDIO -> {
         * SoundFile file = new SoundFile(SKETCH, this.PATH,
         * NerdAsset.CACHE_SOUNDFILES);
         * 
         * try {
         * file.channels();
         * } catch (NullPointerException e) {
         * file.removeFromCache();
         * this.failure = true;
         * this.data = null; // `file` should be GC'ed by the end of this method.
         * }
         * 
         * this.data = file;
         * }
         * 
         * // I know where the `default` may be used!
         * // WHEN THE "TYPE:NULL"! ;)
         * // default -> {}
         * 
         * }
         */

    }

    public void updatePreviousLoadState() { // AssetManager.AssetKey p_key) {
        // if (this.verifyKey(p_key))
        this.ploaded = this.loaded;
    }

}
