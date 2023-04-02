package com.brahvim.nerd_tests;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.function.Predicate;

import com.brahvim.nerd.math.collision.CollisionAlgorithms;
import com.brahvim.nerd.openal.al_buffers.AlBuffer;
import com.brahvim.nerd.papplet_wrapper.Sketch;
import com.brahvim.nerd.scene_api.NerdScene;

import processing.core.PConstants;
import processing.core.PShape;
import processing.core.PVector;

public class CubeManager {

	// region Fields.
	public int CUBES_PER_CLICK = 15;
	public int CUBES_ADDED_EVERY_FRAME = 2;

	private final ArrayList<AnimatedCube> CUBES_TO_REMOVE = new ArrayList<>();
	private final ArrayList<AnimatedCube> CUBES = new ArrayList<>();
	private final HashSet<AlBuffer<?>> popAudios = new HashSet<>();
	private final NerdScene SCENE;
	private final Sketch SKETCH;

	private int cubesToAdd, cubesToRemove;
	private PShape CUBE_SHAPE;
	// endregion

	// region Constructors.
	public CubeManager(final NerdScene p_scene) {
		this.SCENE = Objects.requireNonNull(p_scene);
		this.SKETCH = this.SCENE.SKETCH;

		this.CUBE_SHAPE = this.SKETCH.createShape(PConstants.BOX, 1);
		this.CUBE_SHAPE.setStrokeWeight(0.28f);
	}

	public CubeManager(final NerdScene p_scene, final PShape p_cubeShape) {
		this.SCENE = Objects.requireNonNull(p_scene);
		this.SKETCH = this.SCENE.SKETCH;
		this.CUBE_SHAPE = p_cubeShape;
	}
	// endregion

	public void draw() {
		if (this.cubesToAdd != 0)
			for (int i = 0; i != this.CUBES_ADDED_EVERY_FRAME; i++) {
				if (this.cubesToAdd == 0)
					break;

				final AlBuffer<?> randomPop = this.SCENE.ASSETS.get(
						"Pop" + (int) SKETCH.random(1, 4)).getData();
				this.CUBES.add(new AnimatedCube(this.SCENE).plopIn(randomPop));
			}

		for (int i = 0; i != this.cubesToRemove; i++) {
			this.CUBES.get(this.CUBES.size() - i + 1).plopOut();
		}

		for (int i = this.CUBES.size() - 1; i != -1; i--) {
			final AnimatedCube p = this.CUBES.get(i);
			final PVector pos = p.getPos();

			if (!CollisionAlgorithms.ptRect(
					pos.x, pos.y,
					-5000, -5000,
					5000, 5000))
				this.CUBES_TO_REMOVE.add(p);
		}

		this.CUBES.removeIf((o) -> this.CUBES_TO_REMOVE.contains(o));

		for (AnimatedCube cube : this.CUBES)
			cube.draw(this.CUBE_SHAPE);
	}

	public void removeAll() {
		this.CUBES_TO_REMOVE.addAll(this.CUBES);
	}

	public void setShape(final PShape p_cubeShape) {
		this.CUBE_SHAPE = Objects.requireNonNull(p_cubeShape);
	}

}