package com.brahvim.nerd.rendering.overtly_simple_physics;

import com.brahvim.nerd.papplet_wrapper.Sketch;

import processing.core.PVector;

public class NerdEulerBody {

	// region Fields.
	protected final Sketch SKETCH;
	protected float frict = 1, rotFrict = 1, dtMult = 0.1f;
	protected PVector pos = new PVector(), vel = new PVector(), acc = new PVector();
	protected PVector rot = new PVector(), rotVel = new PVector(), rotAcc = new PVector();
	// endregion

	public NerdEulerBody(Sketch p_sketch) {
		this.SKETCH = p_sketch;
	}

	public void integrate() {
		float deltaTime = this.SKETCH.frameTime * this.dtMult;

		this.vel.add(this.acc);
		this.pos.add(PVector.mult(this.vel, this.frict + deltaTime));

		this.rotVel.add(this.rotAcc);
		this.rot.add(PVector.mult(this.rotVel, this.rotFrict + deltaTime));
	}

}
