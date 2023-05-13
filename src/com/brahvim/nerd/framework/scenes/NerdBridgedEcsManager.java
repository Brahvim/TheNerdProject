package com.brahvim.nerd.framework.scenes;

import com.brahvim.nerd.framework.ecs.NerdEcsEntity;
import com.brahvim.nerd.framework.ecs.NerdEcsManager;
import com.brahvim.nerd.framework.ecs.NerdEcsSystem;
import com.brahvim.nerd.processing_wrapper.NerdSketch;

public class NerdBridgedEcsManager extends NerdEcsManager {

	public NerdBridgedEcsManager(final NerdSketch p_sketch, final NerdEcsSystem<?>[] p_systems) {
		super(p_sketch, p_systems);
	}

	// region Public API!
	@Override
	public NerdEcsEntity createEntity() {
		return super.createEntity();
	}

	@Override
	public void removeEntity(final NerdEcsEntity p_entity) {
		super.removeEntity(p_entity);
	}

	@Override
	public NerdEcsSystem<?>[] getSystemsOrder() {
		return super.getSystemsOrder();
	}

	@Override
	public void setSystemsOrder(final NerdEcsSystem<?>[] p_ecsSystems) {
		super.setSystemsOrder(p_ecsSystems);
	}
	// endregion

	// region Sketch workflow callbacks.
	/**
	 * Used by a {@code NerdScene} to load {@code NerdAsset}s
	 * into their, or their {@code NerdSceneManager}'s {@code NerdAssetManager}.<br>
	 * <br>
	 * Use this method for all asset-loading purposes that you would like to do in
	 * the background. If {@code NerdSceneManager::preloadSceneAssets} or
	 * {@code NerdSceneManager::loadSceneAsync} is called, this method is run
	 * async, loading-in all {@code NerdAssets}!<br>
	 * <br>
	 * Since {@code NerdScene}s could be a part of the same `Sketch`, it is
	 * important to ensure that this method is `synchronized`.
	 */
	@Override
	protected synchronized void preload() {
		super.preload();
	}

	@Override
	protected void sceneChanged() {
		super.sceneChanged();
	}

	@Override
	protected void setup(final NerdSceneState p_state) {
		super.setup(p_state);
	}

	@Override
	protected void pre() {
		super.pre();
	}

	@Override
	protected void draw() {
		super.draw();
	}

	@Override
	protected void post() {
		super.post();
	}

	@Override
	protected void exit() {
		super.exit();
	}

	// protected void preDraw() { }

	@Override
	protected void dispose() {
		super.dispose();
	}

	// protected void postDraw() { }
	// endregion

	// region Events.
	// region Mouse events.
	@Override
	public void mousePressed() {
		super.mousePressed();
	}

	@Override
	public void mouseReleased() {
		super.mouseReleased();
	}

	@Override
	public void mouseMoved() {
		super.mouseMoved();
	}

	@Override
	public void mouseClicked() {
		super.mouseClicked();
	}

	@Override
	public void mouseDragged() {
		super.mouseDragged();
	}

	@Override
	public void mouseWheel(processing.event.MouseEvent p_mouseEvent) {
		super.mouseWheel(p_mouseEvent);
	}
	// endregion

	// region Keyboard events.
	@Override
	public void keyTyped() {
		super.keyTyped();
	}

	@Override
	public void keyPressed() {
		super.keyPressed();
	}

	@Override
	public void keyReleased() {
		super.keyReleased();
	}
	// endregion

	// region Touch events.
	@Override
	public void touchStarted() {
		super.touchStarted();
	}

	@Override
	public void touchMoved() {
		super.touchMoved();
	}

	@Override
	public void touchEnded() {
		super.touchEnded();
	}
	// endregion

	// region Window focus event
	@Override
	public void focusLost() {
		super.focusLost();
	}

	@Override
	public void resized() {
		super.resized();
	}

	@Override
	public void focusGained() {
		super.focusGained();
	}

	@Override
	public void monitorChanged() {
		super.monitorChanged();
	}

	@Override
	public void fullscreenChanged(boolean p_state) {
		super.fullscreenChanged(p_state);
	}
	// endregion
	// endregion

}
