package com.brahvim.nerd.framework.lights;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import processing.core.PVector;

public class NerdSpotLightQueue implements NerdLightSlotEntry {

	// region Inner classes.
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

	public static class NerdSpotLightDistanceComparator implements Comparator<NerdSpotLight> {

		// region Fields.
		public static final Comparator<NerdSpotLight> DEFAULT_EQUALS_CASE_COMPARATOR = (a, b) -> 0;

		public Comparator<NerdSpotLight> equalsCaseComparator = NerdSpotLightDistanceComparator.DEFAULT_EQUALS_CASE_COMPARATOR;

		private float centerVecMagSq;
		// endregion

		public NerdSpotLightDistanceComparator(final PVector p_center) {
			this.centerVecMagSq = p_center.magSq();
		}

		public NerdSpotLightDistanceComparator(
				final PVector p_center,
				final Comparator<NerdSpotLight> p_equalsCaseComparator) {
			this(p_center);
			this.equalsCaseComparator = p_equalsCaseComparator;
		}

		public void setCenter(final PVector p_center) {
			this.centerVecMagSq = p_center.magSq();
		}

		@Override
		public int compare(final NerdSpotLight p_1, final NerdSpotLight p_2) {
			// Distances from the center:
			// final float d1 = p_1.POSITION.dist(this.CENTER);
			// final float d2 = p_2.POSITION.dist(this.CENTER);

			final float d1 = p_1.POSITION.magSq() - this.centerVecMagSq;
			final float d2 = p_2.POSITION.magSq() - this.centerVecMagSq;

			if (d1 == d2)
				return this.equalsCaseComparator.compare(p_1, p_2);

			if (d1 < d2)
				return -1; // `p_1` is closer.
			else
				return 1; // `p_2` is closer!
		}

	}

	public static class NerdSpotLightDirectionComparator implements Comparator<NerdSpotLight> {

		// region Fields.
		public static final Comparator<NerdSpotLight> DEFAULT_EQUALS_CASE_COMPARATOR = (a, b) -> 0;

		public Comparator<NerdSpotLight> equalsCaseComparator = NerdSpotLightDirectionComparator.DEFAULT_EQUALS_CASE_COMPARATOR;

		private float dirVecComponentSum;
		// endregion

		public NerdSpotLightDirectionComparator(final PVector p_center) {
			this.setDirection(p_center);
		}

		public NerdSpotLightDirectionComparator(
				final PVector p_center,
				final Comparator<NerdSpotLight> p_equalsCaseComparator) {
			this.setDirection(p_center);
			this.equalsCaseComparator = p_equalsCaseComparator;
		}

		public void setDirection(final PVector p_direction) {
			this.dirVecComponentSum = p_direction.x + p_direction.y + p_direction.z;
		}

		@Override
		public int compare(final NerdSpotLight p_1, final NerdSpotLight p_2) {
			// Dot against the origin to get a simpler number to compare against.
			// We'll do this to the `this::DIRECTION` vector we have already, too!:
			// final float d1 = p_1.DIRECTION.dot(0, 0, 0);
			// final float d2 = p_2.DIRECTION.dot(0, 0, 0);

			// ...Actually, actually! Screw that! We could instead just compare
			// the sum of the differences of the components of each vector!

			final float d1 = (p_1.DIRECTION.x + p_1.DIRECTION.y + p_1.DIRECTION.z) - this.dirVecComponentSum;
			final float d2 = (p_2.DIRECTION.x + p_2.DIRECTION.y + p_2.DIRECTION.z) - this.dirVecComponentSum;

			if (d1 == d2)
				return this.equalsCaseComparator.compare(p_1, p_2);

			if (d1 < d2)
				return -1; // `p_1` is closer.
			else
				return 1; // `p_2` is closer!
		}

	}
	// endregion

	private final List<NerdSpotLight> QUEUE = new ArrayList<>(2);

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
	/** @return The light that gets to the top upon sorting. */
	public NerdSpotLight sort() {
		if (this.QUEUE.isEmpty())
			throw new IndexOutOfBoundsException("This `" +
					this.getClass().getSimpleName() + "` has no lights!");

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
