package com.brahvim.nerd_tests;

import java.util.ArrayList;
import java.util.Objects;

import com.brahvim.nerd.math.collision.CollisionAlgorithms;
import com.brahvim.nerd.openal.al_buffers.AlBuffer;
import com.brahvim.nerd.papplet_wrapper.Sketch;
import com.brahvim.nerd.scene_api.NerdScene;

import processing.core.PConstants;
import processing.core.PShape;
import processing.core.PVector;

public class CubeManager {

	// region Fields.
	public int CUBES_PER_CLICK = 7;
	public int CUBES_ADDED_EVERY_FRAME = 2;

	private final ArrayList<AnimatedCube> CUBES = new ArrayList<>();
	private final NerdScene SCENE;
	private final Sketch SKETCH;

	@SuppressWarnings("unused")
	private int cubesToAdd, cubesToRemove;
	private AlBuffer<?>[] popAudios;
	private PShape CUBE_SHAPE;
	// endregion

	// region Constructors.
	public CubeManager(final NerdScene p_scene) {
		this.SCENE = Objects.requireNonNull(p_scene);
		SKETCH = this.SCENE.SKETCH;

		this.CUBE_SHAPE = SKETCH.createShape(PConstants.BOX, 1);
		this.CUBE_SHAPE.setStrokeWeight(0.28f);
	}

	public CubeManager(final NerdScene p_scene, final PShape p_cubeShape) {
		this.SCENE = Objects.requireNonNull(p_scene);
		SKETCH = this.SCENE.SKETCH;
		this.CUBE_SHAPE = p_cubeShape;
	}

	public CubeManager(final NerdScene p_scene, final AlBuffer<?>[] p_buffers) {
		this.SCENE = Objects.requireNonNull(p_scene);
		SKETCH = this.SCENE.SKETCH;
		this.popAudios = p_buffers;

		this.CUBE_SHAPE = SKETCH.createShape(PConstants.BOX, 1);
		this.CUBE_SHAPE.setStrokeWeight(0.28f);
	}

	public CubeManager(final NerdScene p_scene,
			final PShape p_cubeShape, final AlBuffer<?>[] p_buffers) {
		this.SCENE = Objects.requireNonNull(p_scene);
		SKETCH = this.SCENE.SKETCH;
		this.CUBE_SHAPE = p_cubeShape;
		this.popAudios = p_buffers;
	}
	// endregion

	public void draw() {
		if (this.cubesToAdd != 0)
			for (int i = 0; i != this.CUBES_ADDED_EVERY_FRAME; i++) {
				if (--this.cubesToAdd == 0)
					break;

				if (this.popAudios == null)
					this.CUBES.add(new AnimatedCube(this.SCENE).plopIn(null));
				else {
					final AlBuffer<?> randomPop = this.popAudios[(int) SKETCH.random(this.popAudios.length)];
					this.CUBES.add(new AnimatedCube(this.SCENE).plopIn(randomPop));
				}
			}

		for (int i = this.CUBES.size() - 1; i != -1; i--) {
			final AnimatedCube cube = this.CUBES.get(i);
			final PVector pos = cube.getPos();

			if (!CollisionAlgorithms.ptRect(
					pos.x, pos.y,
					-5000, -5000,
					5000, 5000))
				this.CUBES.remove(i); // `this.cubesToRemove++;`
		}

		// Used to let the cubes 'plop-out', though unused right now!
		// for (int i = this.cubesToRemove; i != 0; i--) {
		// final AnimatedCube cube = this.CUBES.get(i);
		// cube.plopOut(() -> this.CUBES.remove(cube));
		// }

		for (final AnimatedCube c : this.CUBES)
			c.draw(this.CUBE_SHAPE);
	}

	public void emitCubes(final int p_howMany) {
		this.cubesToAdd += p_howMany;
	}

	public void removeAll() {
		this.CUBES.clear();
		// this.cubesToRemove = this.CUBES.size() - 1; // I dunno how to do this.
	}

	public void setShape(final PShape p_cubeShape) {
		this.CUBE_SHAPE = Objects.requireNonNull(p_cubeShape);
	}

}