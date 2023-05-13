package com.brahvim.nerd_demos.scenes.scene3;

import com.brahvim.nerd.papplet_wrapper.NerdSketch;

import processing.core.PVector;

public class TestEulerBody {

	// region Fields.
	protected final NerdSketch SKETCH;
	protected float frict = 1, rotFrict = 1, dtMult = 0.1f;
	protected PVector pos = new PVector(), vel = new PVector(), acc = new PVector();
	protected PVector rot = new PVector(), rotVel = new PVector(), rotAcc = new PVector();
	// endregion

	public TestEulerBody(final NerdSketch p_sketch) {
		this.SKETCH = p_sketch;
	}

	public void integrate() {
		final float deltaTime = SKETCH.frameTime * this.dtMult;

		this.vel.add(this.acc);
		this.rotVel.add(this.rotAcc);

		this.pos.add(PVector.mult(this.vel, this.frict + deltaTime));
		this.rot.add(PVector.mult(this.rotVel, this.rotFrict + deltaTime));
	}

}
