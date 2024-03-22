package com.brahvim.nerd.framework;

import java.util.Objects;

import com.brahvim.nerd.framework.colors.NerdNoAlphaColor;
import com.brahvim.nerd.framework.colors.rgb.NerdSplitRgbColor;

public class NerdMaterial {

	protected float shininess;
	protected NerdNoAlphaColor
	/*   */ ambience = new NerdSplitRgbColor(),
			emission = new NerdSplitRgbColor(),
			specular = new NerdSplitRgbColor();

	// region Constructors.
	public NerdMaterial() {
	}

	public NerdMaterial(final float p_shininess) {
		this.setShininess(p_shininess);
	}

	public NerdMaterial(
			final float p_shininess,
			final NerdNoAlphaColor p_ambience,
			final NerdNoAlphaColor p_emission,
			final NerdNoAlphaColor p_specular) {
		this.ambience = p_ambience;
		this.emission = p_emission;
		this.specular = p_specular;
		this.shininess = p_shininess;
	}

	public NerdMaterial(
			final NerdNoAlphaColor p_ambience,
			final NerdNoAlphaColor p_specular) {
		this.ambience = p_ambience;
		this.specular = p_specular;
	}
	// endregion

	// region Getters.
	public float getShininess() {
		return this.shininess;
	}

	public NerdNoAlphaColor getAmbience() {
		return this.ambience;
	}

	public NerdNoAlphaColor getEmission() {
		return this.emission;
	}

	public NerdNoAlphaColor getSpecular() {
		return this.specular;
	}
	// endregion

	// region Setters.
	public NerdMaterial setShininess(final float p_shininess) {
		// I think there's a limit to this, but anyway.
		this.shininess = p_shininess;
		return this;
	}

	public NerdMaterial setAmbience(final NerdNoAlphaColor p_ambience) {
		this.ambience = Objects.requireNonNull(p_ambience);
		return this;
	}

	public NerdMaterial setEmission(final NerdNoAlphaColor p_emission) {
		this.emission = Objects.requireNonNull(p_emission);
		return this;
	}

	public NerdMaterial setSpecular(final NerdNoAlphaColor p_specular) {
		this.specular = Objects.requireNonNull(p_specular);
		return this;
	}
	// endregion

}
