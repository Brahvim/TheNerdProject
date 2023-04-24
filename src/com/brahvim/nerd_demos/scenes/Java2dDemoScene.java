package com.brahvim.nerd_demos.scenes;

import com.brahvim.nerd.openal.AlSource;
import com.brahvim.nerd.openal.al_asset_loaders.OggBufferDataAsset;
import com.brahvim.nerd.scene_api.NerdScene;
import com.brahvim.nerd.scene_api.SceneState;
import com.brahvim.nerd_demos.App;

public class Java2dDemoScene extends NerdScene {

    private AlSource rubberDuckSource;

    @Override
    protected synchronized void preload() {
        ASSETS.add(new OggBufferDataAsset(), "data/RUBBER DUCK.ogg");
    }

    @Override
    protected void setup(SceneState p_state) {
        App.OPENAL.unitSize = 1;
        this.rubberDuckSource = new AlSource(App.OPENAL, ASSETS.get("RUBBER DUCK").getData());
        this.rubberDuckSource.setGain(5);
    }

    @Override
    protected void draw() {
        App.OPENAL.setListenerPosition(WINDOW.cx, WINDOW.cy, 0);
    }

    @Override
    public void mouseClicked() {
        this.rubberDuckSource.setPosition(SKETCH.mouseX, SKETCH.mouseY, 0);
        this.rubberDuckSource.play();
    }

}
