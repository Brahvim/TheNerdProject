package com.brahvim.nerd_demos.scenes;

import com.brahvim.nerd.io.asset_loader.processing_loaders.PImageAsset;
import com.brahvim.nerd.openal.AlSource;
import com.brahvim.nerd.openal.al_asset_loaders.OggBufferDataAsset;
import com.brahvim.nerd.scene_api.NerdScene;
import com.brahvim.nerd.scene_api.SceneState;
import com.brahvim.nerd_demos.App;

import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.event.MouseEvent;

public class DemoScene4 extends NerdScene {

    // region Fields!
    private PImage nerd;
    private float ncx, ncy;
    private AlSource rubberDuck;
    private PGraphics nerdGraphics;

    private final float MAG_SCROLL_ACC_MOD = 0.001f,
            MAG_SCROLL_DECAY_ACC = 0.8f,
            MAG_SCROLL_DECAY_VEL = 0.99f;
    private float magScrollAcc, magScrollVel, magScroll = 1;
    // endregion

    @Override
    protected void preload() {
        ASSETS.add(PImageAsset.getLoader(), SKETCH.ICON_PATH);
        ASSETS.add(OggBufferDataAsset.getLoader(), "data/RUBBER DUCK.ogg");
        System.out.println("Test Scene 4 asset preload completed!");
    }

    @Override
    protected void setup(final SceneState p_state) {
        System.out.printf("`TestScene4::setup()` here, I was called `%d` times!\n",
                this.SCENE.getTimesLoaded());

        // region OpenAL Test.
        // ..so the effects and filters wrk perfectly, but I just didn't want them in
        // this example. Feel free to uncomment!~

        // AlAuxiliaryEffectSlot slot = new AlAuxiliaryEffectSlot(App.AL);
        // AlEcho effect = new AlEcho(App.AL);
        // slot.setEffect(effect);
        // effect.setEchoDelay(0.01f);
        // effect.setEchoDamping(0.8f);
        // effect.setEchoFeedback(0.001f);

        // AlLowpassFilter filter = new AlLowpassFilter(App.AL);
        // filter.setLowpassGain(1);
        // filter.setLowpassGainHf(0.1f);

        this.rubberDuck = new AlSource(App.OPENAL, ASSETS.get("RUBBER DUCK").getData());
        // this.rubberDuck.attachDirectFilter(filter);
        this.rubberDuck.setGain(0.1f);
        // this.rubberDuck.setEffectSlot(slot);
        // endregion

        // Loaded this scene for the first time? Do this!:
        if (App.FIRST_SCENE_CLASS == DemoScene4.class && this.SCENE.getTimesLoaded() == 0) {
            SKETCH.fullscreen = false;
            App.WINDOW.setSize(1600, 900);
            SKETCH.centerWindow();
        } else { // Do not play `this.rubberDuck` if this is the first start!
            App.OPENAL.setListenerVelocity(0, 0, 0);
            App.OPENAL.setListenerPosition(0, 0, 500);
            App.OPENAL.setListenerOrientation(0, 1, 0);

            // for (int i = 0; i < 50; i++) // I literally told OpenAL to do this 50 TIMES.
            this.rubberDuck.setPosition(
                    5 * (SKETCH.mouseX - SKETCH.cx), 0,
                    5 * (SKETCH.mouseY - SKETCH.cy));

            App.OPENAL.unitSize = 1;
            // System.out.println(CAMERA.pos);
            // System.out.println(this.rubberDuck.getPosition());

            if (!this.rubberDuck.isPlaying())
                this.rubberDuck.play();
        }

        this.nerd = ASSETS.get("sunglass_nerd").getData();
        this.nerdGraphics = SKETCH.createGraphics(this.nerd.width, this.nerd.height);

        SKETCH.noStroke();
        SKETCH.getCamera().pos.z = 500;
        SKETCH.textureWrap(PConstants.REPEAT);

        this.ncx = this.nerd.width * 0.5f;
        this.ncy = this.nerd.height * 0.5f;
    }

    @Override
    protected void draw() {
        SKETCH.clear();
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
        SKETCH.vertex(SKETCH.width, 0, this.nerdRotTime() + SKETCH.width,
                this.nerdRotTime());
        SKETCH.vertex(SKETCH.width, SKETCH.height,
                this.nerdRotTime() + SKETCH.width, this.nerdRotTime() + SKETCH.height);
        SKETCH.vertex(0, SKETCH.height, this.nerdRotTime(), this.nerdRotTime() +
                SKETCH.height);

        SKETCH.endShape();
        // endregion

        // SKETCH.in2d(() -> {
        SKETCH.translate(SKETCH.getMouseInWorld());
        SKETCH.circle(0, 0, 20);
        // });

    }

    private float nerdRotTime() {
        return this.SCENE.getMillisSinceStart() * 0.1f;
    }

    // region Events.
    @Override
    public void mouseClicked() {
        switch (SKETCH.mouseButton) {
            case PConstants.LEFT -> MANAGER.restartScene();
            case PConstants.RIGHT -> MANAGER.startScene(DemoScene3.class);
        }
    }

    @Override
    public void mouseWheel(final MouseEvent p_mouseEvent) {
        this.magScrollAcc += p_mouseEvent.getCount() * this.MAG_SCROLL_ACC_MOD;
    }

    @Override
    public void exit() {
        System.out.println("Nerd exited!");
    }
    // endregion

}
