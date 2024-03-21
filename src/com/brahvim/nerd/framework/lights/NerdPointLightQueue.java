package com.brahvim.nerd.framework.lights;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import processing.core.PVector;

public class NerdPointLightQueue implements NerdLightSlotEntry {

	// region Inner classes.
	public static class NerdPointLight implements NerdLightSlotEntry {

		private PVector
		/*   */ position = new PVector(),
				color = new PVector();

		public NerdPointLight() {
		}

		public NerdPointLight(final PVector p_color, final PVector p_position) {
			this.setColor(p_color);
			this.setPosition(p_position);
		}

		public NerdPointLight(
				final float p_colorX, final float p_colorY, final float p_colorZ,
				final float p_posX, final float p_posY, final float p_posZ) {
			this.color.set(p_colorX, p_colorY, p_colorZ);
			this.position.set(p_posX, p_posY, p_posZ);
		}

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

	}

	public static class NerdPointLightDistanceComparator implements Comparator<NerdPointLight> {

		// region Fields.
		public static final Comparator<NerdPointLight> DEFAULT_EQUALS_CASE_COMPARATOR = (a, b) -> 0;

		public Comparator<NerdPointLight> equalsCaseComparator = NerdPointLightDistanceComparator.DEFAULT_EQUALS_CASE_COMPARATOR;

		private float centerVecMagSq;
		// endregion

		public NerdPointLightDistanceComparator(final PVector p_center) {
			this.centerVecMagSq = p_center.magSq();
		}

		public NerdPointLightDistanceComparator(
				final PVector p_center,
				final Comparator<NerdPointLight> p_equalsCaseComparator) {
			this(p_center);
			this.equalsCaseComparator = p_equalsCaseComparator;
		}

		public void setCenter(final PVector p_center) {
			this.centerVecMagSq = p_center.magSq();
		}

		@Override
		public int compare(final NerdPointLight p_1, final NerdPointLight p_2) {
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
	// endregion

	private final List<NerdPointLight> QUEUE = new ArrayList<>(2);

	private Comparator<NerdPointLight> sortingFunction;

	// region Light management.
	public int numLights() {
		return this.QUEUE.size();
	}

	public void addLight(final NerdPointLight p_spotLight) {
		this.QUEUE.add(p_spotLight);
	}

	public void removeLight(final NerdPointLight p_spotLight) {
		this.QUEUE.remove(p_spotLight);
	}
	// endregion

	// region Sorting function management.
	/** @return The light that gets to the top upon sorting. */
	public NerdPointLight sort() {
		if (this.QUEUE.isEmpty())
			throw new IndexOutOfBoundsException("This `" +
					this.getClass().getSimpleName() + "` has no lights!");

		this.QUEUE.sort(this.sortingFunction);
		return this.QUEUE.get(0);
	}

	public Comparator<NerdPointLight> getSortingFunction() {
		return this.sortingFunction;
	}

	public void setSortingFunction(final Comparator<NerdPointLight> p_sortingFunction) {
		this.sortingFunction = p_sortingFunction;
	}
	// endregion

}
