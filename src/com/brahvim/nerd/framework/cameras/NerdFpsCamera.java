package com.brahvim.nerd.framework.cameras;

import com.brahvim.nerd.processing_wrapper.NerdSketch;

public class NerdFpsCamera extends NerdFlyCamera {
	private float height;

	public NerdFpsCamera(final NerdSketch p_sketch) {
		super(p_sketch);
	}

	@Override
	public void applyMatrix() {
		super.pos.y = this.height;
		super.applyMatrix();
	}

	@Override
	public void moveY(final float p_velY) {
		this.height += p_velY;
	}

	public float getHeight() {
		return this.height;
	}

	public void setHeight(final float p_height) {
		this.height = p_height;
	}

}
