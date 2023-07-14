package com.brahvim.nerd.framework.scene_api;

import java.util.HashSet;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.brahvim.nerd.framework.NerdTriConsumer;
import com.brahvim.nerd.framework.ecs.NerdEcsComponent;
import com.brahvim.nerd.framework.ecs.NerdEcsModule;
import com.brahvim.nerd.framework.ecs.NerdEcsSystem;
import com.brahvim.nerd.processing_wrapper.NerdSketch;

public class NerdBridgedEcsModule extends NerdEcsModule {

	public NerdBridgedEcsModule(final NerdSketch p_sketch) {
		super(p_sketch);
	}

	// region Custom methods.
	protected NerdBridgedEcsModule clearAllData() {
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
	protected void sceneChanged() {
		super.sceneChanged();
	}

	@Override
	protected void setup(final NerdSceneState p_state) {
		super.setup(p_state);
	}
	// endregion

}
