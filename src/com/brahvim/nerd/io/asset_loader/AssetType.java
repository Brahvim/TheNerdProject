package com.brahvim.nerd.io.asset_loader;

import com.brahvim.nerd.papplet_wrapper.Sketch;

// Keeping this outside saves typing!:
public abstract class AssetType<T> {
    // Types:
    /*
     * XML(processing.data.XML.class),
     * SVG(processing.core.PShape.class),
     * SERIALIZED(java.lang.Object.class),
     * // PVIDEO(processing.video.Movie.class),
     * PBYTES(java.lang.reflect.Array.class),
     * MODEL_3D(processing.core.PShape.class),
     * PSTRINGS(java.lang.reflect.Array.class),
     * // PAUDIO(processing.sound.SoundFile.class),
     * PSHADER(processing.opengl.PShader.class),
     * FILESTREAM(java.io.FileInputStream.class),
     * PJSON_ARRAY(processing.data.JSONArray.class),
     * PJSON_OBJECT(processing.data.JSONObject.class);
     */

    protected AssetType() {
    }

    public static AssetType<?> getLoader() {
        return null; // return AssetType.LOADER;
    }

    public abstract T fetchData(Sketch p_sketch, String p_path, Object... p_options)
            throws AssetLoaderFailedException, IllegalArgumentException;

}
