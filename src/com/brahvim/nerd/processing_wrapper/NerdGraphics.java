package com.brahvim.nerd.processing_wrapper;

import java.awt.Image;
import java.util.Objects;

import com.brahvim.nerd.framework.cameras.NerdAbstractCamera;
import com.brahvim.nerd.framework.cameras.NerdBasicCamera;
import com.brahvim.nerd.framework.cameras.NerdFlyCamera;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PMatrix;
import processing.core.PMatrix2D;
import processing.core.PMatrix3D;
import processing.core.PShape;
import processing.core.PStyle;
import processing.core.PSurface;
import processing.core.PVector;
import processing.opengl.PGL;
import processing.opengl.PGraphics3D;
import processing.opengl.PShader;

public class NerdGraphics {

	private final NerdSketch SKETCH;

	private final PGraphics graphics;

	public NerdGraphics(final NerdSketch p_sketch, final PGraphics p_graphics) {
		this.SKETCH = p_sketch;
		this.graphics = p_graphics;
	};

	// region Rendering utilities!
	public final PGraphics getUnderlyingBuffer() {
		return this.graphics;
	}

	// public final PGraphics setUnderlyingBuffer(final PGraphics p_graphics) {
	// return this.graphics = p_graphics;
	// }

	// region From `PGraphics`.
	// region Shapes.
	// region `drawShape()` overloads.
	public void drawShape(final float p_x, final float p_y, final float p_z, final int p_shapeType,
			final Runnable p_shapingFxn) {
		this.graphics.pushMatrix();
		this.translate(p_x, p_y, p_z);
		SKETCH.beginShape(p_shapeType);
		p_shapingFxn.run();
		SKETCH.endShape(PConstants.CLOSE);
		this.graphics.popMatrix();
	}

	public void drawShape(final float p_x, final float p_y, final int p_shapeType, final Runnable p_shapingFxn) {
		this.graphics.pushMatrix();
		this.translate(p_x, p_y);
		SKETCH.beginShape(p_shapeType);
		p_shapingFxn.run();
		SKETCH.endShape(PConstants.CLOSE);
		this.graphics.popMatrix();
	}

	public void drawShape(final PVector p_pos, final int p_shapeType, final Runnable p_shapingFxn) {
		this.graphics.pushMatrix();
		this.translate(p_pos);
		SKETCH.beginShape(p_shapeType);
		p_shapingFxn.run();
		SKETCH.endShape(PConstants.CLOSE);
		this.graphics.popMatrix();
	}

	public void drawShape(final int p_shapeType, final Runnable p_shapingFxn) {
		SKETCH.beginShape(p_shapeType);
		p_shapingFxn.run();
		SKETCH.endShape(PConstants.CLOSE);
	}
	// endregion

	// region `drawOpenShape()` overloads.
	public void drawOpenShape(final float p_x, final float p_y, final float p_z, final int p_shapeType,
			final Runnable p_shapingFxn) {
		this.graphics.pushMatrix();
		this.translate(p_x, p_y, p_z);
		SKETCH.beginShape(p_shapeType);
		p_shapingFxn.run();
		SKETCH.endShape();
		this.graphics.popMatrix();
	}

	public void drawOpenShape(final float p_x, final float p_y, final int p_shapeType, final Runnable p_shapingFxn) {
		this.graphics.pushMatrix();
		this.translate(p_x, p_y);
		SKETCH.beginShape(p_shapeType);
		p_shapingFxn.run();
		SKETCH.endShape();
		this.graphics.popMatrix();
	}

	public void drawOpenShape(final PVector p_pos, final int p_shapeType, final Runnable p_shapingFxn) {
		this.graphics.pushMatrix();
		this.translate(p_pos);
		SKETCH.beginShape(p_shapeType);
		p_shapingFxn.run();
		SKETCH.endShape();
		this.graphics.popMatrix();
	}

	public void drawOpenShape(final int p_shapeType, final Runnable p_shapingFxn) {
		SKETCH.beginShape(p_shapeType);
		p_shapingFxn.run();
		SKETCH.endShape();
	}
	// endregion

	public void drawContour(final Runnable p_countouringFxn) {
		this.graphics.beginContour();
		p_countouringFxn.run();
		this.graphics.endContour();
	}

	// region `PVector` overloads.
	// region 3D shapes.
	// region `box()` overloads.
	public void box(final float p_x, final float p_y, final float p_z,
			final float p_width, final float p_height, final float p_depth) {
		this.graphics.pushMatrix();
		this.graphics.translate(p_x, p_y, p_z);
		this.graphics.box(p_width, p_height, p_depth);
		this.graphics.popMatrix();
	}

	public void box(final PVector p_pos, final float p_width, final float p_height, final float p_depth) {
		this.graphics.pushMatrix();
		this.translate(p_pos);
		this.graphics.box(p_width, p_height, p_depth);
		this.graphics.popMatrix();
	}

	public void box(final float p_x, final float p_y, final float p_z, final PVector p_dimensions) {
		this.graphics.pushMatrix();
		this.graphics.translate(p_x, p_y, p_z);
		this.graphics.box(p_dimensions.x, p_dimensions.y, p_dimensions.z);
		this.graphics.popMatrix();
	}

	public void box(final float p_x, final float p_y, final float p_z, final float p_size) {
		this.graphics.pushMatrix();
		this.graphics.translate(p_x, p_y, p_z);
		this.graphics.box(p_size);
		this.graphics.popMatrix();
	}

	public void box(final PVector p_pos, final PVector p_dimensions) {
		this.graphics.pushMatrix();
		this.translate(p_pos);
		this.graphics.box(p_dimensions.x, p_dimensions.y, p_dimensions.z);
		this.graphics.popMatrix();
	}

	public void box(final PVector p_pos, final float p_size) {
		this.graphics.pushMatrix();
		this.translate(p_pos);
		this.graphics.box(p_size);
		this.graphics.popMatrix();
	}
	// endregion

	// region `sphere()` overloads (just a copy of the `box()` ones, hehehe.).
	public void sphere(final float p_x, final float p_y, final float p_z,
			final float p_width, final float p_height, final float p_depth) {
		this.graphics.pushMatrix();
		this.graphics.translate(p_x, p_y, p_z);
		this.graphics.scale(p_width, p_height, p_depth);
		this.graphics.sphere(1);
		this.graphics.popMatrix();
	}

	public void sphere(final PVector p_pos, final float p_width, final float p_height, final float p_depth) {
		this.graphics.pushMatrix();
		this.translate(p_pos);
		this.graphics.scale(p_width, p_height, p_depth);
		this.graphics.sphere(1);
		this.graphics.popMatrix();
	}

	public void sphere(final float p_x, final float p_y, final float p_z, final PVector p_dimensions) {
		this.graphics.pushMatrix();
		this.graphics.translate(p_x, p_y, p_z);
		this.graphics.scale(p_dimensions.x, p_dimensions.y, p_dimensions.z);
		this.graphics.sphere(1);
		this.graphics.popMatrix();
	}

	public void sphere(final float p_x, final float p_y, final float p_z, final float p_size) {
		this.graphics.pushMatrix();
		this.graphics.translate(p_x, p_y, p_z);
		this.graphics.sphere(p_size);
		this.graphics.popMatrix();
	}

	public void sphere(final PVector p_pos, final PVector p_dimensions) {
		this.graphics.pushMatrix();
		this.translate(p_pos);
		this.graphics.scale(p_dimensions.x, p_dimensions.y, p_dimensions.z);
		this.graphics.sphere(1);
		this.graphics.popMatrix();
	}

	public void sphere(final PVector p_pos, final float p_size) {
		this.graphics.pushMatrix();
		this.translate(p_pos);
		this.graphics.sphere(p_size);
		this.graphics.popMatrix();
	}
	// endregion
	// endregion

	public void arc(final PVector p_translation, final PVector p_size,
			final float p_startAngle, final float p_endAngle) {
		this.graphics.pushMatrix();
		this.translate(p_translation);
		this.graphics.arc(0, 0, p_size.x, p_size.y, p_startAngle, p_endAngle);
		this.graphics.popMatrix();
	}

	// Perhaps I should figure out the default arc mode and make the upper one call
	// this one?:
	public void arc(final PVector p_translation, final PVector p_size,
			final float p_startAngle, final float p_endAngle, final int p_mode) {
		this.graphics.pushMatrix();
		this.translate(p_translation);
		this.graphics.arc(0, 0, p_size.x, p_size.y, p_startAngle, p_endAngle, p_mode);
		this.graphics.popMatrix();
	}

	public void circle(final PVector p_pos, final float p_size) {
		this.graphics.pushMatrix();
		this.translate(p_pos);
		this.graphics.circle(0, 0, p_size);
		this.graphics.popMatrix();
	}

	// region E L L I P S E S.
	// ...For when you want to use an ellipse like a circle:
	public void ellipse(final float p_x, final float p_y, final PVector p_dimensions) {
		this.graphics.ellipse(p_x, p_y, p_dimensions.x, p_dimensions.y);
	}

	public void ellipse(final float p_x, final float p_y, final float p_z, final float p_width, final float p_height) {
		this.graphics.pushMatrix();
		this.translate(p_x, p_y, p_z);
		this.graphics.ellipse(0, 0, p_width, p_height);
		this.graphics.popMatrix();
	}

	public void ellipse(final float p_x, final float p_y, final float p_z, final PVector p_dimensions) {
		this.graphics.pushMatrix();
		this.translate(p_x, p_y, p_z);
		this.graphics.ellipse(0, 0, p_dimensions.x, p_dimensions.y);
		this.graphics.popMatrix();
	}

	public void ellipse(final PVector p_pos, final float p_size) {
		this.graphics.pushMatrix();
		this.translate(p_pos);
		this.graphics.ellipse(0, 0, p_size, p_size);
		this.graphics.popMatrix();
	}

	public void ellipse(final PVector p_pos, final float p_width, final float p_height) {
		this.graphics.pushMatrix();
		this.translate(p_pos);
		this.graphics.ellipse(0, 0, p_width, p_height);
		this.graphics.popMatrix();
	}

	public void ellipse(final PVector p_pos, final PVector p_dimensions) {
		this.graphics.pushMatrix();
		this.translate(p_pos);
		this.graphics.ellipse(0, 0, p_dimensions.x, p_dimensions.y);
		this.graphics.popMatrix();
	}
	// endregion

	public void line(final PVector p_from, final PVector p_to) {
		// `z`-coordinate of first and THEN the second point!:
		this.graphics.line(p_from.x, p_from.y, p_to.x, p_to.y, p_from.z, p_to.y);
	}

	public void lineInDir/* `lineInDirOfLength` */(
			final PVector p_start, final PVector p_dir, final float p_length) {
		// `z`-coordinate of first and THEN the second point!:
		this.graphics.line(p_start.x, p_start.y,
				p_start.x + p_dir.x * p_length,
				p_start.y + p_dir.y * p_length,
				// `z` stuff!:
				p_start.z, p_start.z + p_dir.z * p_length);
	}

	public void line2d(final PVector p_from, final PVector p_to) {
		this.graphics.line(p_from.x, p_from.y, p_to.x, p_to.y);
	}

	// region `radialLine*d()`!
	public void radialLine2d(final PVector p_from, final float p_angle) {
		this.graphics.line(p_from.x, p_from.y, PApplet.sin(p_angle), PApplet.cos(p_angle));
	}

	public void radialLine2d(final PVector p_from, final float p_x, final float p_y, final float p_length) {
		this.graphics.line(p_from.x, p_from.y,
				p_from.x + PApplet.sin(p_x) * p_length,
				p_from.y + PApplet.cos(p_y) * p_length);
	}

	public void radialLine2d(final PVector p_from, final float p_x, final float p_y,
			final float p_xLen, final float p_yLen) {
		this.graphics.line(p_from.x, p_from.y,
				p_from.x + PApplet.sin(p_x) * p_xLen,
				p_from.y + PApplet.cos(p_y) * p_yLen);
	}

	public void radialLine2d(final PVector p_from, final PVector p_trigVals, final float p_size) {
		this.line(p_from.x, p_from.y, p_trigVals.x, p_trigVals.y);
	}

	public void radialLine2d(final PVector p_from, final PVector p_values) {
		this.radialLine2d(p_from, p_values, p_values.z);
	}

	public void radialLine3d(final PVector p_from, final float p_theta, final float p_phi, final float p_length) {
		final float sineOfPitch = PApplet.sin(p_theta);
		this.graphics.line(p_from.x, p_from.y,
				p_from.x + sineOfPitch * PApplet.cos(p_phi) * p_length,
				p_from.x + sineOfPitch * PApplet.sin(p_phi) * p_length,
				p_from.z, p_from.x + PApplet.cos(p_theta) * p_length);
	}

	public void radialLine3d(final PVector p_from, final float p_theta, final float p_phi,
			final float p_xLen, final float p_yLen, final float p_zLen) {
		final float sineOfPitch = PApplet.sin(p_theta);
		this.graphics.line(p_from.x, p_from.y,
				p_from.x + sineOfPitch * PApplet.cos(p_phi) * p_xLen,
				p_from.x + sineOfPitch * PApplet.sin(p_phi) * p_yLen,
				p_from.z, p_from.x + PApplet.cos(p_theta) * p_zLen);
	}
	// endregion

	public void point(final PVector p_pos) {
		this.graphics.point(p_pos.x, p_pos.y, p_pos.z);
	}

	public void point2d(final PVector p_pos) {
		this.graphics.point(p_pos.x, p_pos.y, 0);
	}

	public void quad(final PVector p_first, final PVector p_second, final PVector p_third, final PVector p_fourth) {
		this.graphics.quad(
				p_first.x, p_first.y,
				p_second.x, p_second.y,
				p_third.x, p_third.y,
				p_fourth.x, p_first.y);
	}

	// region `rect()` overloads, ;)!
	public void rect(final float p_x, final float p_y, final float p_z, final float p_width, final float p_height) {
		this.graphics.pushMatrix();
		this.translate(p_x, p_y, p_z);
		this.graphics.rect(0, 0, p_width, p_height);
		this.graphics.popMatrix();
	}

	public void rect(final float p_x, final float p_y, final float p_z, final float p_width, final float p_height,
			final float p_radius) {
		this.graphics.pushMatrix();
		this.translate(p_x, p_y, p_z);
		this.graphics.rect(0, 0, p_width, p_height, p_radius);
		this.graphics.popMatrix();
	}

	public void rect(final float p_x, final float p_y, final float p_z,
			final float p_width, final float p_height,
			final float p_topLeftRadius, final float p_topRightRadius,
			final float p_bottomRightRadius, final float p_bottomLeftRadius) {
		this.graphics.pushMatrix();
		this.graphics.translate(p_x, p_y, p_z);
		this.graphics.rect(0, 0, p_width, p_height,
				p_topLeftRadius, p_topRightRadius,
				p_bottomRightRadius, p_bottomLeftRadius);
		this.graphics.popMatrix();
	}

	public void rect(final float p_x, final float p_y, final float p_z, final float p_width, final float p_height,
			final PVector p_topRadii, final PVector p_bottomRadii) {
		this.graphics.pushMatrix();
		this.graphics.translate(p_x, p_y, p_z);
		this.graphics.rect(0, 0, p_width, p_height,
				p_topRadii.x, p_topRadii.y,
				p_bottomRadii.x, p_bottomRadii.y);
		this.graphics.popMatrix();
	}

	public void rect(final float p_x, final float p_y, final PVector p_dimensions) {
		this.graphics.rect(p_x, p_y, p_dimensions.x, p_dimensions.y);
	}

	public void rect(final float p_x, final float p_y, final PVector p_dimensions, final float p_radius) {
		this.graphics.rect(p_x, p_y, p_dimensions.x, p_dimensions.y, p_radius);
	}

	public void rect(final float p_x, final float p_y, final float p_z, final PVector p_dimensions,
			final float p_topLeftRadius, final float p_topRightRadius,
			final float p_bottomRightRadius, final float p_bottomLeftRadius) {
		this.graphics.pushMatrix();
		this.graphics.translate(p_x, p_y, p_z);
		this.graphics.rect(0, 0,
				p_dimensions.x, p_dimensions.y,
				p_topLeftRadius, p_topRightRadius,
				p_bottomRightRadius, p_bottomLeftRadius);
		this.graphics.popMatrix();
	}

	public void rect(final float p_x, final float p_y, final float p_z, final PVector p_dimensions,
			final PVector p_topRadii, final PVector p_bottomRadii) {
		this.graphics.pushMatrix();
		this.graphics.translate(p_x, p_y, p_z);
		this.graphics.rect(0, 0,
				p_dimensions.x, p_dimensions.y,
				p_topRadii.x, p_topRadii.y,
				p_bottomRadii.x, p_bottomRadii.y);
		this.graphics.popMatrix();
	}

	public void rect(final PVector p_pos, final float p_width, final float p_height) {
		this.graphics.pushMatrix();
		this.translate(p_pos);
		this.graphics.rect(0, 0, p_width, p_height);
		this.graphics.popMatrix();
	}

	public void rect(final PVector p_pos, final float p_width, final float p_height, final float p_radius) {
		this.graphics.pushMatrix();
		this.translate(p_pos);
		this.graphics.rect(0, 0, p_width, p_height, p_radius);
		this.graphics.popMatrix();
	}

	public void rect(final PVector p_pos, final float p_width, final float p_height,
			final float p_topLeftRadius, final float p_topRightRadius,
			final float p_bottomRightRadius, final float p_bottomLeftRadius) {
		this.graphics.pushMatrix();
		this.translate(p_pos);
		this.graphics.rect(0, 0, p_width, p_height,
				p_topLeftRadius, p_topRightRadius,
				p_bottomRightRadius, p_bottomLeftRadius);
		this.graphics.popMatrix();
	}

	public void rect(final PVector p_pos, final float p_width, final float p_height,
			final PVector p_topRadii, final PVector p_bottomRadii) {
		this.graphics.pushMatrix();
		this.translate(p_pos);
		this.graphics.rect(0, 0, p_width, p_height,
				p_topRadii.x, p_topRadii.y,
				p_bottomRadii.x, p_bottomRadii.y);
		this.graphics.popMatrix();
	}

	public void rect(final PVector p_pos, final PVector p_dimensions) {
		this.graphics.pushMatrix();
		this.translate(p_pos);
		this.graphics.rect(0, 0, p_dimensions.x, p_dimensions.y);
		this.graphics.popMatrix();
	}

	public void rect(final PVector p_pos, final PVector p_dimensions, final float p_radius) {
		this.graphics.pushMatrix();
		this.translate(p_pos);
		this.graphics.rect(0, 0, p_dimensions.x, p_dimensions.y, p_radius);
		this.graphics.popMatrix();
	}

	public void rect(final PVector p_pos, final PVector p_dimensions,
			final float p_topLeftRadius, final float p_topRightRadius,
			final float p_bottomRightRadius, final float p_bottomLeftRadius) {
		this.graphics.pushMatrix();
		this.translate(p_pos);
		this.graphics.rect(0, 0, p_dimensions.x, p_dimensions.y,
				p_topLeftRadius, p_topRightRadius,
				p_bottomRightRadius, p_bottomLeftRadius);
		this.graphics.popMatrix();
	}

	public void rect(final PVector p_pos, final PVector p_dimensions,
			final PVector p_topRadii, final PVector p_bottomRadii) {
		this.graphics.pushMatrix();
		this.translate(p_pos);
		this.graphics.rect(0, 0, p_dimensions.x, p_dimensions.y,
				p_topRadii.x, p_topRadii.y,
				p_bottomRadii.x, p_bottomRadii.y);
		this.graphics.popMatrix();
	}
	// endregion

	public void square(final PVector p_pos, final float p_size) {
		this.graphics.pushMatrix();
		this.graphics.translate(p_pos.x, p_pos.y, p_pos.z);
		this.graphics.square(0, 0, p_size);
		this.graphics.popMatrix();
	}

	// region `triangle()` overload!
	// region ...Thoughts about the-uhh, `triangle()` overloads!
	// I wanted to go crazy, writing out (BY HAND!) versions like...
	// (shown here without the `final` keyword, of course!):
	// `triangle(PVector p_v1, float x2, float y2, float x3, float y3)`
	// `triangle(float x1, float y1, PVector p_v2, float x3, float y3)`
	// ...
	// ...AND EVEN:
	// `triangle(PVector p_v2, float x2, float y2, PVector v3)`.
	// ...Yeah. You get the point. I'm crazy ;)
	// Yes, I was going to use more generator code (JavaScript!) for this.
	// I might be crazy, but am also lazy! :joy:
	// endregion

	public void triangle(final PVector p_v1, final PVector p_v2, final PVector p_v3) {
		this.graphics.triangle(
				p_v1.x, p_v1.y,
				p_v2.x, p_v2.y,
				p_v3.x, p_v3.y);
	}
	// endregion
	// endregion
	// endregion

	/**
	 * Draws the {@code p_bgImage} as if it was a background. You may even choose to
	 * call one of the {@link PApplet#tint()} overloads before calling this!
	 */
	public void background(final PImage p_bgImage) {
		Objects.requireNonNull(p_bgImage);

		this.begin2d();
		this.graphics.image(p_bgImage,
				this.SKETCH.WINDOW.cx, this.SKETCH.WINDOW.cy,
				this.SKETCH.WINDOW.width, this.SKETCH.WINDOW.height);
		this.end2d();
	}

	// region Transformations!
	// "Hah! Gott'em with the name alignment!"
	public void translate(final PVector p_vec) {
		this.graphics.translate(p_vec.x, p_vec.y, p_vec.z);
	}

	public void scale(final PVector p_scaling) {
		this.graphics.scale(p_scaling.x, p_scaling.y, p_scaling.z);
	}

	public void rotate(final PVector p_rotVec) {
		this.graphics.rotateX(p_rotVec.x);
		this.graphics.rotateY(p_rotVec.y);
		this.graphics.rotateZ(p_rotVec.z);
	}

	public void rotate(final float p_x, final float p_y, final float p_z) {
		this.graphics.rotateX(p_x);
		this.graphics.rotateY(p_y);
		this.graphics.rotateZ(p_z);
	}
	// endregion

	// region `modelVec()` and `screenVec()`.
	public PVector modelVec() {
		return new PVector(
				// "I passed these `0`s in myself, yeah. Let's not rely on the JIT too much!"
				// - Me before re-thinking that.
				this.modelX(),
				this.modelY(),
				this.modelZ());
	}

	public PVector modelVec(final PVector p_vec) {
		return new PVector(
				this.graphics.modelX(p_vec.x, p_vec.y, p_vec.z),
				this.graphics.modelY(p_vec.x, p_vec.y, p_vec.z),
				this.graphics.modelZ(p_vec.x, p_vec.y, p_vec.z));
	}

	public PVector modelVec(final float p_x, final float p_y, final float p_z) {
		return new PVector(
				this.graphics.modelX(p_x, p_y, p_z),
				this.graphics.modelY(p_x, p_y, p_z),
				this.graphics.modelZ(p_x, p_y, p_z));
	}

	public PVector screenVec() {
		return new PVector(
				this.screenX(),
				this.screenY(),
				this.screenZ());
	}

	public PVector screenVec(final PVector p_vec) {
		return new PVector(
				this.screenX(p_vec.x, p_vec.y, p_vec.z),
				this.screenY(p_vec.x, p_vec.y, p_vec.z),
				this.screenZ(p_vec.x, p_vec.y, p_vec.z));
	}

	public PVector screenVec(final float p_x, final float p_y, final float p_z) {
		return new PVector(
				this.screenX(p_x, p_y, p_z),
				this.screenY(p_x, p_y, p_z),
				this.screenZ(p_x, p_y, p_z));
	}
	// endregion

	// region `modelX()`-`modelY()`-`modelZ()` `PVector` and no-parameter overloads.
	// region Parameterless overloads.
	public float modelX() {
		return this.graphics.modelX(0, 0, 0);
	}

	public float modelY() {
		return this.graphics.modelY(0, 0, 0);
	}

	public float modelZ() {
		return this.graphics.modelZ(0, 0, 0);
	}
	// endregion

	// region `p_vec`?
	// ...how about `p_modelMatInvMulter`? :rofl:!
	public float modelX(final PVector p_vec) {
		return this.graphics.modelX(p_vec.x, p_vec.y, p_vec.z);
	}

	public float modelY(final PVector p) {
		return this.graphics.modelY(p.x, p.y, p.z);
	}

	public float modelZ(final PVector p) {
		return this.graphics.modelZ(p.x, p.y, p.z);
	}
	// endregion
	// endregion

	// region `screenX()`-`screenY()`-`screenZ()`, `PVector`, plus no-arg overloads.
	// "Oh! And when the `z` is `-1`, you just add this and sub that. Optimization!"
	// - That ONE Mathematician.

	// region Parameterless overloads.
	public float screenX() {
		return this.graphics.screenX(0, 0, 0);
	}

	public float screenY() {
		return this.graphics.screenY(0, 0, 0);
	}

	public float screenZ() {
		return this.graphics.screenY(0, 0, 0);
	}
	// endregion

	// region `p_vec`!
	// The following two were going to disclude the `z` if it was `0`.
	// And later, I felt this was risky.
	// This two-`float` overload ain't in the docs, that scares me!

	// ...ACTUALLY,
	// https://github.com/SKETCHssing/SKETCHssing/blob/459853d0dcdf1e1648b1049d3fdbb4bf233fded8/core/src/SKETCHssing/opengl/PGraphicsOpenGL.java#L4611
	// ..."they rely on the JIT too!" (no, they don't optimize this at all. They
	// just put the `0` themselves, LOL.) :joy:

	public float screenX(final PVector p_vec) {
		return this.graphics.screenX(p_vec.x, p_vec.y, p_vec.z);

		// return p_vec.z == 0
		// ? this.GRAPHICS.screenX(p_vec.x, p_vec.y)
		// : this.GRAPHICS.screenX(p_vec.x, p_vec.y, p_vec.z);
	}

	public float screenY(final PVector p_vec) {
		return this.graphics.screenY(p_vec.x, p_vec.y, p_vec.z);

		// return p_vec.z == 0
		// ? this.GRAPHICS.screenY(p_vec.x, p_vec.y)
		// : this.GRAPHICS.screenY(p_vec.x, p_vec.y, p_vec.z);
	}

	public float screenZ(final PVector p_vec) {
		// Hmmm...
		// ..so `z` cannot be `0` here.
		// ..and `x` and `y` cannot be ignored!
		// "No room for optimization here!"
		return this.graphics.screenZ(p_vec.x, p_vec.y, p_vec.z);
	}
	// endregion
	// endregion

	// region Camera matrix configuration.
	public void camera(final NerdBasicCamera p_cam) {
		this.graphics.camera(
				p_cam.getPos().x, p_cam.getPos().y, p_cam.getPos().z,
				p_cam.getCenter().x, p_cam.getCenter().y, p_cam.getCenter().z,
				p_cam.getUp().x, p_cam.getUp().y, p_cam.getUp().z);
	}

	public void camera(final NerdFlyCamera p_cam) {
		this.graphics.camera(
				p_cam.getPos().x, p_cam.getPos().y, p_cam.getPos().z,

				p_cam.getPos().x + p_cam.front.x,
				p_cam.getPos().y + p_cam.front.y,
				p_cam.getPos().z + p_cam.front.z,

				p_cam.getUp().x, p_cam.getUp().y, p_cam.getUp().z);
	}

	public void camera(final PVector p_pos, final PVector p_center, final PVector p_up) {
		this.graphics.camera(
				p_pos.x, p_pos.y, p_pos.z,
				p_center.x, p_center.y, p_center.z,
				p_up.x, p_up.y, p_up.z);
	}
	// endregion

	// region Projection functions.
	public void perspective(final NerdAbstractCamera p_cam) {
		this.graphics.perspective(p_cam.fov, p_cam.aspect, p_cam.near, p_cam.far);
	}

	public void perspective(final float p_fov, final float p_near, final float p_far) {
		this.graphics.perspective(p_fov, this.SKETCH.WINDOW.scr, p_near, p_far);
	}

	public void ortho(final NerdAbstractCamera p_cam) {
		this.graphics.ortho(-this.SKETCH.WINDOW.cx, this.SKETCH.WINDOW.cx, -this.SKETCH.WINDOW.cy,
				this.SKETCH.WINDOW.cy, p_cam.near, p_cam.far);
	}

	public void ortho(final float p_near, final float p_far) {
		this.graphics.ortho(-this.SKETCH.WINDOW.cx, this.SKETCH.WINDOW.cx, -this.SKETCH.WINDOW.cy,
				this.SKETCH.WINDOW.cy, p_near, p_far);
	}

	/**
	 * Expands to {@code PApplet::ortho(-p_cx, p_cx, -p_cy, p_cy, p_near, p_far)}.
	 */
	public void ortho(final float p_cx, final float p_cy, final float p_near, final float p_far) {
		this.graphics.ortho(-p_cx, p_cx, -p_cy, p_cy, p_near, p_far);
	}
	// endregion

	// region Unprojection functions.
	// region 2D versions!
	public float worldX(final float p_x, final float p_y) {
		return this.worldVec(p_x, p_y, 0).x;
	}

	public float worldY(final float p_x, final float p_y) {
		return this.worldVec(p_x, p_y, 0).y;
	}

	public float worldZ(final float p_x, final float p_y) {
		return this.worldVec(p_x, p_y, 0).z;
	}
	// endregion

	// region 3D versions (should use!).
	public float worldX(final float p_x, final float p_y, final float p_z) {
		return this.worldVec(p_x, p_y, p_z).x;
	}

	public float worldY(final float p_x, final float p_y, final float p_z) {
		return this.worldVec(p_x, p_y, p_z).y;
	}

	public float worldZ(final float p_x, final float p_y, final float p_z) {
		return this.worldVec(p_x, p_y, p_z).z;
	}
	// endregion

	// region `worldVec()`!
	public PVector worldVec(final PVector p_vec) {
		return this.worldVec(p_vec.x, p_vec.y, p_vec.z);
	}

	public PVector worldVec(final float p_x, final float p_y) {
		return this.worldVec(p_x, p_y, 0);
	}

	public PVector worldVec(final float p_x, final float p_y, final float p_z) {
		final PVector toRet = new PVector();
		// Unproject:
		this.SKETCH.UNPROJECTOR.captureViewMatrix((PGraphics3D) this.graphics);
		this.SKETCH.UNPROJECTOR.gluUnProject(
				p_x, this.SKETCH.height - p_y,
				// `0.9f`: at the near clipping plane.
				// `0.9999f`: at the far clipping plane. (NO! Calculate EPSILON first! *Then-*)
				// 0.9f + map(mouseY, height, 0, 0, 0.1f),
				PApplet.map(p_z, this.SKETCH.getCamera().near, this.SKETCH.getCamera().far, 0, 1),
				toRet);

		return toRet;
	}
	// endregion

	// region Mouse!
	/**
	 * Caching this vector never works. Call this method everytime!~
	 * People recalculate things framely in computer graphics anyway! :joy:
	 */
	public PVector getMouseInWorld() {
		return this.getMouseInWorldFromFarPlane(this.SKETCH.getCamera().mouseZ);
	}

	public PVector getMouseInWorldFromFarPlane(final float p_distanceFromFarPlane) {
		return this.worldVec(this.SKETCH.INPUT.mouseX, this.SKETCH.INPUT.mouseY,
				this.SKETCH.getCamera().far - p_distanceFromFarPlane + this.SKETCH.getCamera().near);
	}

	public PVector getMouseInWorldAtZ(final float p_distanceFromCamera) {
		return this.worldVec(this.SKETCH.INPUT.mouseX, this.SKETCH.INPUT.mouseY, p_distanceFromCamera);
	}
	// endregion

	// region Touches.
	// /**
	// * Caching this vector never works. Call this method everytime!~
	// * People recalculate things framely in computer graphics anyway! :joy:
	// */
	// public PVector getTouchInWorld(final int p_touchId) {
	// return this.getTouchInWorldFromFarPlane(p_touchId,
	// this.SKETCH.getCamera().mouseZ);
	// }

	// public PVector getTouchInWorldFromFarPlane(final float p_touchId, final float
	// p_distanceFromFarPlane) {
	// final TouchEvent.Pointer touch = this.SKETCH.touches[p_touchId];
	// return this.worldVec(touch.x, touch.y,
	// this.SKETCH.getCamera().far - p_distanceFromFarPlane +
	// this.SKETCH.getCamera().near);
	// }

	// public PVector getTouchInWorldAtZ(final int p_touchId, final float
	// p_distanceFromCamera) {
	// final TouchEvent.Pointer touch = this.SKETCH.touches[p_touchId];
	// return this.worldVec(touch.x, touch.y, p_distanceFromCamera);
	// }
	// endregion
	// endregion

	// region The billion `image()` overloads. Help me make "standards"?
	// region For `PImage`s.
	public void image(final PImage p_image) {
		// https://processing.org/reference/set_.html.
		// Faster than `image()`!:
		// `this.GRAPHICS.set(0, 0, p_image);`
		// However, we also need to remember that it doesn't render the image on to a
		// quad, meaning that transformations won't apply.
		this.graphics.image(p_image, 0, 0);
	}

	/**
	 * @param p_side The length of the side of the square.
	 */
	public void image(final PImage p_image, final float p_side) {
		this.graphics.image(p_image, 0, 0, p_side, p_side);
	}

	public void image(final PImage p_image, final PVector p_pos) {
		this.graphics.pushMatrix();
		this.graphics.translate(p_pos.x, p_pos.y, p_pos.z);
		this.graphics.image(p_image, 0, 0);
		this.graphics.popMatrix();
	}

	public void image(final PImage p_image, final PVector p_pos, final float p_size) {
		this.graphics.pushMatrix();
		this.graphics.translate(p_pos.x, p_pos.y, p_pos.z);
		this.image(p_image, p_pos.x, p_pos.y, p_size, p_size);
		this.graphics.popMatrix();
	}

	public void image(final PImage p_image, final float p_x, final float p_y, final float p_z) {
		this.graphics.pushMatrix();
		this.graphics.translate(p_x, p_y, p_z);
		this.graphics.image(p_image, 0, 0);
		this.graphics.popMatrix();
	}
	// endregion

	// region For `PGraphics`.
	public void image(final PGraphics p_graphics) {
		this.graphics.image(p_graphics, 0, 0);
	}

	public void image(final PGraphics p_graphics, final PVector p_pos) {
		this.graphics.pushMatrix();
		this.graphics.translate(p_pos.x, p_pos.y, p_pos.z);
		this.graphics.image(p_graphics, 0, 0);
		this.graphics.popMatrix();
	}

	public void image(final PGraphics p_graphics, final float p_scale) {
		this.graphics.image(p_graphics, 0, 0, p_scale, p_scale);
	}

	public void image(final PGraphics p_graphics, final PVector p_pos, final float p_scale) {
		this.graphics.pushMatrix();
		this.graphics.translate(p_pos.x, p_pos.y, p_pos.z);
		this.image(p_graphics, 0, 0, p_scale, p_scale);
		this.graphics.popMatrix();
	}

	public void image(final PGraphics p_graphics, final float p_x, final float p_y, final float p_z) {
		this.image((PImage) p_graphics, p_x, p_y, p_z);
	}

	public void image(final PGraphics p_graphics, final PVector p_pos, final float p_width, final float p_height) {
		this.graphics.pushMatrix();
		this.graphics.translate(p_pos.x, p_pos.y, p_pos.z);
		this.image(p_graphics, p_pos.x, p_pos.y, p_width, p_height);
		this.graphics.popMatrix();
	}
	// endregion

	// region For `NerdGraphics`.
	public void image(final NerdGraphics p_graphics) {
		this.graphics.image(p_graphics.getUnderlyingBuffer(), 0, 0);
	}

	public void image(final NerdGraphics p_graphics,
			final float p_x, final float p_y,
			final float p_width, final float p_height) {
		this.graphics.image(p_graphics.getUnderlyingBuffer(), p_x, p_y, p_width, p_height);
	}

	public void image(final NerdGraphics p_graphics, final PVector p_pos) {
		this.graphics.pushMatrix();
		this.graphics.translate(p_pos.x, p_pos.y, p_pos.z);
		this.graphics.image(p_graphics.getUnderlyingBuffer(), 0, 0);
		this.graphics.popMatrix();
	}

	public void image(final NerdGraphics p_graphics, final float p_scale) {
		this.graphics.image(p_graphics.getUnderlyingBuffer(), 0, 0, p_scale, p_scale);
	}

	public void image(final NerdGraphics p_graphics, final float p_x, final float p_y) {
		this.image((PImage) p_graphics.getUnderlyingBuffer(), p_x, p_y);
	}

	public void image(final NerdGraphics p_graphics, final PVector p_pos, final float p_scale) {
		this.graphics.pushMatrix();
		this.graphics.translate(p_pos.x, p_pos.y, p_pos.z);
		this.image(p_graphics.getUnderlyingBuffer(), 0, 0, p_scale, p_scale);
		this.graphics.popMatrix();
	}

	public void image(final NerdGraphics p_graphics, final float p_x, final float p_y, final float p_z) {
		this.image((PImage) p_graphics.getUnderlyingBuffer(), p_x, p_y, p_z);
	}

	public void image(final NerdGraphics p_graphics, final PVector p_pos, final float p_width, final float p_height) {
		this.graphics.pushMatrix();
		this.graphics.translate(p_pos.x, p_pos.y, p_pos.z);
		this.image(p_graphics.getUnderlyingBuffer(), p_pos.x, p_pos.y, p_width, p_height);
		this.graphics.popMatrix();
	}
	// endregion
	// endregion

	// region `push()` and `pop()` simply don't work in `PApplet`,
	// ...so I re-wrote them myself!
	public void push() {
		this.graphics.pushMatrix();
		this.graphics.pushStyle();
	}

	public void pop() {
		this.graphics.popStyle();
		this.graphics.popMatrix();
	}
	// endregion
	// endregion

	public float textHeight() {
		return this.graphics.textAscent() - this.graphics.textDescent();
	}

	/**
	 * Translates by the width of {@code p_text} halved, and the current text
	 * height, halved, before actually rendering the text.
	 * 
	 * @see NerdSketch#textHeight()
	 * @see PApplet#textWidth(String)
	 */
	public void centeredText(final String p_text) {
		this.graphics.text(p_text,
				this.graphics.textWidth(p_text) * 0.5f,
				this.textHeight() * 0.5f);
	}

	// region 2D rendering.
	/**
	 * Pushes the graphics buffer, disables depth testing and resets all current
	 * transformations (they're restored by a call to `Sketch::pop()` later!).
	 */
	public void begin2d() {
		this.push();
		this.graphics.hint(PConstants.DISABLE_DEPTH_TEST);
		this.graphics.perspective();
		this.graphics.camera();
	}

	/**
	 * Pops back transformations and enables depth testing.
	 */
	public void end2d() {
		this.pop();
		this.graphics.hint(PConstants.ENABLE_DEPTH_TEST);
		this.SKETCH.getCamera().applyMatrix(this.graphics);
	}

	/**
	 * Pushes the graphics buffer, disables depth testing, resets all current
	 * transformations, calls your {@link Runnable} {@code p_toDraw}, and
	 * finally, pops back the transformations and enables depth testing!
	 * 
	 * @see {@link NerdSketch#end2d()}
	 * @see {@link NerdSketch#begin2d()}
	 */
	public void in2d(final Runnable p_toDraw) {
		// #JIT_FTW!
		this.begin2d();
		p_toDraw.run();
		this.end2d();
	}
	// endregion

	// region `Sketch::alphaBg()` overloads.
	public void alphaBg(final int p_color) {
		this.begin2d();
		this.graphics.fill(p_color);
		this.alphaBgImplRect();
	}

	public void alphaBg(final int p_color, final float p_alpha) {
		this.begin2d();
		this.graphics.fill(p_color, p_alpha);
		this.alphaBgImplRect();
	}

	public void alphaBg(final float p_grey, final float p_alpha) {
		this.begin2d();
		this.graphics.fill(p_grey, p_alpha);
		this.alphaBgImplRect();
	}

	public void alphaBg(final float p_v1, final float p_v2, final float p_v3) {
		this.begin2d();
		this.graphics.fill(p_v1, p_v2, p_v3);
		this.alphaBgImplRect();
	}

	public void alphaBg(final float p_v1, final float p_v2, final float p_v3, final float p_alpha) {
		this.begin2d();
		this.graphics.fill(p_v1, p_v2, p_v3, p_alpha);
		this.alphaBgImplRect();
	}

	protected void alphaBgImplRect() {
		// Removing this will not display the previous camera's view,
		// but still show clipping:
		this.graphics.camera();
		this.graphics.noStroke();
		this.graphics.rectMode(PConstants.CORNER);
		this.graphics.rect(0, 0, this.graphics.width, this.graphics.height);
		this.end2d();
	}
	// endregion
	// endregion

	// region ...From `PGraphics`.
	public void ambient(final int rgb) {
		this.graphics.ambient(rgb);
	}

	public void ambient(final float gray) {
		this.graphics.ambient(gray);
	}

	public void ambient(final float v1, final float v2, final float v3) {
		this.graphics.ambient(v1, v2, v3);
	}

	public void ambientLight(final float v1, final float v2, final float v3) {
		this.graphics.ambientLight(v1, v2, v3);
	}

	public void ambientLight(final float v1, final float v2, final float v3, final float x, final float y,
			final float z) {
		this.graphics.ambientLight(v1, v2, v3, x, y, z);
	}

	public void applyMatrix(final PMatrix source) {
		this.graphics.applyMatrix(source);
	}

	public void applyMatrix(final PMatrix2D source) {
		this.graphics.applyMatrix(source);
	}

	public void applyMatrix(final PMatrix3D source) {
		this.graphics.applyMatrix(source);
	}

	public void applyMatrix(final float n00, final float n01, final float n02, final float n10, final float n11,
			final float n12) {
		this.graphics.applyMatrix(n00, n01, n02, n10, n11, n12);
	}

	public void applyMatrix(final float n00, final float n01, final float n02, final float n03, final float n10,
			final float n11, final float n12, final float n13,
			final float n20, final float n21, final float n22, final float n23, final float n30, final float n31,
			final float n32, final float n33) {
		this.graphics.applyMatrix(n00, n01, n02, n03, n10, n11, n12, n13, n20, n21, n22, n23, n30, n31, n32, n33);
	}

	public void arc(final float a, final float b, final float c, final float d, final float start, final float stop) {
		this.graphics.arc(a, b, c, d, start, stop);
	}

	public void arc(final float a, final float b, final float c, final float d, final float start, final float stop,
			final int mode) {
		this.graphics.arc(a, b, c, d, start, stop, mode);
	}

	public void attrib(final String name, final float... values) {
		this.graphics.attrib(name, values);
	}

	public void attrib(final String name, final int... values) {
		this.graphics.attrib(name, values);
	}

	public void attrib(final String name, final boolean... values) {
		this.graphics.attrib(name, values);
	}

	public void attribColor(final String name, final int color) {
		this.graphics.attribColor(name, color);
	}

	public void attribNormal(final String name, final float nx, final float ny, final float nz) {
		this.graphics.attribNormal(name, nx, ny, nz);
	}

	public void attribPosition(final String name, final float x, final float y, final float z) {
		this.graphics.attribPosition(name, x, y, z);
	}

	public void background(final int rgb) {
		this.graphics.background(rgb);
	}

	public void background(final float gray) {
		this.graphics.background(gray);
	}

	public void background(final int rgb, final float alpha) {
		this.graphics.background(rgb, alpha);
	}

	public void background(final float gray, final float alpha) {
		this.graphics.background(gray, alpha);
	}

	public void background(final float v1, final float v2, final float v3) {
		this.graphics.background(v1, v2, v3);
	}

	public void background(final float v1, final float v2, final float v3, final float alpha) {
		this.graphics.background(v1, v2, v3, alpha);
	}

	public void beginCamera() {
		this.graphics.beginCamera();
	}

	public void beginContour() {
		this.graphics.beginContour();
	}

	public void beginDraw() {
		this.graphics.beginDraw();
	}

	public PGL beginPGL() {
		return this.graphics.beginPGL();
	}

	public void beginRaw(final PGraphics rawGraphics) {
		this.graphics.beginRaw(rawGraphics);
	}

	public void beginShape() {
		this.graphics.beginShape();
	}

	public void beginShape(final int kind) {
		this.graphics.beginShape(kind);
	}

	public void bezier(final float x1, final float y1, final float x2, final float y2, final float x3, final float y3,
			final float x4, final float y4) {
		this.graphics.bezier(x1, y1, x2, y2, x3, y3, x4, y4);
	}

	public void bezier(final float x1, final float y1, final float z1, final float x2, final float y2, final float z2,
			final float x3, final float y3, final float z3,
			final float x4, final float y4, final float z4) {
		this.graphics.bezier(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4);
	}

	public void bezierDetail(final int detail) {
		this.graphics.bezierDetail(detail);
	}

	public float bezierPoint(final float a, final float b, final float c, final float d, final float t) {
		return this.graphics.bezierPoint(a, b, c, d, t);
	}

	public float bezierTangent(final float a, final float b, final float c, final float d, final float t) {
		return this.graphics.bezierTangent(a, b, c, d, t);
	}

	public void bezierVertex(final float x2, final float y2, final float x3, final float y3, final float x4,
			final float y4) {
		this.graphics.bezierVertex(x2, y2, x3, y3, x4, y4);
	}

	public void bezierVertex(final float x2, final float y2, final float z2, final float x3, final float y3,
			final float z3, final float x4, final float y4, final float z4) {
		this.graphics.bezierVertex(x2, y2, z2, x3, y3, z3, x4, y4, z4);
	}

	public void blendMode(final int mode) {
		this.graphics.blendMode(mode);
	}

	public void box(final float size) {
		this.graphics.box(size);
	}

	public void box(final float w, final float h, final float d) {
		this.graphics.box(w, h, d);
	}

	public void camera() {
		this.graphics.camera();
	}

	public void camera(final float eyeX, final float eyeY, final float eyeZ, final float centerX, final float centerY,
			final float centerZ, final float upX,
			final float upY, final float upZ) {
		this.graphics.camera(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
	}

	public void circle(final float x, final float y, final float extent) {
		this.graphics.circle(x, y, extent);
	}

	public void clear() {
		this.graphics.clear();
	}

	public void clip(final float a, final float b, final float c, final float d) {
		this.graphics.clip(a, b, c, d);
	}

	public void colorMode(final int mode) {
		this.graphics.colorMode(mode);
	}

	public void colorMode(final int mode, final float max) {
		this.graphics.colorMode(mode, max);
	}

	public void colorMode(final int mode, final float max1, final float max2, final float max3) {
		this.graphics.colorMode(mode, max1, max2, max3);
	}

	public void colorMode(final int mode, final float max1, final float max2, final float max3, final float maxA) {
		this.graphics.colorMode(mode, max1, max2, max3, maxA);
	}

	public PShape createShape() {
		return this.graphics.createShape();
	}

	public PShape createShape(final int type) {
		return this.graphics.createShape(type);
	}

	public PShape createShape(final int kind, final float... p) {
		return this.graphics.createShape(kind, p);
	}

	public PSurface createSurface() {
		return this.graphics.createSurface();
	}

	public void curve(final float x1, final float y1, final float x2, final float y2, final float x3, final float y3,
			final float x4, final float y4) {
		this.graphics.curve(x1, y1, x2, y2, x3, y3, x4, y4);
	}

	public void curve(final float x1, final float y1, final float z1, final float x2, final float y2, final float z2,
			final float x3, final float y3, final float z3,
			final float x4, final float y4, final float z4) {
		this.graphics.curve(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4);
	}

	public void curveDetail(final int detail) {
		this.graphics.curveDetail(detail);
	}

	public float curvePoint(final float a, final float b, final float c, final float d, final float t) {
		return this.graphics.curvePoint(a, b, c, d, t);
	}

	public float curveTangent(final float a, final float b, final float c, final float d, final float t) {
		return this.graphics.curveTangent(a, b, c, d, t);
	}

	public void curveTightness(final float tightness) {
		this.graphics.curveTightness(tightness);
	}

	public void curveVertex(final float x, final float y) {
		this.graphics.curveVertex(x, y);
	}

	public void curveVertex(final float x, final float y, final float z) {
		this.graphics.curveVertex(x, y, z);
	}

	public void directionalLight(final float v1, final float v2, final float v3, final float nx, final float ny,
			final float nz) {
		this.graphics.directionalLight(v1, v2, v3, nx, ny, nz);
	}

	public boolean displayable() {
		return this.graphics.displayable();
	}

	public void dispose() {
		this.graphics.dispose();
	}

	public void edge(final boolean edge) {
		this.graphics.edge(edge);
	}

	public void ellipse(final float a, final float b, final float c, final float d) {
		this.graphics.ellipse(a, b, c, d);
	}

	public void ellipseMode(final int mode) {
		this.graphics.ellipseMode(mode);
	}

	public void emissive(final int rgb) {
		this.graphics.emissive(rgb);
	}

	public void emissive(final float gray) {
		this.graphics.emissive(gray);
	}

	public void emissive(final float v1, final float v2, final float v3) {
		this.graphics.emissive(v1, v2, v3);
	}

	public void endCamera() {
		this.graphics.endCamera();
	}

	public void endContour() {
		this.graphics.endContour();
	}

	public void endDraw() {
		this.graphics.endDraw();
	}

	public void endPGL() {
		this.graphics.endPGL();
	}

	public void endRaw() {
		this.graphics.endRaw();
	}

	public void endShape() {
		this.graphics.endShape();
	}

	public void endShape(final int mode) {
		this.graphics.endShape(mode);
	}

	public void fill(final int rgb) {
		this.graphics.fill(rgb);
	}

	public void fill(final float gray) {
		this.graphics.fill(gray);
	}

	public void fill(final int rgb, final float alpha) {
		this.graphics.fill(rgb, alpha);
	}

	public void fill(final float gray, final float alpha) {
		this.graphics.fill(gray, alpha);
	}

	public void fill(final float v1, final float v2, final float v3) {
		this.graphics.fill(v1, v2, v3);
	}

	public void fill(final float v1, final float v2, final float v3, final float alpha) {
		this.graphics.fill(v1, v2, v3, alpha);
	}

	public void filter(final PShader shader) {
		this.graphics.filter(shader);
	}

	public void flush() {
		this.graphics.flush();
	}

	public void frustum(final float left, final float right, final float bottom, final float top, final float near,
			final float far) {
		this.graphics.frustum(left, right, bottom, top, near, far);
	}

	public Object getCache(final PImage image) {
		return this.graphics.getCache(image);
	}

	public PMatrix getMatrix() {
		return this.graphics.getMatrix();
	}

	public PMatrix2D getMatrix(final PMatrix2D target) {
		return this.graphics.getMatrix(target);
	}

	public PMatrix3D getMatrix(final PMatrix3D target) {
		return this.graphics.getMatrix(target);
	}

	public PGraphics getRaw() {
		return this.graphics.getRaw();
	}

	public PStyle getStyle() {
		return this.graphics.getStyle();
	}

	public PStyle getStyle(final PStyle s) {
		return this.graphics.getStyle(s);
	}

	public boolean haveRaw() {
		return this.graphics.haveRaw();
	}

	public void hint(final int which) {
		this.graphics.hint(which);
	}

	public void image(final PImage img, final float a, final float b) {
		this.graphics.image(img, a, b);
	}

	public void image(final PImage img, final float a, final float b, final float c, final float d) {
		this.graphics.image(img, a, b, c, d);
	}

	public void image(final PImage img, final float a, final float b, final float c, final float d, final int u1,
			final int v1, final int u2, final int v2) {
		this.graphics.image(img, a, b, c, d, u1, v1, u2, v2);
	}

	public void imageMode(final int mode) {
		this.graphics.imageMode(mode);
	}

	public boolean is2D() {
		return this.graphics.is2D();
	}

	public boolean is2X() {
		return this.graphics.is2X();
	}

	public boolean is3D() {
		return this.graphics.is3D();
	}

	public boolean isGL() {
		return this.graphics.isGL();
	}

	public int lerpColor(final int c1, final int c2, final float amt) {
		return this.graphics.lerpColor(c1, c2, amt);
	}

	public void lightFalloff(final float constant, final float linear, final float quadratic) {
		this.graphics.lightFalloff(constant, linear, quadratic);
	}

	public void lightSpecular(final float v1, final float v2, final float v3) {
		this.graphics.lightSpecular(v1, v2, v3);
	}

	public void lights() {
		this.graphics.lights();
	}

	public void line(final float x1, final float y1, final float x2, final float y2) {
		this.graphics.line(x1, y1, x2, y2);
	}

	public void line(final float x1, final float y1, final float z1, final float x2, final float y2, final float z2) {
		this.graphics.line(x1, y1, z1, x2, y2, z2);
	}

	public PShader loadShader(final String fragFilename) {
		return this.graphics.loadShader(fragFilename);
	}

	public PShader loadShader(final String fragFilename, final String vertFilename) {
		return this.graphics.loadShader(fragFilename, vertFilename);
	}

	public PShape loadShape(final String filename) {
		return this.graphics.loadShape(filename);
	}

	public PShape loadShape(final String filename, final String options) {
		return this.graphics.loadShape(filename, options);
	}

	public float modelX(final float x, final float y, final float z) {
		return this.graphics.modelX(x, y, z);
	}

	public float modelY(final float x, final float y, final float z) {
		return this.graphics.modelY(x, y, z);
	}

	public float modelZ(final float x, final float y, final float z) {
		return this.graphics.modelZ(x, y, z);
	}

	public void noClip() {
		this.graphics.noClip();
	}

	public void noFill() {
		this.graphics.noFill();
	}

	public void noLights() {
		this.graphics.noLights();
	}

	public void noSmooth() {
		this.graphics.noSmooth();
	}

	public void noStroke() {
		this.graphics.noStroke();
	}

	public void noTexture() {
		this.graphics.noTexture();
	}

	public void noTint() {
		this.graphics.noTint();
	}

	public void normal(final float nx, final float ny, final float nz) {
		this.graphics.normal(nx, ny, nz);
	}

	public void ortho() {
		this.graphics.ortho();
	}

	public void ortho(final float left, final float right, final float bottom, final float top, final float near,
			final float far) {
		this.graphics.ortho(left, right, bottom, top, near, far);
	}

	public void perspective() {
		this.graphics.perspective();
	}

	public void perspective(final float fovy, final float aspect, final float zNear, final float zFar) {
		this.graphics.perspective(fovy, aspect, zNear, zFar);
	}

	public void point(final float x, final float y) {
		this.graphics.point(x, y);
	}

	public void point(final float x, final float y, final float z) {
		this.graphics.point(x, y, z);
	}

	public void pointLight(final float v1, final float v2, final float v3, final float x, final float y,
			final float z) {
		this.graphics.pointLight(v1, v2, v3, x, y, z);
	}

	public void popMatrix() {
		this.graphics.popMatrix();
	}

	public void popStyle() {
		this.graphics.popStyle();
	}

	public void printCamera() {
		this.graphics.printCamera();
	}

	public void printMatrix() {
		this.graphics.printMatrix();
	}

	public void printProjection() {
		this.graphics.printProjection();
	}

	public void pushMatrix() {
		this.graphics.pushMatrix();
	}

	public void pushStyle() {
		this.graphics.pushStyle();
	}

	public void quad(final float x1, final float y1, final float x2, final float y2, final float x3, final float y3,
			final float x4, final float y4) {
		this.graphics.quad(x1, y1, x2, y2, x3, y3, x4, y4);
	}

	public void quadraticVertex(final float cx, final float cy, final float x3, final float y3) {
		this.graphics.quadraticVertex(cx, cy, x3, y3);
	}

	public void quadraticVertex(final float cx, final float cy, final float cz, final float x3, final float y3,
			final float z3) {
		this.graphics.quadraticVertex(cx, cy, cz, x3, y3, z3);
	}

	public void rect(final float a, final float b, final float c, final float d) {
		this.graphics.rect(a, b, c, d);
	}

	public void rect(final float a, final float b, final float c, final float d, final float tl, final float tr,
			final float br, final float bl) {
		this.graphics.rect(a, b, c, d, tl, tr, br, bl);
	}

	public void rectMode(final int mode) {
		this.graphics.rectMode(mode);
	}

	public void removeCache(final PImage image) {
		this.graphics.removeCache(image);
	}

	public void resetMatrix() {
		this.graphics.resetMatrix();
	}

	public void resetShader() {
		this.graphics.resetShader();
	}

	public void resetShader(final int kind) {
		this.graphics.resetShader(kind);
	}

	public void rotate(final float angle) {
		this.graphics.rotate(angle);
	}

	public void rotate(final float angle, final float x, final float y, final float z) {
		this.graphics.rotate(angle, x, y, z);
	}

	public void rotateX(final float angle) {
		this.graphics.rotateX(angle);
	}

	public void rotateY(final float angle) {
		this.graphics.rotateY(angle);
	}

	public void rotateZ(final float angle) {
		this.graphics.rotateZ(angle);
	}

	public boolean save(final String filename) {
		return this.graphics.save(filename);
	}

	public void scale(final float s) {
		this.graphics.scale(s);
	}

	public void scale(final float x, final float y) {
		this.graphics.scale(x, y);
	}

	public void scale(final float x, final float y, final float z) {
		this.graphics.scale(x, y, z);
	}

	public float screenX(final float x, final float y) {
		return this.graphics.screenX(x, y);
	}

	public float screenX(final float x, final float y, final float z) {
		return this.graphics.screenX(x, y, z);
	}

	public float screenY(final float x, final float y) {
		return this.graphics.screenY(x, y);
	}

	public float screenY(final float x, final float y, final float z) {
		return this.graphics.screenY(x, y, z);
	}

	public float screenZ(final float x, final float y, final float z) {
		return this.graphics.screenZ(x, y, z);
	}

	public void setCache(final PImage image, final Object storage) {
		this.graphics.setCache(image, storage);
	}

	public void setMatrix(final PMatrix source) {
		this.graphics.setMatrix(source);
	}

	public void setMatrix(final PMatrix2D source) {
		this.graphics.setMatrix(source);
	}

	public void setMatrix(final PMatrix3D source) {
		this.graphics.setMatrix(source);
	}

	public void setParent(final PApplet parent) {
		this.graphics.setParent(parent);
	}

	public void setPath(final String path) {
		this.graphics.setPath(path);
	}

	public void setPrimary(final boolean primary) {
		this.graphics.setPrimary(primary);
	}

	public void setSize(final int w, final int h) {
		this.graphics.setSize(w, h);
	}

	public void shader(final PShader shader) {
		this.graphics.shader(shader);
	}

	public void shader(final PShader shader, final int kind) {
		this.graphics.shader(shader, kind);
	}

	public void shape(final PShape shape) {
		this.graphics.shape(shape);
	}

	public void shape(final PShape shape, final float x, final float y) {
		this.graphics.shape(shape, x, y);
	}

	public void shape(final PShape shape, final float a, final float b, final float c, final float d) {
		this.graphics.shape(shape, a, b, c, d);
	}

	public void shapeMode(final int mode) {
		this.graphics.shapeMode(mode);
	}

	public void shearX(final float angle) {
		this.graphics.shearX(angle);
	}

	public void shearY(final float angle) {
		this.graphics.shearY(angle);
	}

	public void shininess(final float shine) {
		this.graphics.shininess(shine);
	}

	public void smooth() {
		this.graphics.smooth();
	}

	public void smooth(final int quality) {
		this.graphics.smooth(quality);
	}

	public void specular(final int rgb) {
		this.graphics.specular(rgb);
	}

	public void specular(final float gray) {
		this.graphics.specular(gray);
	}

	public void specular(final float v1, final float v2, final float v3) {
		this.graphics.specular(v1, v2, v3);
	}

	public void sphere(final float r) {
		this.graphics.sphere(r);
	}

	public void sphereDetail(final int res) {
		this.graphics.sphereDetail(res);
	}

	public void sphereDetail(final int ures, final int vres) {
		this.graphics.sphereDetail(ures, vres);
	}

	public void spotLight(final float v1, final float v2, final float v3, final float x, final float y, final float z,
			final float nx, final float ny, final float nz,
			final float angle, final float concentration) {
		this.graphics.spotLight(v1, v2, v3, x, y, z, nx, ny, nz, angle, concentration);
	}

	public void square(final float x, final float y, final float extent) {
		this.graphics.square(x, y, extent);
	}

	public void stroke(final int rgb) {
		this.graphics.stroke(rgb);
	}

	public void stroke(final float gray) {
		this.graphics.stroke(gray);
	}

	public void stroke(final int rgb, final float alpha) {
		this.graphics.stroke(rgb, alpha);
	}

	public void stroke(final float gray, final float alpha) {
		this.graphics.stroke(gray, alpha);
	}

	public void stroke(final float v1, final float v2, final float v3) {
		this.graphics.stroke(v1, v2, v3);
	}

	public void stroke(final float v1, final float v2, final float v3, final float alpha) {
		this.graphics.stroke(v1, v2, v3, alpha);
	}

	public void strokeCap(final int cap) {
		this.graphics.strokeCap(cap);
	}

	public void strokeJoin(final int join) {
		this.graphics.strokeJoin(join);
	}

	public void strokeWeight(final float weight) {
		this.graphics.strokeWeight(weight);
	}

	public void style(final PStyle s) {
		this.graphics.style(s);
	}

	public void text(final char c, final float x, final float y) {
		this.graphics.text(c, x, y);
	}

	public void text(final String str, final float x, final float y) {
		this.graphics.text(str, x, y);
	}

	public void text(final int num, final float x, final float y) {
		this.graphics.text(num, x, y);
	}

	public void text(final float num, final float x, final float y) {
		this.graphics.text(num, x, y);
	}

	public void text(final char c, final float x, final float y, final float z) {
		this.graphics.text(c, x, y, z);
	}

	public void text(final String str, final float x, final float y, final float z) {
		this.graphics.text(str, x, y, z);
	}

	public void text(final int num, final float x, final float y, final float z) {
		this.graphics.text(num, x, y, z);
	}

	public void text(final float num, final float x, final float y, final float z) {
		this.graphics.text(num, x, y, z);
	}

	public void text(final char[] chars, final int start, final int stop, final float x, final float y) {
		this.graphics.text(chars, start, stop, x, y);
	}

	public void text(final String str, final float x1, final float y1, final float x2, final float y2) {
		this.graphics.text(str, x1, y1, x2, y2);
	}

	public void text(final char[] chars, final int start, final int stop, final float x, final float y, final float z) {
		this.graphics.text(chars, start, stop, x, y, z);
	}

	public void textAlign(final int alignX) {
		this.graphics.textAlign(alignX);
	}

	public void textAlign(final int alignX, final int alignY) {
		this.graphics.textAlign(alignX, alignY);
	}

	public float textAscent() {
		return this.graphics.textAscent();
	}

	public float textDescent() {
		return this.graphics.textDescent();
	}

	public void textFont(final PFont which) {
		this.graphics.textFont(which);
	}

	public void textFont(final PFont which, final float size) {
		this.graphics.textFont(which, size);
	}

	public void textLeading(final float leading) {
		this.graphics.textLeading(leading);
	}

	public void textMode(final int mode) {
		this.graphics.textMode(mode);
	}

	public void textSize(final float size) {
		this.graphics.textSize(size);
	}

	public float textWidth(final char c) {
		return this.graphics.textWidth(c);
	}

	public float textWidth(final String str) {
		return this.graphics.textWidth(str);
	}

	public float textWidth(final char[] chars, final int start, final int length) {
		return this.graphics.textWidth(chars, start, length);
	}

	public void texture(final PImage image) {
		this.graphics.texture(image);
	}

	public void textureMode(final int mode) {
		this.graphics.textureMode(mode);
	}

	public void textureWrap(final int wrap) {
		this.graphics.textureWrap(wrap);
	}

	public void tint(final int rgb) {
		this.graphics.tint(rgb);
	}

	public void tint(final float gray) {
		this.graphics.tint(gray);
	}

	public void tint(final int rgb, final float alpha) {
		this.graphics.tint(rgb, alpha);
	}

	public void tint(final float gray, final float alpha) {
		this.graphics.tint(gray, alpha);
	}

	public void tint(final float v1, final float v2, final float v3) {
		this.graphics.tint(v1, v2, v3);
	}

	public void tint(final float v1, final float v2, final float v3, final float alpha) {
		this.graphics.tint(v1, v2, v3, alpha);
	}

	public void translate(final float x, final float y) {
		this.graphics.translate(x, y);
	}

	public void translate(final float x, final float y, final float z) {
		this.graphics.translate(x, y, z);
	}

	public void triangle(final float x1, final float y1, final float x2, final float y2, final float x3,
			final float y3) {
		this.graphics.triangle(x1, y1, x2, y2, x3, y3);
	}

	public void vertex(final float[] v) {
		this.graphics.vertex(v);
	}

	public void vertex(final float x, final float y) {
		this.graphics.vertex(x, y);
	}

	public void vertex(final float x, final float y, final float z) {
		this.graphics.vertex(x, y, z);
	}

	public void vertex(final float x, final float y, final float u, final float v) {
		this.graphics.vertex(x, y, u, v);
	}

	public void vertex(final float x, final float y, final float z, final float u, final float v) {
		this.graphics.vertex(x, y, z, u, v);
	}

	public void blend(final int sx, final int sy, final int sw, final int sh, final int dx, final int dy, final int dw,
			final int dh, final int mode) {
		this.graphics.blend(sx, sy, sw, sh, dx, dy, dw, dh, mode);
	}

	public void blend(final PImage src, final int sx, final int sy, final int sw, final int sh, final int dx,
			final int dy, final int dw, final int dh, final int mode) {
		this.graphics.blend(src, sx, sy, sw, sh, dx, dy, dw, dh, mode);
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return this.graphics.clone();
	}

	public PImage copy() {
		return this.graphics.copy();
	}

	public void copy(final int sx, final int sy, final int sw, final int sh, final int dx, final int dy, final int dw,
			final int dh) {
		this.graphics.copy(sx, sy, sw, sh, dx, dy, dw, dh);
	}

	public void copy(final PImage src, final int sx, final int sy, final int sw, final int sh, final int dx,
			final int dy, final int dw, final int dh) {
		this.graphics.copy(src, sx, sy, sw, sh, dx, dy, dw, dh);
	}

	public void filter(final int kind) {
		this.graphics.filter(kind);
	}

	public void filter(final int kind, final float param) {
		this.graphics.filter(kind, param);
	}

	public PImage get() {
		return this.graphics.get();
	}

	public int get(final int x, final int y) {
		return this.graphics.get(x, y);
	}

	public PImage get(final int x, final int y, final int w, final int h) {
		return this.graphics.get(x, y, w, h);
	}

	public Image getImage() {
		return this.graphics.getImage();
	}

	public int getModifiedX1() {
		return this.graphics.getModifiedX1();
	}

	public int getModifiedX2() {
		return this.graphics.getModifiedX2();
	}

	public int getModifiedY1() {
		return this.graphics.getModifiedY1();
	}

	public int getModifiedY2() {
		return this.graphics.getModifiedY2();
	}

	public Object getNative() {
		return this.graphics.getNative();
	}

	public void init(final int width, final int height, final int format) {
		this.graphics.init(width, height, format);
	}

	public void init(final int width, final int height, final int format, final int factor) {
		this.graphics.init(width, height, format, factor);
	}

	public boolean isLoaded() {
		return this.graphics.isLoaded();
	}

	public boolean isModified() {
		return this.graphics.isModified();
	}

	public void loadPixels() {
		this.graphics.loadPixels();
	}

	public void mask(final int[] maskArray) {
		this.graphics.mask(maskArray);
	}

	public void mask(final PImage img) {
		this.graphics.mask(img);
	}

	public void resize(final int w, final int h) {
		this.graphics.resize(w, h);
	}

	public void set(final int x, final int y, final int c) {
		this.graphics.set(x, y, c);
	}

	public void set(final int x, final int y, final PImage img) {
		this.graphics.set(x, y, img);
	}

	public void setLoaded() {
		this.graphics.setLoaded();
	}

	public void setLoaded(final boolean l) {
		this.graphics.setLoaded(l);
	}

	public void setModified() {
		this.graphics.setModified();
	}

	public void setModified(final boolean m) {
		this.graphics.setModified(m);
	}

	public void updatePixels() {
		this.graphics.updatePixels();
	}

	public void updatePixels(final int x, final int y, final int w, final int h) {
		this.graphics.updatePixels(x, y, w, h);
	}

	@Override
	public boolean equals(final Object obj) {
		return this.graphics.equals(obj);
	}

	@Override
	public int hashCode() {
		return this.graphics.hashCode();
	}

	@Override
	public String toString() {
		return this.graphics.toString();
	}
	// endregion

}
