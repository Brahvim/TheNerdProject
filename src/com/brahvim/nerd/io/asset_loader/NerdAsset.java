package com.brahvim.nerd.io.asset_loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Paths;

import com.brahvim.nerd.io.ByteSerial;
import com.brahvim.nerd.papplet_wrapper.Sketch;

import processing.core.PImage;
import processing.core.PShape;
import processing.data.XML;
import processing.opengl.PShader;

public class NerdAsset {

    // region Fields!
    public static boolean CACHE_SOUNDFILES = false;
    public final String NAME;

    private boolean loaded, ploaded, failure;
    private long millis = -1;
    private Runnable onLoad;
    private int frame = -1;
    private Object data;

    private final AssetType TYPE;
    private final Sketch SKETCH;
    private final AssetManager.AssetKey KEY;
    private final String PATH;
    // endregion

    // region Constructors!
    // NO. Tell me the type yourself :joy:
    public NerdAsset(AssetManager.AssetKey p_key, AssetType p_type, String p_path) {
        this.verifyKey(p_key);
        if (p_type == null || p_path == null)
            throw new IllegalArgumentException("`NerdAsset`s need data!");

        this.KEY = p_key;
        this.TYPE = p_type;
        this.SKETCH = p_key.SKETCH;

        this.PATH = Sketch.getPathToRootFrom(p_path);
        this.NAME = this.findName();
        this.startLoading();
    }

    public NerdAsset(AssetManager.AssetKey p_key, AssetType p_type, String p_path, Runnable p_onLoad) {
        this(p_key, p_type, p_path);
        this.onLoad = p_onLoad;
    }
    // endregion

    private String findName() {
        String toRet = new File(this.PATH).getName();

        int lastCharId = toRet.lastIndexOf('.');

        if (lastCharId == -1) // We subtracted `1` from it - it'll be `-2` in that case!
            lastCharId = toRet.length();

        toRet = toRet.substring(0, lastCharId);
        return toRet;
    }

    private boolean verifyKey(AssetManager.AssetKey p_key) {
        if (this.KEY != null)
            return p_key == this.KEY;

        if (p_key == null) {
            throw new IllegalArgumentException("""
                    Please use an `AssetManager` instance to make a `NerdAsset`!""");
        }
        // else if (p_key.isUsed()) {
        // throw new IllegalArgumentException("""
        // Please use a `NerdSceneManager` instance to make a `NerdScene`! That is a
        // used key!""");
        // }
        else if (!p_key.isFor(this.getClass()))
            throw new IllegalArgumentException("""
                    Please use a `AssetManager` instance to make a `NerdAsset`! That key is not for me!""");

        p_key.use();
        return true;

    }

    // region Load status requests.
    public NerdAsset setLoadCallback(Runnable p_onLoad) {
        this.onLoad = p_onLoad;
        return this;
    }

    public NerdAsset completeLoad() {
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
    public <T> T getData() {
        this.completeLoad();
        return (T) this.data;
    }

    public long getLoadMillis() {
        return this.millis;
    }

    public int getLoadFrame() {
        return this.frame;
    }
    // endregion
    // endregion

    private synchronized void fetchData() {
        // If the path is absolute,
        if (Paths.get(this.PATH).isAbsolute())
            this.PATH.concat(Sketch.DATA_DIR_PATH_TO_DRIVE_ROOT_SUFFIX);

        switch (this.TYPE) {
            case FILESTREAM -> {
                try {
                    this.data = new FileInputStream(new File(this.PATH));
                } catch (FileNotFoundException e) {
                    this.failure = true;
                    this.data = e;
                }
            }

            case PIMAGE -> {
                PImage img = SKETCH.loadImage(this.PATH);

                // Oh, it failed?
                this.failure = img == null;
                if (!(this.failure))
                    this.failure = img.width == -1;

                this.data = img;
            }

            case SVG, MODEL_3D -> {
                PShape shape = SKETCH.loadShape(this.PATH);

                if (shape == null)
                    this.failure = true;

                this.data = shape;
            }

            /*
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
             * case PVIDEO -> {}
             */

            case PBYTES -> {
                byte[] bytes = this.SKETCH.loadBytes(this.PATH);
                this.failure = bytes == null;
                this.data = bytes; // Compiler: No complaints!

                // // *Me wen typing:* `bufer`, `bugfer`, `bugfestival`...
                // ByteBuffer buffer = ByteBuffer.wrap(bytes);
            }

            case PJSON_ARRAY -> {
                try {
                    this.data = SKETCH.loadJSONArray(this.PATH);
                } catch (NullPointerException e) {
                    this.failure = true;
                    this.data = e;
                }
            }

            case PJSON_OBJECT -> {
                try {
                    this.data = SKETCH.loadJSONObject(this.PATH);
                } catch (NullPointerException e) {
                    this.failure = true;
                    this.data = e;
                }
            }

            case PSHADER -> {
                PShader shader = SKETCH.loadShader(this.PATH);

                if (shader == null)
                    this.failure = true;

                this.data = shader;
            }

            case PSTRINGS -> {
                String[] strings = SKETCH.loadStrings(this.PATH);

                if (strings == null)
                    this.failure = true;

                this.data = strings;
                // haha space else vscode no fold dis
            }

            case XML -> {
                XML markup = SKETCH.loadXML(this.PATH);

                if (markup == null)
                    this.failure = true;

                this.data = markup;
            }

            case SERIALIZED -> {
                this.data = ByteSerial.fromFile(new File(this.PATH));

                if (this.data == null)
                    this.failure = true;
            }

            // I know where the `default` may be used!
            // WHEN THE "TYPE:NULL"! ;)
            // default -> {}

        }
    }

    public void updatePreviousLoadState(AssetManager.AssetKey p_key) {
        if (this.verifyKey(p_key))
            this.ploaded = this.loaded;
    }

}
