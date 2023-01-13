package com.brahvim.nerd.io.asset_loader;

public class NerdAsset<T> {
    private Runnable onLoad;
    private boolean loaded, ploaded;
    private long loadTime;
    private int loadFrame;
    private T loadedData;
    private String path;

}
