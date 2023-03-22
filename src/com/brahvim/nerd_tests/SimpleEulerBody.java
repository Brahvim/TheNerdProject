package com.brahvim.nerd_tests;

import processing.core.PVector;

public class SimpleEulerBody {
	protected float frict = 1, rotFrict = 1;
	protected PVector pos = new PVector(), vel = new PVector(), acc = new PVector();
	protected PVector rot = new PVector(), rotVel = new PVector(), rotAcc = new PVector();

	public void integrate() {
		this.vel.add(this.acc);
		this.pos.add(PVector.mult(this.vel, this.frict));

		this.rotVel.add(this.rotAcc);
		this.rot.add(PVector.mult(this.rotVel, this.rotFrict));
	}

}
