package com.brahvim.nerd.framework.colors.hsb;

import com.brahvim.nerd.framework.colors.NerdProcessingColor;
import com.brahvim.nerd.framework.colors.NerdProcessingColorSpace;

public interface NerdHsbColor extends NerdProcessingColor {

	// region Default methods.
	@Override
	public default float getParam1() {
		return this.getHue();
	}

	@Override
	public default float getParam2() {
		return this.getSaturation();
	}

	@Override
	public default float getParam3() {
		return this.getBrightness();
	}

	@Override
	default NerdProcessingColorSpace getColorSpace() {
		return NerdProcessingColorSpace.HSB;
	}

	@Override
	public default NerdHsbColor setParam1(final float p_value) {
		this.setHue(p_value);
		return this;
	}

	@Override
	public default NerdHsbColor setParam2(final float p_value) {
		this.setSaturation(p_value);
		return this;
	}

	@Override
	public default NerdHsbColor setParam3(final float p_value) {
		this.setBrightness(p_value);
		return this;
	}
	// endregion

	public float getHue();

	public float getSaturation();

	public float getBrightness();

	public NerdHsbColor setHue(final float p_value);

	public NerdHsbColor setSaturation(final float p_value);

	public NerdHsbColor setBrightness(final float p_value);

}
