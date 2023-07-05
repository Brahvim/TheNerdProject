package com.brahvim.nerd.framework.scene_api;

import com.brahvim.nerd.framework.cameras.NerdAbstractCamera;
import com.brahvim.nerd.io.asset_loader.NerdAssetManager;
import com.brahvim.nerd.processing_wrapper.NerdDisplayManager;
import com.brahvim.nerd.processing_wrapper.NerdGraphics;
import com.brahvim.nerd.processing_wrapper.NerdInputManager;
import com.brahvim.nerd.processing_wrapper.NerdSketch;
import com.brahvim.nerd.processing_wrapper.NerdWindowManager;

/**
 * Just like {@link NerdScene}s, {@link NerdLayer}s
 * are used via inheritance, and not anonymous classes.
 *
 * <p>
 * To add a {@link NerdLayer} to a {@link NerdScene}, use the
 * {@link NerdScene#addLayer(Class)}, passing in your {@link NerdLayer}
 * subclass.
 */
public abstract class NerdLayer {

  // region `protected` fields.
  // Seriously, why did I set these to be `protected`?
  public final NerdLayer LAYER = this;

  protected NerdSketch SKETCH;
  protected NerdSceneState STATE;
  protected NerdGraphics GRAPHICS;
  protected NerdInputManager INPUT;
  protected NerdAssetManager ASSETS;
  protected NerdSceneManager MANAGER;
  protected NerdWindowManager WINDOW;
  protected NerdAbstractCamera CAMERA;
  protected NerdBridgedEcsManager ECS;
  protected NerdDisplayManager DISPLAY;

  protected NerdScene SCENE;
  // endregion

  // region `private` fields.
  private int timesActivated;
  private boolean active;
  // endregion

  protected NerdLayer() {
  }

  // region Activity status.
  public boolean isActive() {
    return this.active;
  }

  public void setActive(final boolean p_toggleState) {
    final boolean previouslyActive = this.active; // RECORD!!!!
    this.active = p_toggleState;

    if (this.active && !previouslyActive) {
      this.setup();
      this.timesActivated++;
    } else
      this.layerExit();
  }

  public int getTimesActivated() {
    return this.timesActivated;
  }
  // endregion

  // region Events.
  // region Mouse events.
  public void mousePressed() {
  }

  public void mouseReleased() {
  }

  public void mouseMoved() {
  }

  public void mouseClicked() {
  }

  public void mouseDragged() {
  }

  public void mouseWheel(final processing.event.MouseEvent p_mouseEvent) {
  }
  // endregion

  // region Keyboard events.
  public void keyTyped() {
  }

  public void keyPressed() {
  }

  public void keyReleased() {
  }
  // endregion

  // region Touch events.
  public void touchStarted() {
  }

  public void touchMoved() {
  }

  public void touchEnded() {
  }
  // endregion

  // region Window focus events.
  public void focusLost() {
  }

  public void exit() {
  }

  public void resized() {
  }

  public void focusGained() {
  }

  public void monitorChanged() {
  }

  public void fullscreenChanged(final boolean p_state) {
  }
  // endregion
  // endregion

  // region `protected` methods. Nobody can call them outside of this package!
  // region `NerdLayer`-only (`protected`) callbacks!
  protected void layerExit() {
  }
  // endregion

  // region App workflow callbacks.
  protected void setup() {
  }

  protected void pre() {
  }

  protected void draw() {
  }

  protected void post() {
  }
  // endregion
  // endregion

}
