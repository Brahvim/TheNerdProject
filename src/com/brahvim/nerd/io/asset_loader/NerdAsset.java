package com.brahvim.nerd.io.asset_loader;

public class NerdAsset<T> {
    private boolean loaded, ploaded;
    private Runnable onLoad;
    private long loadTime;
    private int loadFrame;
    private T loadedData;
    private String path;

}
