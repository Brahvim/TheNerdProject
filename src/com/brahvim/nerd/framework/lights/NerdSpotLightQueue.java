package com.brahvim.nerd.framework.lights;

import java.util.ArrayList;
import java.util.Comparator;

import processing.core.PVector;

public class NerdSpotLightQueue implements NerdLightSlotEntry {

	public static class NerdSpotLight {

		public final PVector
		/*   */ COLOR = new PVector(),
				POSITION = new PVector(),
				DIRECTION = new PVector();

		public float angle, concentration = 4000;

		private final PVector PDIR = new PVector();

		public PVector getPreviousFrameDirection() {
			return this.PDIR;
		}

	}

	private final ArrayList<NerdSpotLight> QUEUE = new ArrayList<>(2);
	private Comparator<NerdSpotLight> sortingFunction;

	// region Light management.
	public int numLights() {
		return this.QUEUE.size();
	}

	public void addLight(final NerdSpotLight p_spotLight) {
		this.QUEUE.add(p_spotLight);
	}

	public void removeLight(final NerdSpotLight p_spotLight) {
		this.QUEUE.remove(p_spotLight);
	}
	// endregion

	// region Sorting function management.
	public NerdSpotLight sort() {
		if (this.QUEUE.isEmpty())
			throw new IndexOutOfBoundsException("This `" + this.getClass().getSimpleName() + "` has no lights!");

		this.QUEUE.sort(this.sortingFunction);
		return this.QUEUE.get(0);
	}

	public Comparator<NerdSpotLight> getSortingFunction() {
		return this.sortingFunction;
	}

	public void setSortingFunction(final Comparator<NerdSpotLight> p_sortingFunction) {
		this.sortingFunction = p_sortingFunction;
	}
	// endregion

}
