package com.brahvim.nerd.framework.scene_api;

import java.io.File;
import java.util.HashSet;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.brahvim.nerd.framework.NerdTriConsumer;
import com.brahvim.nerd.framework.ecs.NerdEcsComponent;
import com.brahvim.nerd.framework.ecs.NerdEcsEntity;
import com.brahvim.nerd.framework.ecs.NerdEcsManager;
import com.brahvim.nerd.framework.ecs.NerdEcsSystem;
import com.brahvim.nerd.io.net.NerdSocket;
import com.brahvim.nerd.processing_wrapper.NerdSketch;

import processing.event.MouseEvent;

public class NerdBridgedEcsManager extends NerdEcsManager {

	public NerdBridgedEcsManager(final NerdSketch p_sketch, final NerdEcsSystem<?>[] p_systems) {
		super(p_sketch, p_systems);
	}

	// region Custom methods.
	protected NerdBridgedEcsManager clearAllData() {
		super.ENTITIES.clear();
		super.COMPONENTS.clear();
		super.numUnnamedEntities = 0;
		super.NAME_TO_ENTITY_MAP.clear();
		return this;
	}
	// endregion

	// region *Inheritance.*
	@Override
	@SuppressWarnings("all")
	protected void callOnAllSystems(final BiConsumer<NerdEcsSystem, HashSet<? extends NerdEcsComponent>> p_methodRef) {
		super.callOnAllSystems(p_methodRef);
	}

	@Override
	@SuppressWarnings("all")
	protected <OtherArgT> void callOnAllSystems(
			final NerdTriConsumer<NerdEcsSystem, OtherArgT, HashSet<? extends NerdEcsComponent>> p_methodRef,
			final OtherArgT p_otherArg) {
		super.callOnAllSystems(p_methodRef, p_otherArg);
	}

	@Override
	protected <OtherArgT> void callOnAllSystems(final BiConsumer<NerdEcsSystem<?>, OtherArgT> p_methodRef,
			final OtherArgT p_otherArg) {
		super.callOnAllSystems(p_methodRef, p_otherArg);
	}

	@Override
	protected void callOnAllSystems(final Consumer<NerdEcsSystem<?>> p_method) {
		super.callOnAllSystems(p_method);
	}

	@Override
	public NerdEcsEntity createEntity() {
		return super.createEntity();
	}

	@Override
	public NerdEcsEntity createEntity(final String p_name) {
		return super.createEntity(p_name);
	}

	@Override
	protected void dispose() {
		super.dispose();
	}

	@Override
	protected void draw() {
		super.draw();
	}

	@Override
	protected void exit() {
		super.exit();
	}

	@Override
	public void focusGained() {
		super.focusGained();
	}

	@Override
	public void focusLost() {
		super.focusLost();
	}

	@Override
	public void forEachComponent(final Consumer<? super NerdEcsComponent> p_action) {
		super.forEachComponent(p_action);
	}

	@Override
	public void forEachComponentClassUsed(final Consumer<? super Class<? extends NerdEcsComponent>> p_action) {
		super.forEachComponentClassUsed(p_action);
	}

	@Override
	public void forEachEntity(final Consumer<? super NerdEcsEntity> p_action) {
		super.forEachEntity(p_action);
	}

	@Override
	public void forEachEntityUnnamed(final Consumer<NerdEcsEntity> p_action) {
		super.forEachEntityUnnamed(p_action);
	}

	@Override
	public void forEachEntityWithName(final BiConsumer<String, NerdEcsEntity> p_action) {
		super.forEachEntityWithName(p_action);
	}

	@Override
	public void fullscreenChanged(final boolean p_state) {
		super.fullscreenChanged(p_state);
	}

	@Override
	public String getEntityName(final NerdEcsEntity p_entity) {
		return super.getEntityName(p_entity);
	}

	@Override
	public NerdEcsSystem<?>[] getSystemsOrder() {
		return super.getSystemsOrder();
	}

	@Override
	public <RetT extends NerdSocket> RetT getUnderlyingNerdSocket() {
		return super.getUnderlyingNerdSocket();
	}

	@Override
	public void keyPressed() {
		super.keyPressed();
	}

	@Override
	public void keyReleased() {
		super.keyReleased();
	}

	@Override
	public void keyTyped() {
		super.keyTyped();
	}

	@Override
	public void loadState(final File p_file) {
		super.loadState(p_file);
	}

	@Override
	public void loadState(final byte[] p_serializedData) {
		super.loadState(p_serializedData);
	}

	@Override
	public void monitorChanged() {
		super.monitorChanged();
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
	public void mouseMoved() {
		super.mouseMoved();
	}

	@Override
	public void mousePressed() {
		super.mousePressed();
	}

	@Override
	public void mouseReleased() {
		super.mouseReleased();
	}

	@Override
	public void mouseWheel(final MouseEvent p_mouseEvent) {
		super.mouseWheel(p_mouseEvent);
	}

	@Override
	protected void post() {
		super.post();
	}

	@Override
	protected void pre() {
		super.pre();
	}

	@Override
	protected synchronized void preload() {
		super.preload();
	}

	@Override
	public void removeEntity(final NerdEcsEntity p_entity) {
		super.removeEntity(p_entity);
	}

	@Override
	public void renameEntity(final NerdEcsEntity p_entity, final String p_name) {
		super.renameEntity(p_entity, p_name);
	}

	@Override
	public void resized() {
		super.resized();
	}

	@Override
	public byte[] saveState() {
		return super.saveState();
	}

	@Override
	public void saveState(final File p_file) {
		super.saveState(p_file);
	}

	@Override
	protected void sceneChanged() {
		super.sceneChanged();
	}

	@Override
	public void setSystemsOrder(final NerdEcsSystem<?>[] p_ecsSystems) {
		super.setSystemsOrder(p_ecsSystems);
	}

	@Override
	protected void setup(final NerdSceneState p_state) {
		super.setup(p_state);
	}

	@Override
	public void shutdownSocket() {
		super.shutdownSocket();
	}

	@Override
	public void startSocket(final NerdSocket p_socket) {
		super.startSocket(p_socket);
	}

	@Override
	public void touchEnded() {
		super.touchEnded();
	}

	@Override
	public void touchMoved() {
		super.touchMoved();
	}

	@Override
	public void touchStarted() {
		super.touchStarted();
	}
	// endregion

}
