package com.brahvim.nerd_test.scenes;

import com.brahvim.nerd.io.asset_loader.NerdAssetType;
import com.brahvim.nerd.scene_api.NerdScene;
import com.brahvim.nerd.scene_api.NerdSceneManager.SceneKey;

import processing.core.PGraphics;

public class TestScene4 extends NerdScene {
    PGraphics nerdGraphics;
    float ncx, ncy;

    public TestScene4(SceneKey p_sceneKey) {
        super(p_sceneKey);
    }

    @Override
    protected void preload() {
        ASSETS.add(NerdAssetType.PIMAGE, "data/sunglass_nerd.png")
                .add(null, null);
    }

    @Override
    protected void draw() {
    }

}
