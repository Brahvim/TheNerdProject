package com.brahvim.nerd.io.asset_loader;

// Keeping this outside saves typing!:
public enum NerdAssetType {
    XML(processing.data.XML.class),
    SVG(processing.core.PShape.class),
    SERIALIZED(java.lang.Object.class),
    PIMAGE(processing.core.PImage.class),
    // PVIDEO(processing.video.Movie.class),
    PBYTES(java.lang.reflect.Array.class),
    MODEL_3D(processing.core.PShape.class),
    PSTRINGS(java.lang.reflect.Array.class),
    PAUDIO(processing.sound.SoundFile.class),
    PSHADER(processing.opengl.PShader.class),
    FILESTREAM(java.io.FileInputStream.class),
    PJSON_ARRAY(processing.data.JSONArray.class),
    PJSON_OBJECT(processing.data.JSONObject.class);

    // What the `NerdAsset` object stores a reference to:
    public final Class<?> CLASS;

    private NerdAssetType(Class<?> p_class) {
        this.CLASS = p_class;
    }
}
