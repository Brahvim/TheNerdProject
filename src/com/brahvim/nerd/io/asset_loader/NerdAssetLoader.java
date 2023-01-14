package com.brahvim.nerd.io.asset_loader;

import java.util.HashSet;

import com.brahvim.nerd.scene_api.NerdScene;

public class NerdAssetLoader {
    private final NerdScene SCENE;
    private final HashSet<NerdAsset> assets = new HashSet<>();

    public NerdAssetLoader(NerdScene p_scene) {
        this.SCENE = p_scene;
    }

    public void completeLoad(NerdAsset p_asset) {

    }

}
