package com.brahvim.nerd.io.asset_loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.brahvim.nerd.io.ByteSerial;
import com.brahvim.nerd.scene_api.NerdScene;

import processing.core.PImage;
import processing.core.PShape;
import processing.data.XML;
import processing.opengl.PShader;
import processing.sound.SoundFile;

public class NerdAsset {
    // region Fields!
    public static boolean CACHE_SOUNDFILES = false;

    private boolean loaded, ploaded, failure;
    private long millis = -1;
    private Runnable onLoad;
    private int frame = -1;
    private Object data;

    private final NerdAssetType TYPE;
    private final NerdScene SCENE;
    private final String PATH;
    // endregion

    // region Constructors!
    // NO. Tell me the type yourself :joy:
    public NerdAsset(NerdScene p_scene, NerdAssetType p_type, String p_path) {
        if (p_path == null || p_scene == null || p_type == null) {
            throw new IllegalArgumentException("Those arguments can't be `null`!");
        }

        this.SCENE = p_scene;
        this.TYPE = p_type;
        this.PATH = p_path;
    }

    public NerdAsset(NerdScene p_scene, NerdAssetType p_type, String p_path, Runnable p_onLoad) {
        this(p_scene, p_type, p_path);
        this.onLoad = p_onLoad;
    }
    // endregion

    // region Load status requests.
    public NerdAsset startLoading() {
        new Thread(() -> {
            this.loadImpl();
        }).start();

        return this;
    }

    public NerdAsset onLoad(Runnable p_onLoad) {
        this.onLoad = p_onLoad;
        return this;
    }

    public NerdAsset completeLoad() {
        while (!this.loaded)
            ;
        return this;
    }

    // region "Yes/No" questions.
    public boolean hasLoaded() {
        return this.loaded;
    }

    public boolean wasLoaded() {
        return this.ploaded;
    }

    public boolean hasFailed() {
        return this.failure;
    }
    // endregion

    // region Getters.
    @SuppressWarnings("unchecked")
    public <T> T getData() {
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

    private void loadImpl() {
        this.fetchData();
        this.onLoad.run();
    }

    private synchronized void fetchData() {
        switch (this.TYPE) {
            case FILESTREAM -> {
                try {
                    this.data = new FileInputStream(new File(this.PATH));
                } catch (FileNotFoundException e) {
                    this.failure = true;
                    this.data = e;
                }
            }

            case IMAGE -> {
                PImage img = SCENE.getSketch().loadImage(this.PATH);

                // Oh, it failed?
                this.failure = img == null;
                if (!(this.failure))
                    this.failure = img.width == -1;

                this.data = img;
            }

            case SVG, MODEL_3D -> {
                PShape shape = SCENE.getSketch().loadShape(this.PATH);

                if (shape == null)
                    this.failure = true;

                this.data = shape;
            }

            case PAUDIO -> {
                SoundFile file = new SoundFile(SCENE.getSketch(), this.PATH, NerdAsset.CACHE_SOUNDFILES);

                try {
                    file.channels();
                } catch (NullPointerException e) {
                    file.removeFromCache();
                    this.failure = true;
                    this.data = null; // `file` should be GC'ed by the end of this method.
                }

                this.data = file;
            }

            case PBYTES -> {
                byte[] bytes = this.SCENE.getSketch().loadBytes(this.PATH);
                this.failure = bytes == null;
                this.data = bytes; // Compiler: No complaints!

                // // *Me wen typing:* `bufer`, `bugfer`, `bugfestival`...
                // ByteBuffer buffer = ByteBuffer.wrap(bytes);
            }

            case PJSON_ARRAY -> {
                try {
                    this.data = SCENE.getSketch().loadJSONArray(this.PATH);
                } catch (NullPointerException e) {
                    this.failure = true;
                    this.data = e;
                }
            }

            case PJSON_OBJECT -> {
                try {
                    this.data = SCENE.getSketch().loadJSONObject(this.PATH);
                } catch (NullPointerException e) {
                    this.failure = true;
                    this.data = e;
                }
            }

            case PSHADER -> {
                PShader shader = SCENE.getSketch().loadShader(this.PATH);

                if (shader == null)
                    this.failure = true;

                this.data = shader;
            }

            case PSTRINGS -> {
                String[] strings = SCENE.getSketch().loadStrings(this.PATH);

                if (strings == null)
                    this.failure = true;

                this.data = strings;
            }

            case SERIALIZED -> {
                this.data = ByteSerial.fromFile(new File(this.PATH));

                if (this.data == null)
                    this.failure = true;
            }

            case XML -> {
                XML markup = SCENE.getSketch().loadXML(this.PATH);

                if (markup == null)
                    this.failure = true;

                this.data = markup;
            }

            // I know where the `default` may be used!
            // WHEN THE "TYPE:NULL"! ;)
            // default -> {}

        }
    }

}
