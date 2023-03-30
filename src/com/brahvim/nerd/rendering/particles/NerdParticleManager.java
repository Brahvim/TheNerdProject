package com.brahvim.nerd.rendering.particles;

import java.util.function.Consumer;

public abstract class NerdParticleManager {

	// region Fields.
	private int count;
	private Consumer<NerdParticleManager> enterCallback, drawCallback, endCallback;

	public NerdParticleManager() {
	}
	// endregion

	public NerdParticleManager onEnd(final Consumer<NerdParticleManager> p_endCallback) {
		this.endCallback = p_endCallback;
		return this;
	}

	public NerdParticleManager onEmit(final Consumer<NerdParticleManager> p_emitCallback) {
		this.enterCallback = p_emitCallback;
		return this;
	}

	public NerdParticleManager onDraw(final Consumer<NerdParticleManager> p_drawCallback) {
		this.drawCallback = p_drawCallback;
		return this;
	}

	public void enter() {
		this.count++;
		this.enterCallback.accept(this);
	}

	public void enter(int p_howMany) {
		this.count += p_howMany;
		for (; p_howMany != 0; p_howMany--)
			this.enterCallback.accept(this);
	}

	public void draw() {
		this.drawCallback.accept(this);
	}

	public void end() {
		this.endCallback.accept(this);
	}

}
