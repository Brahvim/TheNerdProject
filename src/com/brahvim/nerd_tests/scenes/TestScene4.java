package com.brahvim.nerd_tests.scenes;

import com.brahvim.nerd.io.asset_loader.processing_loaders.PImageAsset;
import com.brahvim.nerd.openal.AlSource;
import com.brahvim.nerd.openal.al_asset_loaders.OggBufferAsset;
import com.brahvim.nerd.scene_api.NerdScene;
import com.brahvim.nerd.scene_api.SceneState;

import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.event.MouseEvent;

public class TestScene4 extends NerdScene {

    // region Fields!
    private PImage nerd;
    private PGraphics nerdGraphics;
    private AlSource rubberDuck;
    private float ncx, ncy;

    private final float MAG_SCROLL_ACC_MOD = 0.001f,
            MAG_SCROLL_DECAY_ACC = 0.8f,
            MAG_SCROLL_DECAY_VEL = 0.99f;
    private float magScrollAcc, magScrollVel, magScroll = 1;
    // endregion

    @Override
    protected void preload() {
        System.out.println("This is async LOL!");
        ASSETS.add(PImageAsset.getLoader(), SKETCH.ICON_PATH);
        ASSETS.add(OggBufferAsset.getLoader(), "data/RUBBER DUCK.ogg");
    }

    @Override
    protected void setup(SceneState p_state) {
        System.out.println("TestScene4.setup(), " + SCENE.getTimesLoaded());

        // Loaded this scene for the first time? Do this!:
        if (SCENE.getTimesLoaded() == 0) {
            SKETCH.fullscreen = false;
            SKETCH.getSurface().setSize(1600, 900);
            SKETCH.centerWindow();
        }

        this.nerd = this.ASSETS.get("sunglass_nerd").getData();
        this.nerdGraphics = SKETCH.createGraphics(this.nerd.width, this.nerd.height);
        // this.rubberDuck = new AlSource(SKETCH.AL, ASSETS.get("RUBBER
        // DUCK").getData());
        this.rubberDuck = SKETCH.AL.sourceFromOgg("data/RUBBER DUCK.ogg");

        SKETCH.noStroke();
        SKETCH.textureWrap(PConstants.REPEAT);
        SKETCH.getCurrentCamera().pos.z = 500;

        this.ncx = this.nerd.width * 0.5f;
        this.ncy = this.nerd.height * 0.5f;

        this.rubberDuck.setGain(0.1f);
    }

    @Override
    protected void draw() {
        SKETCH.background(0);
        SKETCH.translate(-SKETCH.cx, -SKETCH.cy);

        this.magScrollVel += (this.magScrollAcc *= this.MAG_SCROLL_DECAY_ACC);
        this.magScroll += (this.magScrollVel *= this.MAG_SCROLL_DECAY_VEL);
        CAMERA.pos.z += this.magScrollVel;

        // region Draw the nerds!!!
        SKETCH.beginShape();

        this.nerdGraphics.beginDraw();
        this.nerdGraphics.imageMode(PConstants.CENTER);
        this.nerdGraphics.translate(this.ncx, this.ncy);
        this.nerdGraphics.rotateZ(this.nerdRotTime() * 0.01f);
        this.nerdGraphics.image(this.nerd, 0, 0,
                this.nerd.width * this.magScroll,
                this.nerd.height * this.magScroll);
        this.nerdGraphics.endDraw();

        SKETCH.texture(this.nerdGraphics);

        // For just infinite tiles (no scrolling!):

        // SKETCH.vertex(0, 0, 0, 0);
        // SKETCH.vertex(SKETCH.width, 0, SKETCH.width, 0);
        // SKETCH.vertex(SKETCH.width, SKETCH.height, SKETCH.width, SKETCH.height);
        // SKETCH.vertex(0, SKETCH.height, 0, SKETCH.height);

        SKETCH.vertex(0, 0, this.nerdRotTime(), this.nerdRotTime());
        SKETCH.vertex(SKETCH.width, 0, this.nerdRotTime() + SKETCH.width, this.nerdRotTime());
        SKETCH.vertex(SKETCH.width, SKETCH.height,
                this.nerdRotTime() + SKETCH.width, this.nerdRotTime() + SKETCH.height);
        SKETCH.vertex(0, SKETCH.height, this.nerdRotTime(), this.nerdRotTime() + SKETCH.height);

        SKETCH.endShape();
        // endregion

    }

    private float nerdRotTime() {
        return SCENE.getMillisSinceStart() * 0.1f;
    }

    // region Events.
    @Override
    public void mouseClicked() {
        /*
         * 
         * System.out.printf("""
         * OpenAL source states:
         * - `AL_PAUSED` : `%d`,
         * - `AL_STOPPED` : `%d`,
         * - `AL_INITIAL` : `%d`,
         * - `AL_PLAYING` : `%d`.
         * """,
         * AL11.AL_PAUSED,
         * AL11.AL_STOPPED,
         * AL11.AL_INITIAL,
         * AL11.AL_PLAYING);
         */
        // System.out.println(this.rubberDuck.getSourceState());

        if (!this.rubberDuck.isPlaying()) {
            this.rubberDuck.setPosition(
                    SKETCH.mouseX, SKETCH.mouseY, SKETCH.random(5600));
            this.rubberDuck.play();
        }

        System.out.println("TestScene4.mousePressed()");
        MANAGER.restartScene();
    }

    @Override
    public void mouseWheel(MouseEvent p_mouseEvent) {
        this.magScrollAcc += p_mouseEvent.getCount() * this.MAG_SCROLL_ACC_MOD;
    }

    @Override
    public void exit() {
        System.out.println("Nerd exited!");
    }
    // endregion

}
