package com.brahvim.nerd.framework.lights;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import processing.core.PVector;

public class NerdSpotLightQueue implements NerdLightSlotEntry {

	// region Inner classes.
	public static class NerdSpotLight {

		public float angle, concentration = 4000;

		private PVector
		/*   */ color = new PVector(),
				position = new PVector(),
				direction = new PVector();

		private final PVector PDIR = new PVector();

		// region Color and position getters and setters.
		public PVector getColor() {
			return this.color;
		}

		public PVector getPosition() {
			return this.position;
		}

		public PVector setColor(final PVector p_color) {
			final PVector toRet = this.color;
			this.color = Objects.requireNonNull(p_color);
			return toRet;
		}

		public PVector setPosition(final PVector p_position) {
			final PVector toRet = this.position;
			this.position = Objects.requireNonNull(p_position);
			return toRet;
		}
		// endregion

		public PVector getDirection() {
			return this.direction;
		}

		public PVector setDirection(final PVector p_direction) {
			final var toRet = this.direction;
			this.direction = Objects.requireNonNull(p_direction);
			return toRet;
		}

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
			this.setCenter(p_center);
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

			final float d1 = p_1.position.magSq() - this.centerVecMagSq;
			final float d2 = p_2.position.magSq() - this.centerVecMagSq;

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

			// TODO: Normalize deez vectorz. This is not the very best assumption haha!
			final float d1 = (p_1.direction.x + p_1.direction.y + p_1.direction.z) - this.dirVecComponentSum;
			final float d2 = (p_2.direction.x + p_2.direction.y + p_2.direction.z) - this.dirVecComponentSum;

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
