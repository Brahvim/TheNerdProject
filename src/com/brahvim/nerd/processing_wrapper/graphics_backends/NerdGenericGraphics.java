package com.brahvim.nerd.processing_wrapper.graphics_backends;

import java.awt.Image;
import java.util.Objects;

import com.brahvim.nerd.framework.cameras.NerdAbstractCamera;
import com.brahvim.nerd.processing_wrapper.NerdSketch;
import com.brahvim.nerd.window_management.NerdInputModule;
import com.brahvim.nerd.window_management.NerdWindowModule;

import processing.awt.PGraphicsJava2D;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PShape;
import processing.core.PStyle;
import processing.core.PSurface;
import processing.core.PVector;
import processing.javafx.PGraphicsFX2D;
import processing.opengl.PGraphics2D;
import processing.opengl.PGraphics3D;
import processing.pdf.PGraphicsPDF;
import processing.svg.PGraphicsSVG;

public abstract class NerdGenericGraphics<SketchPGraphicsT extends PGraphics> {

	// region Instance fields.
	protected NerdAbstractCamera currentCamera; // CAMERA! wher lite?! wher accsunn?!

	protected final NerdInputModule INPUT;
	protected final SketchPGraphicsT GRAPHICS;
	protected final NerdSketch<SketchPGraphicsT> SKETCH;
	protected final NerdWindowModule<SketchPGraphicsT> WINDOW;

	// Dimensions for the current and previous frames:
	public float pdbx, pdby, pcx, pcy, pqx, pqy, pq3x, pq3y, pscr;
	public float dbx, dby, cx, cy, qx, qy, q3x, q3y, scr;
	public int width, height, pwidth, pheight;
	// endregion

	// region Static methods.
	public static NerdGenericGraphics<?> createNerdGenericGraphicsWrapperForSketch(final NerdSketch<?> p_sketch) {
		final NerdGenericGraphics<?> toRet = NerdGenericGraphics.supplySubModuleForSketch(p_sketch);

		if (toRet == null)
			throw new IllegalArgumentException("Please write your own `NerdGenericGraphics` subclass for this one!");

		return toRet;
	}

	@SuppressWarnings("unchecked")
	protected static <RetGraphicsT extends PGraphics> NerdGenericGraphics<?> supplySubModuleForSketch(
			final NerdSketch<RetGraphicsT> p_sketch) {
		return switch (p_sketch.SKETCH_SETTINGS.renderer) {
			case PConstants.P2D -> new NerdP2dGraphics(
					(NerdSketch<PGraphics2D>) p_sketch, (PGraphics2D) p_sketch.getGraphics());

			case PConstants.P3D -> new NerdP3dGraphics(
					(NerdSketch<PGraphics3D>) p_sketch, (PGraphics3D) p_sketch.getGraphics());

			case PConstants.PDF -> new NerdPdfGraphics(
					(NerdSketch<PGraphicsPDF>) p_sketch, (PGraphicsPDF) p_sketch.getGraphics());

			case PConstants.SVG -> new NerdSvgGraphics(
					(NerdSketch<PGraphicsSVG>) p_sketch, (PGraphicsSVG) p_sketch.getGraphics());

			case PConstants.FX2D -> new NerdFx2dGraphics(
					(NerdSketch<PGraphicsFX2D>) p_sketch, (PGraphicsFX2D) p_sketch.getGraphics());

			case PConstants.JAVA2D -> new NerdJava2dGraphics(
					(NerdSketch<PGraphicsJava2D>) p_sketch, (PGraphicsJava2D) p_sketch.getGraphics());

			default -> null;
		};
	}
	// endregion

	@SuppressWarnings("unchecked")
	protected NerdGenericGraphics(
			final NerdSketch<SketchPGraphicsT> p_sketch,
			final SketchPGraphicsT p_pGraphicsToWrap) {
		this.SKETCH = p_sketch;
		this.GRAPHICS = Objects.requireNonNull(p_pGraphicsToWrap,
				"The `"
						+ this.getClass().getSimpleName()
						+ "` constructor received a `null` `"
						+ p_pGraphicsToWrap.getClass().getSimpleName()
						+ "` instance!");
		this.INPUT = this.SKETCH.getNerdModule(NerdInputModule.class);
		this.WINDOW = this.SKETCH.getNerdModule(NerdWindowModule.class);
	}

	public NerdSketch<SketchPGraphicsT> getSketch() {
		return this.SKETCH;
	}

	// region Rendering utilities!
	public final SketchPGraphicsT getUnderlyingBuffer() {
		return this.GRAPHICS;
	}

	// region From `PGraphics`.
	// region Shapes.
	// region `drawShape()` overloads.
	// // OpenGL-specific:
	// public void drawShape(final float p_x, final float p_y, final float p_z,
	// final int p_shapeType,
	// final Runnable p_shapingFxn) {
	// this.GRAPHICS.pushMatrix();
	// this.translate(p_x, p_y, p_z);
	// this.SKETCH.beginShape(p_shapeType);
	// p_shapingFxn.run();
	// this.SKETCH.endShape(PConstants.CLOSE);
	// this.GRAPHICS.popMatrix();
	// }

	public void drawShape(final float p_x, final float p_y, final int p_shapeType, final Runnable p_shapingFxn) {
		this.GRAPHICS.pushMatrix();
		this.translate(p_x, p_y);
		this.SKETCH.beginShape(p_shapeType);
		p_shapingFxn.run();
		this.SKETCH.endShape(PConstants.CLOSE);
		this.GRAPHICS.popMatrix();
	}

	public void drawShape(final PVector p_pos, final int p_shapeType, final Runnable p_shapingFxn) {
		this.GRAPHICS.pushMatrix();
		this.translate(p_pos);
		this.SKETCH.beginShape(p_shapeType);
		p_shapingFxn.run();
		this.SKETCH.endShape(PConstants.CLOSE);
		this.GRAPHICS.popMatrix();
	}

	public void drawShape(final int p_shapeType, final Runnable p_shapingFxn) {
		this.SKETCH.beginShape(p_shapeType);
		p_shapingFxn.run();
		this.SKETCH.endShape(PConstants.CLOSE);
	}
	// endregion

	// region `drawOpenShape()` overloads.
	// OpenGL-specific:
	// public void drawOpenShape(final float p_x, final float p_y, final float p_z,
	// final int p_shapeType,
	// final Runnable p_shapingFxn) {
	// this.GRAPHICS.pushMatrix();
	// this.translate(p_x, p_y, p_z);
	// this.SKETCH.beginShape(p_shapeType);
	// p_shapingFxn.run();
	// this.SKETCH.endShape();
	// this.GRAPHICS.popMatrix();
	// }

	public void drawOpenShape(final float p_x, final float p_y, final int p_shapeType, final Runnable p_shapingFxn) {
		this.GRAPHICS.pushMatrix();
		this.translate(p_x, p_y);
		this.SKETCH.beginShape(p_shapeType);
		p_shapingFxn.run();
		this.SKETCH.endShape();
		this.GRAPHICS.popMatrix();
	}

	public void drawOpenShape(final PVector p_pos, final int p_shapeType, final Runnable p_shapingFxn) {
		this.GRAPHICS.pushMatrix();
		this.translate(p_pos);
		this.SKETCH.beginShape(p_shapeType);
		p_shapingFxn.run();
		this.SKETCH.endShape();
		this.GRAPHICS.popMatrix();
	}

	public void drawOpenShape(final int p_shapeType, final Runnable p_shapingFxn) {
		this.SKETCH.beginShape(p_shapeType);
		p_shapingFxn.run();
		this.SKETCH.endShape();
	}
	// endregion

	public void drawContour(final Runnable p_contouringFxn) {
		this.GRAPHICS.beginContour();
		p_contouringFxn.run();
		this.GRAPHICS.endContour();
	}

	// region `PVector` overloads.
	public void arc(final PVector p_translation, final PVector p_size, final float p_startAngle,
			final float p_endAngle) {
		this.GRAPHICS.pushMatrix();
		this.translate(p_translation);
		this.GRAPHICS.arc(0, 0, p_size.x, p_size.y, p_startAngle, p_endAngle);
		this.GRAPHICS.popMatrix();
	}

	// Perhaps I should figure out the default arc mode and make the upper one call
	// this one?:
	public void arc(final PVector p_translation, final PVector p_size, final float p_startAngle, final float p_endAngle,
			final int p_mode) {
		this.GRAPHICS.pushMatrix();
		this.translate(p_translation);
		this.GRAPHICS.arc(0, 0, p_size.x, p_size.y, p_startAngle, p_endAngle, p_mode);
		this.GRAPHICS.popMatrix();
	}

	public void circle(final PVector p_pos, final float p_size) {
		this.GRAPHICS.pushMatrix();
		this.translate(p_pos);
		this.GRAPHICS.circle(0, 0, p_size);
		this.GRAPHICS.popMatrix();
	}

	// region E L L I P S E S.
	// ...For when you want to use an ellipse like a circle:
	public void ellipse(final float p_x, final float p_y, final PVector p_dimensions) {
		this.GRAPHICS.ellipse(p_x, p_y, p_dimensions.x, p_dimensions.y);
	}

	// OpenGL-specific:
	// public void ellipse(final float p_x, final float p_y, final float p_z, final
	// float p_width, final float p_height) {
	// this.GRAPHICS.pushMatrix();
	// this.translate(p_x, p_y, p_z);
	// this.GRAPHICS.ellipse(0, 0, p_width, p_height);
	// this.GRAPHICS.popMatrix();
	// }

	// public void ellipse(final float p_x, final float p_y, final float p_z, final
	// PVector p_dimensions) {
	// this.GRAPHICS.pushMatrix();
	// this.translate(p_x, p_y, p_z);
	// this.GRAPHICS.ellipse(0, 0, p_dimensions.x, p_dimensions.y);
	// this.GRAPHICS.popMatrix();
	// }

	public void ellipse(final PVector p_pos, final float p_size) {
		this.GRAPHICS.pushMatrix();
		this.translate(p_pos);
		this.GRAPHICS.ellipse(0, 0, p_size, p_size);
		this.GRAPHICS.popMatrix();
	}

	public void ellipse(final PVector p_pos, final float p_width, final float p_height) {
		this.GRAPHICS.pushMatrix();
		this.translate(p_pos);
		this.GRAPHICS.ellipse(0, 0, p_width, p_height);
		this.GRAPHICS.popMatrix();
	}

	public void ellipse(final PVector p_pos, final PVector p_dimensions) {
		this.GRAPHICS.pushMatrix();
		this.translate(p_pos);
		this.GRAPHICS.ellipse(0, 0, p_dimensions.x, p_dimensions.y);
		this.GRAPHICS.popMatrix();
	}
	// endregion

	public void line(final PVector p_from, final PVector p_to) {
		// `z`-coordinate of first and THEN the second point!:
		this.GRAPHICS.line(p_from.x, p_from.y, p_to.x, p_to.y, p_from.z, p_to.y);
	}

	public void lineInDir/* `lineInDirOfLength` */(final PVector p_start, final PVector p_dir, final float p_length) {
		// `z`-coordinate of first and THEN the second point!:
		this.GRAPHICS.line(p_start.x, p_start.y, p_start.x + p_dir.x * p_length, p_start.y + p_dir.y * p_length,
				// `z` stuff!:
				p_start.z, p_start.z + p_dir.z * p_length);
	}

	public void line2d(final PVector p_from, final PVector p_to) {
		this.GRAPHICS.line(p_from.x, p_from.y, p_to.x, p_to.y);
	}

	// region `radialLine*d()`!
	public void radialLine2d(final PVector p_from, final float p_angle) {
		this.GRAPHICS.line(p_from.x, p_from.y, PApplet.sin(p_angle), PApplet.cos(p_angle));
	}

	public void radialLine2d(final PVector p_from, final float p_x, final float p_y, final float p_length) {
		this.GRAPHICS.line(p_from.x, p_from.y, p_from.x + PApplet.sin(p_x) * p_length,
				p_from.y + PApplet.cos(p_y) * p_length);
	}

	public void radialLine2d(final PVector p_from, final float p_x, final float p_y, final float p_xLen,
			final float p_yLen) {
		this.GRAPHICS.line(p_from.x, p_from.y, p_from.x + PApplet.sin(p_x) * p_xLen,
				p_from.y + PApplet.cos(p_y) * p_yLen);
	}

	public void radialLine2d(final PVector p_from, final PVector p_trigVals, final float p_size) {
		this.line(p_from.x, p_from.y, p_trigVals.x * p_size, p_trigVals.y * p_size);
	}

	public void radialLine2d(final PVector p_from, final PVector p_values) {
		this.radialLine2d(p_from, p_values, p_values.z);
	}

	public void radialLine3d(final PVector p_from, final float p_theta, final float p_phi, final float p_length) {
		final float sineOfPitch = PApplet.sin(p_theta);
		this.GRAPHICS.line(p_from.x, p_from.y, p_from.x + sineOfPitch * PApplet.cos(p_phi) * p_length,
				p_from.x + sineOfPitch * PApplet.sin(p_phi) * p_length, p_from.z,
				p_from.x + PApplet.cos(p_theta) * p_length);
	}

	public void radialLine3d(final PVector p_from, final float p_theta, final float p_phi, final float p_xLen,
			final float p_yLen, final float p_zLen) {
		final float sineOfPitch = PApplet.sin(p_theta);
		this.GRAPHICS.line(p_from.x, p_from.y, p_from.x + sineOfPitch * PApplet.cos(p_phi) * p_xLen,
				p_from.x + sineOfPitch * PApplet.sin(p_phi) * p_yLen, p_from.z,
				p_from.x + PApplet.cos(p_theta) * p_zLen);
	}
	// endregion

	public void point(final PVector p_pos) {
		this.GRAPHICS.point(p_pos.x, p_pos.y, p_pos.z);
	}

	public void point2d(final PVector p_pos) {
		this.GRAPHICS.point(p_pos.x, p_pos.y, 0);
	}

	public void quad(final PVector p_first, final PVector p_second, final PVector p_third, final PVector p_fourth) {
		this.GRAPHICS.quad(p_first.x, p_first.y, p_second.x, p_second.y, p_third.x, p_third.y, p_fourth.x, p_first.y);
	}

	// region `rect()` overloads, ;)!
	// OpenGL-specific:
	// public void rect(final float p_x, final float p_y, final float p_z, final
	// float p_width, final float p_height) {
	// this.GRAPHICS.pushMatrix();
	// this.translate(p_x, p_y, p_z);
	// this.GRAPHICS.rect(0, 0, p_width, p_height);
	// this.GRAPHICS.popMatrix();
	// }

	// public void rect(final float p_x, final float p_y, final float p_z, final
	// float p_width, final float p_height,
	// final float p_radius) {
	// this.GRAPHICS.pushMatrix();
	// this.translate(p_x, p_y, p_z);
	// this.GRAPHICS.rect(0, 0, p_width, p_height, p_radius);
	// this.GRAPHICS.popMatrix();
	// }

	public void rect(final float p_x, final float p_y, final float p_z, final float p_width, final float p_height,
			final float p_topLeftRadius, final float p_topRightRadius, final float p_bottomRightRadius,
			final float p_bottomLeftRadius) {
		this.GRAPHICS.pushMatrix();
		this.GRAPHICS.translate(p_x, p_y, p_z);
		this.GRAPHICS.rect(0, 0, p_width, p_height, p_topLeftRadius, p_topRightRadius, p_bottomRightRadius,
				p_bottomLeftRadius);
		this.GRAPHICS.popMatrix();
	}

	public void rect(final float p_x, final float p_y, final float p_z, final float p_width, final float p_height,
			final PVector p_topRadii, final PVector p_bottomRadii) {
		this.GRAPHICS.pushMatrix();
		this.GRAPHICS.translate(p_x, p_y, p_z);
		this.GRAPHICS.rect(0, 0, p_width, p_height, p_topRadii.x, p_topRadii.y, p_bottomRadii.x, p_bottomRadii.y);
		this.GRAPHICS.popMatrix();
	}

	public void rect(final float p_x, final float p_y, final PVector p_dimensions) {
		this.GRAPHICS.rect(p_x, p_y, p_dimensions.x, p_dimensions.y);
	}

	public void rect(final float p_x, final float p_y, final PVector p_dimensions, final float p_radius) {
		this.GRAPHICS.rect(p_x, p_y, p_dimensions.x, p_dimensions.y, p_radius);
	}

	public void rect(final float p_x, final float p_y, final float p_z, final PVector p_dimensions,
			final float p_topLeftRadius, final float p_topRightRadius, final float p_bottomRightRadius,
			final float p_bottomLeftRadius) {
		this.GRAPHICS.pushMatrix();
		this.GRAPHICS.translate(p_x, p_y, p_z);
		this.GRAPHICS.rect(0, 0, p_dimensions.x, p_dimensions.y, p_topLeftRadius, p_topRightRadius, p_bottomRightRadius,
				p_bottomLeftRadius);
		this.GRAPHICS.popMatrix();
	}

	public void rect(final float p_x, final float p_y, final float p_z, final PVector p_dimensions,
			final PVector p_topRadii, final PVector p_bottomRadii) {
		this.GRAPHICS.pushMatrix();
		this.GRAPHICS.translate(p_x, p_y, p_z);
		this.GRAPHICS.rect(0, 0, p_dimensions.x, p_dimensions.y, p_topRadii.x, p_topRadii.y, p_bottomRadii.x,
				p_bottomRadii.y);
		this.GRAPHICS.popMatrix();
	}

	public void rect(final PVector p_pos, final float p_width, final float p_height) {
		this.GRAPHICS.pushMatrix();
		this.translate(p_pos);
		this.GRAPHICS.rect(0, 0, p_width, p_height);
		this.GRAPHICS.popMatrix();
	}

	public void rect(final PVector p_pos, final float p_width, final float p_height, final float p_radius) {
		this.GRAPHICS.pushMatrix();
		this.translate(p_pos);
		this.GRAPHICS.rect(0, 0, p_width, p_height, p_radius);
		this.GRAPHICS.popMatrix();
	}

	public void rect(final PVector p_pos, final float p_width, final float p_height, final float p_topLeftRadius,
			final float p_topRightRadius, final float p_bottomRightRadius, final float p_bottomLeftRadius) {
		this.GRAPHICS.pushMatrix();
		this.translate(p_pos);
		this.GRAPHICS.rect(0, 0, p_width, p_height, p_topLeftRadius, p_topRightRadius, p_bottomRightRadius,
				p_bottomLeftRadius);
		this.GRAPHICS.popMatrix();
	}

	public void rect(final PVector p_pos, final float p_width, final float p_height, final PVector p_topRadii,
			final PVector p_bottomRadii) {
		this.GRAPHICS.pushMatrix();
		this.translate(p_pos);
		this.GRAPHICS.rect(0, 0, p_width, p_height, p_topRadii.x, p_topRadii.y, p_bottomRadii.x, p_bottomRadii.y);
		this.GRAPHICS.popMatrix();
	}

	public void rect(final PVector p_pos, final PVector p_dimensions) {
		this.GRAPHICS.pushMatrix();
		this.translate(p_pos);
		this.GRAPHICS.rect(0, 0, p_dimensions.x, p_dimensions.y);
		this.GRAPHICS.popMatrix();
	}

	public void rect(final PVector p_pos, final PVector p_dimensions, final float p_radius) {
		this.GRAPHICS.pushMatrix();
		this.translate(p_pos);
		this.GRAPHICS.rect(0, 0, p_dimensions.x, p_dimensions.y, p_radius);
		this.GRAPHICS.popMatrix();
	}

	public void rect(final PVector p_pos, final PVector p_dimensions, final float p_topLeftRadius,
			final float p_topRightRadius, final float p_bottomRightRadius, final float p_bottomLeftRadius) {
		this.GRAPHICS.pushMatrix();
		this.translate(p_pos);
		this.GRAPHICS.rect(0, 0, p_dimensions.x, p_dimensions.y, p_topLeftRadius, p_topRightRadius, p_bottomRightRadius,
				p_bottomLeftRadius);
		this.GRAPHICS.popMatrix();
	}

	public void rect(final PVector p_pos, final PVector p_dimensions, final PVector p_topRadii,
			final PVector p_bottomRadii) {
		this.GRAPHICS.pushMatrix();
		this.translate(p_pos);
		this.GRAPHICS.rect(0, 0, p_dimensions.x, p_dimensions.y, p_topRadii.x, p_topRadii.y, p_bottomRadii.x,
				p_bottomRadii.y);
		this.GRAPHICS.popMatrix();
	}
	// endregion

	public void square(final PVector p_pos, final float p_size) {
		this.GRAPHICS.pushMatrix();
		this.GRAPHICS.translate(p_pos.x, p_pos.y, p_pos.z);
		this.GRAPHICS.square(0, 0, p_size);
		this.GRAPHICS.popMatrix();
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
		this.GRAPHICS.triangle(p_v1.x, p_v1.y, p_v2.x, p_v2.y, p_v3.x, p_v3.y);
	}
	// endregion
	// endregion
	// endregion

	/**
	 * Draws the {@code p_bgImage} as if it was a background. You may even choose to
	 * call one of the {@linkplain PApplet#tint() PApplet::tint()} overloads before
	 * calling this!
	 */
	public void background(final PImage p_bgImage) {
		Objects.requireNonNull(p_bgImage);
		this.GRAPHICS.image(p_bgImage, this.WINDOW.cx, this.WINDOW.cy, this.WINDOW.width,
				this.WINDOW.height);
	}

	// region Transformations!
	// "Hah! Gott'em with the name alignment!"
	public void translate(final PVector p_vec) {
		this.GRAPHICS.translate(p_vec.x, p_vec.y, p_vec.z);
	}

	public void scale(final PVector p_scaling) {
		this.GRAPHICS.scale(p_scaling.x, p_scaling.y, p_scaling.z);
	}

	public void rotate(final PVector p_rotVec) {
		this.GRAPHICS.rotateX(p_rotVec.x);
		this.GRAPHICS.rotateY(p_rotVec.y);
		this.GRAPHICS.rotateZ(p_rotVec.z);
	}

	public void rotate(final float p_x, final float p_y, final float p_z) {
		this.GRAPHICS.rotateX(p_x);
		this.GRAPHICS.rotateY(p_y);
		this.GRAPHICS.rotateZ(p_z);
	}
	// endregion

	// region The billion `image()` overloads. Help me make "standards"?
	// region For `PImage`s.
	public void image(final PImage p_image) {
		// https://processing.org/reference/set_.html.
		// Faster than `image()`!:
		// `this.GRAPHICS.set(0, 0, p_image);`
		// However, we also need to remember that it doesn't render the image on to a
		// quad, meaning that transformations won't apply.
		this.GRAPHICS.image(p_image, 0, 0);
	}

	/**
	 * @param p_side The length of the side of the square.
	 */
	public void image(final PImage p_image, final float p_side) {
		this.GRAPHICS.image(p_image, 0, 0, p_side, p_side);
	}

	public void image(final PImage p_image, final PVector p_pos) {
		this.GRAPHICS.pushMatrix();
		this.GRAPHICS.translate(p_pos.x, p_pos.y, p_pos.z);
		this.GRAPHICS.image(p_image, 0, 0);
		this.GRAPHICS.popMatrix();
	}

	public void image(final PImage p_image, final PVector p_pos, final float p_size) {
		this.GRAPHICS.pushMatrix();
		this.GRAPHICS.translate(p_pos.x, p_pos.y, p_pos.z);
		this.image(p_image, p_pos.x, p_pos.y, p_size, p_size);
		this.GRAPHICS.popMatrix();
	}

	public void image(final PImage p_image, final float p_x, final float p_y, final float p_z) {
		this.GRAPHICS.pushMatrix();
		this.GRAPHICS.translate(p_x, p_y, p_z);
		this.GRAPHICS.image(p_image, 0, 0);
		this.GRAPHICS.popMatrix();
	}
	// endregion

	// region For `PGraphics`.
	public void image(final PGraphics p_graphics) {
		this.GRAPHICS.image(p_graphics, 0, 0);
	}

	public void image(final PGraphics p_graphics, final PVector p_pos) {
		this.GRAPHICS.pushMatrix();
		this.GRAPHICS.translate(p_pos.x, p_pos.y, p_pos.z);
		this.GRAPHICS.image(p_graphics, 0, 0);
		this.GRAPHICS.popMatrix();
	}

	public void image(final PGraphics p_graphics, final float p_scale) {
		this.GRAPHICS.image(p_graphics, 0, 0, p_scale, p_scale);
	}

	public void image(final PGraphics p_graphics, final PVector p_pos, final float p_scale) {
		this.GRAPHICS.pushMatrix();
		this.GRAPHICS.translate(p_pos.x, p_pos.y, p_pos.z);
		this.image(p_graphics, 0, 0, p_scale, p_scale);
		this.GRAPHICS.popMatrix();
	}

	public void image(final PGraphics p_graphics, final float p_x, final float p_y, final float p_z) {
		this.image((PImage) p_graphics, p_x, p_y, p_z);
	}

	public void image(final PGraphics p_graphics, final PVector p_pos, final float p_width, final float p_height) {
		this.GRAPHICS.pushMatrix();
		this.GRAPHICS.translate(p_pos.x, p_pos.y, p_pos.z);
		this.image(p_graphics, p_pos.x, p_pos.y, p_width, p_height);
		this.GRAPHICS.popMatrix();
	}
	// endregion

	// region For `NerdGraphics`.
	public void image(final NerdGenericGraphics<SketchPGraphicsT> p_graphics) {
		this.GRAPHICS.image(p_graphics.getUnderlyingBuffer(), 0, 0);
	}

	public void image(final NerdGenericGraphics<SketchPGraphicsT> p_graphics, final float p_x, final float p_y,
			final float p_width,
			final float p_height) {
		this.GRAPHICS.image(p_graphics.getUnderlyingBuffer(), p_x, p_y, p_width, p_height);
	}

	public void image(final NerdGenericGraphics<SketchPGraphicsT> p_graphics, final PVector p_pos) {
		this.GRAPHICS.pushMatrix();
		this.GRAPHICS.translate(p_pos.x, p_pos.y, p_pos.z);
		this.GRAPHICS.image(p_graphics.getUnderlyingBuffer(), 0, 0);
		this.GRAPHICS.popMatrix();
	}

	public void image(final NerdGenericGraphics<SketchPGraphicsT> p_graphics, final float p_scale) {
		this.GRAPHICS.image(p_graphics.getUnderlyingBuffer(), 0, 0, p_scale, p_scale);
	}

	public void image(final NerdGenericGraphics<SketchPGraphicsT> p_graphics, final float p_x, final float p_y) {
		this.image(p_graphics.getUnderlyingBuffer(), p_x, p_y);
	}

	public void image(final NerdGenericGraphics<SketchPGraphicsT> p_graphics, final PVector p_pos,
			final float p_scale) {
		this.GRAPHICS.pushMatrix();
		this.GRAPHICS.translate(p_pos.x, p_pos.y, p_pos.z);
		this.image(p_graphics.getUnderlyingBuffer(), 0, 0, p_scale, p_scale);
		this.GRAPHICS.popMatrix();
	}

	public void image(final NerdGenericGraphics<SketchPGraphicsT> p_graphics, final float p_x, final float p_y,
			final float p_z) {
		this.image((PImage) p_graphics.getUnderlyingBuffer(), p_x, p_y, p_z);
	}

	public void image(final NerdGenericGraphics<SketchPGraphicsT> p_graphics, final PVector p_pos, final float p_width,
			final float p_height) {
		this.GRAPHICS.pushMatrix();
		this.GRAPHICS.translate(p_pos.x, p_pos.y, p_pos.z);
		this.image(p_graphics.getUnderlyingBuffer(), p_pos.x, p_pos.y, p_width, p_height);
		this.GRAPHICS.popMatrix();
	}
	// endregion
	// endregion

	// region `push()` and `pop()` simply don't work in `PApplet`,
	// ...so I re-wrote them myself!
	public void push() {
		this.GRAPHICS.pushMatrix();
		this.GRAPHICS.pushStyle();
	}

	public void pop() {
		this.GRAPHICS.popStyle();
		this.GRAPHICS.popMatrix();
	}
	// endregion
	// endregion

	// TODO: Get these bois going:
	private void updateParameters() {
		this.width = this.GRAPHICS.width;
		this.height = this.GRAPHICS.height;

		this.dbx = this.width * 2.0f;
		this.dby = this.height * 2.0f;

		this.cx = this.width * 0.5f;
		this.cy = this.height * 0.5f;

		this.qx = this.cx * 0.5f;
		this.qy = this.cy * 0.5f;

		this.q3x = this.cx + this.qx;
		this.q3y = this.cy + this.qy;

		this.scr = (float) this.width / (float) this.height;
	}

	private void recordCurrentParameters() {
		this.pwidth = this.width;
		this.pheight = this.height;

		this.pdbx = this.dbx;
		this.pdby = this.dby;

		this.pcx = this.cx;
		this.pcy = this.cy;

		this.pqx = this.qx;
		this.pqy = this.qy;

		this.pq3x = this.q3x;
		this.pq3y = this.q3y;

		this.pscr = this.scr;
	}

	// region `translateToCenter()` and `translateFromCenter()`.
	public void translateToCenter() {
		this.GRAPHICS.translate(this.cx, this.cy);
	}

	public void translateX(final float p_value) {
		this.GRAPHICS.translate(p_value, 0);
	}

	public void translateY(final float p_value) {
		this.GRAPHICS.translate(0, p_value);
	}

	public void translateFromCenterX(final float p_value) {
		this.GRAPHICS.translate(this.cx + p_value, this.cy);
	}

	public void translateFromCenterY(final float p_value) {
		this.GRAPHICS.translate(this.cx, this.cy + p_value);
	}

	public void translateFromCenter(final float p_x, final float p_y) {
		this.GRAPHICS.translate(this.cx + p_x, this.cy + p_y);
	}
	// endregion

	// region Text!
	/**
	 * @return the height of text
	 */
	public float textHeight() {
		return this.GRAPHICS.textAscent() - this.GRAPHICS.textDescent();
	}

	/**
	 * Translates by the width of {@code p_text} halved, and the current text
	 * height, halved, before actually rendering
	 * the text.
	 *
	 * @param p_text is the text to render.
	 *
	 * @see {@linkplain NerdGenericGraphics#textHeight()
	 *      NerdGenericGraphics::textHeight()},
	 * @see {@linkplain PGraphics#textWidth(String) PGraphics::textWidth(String)}.
	 */
	public void centeredText(final String p_text) {
		this.GRAPHICS.text(p_text, this.GRAPHICS.textWidth(p_text) * 0.5f, this.textHeight() * 0.5f);
	}
	// endregion

	// region `background()`-with-alpha overloads.
	public void background(final int p_color) {
		this.push();
		this.GRAPHICS.fill(p_color);
		this.backgroundWithAlphaRectRenderingImpl();
	}

	public void background(final int p_color, final float p_alpha) {
		this.push();
		this.GRAPHICS.fill(p_color, p_alpha);
		this.backgroundWithAlphaRectRenderingImpl();
	}

	public void background(final float p_grey, final float p_alpha) {
		this.push();
		this.GRAPHICS.fill(p_grey, p_alpha);
		this.backgroundWithAlphaRectRenderingImpl();
	}

	public void background(final float p_v1, final float p_v2, final float p_v3, final float p_alpha) {
		this.push();
		this.GRAPHICS.fill(p_v1, p_v2, p_v3, p_alpha);
		this.backgroundWithAlphaRectRenderingImpl();
	}

	protected void backgroundWithAlphaInitialPushMethod() {
		this.push();
	}

	/**
	 * This method provides an implementation for how overloads of
	 * {@linkplain PGraphics#background() PGraphics::background()} that have an
	 * alpha value are rendered after a color has been chosen using
	 * {@linkplain PGraphics#fill() PGraphics::fill()}.
	 * <p>
	 * {@link NerdP3dGraphics} call {@linkplain PGraphics#camera()
	 * PGraphics::camera()}, while {@link NerdP2dGraphics} ones don't.
	 */
	protected void backgroundWithAlphaRectRenderingImpl() {
		// Removing this will not display the previous camera's view,
		// but still show clipping:

		// this.GRAPHICS.camera(); // Nuh-uh, a-uh! Not in `P2D`, my friend!
		this.GRAPHICS.noStroke();
		this.GRAPHICS.rectMode(PConstants.CORNER);
		this.GRAPHICS.rect(0, 0, this.GRAPHICS.width, this.GRAPHICS.height);
		this.pop();
	}
	// endregion
	// endregion

	// region ...From `PGraphics`.
	// OpenGL-specific:
	// public void ambient(final int rgb) {
	// this.GRAPHICS.ambient(rgb);
	// }

	// public void ambient(final float gray) {
	// this.GRAPHICS.ambient(gray);
	// }

	// public void ambient(final float v1, final float v2, final float v3) {
	// this.GRAPHICS.ambient(v1, v2, v3);
	// }

	// public void ambientLight(final float v1, final float v2, final float v3) {
	// this.GRAPHICS.ambientLight(v1, v2, v3);
	// }

	// public void ambientLight(final float v1, final float v2, final float v3,
	// final float x, final float y,
	// final float z) {
	// this.GRAPHICS.ambientLight(v1, v2, v3, x, y, z);
	// }

	// public void applyMatrix(final PMatrix source) {
	// this.GRAPHICS.applyMatrix(source);
	// }

	// public void applyMatrix(final PMatrix2D source) {
	// this.GRAPHICS.applyMatrix(source);
	// }

	// public void applyMatrix(final PMatrix3D source) {
	// this.GRAPHICS.applyMatrix(source);
	// }

	// public void applyMatrix(final float n00, final float n01, final float n02,
	// final float n10, final float n11,
	// final float n12) {
	// this.GRAPHICS.applyMatrix(n00, n01, n02, n10, n11, n12);
	// }

	// public void applyMatrix(final float n00, final float n01, final float n02,
	// final float n03, final float n10,
	// final float n11, final float n12, final float n13, final float n20, final
	// float n21, final float n22,
	// final float n23, final float n30, final float n31, final float n32, final
	// float n33) {
	// this.GRAPHICS.applyMatrix(n00, n01, n02, n03, n10, n11, n12, n13, n20, n21,
	// n22, n23, n30, n31, n32, n33);
	// }

	public void arc(final float a, final float b, final float c, final float d, final float start, final float stop) {
		this.GRAPHICS.arc(a, b, c, d, start, stop);
	}

	public void arc(final float a, final float b, final float c, final float d, final float start, final float stop,
			final int mode) {
		this.GRAPHICS.arc(a, b, c, d, start, stop, mode);
	}

	// OpenGL-specific:
	// public void attrib(final String name, final float... values) {
	// this.GRAPHICS.attrib(name, values);
	// }

	// public void attrib(final String name, final int... values) {
	// this.GRAPHICS.attrib(name, values);
	// }

	// public void attrib(final String name, final boolean... values) {
	// this.GRAPHICS.attrib(name, values);
	// }

	// public void attribColor(final String name, final int color) {
	// this.GRAPHICS.attribColor(name, color);
	// }

	// public void attribNormal(final String name, final float nx, final float ny,
	// final float nz) {
	// this.GRAPHICS.attribNormal(name, nx, ny, nz);
	// }

	// public void attribPosition(final String name, final float x, final float y,
	// final float z) {
	// this.GRAPHICS.attribPosition(name, x, y, z);
	// }

	public void background(final float p_color) {
		this.GRAPHICS.background(p_color);
	}

	public void background(final float p_v1, final float p_v2, final float p_v3) {
		this.GRAPHICS.background(p_v1, p_v2, p_v3);
	}

	// OpenGL-specific:
	// public void beginCamera() {
	// this.GRAPHICS.beginCamera();
	// }

	public void beginContour() {
		this.GRAPHICS.beginContour();
	}

	public void beginDraw() {
		this.GRAPHICS.beginDraw();
	}

	// OpenGL-specific:
	// public PGL beginPGL() {
	// return this.GRAPHICS.beginPGL();
	// }

	public void beginRaw(final PGraphics rawGraphics) {
		this.GRAPHICS.beginRaw(rawGraphics);
	}

	public void beginShape() {
		this.GRAPHICS.beginShape();
	}

	public void beginShape(final int kind) {
		this.GRAPHICS.beginShape(kind);
	}

	public void bezier(final float x1, final float y1, final float x2, final float y2, final float x3, final float y3,
			final float x4, final float y4) {
		this.GRAPHICS.bezier(x1, y1, x2, y2, x3, y3, x4, y4);
	}

	// OpenGL-specific:
	// public void bezier(final float x1, final float y1, final float z1, final
	// float x2, final float y2, final float z2,
	// final float x3, final float y3, final float z3, final float x4, final float
	// y4, final float z4) {
	// this.GRAPHICS.bezier(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4);
	// }

	public void bezierDetail(final int detail) {
		this.GRAPHICS.bezierDetail(detail);
	}

	public float bezierPoint(final float a, final float b, final float c, final float d, final float t) {
		return this.GRAPHICS.bezierPoint(a, b, c, d, t);
	}

	public float bezierTangent(final float a, final float b, final float c, final float d, final float t) {
		return this.GRAPHICS.bezierTangent(a, b, c, d, t);
	}

	public void bezierVertex(final float x2, final float y2, final float x3, final float y3, final float x4,
			final float y4) {
		this.GRAPHICS.bezierVertex(x2, y2, x3, y3, x4, y4);
	}

	// OpenGL-specific:
	// public void bezierVertex(final float x2, final float y2, final float z2,
	// final float x3, final float y3,
	// final float z3, final float x4, final float y4, final float z4) {
	// this.GRAPHICS.bezierVertex(x2, y2, z2, x3, y3, z3, x4, y4, z4);
	// }

	public void blendMode(final int mode) {
		this.GRAPHICS.blendMode(mode);
	}

	// OpenGL-specific:
	// public void box(final float size) {
	// this.GRAPHICS.box(size);
	// }

	// public void box(final float w, final float h, final float d) {
	// this.GRAPHICS.box(w, h, d);
	// }

	// public void camera() {
	// this.GRAPHICS.camera();
	// }

	// public void camera(final float eyeX, final float eyeY, final float eyeZ,
	// final float centerX, final float centerY,
	// final float centerZ, final float upX, final float upY, final float upZ) {
	// this.GRAPHICS.camera(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY,
	// upZ);
	// }

	// public void circle(final float x, final float y, final float extent) {
	// this.GRAPHICS.circle(x, y, extent);
	// }

	public void clear() {
		this.GRAPHICS.clear();
	}

	public void clip(final float a, final float b, final float c, final float d) {
		this.GRAPHICS.clip(a, b, c, d);
	}

	public void colorMode(final int mode) {
		this.GRAPHICS.colorMode(mode);
	}

	public void colorMode(final int mode, final float max) {
		this.GRAPHICS.colorMode(mode, max);
	}

	public void colorMode(final int mode, final float max1, final float max2, final float max3) {
		this.GRAPHICS.colorMode(mode, max1, max2, max3);
	}

	public void colorMode(final int mode, final float max1, final float max2, final float max3, final float maxA) {
		this.GRAPHICS.colorMode(mode, max1, max2, max3, maxA);
	}

	public PShape createShape() {
		return this.GRAPHICS.createShape();
	}

	public PShape createShape(final int type) {
		return this.GRAPHICS.createShape(type);
	}

	public PShape createShape(final int kind, final float... p) {
		return this.GRAPHICS.createShape(kind, p);
	}

	public PSurface createSurface() {
		return this.GRAPHICS.createSurface();
	}

	public void curve(final float x1, final float y1, final float x2, final float y2, final float x3, final float y3,
			final float x4, final float y4) {
		this.GRAPHICS.curve(x1, y1, x2, y2, x3, y3, x4, y4);
	}

	// public void curve(final float x1, final float y1, final float z1, final float
	// x2, final float y2, final float z2,
	// final float x3, final float y3, final float z3, final float x4, final float
	// y4, final float z4) {
	// this.GRAPHICS.curve(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4);
	// }

	public void curveDetail(final int detail) {
		this.GRAPHICS.curveDetail(detail);
	}

	public float curvePoint(final float a, final float b, final float c, final float d, final float t) {
		return this.GRAPHICS.curvePoint(a, b, c, d, t);
	}

	public float curveTangent(final float a, final float b, final float c, final float d, final float t) {
		return this.GRAPHICS.curveTangent(a, b, c, d, t);
	}

	public void curveTightness(final float tightness) {
		this.GRAPHICS.curveTightness(tightness);
	}

	public void curveVertex(final float x, final float y) {
		this.GRAPHICS.curveVertex(x, y);
	}

	// OpenGL-specific:
	// public void curveVertex(final float x, final float y, final float z) {
	// this.GRAPHICS.curveVertex(x, y, z);
	// }

	// public void directionalLight(final float v1, final float v2, final float v3,
	// final float nx, final float ny,
	// final float nz) {
	// this.GRAPHICS.directionalLight(v1, v2, v3, nx, ny, nz);
	// }

	public boolean displayable() {
		return this.GRAPHICS.displayable();
	}

	public void dispose() {
		this.GRAPHICS.dispose();
	}

	public void edge(final boolean edge) {
		this.GRAPHICS.edge(edge);
	}

	public void ellipse(final float a, final float b, final float c, final float d) {
		this.GRAPHICS.ellipse(a, b, c, d);
	}

	public void ellipseMode(final int mode) {
		this.GRAPHICS.ellipseMode(mode);
	}

	public void emissive(final int rgb) {
		this.GRAPHICS.emissive(rgb);
	}

	public void emissive(final float gray) {
		this.GRAPHICS.emissive(gray);
	}

	public void emissive(final float v1, final float v2, final float v3) {
		this.GRAPHICS.emissive(v1, v2, v3);
	}

	public void endCamera() {
		this.GRAPHICS.endCamera();
	}

	public void endContour() {
		this.GRAPHICS.endContour();
	}

	public void endDraw() {
		this.GRAPHICS.endDraw();
	}

	public void endPGL() {
		this.GRAPHICS.endPGL();
	}

	public void endRaw() {
		this.GRAPHICS.endRaw();
	}

	public void endShape() {
		this.GRAPHICS.endShape();
	}

	public void endShape(final int mode) {
		this.GRAPHICS.endShape(mode);
	}

	public void fill(final int rgb) {
		this.GRAPHICS.fill(rgb);
	}

	public void fill(final float gray) {
		this.GRAPHICS.fill(gray);
	}

	public void fill(final int rgb, final float alpha) {
		this.GRAPHICS.fill(rgb, alpha);
	}

	public void fill(final float gray, final float alpha) {
		this.GRAPHICS.fill(gray, alpha);
	}

	public void fill(final float v1, final float v2, final float v3) {
		this.GRAPHICS.fill(v1, v2, v3);
	}

	public void fill(final float v1, final float v2, final float v3, final float alpha) {
		this.GRAPHICS.fill(v1, v2, v3, alpha);
	}

	// OpenGL-specific:
	// public void filter(final PShader shader) {
	// this.GRAPHICS.filter(shader);
	// }

	public void flush() {
		this.GRAPHICS.flush();
	}

	// OpenGL-specific:
	// public void frustum(final float left, final float right, final float bottom,
	// final float top, final float near,
	// final float far) {
	// this.GRAPHICS.frustum(left, right, bottom, top, near, far);
	// }

	public Object getCache(final PImage image) {
		return this.GRAPHICS.getCache(image);
	}

	// OpenGL-specific:
	// public PMatrix getMatrix() {
	// return this.GRAPHICS.getMatrix();
	// }

	// public PMatrix2D getMatrix(final PMatrix2D target) {
	// return this.GRAPHICS.getMatrix(target);
	// }

	// public PMatrix3D getMatrix(final PMatrix3D target) {
	// return this.GRAPHICS.getMatrix(target);
	// }

	public PGraphics getRaw() {
		return this.GRAPHICS.getRaw();
	}

	public PStyle getStyle() {
		return this.GRAPHICS.getStyle();
	}

	public PStyle getStyle(final PStyle s) {
		return this.GRAPHICS.getStyle(s);
	}

	public boolean haveRaw() {
		return this.GRAPHICS.haveRaw();
	}

	public void hint(final int which) {
		this.GRAPHICS.hint(which);
	}

	public void image(final PImage img, final float a, final float b) {
		this.GRAPHICS.image(img, a, b);
	}

	public void image(final PImage img, final float a, final float b, final float c, final float d) {
		this.GRAPHICS.image(img, a, b, c, d);
	}

	public void image(final PImage img, final float a, final float b, final float c, final float d, final int u1,
			final int v1, final int u2, final int v2) {
		this.GRAPHICS.image(img, a, b, c, d, u1, v1, u2, v2);
	}

	public void imageMode(final int mode) {
		this.GRAPHICS.imageMode(mode);
	}

	public boolean is2D() {
		return this.GRAPHICS.is2D();
	}

	public boolean is2X() {
		return this.GRAPHICS.is2X();
	}

	public boolean is3D() {
		return this.GRAPHICS.is3D();
	}

	public boolean isGL() {
		return this.GRAPHICS.isGL();
	}

	public int lerpColor(final int c1, final int c2, final float amt) {
		return this.GRAPHICS.lerpColor(c1, c2, amt);
	}

	// OpenGL-specific:
	// public void lightFalloff(final float constant, final float linear, final
	// float quadratic) {
	// this.GRAPHICS.lightFalloff(constant, linear, quadratic);
	// }

	// public void lightSpecular(final float v1, final float v2, final float v3) {
	// this.GRAPHICS.lightSpecular(v1, v2, v3);
	// }

	// public void lights() {
	// this.GRAPHICS.lights();
	// }

	public void line(final float x1, final float y1, final float x2, final float y2) {
		this.GRAPHICS.line(x1, y1, x2, y2);
	}

	// OpenGL-specific:
	// public void line(final float x1, final float y1, final float z1, final float
	// x2, final float y2, final float z2) {
	// this.GRAPHICS.line(x1, y1, z1, x2, y2, z2);
	// }

	// OpenGL-specific:
	// public PShader loadShader(final String fragFilename) {
	// return this.GRAPHICS.loadShader(fragFilename);
	// }

	// public PShader loadShader(final String fragFilename, final String
	// vertFilename) {
	// return this.GRAPHICS.loadShader(fragFilename, vertFilename);
	// }

	public PShape loadShape(final String filename) {
		return this.GRAPHICS.loadShape(filename);
	}

	public PShape loadShape(final String filename, final String options) {
		return this.GRAPHICS.loadShape(filename, options);
	}

	// OpenGL-specific:
	// public float modelX(final float x, final float y, final float z) {
	// return this.GRAPHICS.modelX(x, y, z);
	// }

	// public float modelY(final float x, final float y, final float z) {
	// return this.GRAPHICS.modelY(x, y, z);
	// }

	// public float modelZ(final float x, final float y, final float z) {
	// return this.GRAPHICS.modelZ(x, y, z);
	// }

	public void noClip() {
		this.GRAPHICS.noClip();
	}

	public void noFill() {
		this.GRAPHICS.noFill();
	}

	// OpenGL-specific:
	// public void noLights() {
	// this.GRAPHICS.noLights();
	// }

	public void noSmooth() {
		this.GRAPHICS.noSmooth();
	}

	public void noStroke() {
		this.GRAPHICS.noStroke();
	}

	public void noTexture() {
		this.GRAPHICS.noTexture();
	}

	public void noTint() {
		this.GRAPHICS.noTint();
	}

	public void normal(final float nx, final float ny, final float nz) {
		this.GRAPHICS.normal(nx, ny, nz);
	}

	// OpenGL-specific:
	// public void ortho() {
	// this.GRAPHICS.ortho();
	// }

	// public void ortho(final float left, final float right, final float bottom,
	// final float top, final float near,
	// final float far) {
	// this.GRAPHICS.ortho(left, right, bottom, top, near, far);
	// }

	// public void perspective() {
	// this.GRAPHICS.perspective();
	// }

	// public void perspective(final float fovy, final float aspect, final float
	// zNear, final float zFar) {
	// this.GRAPHICS.perspective(fovy, aspect, zNear, zFar);
	// }

	public void point(final float x, final float y) {
		this.GRAPHICS.point(x, y);
	}

	// OpenGL-specific:
	// public void point(final float x, final float y, final float z) {
	// this.GRAPHICS.point(x, y, z);
	// }

	// public void pointLight(final float v1, final float v2, final float v3,
	// final float x, final float y, final float z) {
	// this.GRAPHICS.pointLight(v1, v2, v3, x, y, z);
	// }

	public void popMatrix() {
		this.GRAPHICS.popMatrix();
	}

	public void popStyle() {
		this.GRAPHICS.popStyle();
	}

	// OpenGL-specific:
	// public void printCamera() {
	// this.GRAPHICS.printCamera();
	// }

	// public void printMatrix() {
	// this.GRAPHICS.printMatrix();
	// }

	// public void printProjection() {
	// this.GRAPHICS.printProjection();
	// }

	public void pushMatrix() {
		this.GRAPHICS.pushMatrix();
	}

	public void pushStyle() {
		this.GRAPHICS.pushStyle();
	}

	public void quad(final float x1, final float y1, final float x2, final float y2, final float x3, final float y3,
			final float x4, final float y4) {
		this.GRAPHICS.quad(x1, y1, x2, y2, x3, y3, x4, y4);
	}

	public void quadraticVertex(final float cx, final float cy, final float x3, final float y3) {
		this.GRAPHICS.quadraticVertex(cx, cy, x3, y3);
	}

	// OpenGL-specific:
	// public void quadraticVertex(final float cx, final float cy, final float cz,
	// final float x3, final float y3,
	// final float z3) {
	// this.GRAPHICS.quadraticVertex(cx, cy, cz, x3, y3, z3);
	// }

	public void rect(final float a, final float b, final float c, final float d) {
		this.GRAPHICS.rect(a, b, c, d);
	}

	public void rect(final float a, final float b, final float c, final float d, final float tl, final float tr,
			final float br, final float bl) {
		this.GRAPHICS.rect(a, b, c, d, tl, tr, br, bl);
	}

	public void rectMode(final int mode) {
		this.GRAPHICS.rectMode(mode);
	}

	public void removeCache(final PImage image) {
		this.GRAPHICS.removeCache(image);
	}

	public void resetMatrix() {
		this.GRAPHICS.resetMatrix();
	}

	public void resetShader() {
		this.GRAPHICS.resetShader();
	}

	public void resetShader(final int kind) {
		this.GRAPHICS.resetShader(kind);
	}

	public void rotate(final float angle) {
		this.GRAPHICS.rotate(angle);
	}

	// OpenGL-specific:
	// public void rotate(final float angle, final float x, final float y, final
	// float z) {
	// this.GRAPHICS.rotate(angle, x, y, z);
	// }

	public void rotateX(final float angle) {
		this.GRAPHICS.rotateX(angle);
	}

	public void rotateY(final float angle) {
		this.GRAPHICS.rotateY(angle);
	}

	public void rotateZ(final float angle) {
		this.GRAPHICS.rotateZ(angle);
	}

	// Disallowed by `SVG`/`PDF`:
	// public boolean save(final String filename) {
	// return this.GRAPHICS.save(filename);
	// }

	public void scale(final float s) {
		this.GRAPHICS.scale(s);
	}

	public void scale(final float x, final float y) {
		this.GRAPHICS.scale(x, y);
	}

	// OpenGL-specific:
	// public void scale(final float x, final float y, final float z) {
	// this.GRAPHICS.scale(x, y, z);
	// }

	// public float screenX(final float x, final float y) {
	// return this.GRAPHICS.screenX(x, y);
	// }

	// OpenGL-specific:
	// public float screenX(final float x, final float y, final float z) {
	// return this.GRAPHICS.screenX(x, y, z);
	// }

	// public float screenY(final float x, final float y) {
	// return this.GRAPHICS.screenY(x, y);
	// }

	// public float screenY(final float x, final float y, final float z) {
	// return this.GRAPHICS.screenY(x, y, z);
	// }

	// public float screenZ(final float x, final float y, final float z) {
	// return this.GRAPHICS.screenZ(x, y, z);
	// }

	public void setCache(final PImage image, final Object storage) {
		this.GRAPHICS.setCache(image, storage);
	}

	// OpenGL-specific:
	// public void setMatrix(final PMatrix source) {
	// this.GRAPHICS.setMatrix(source);
	// }

	// public void setMatrix(final PMatrix2D source) {
	// this.GRAPHICS.setMatrix(source);
	// }

	// public void setMatrix(final PMatrix3D source) {
	// this.GRAPHICS.setMatrix(source);
	// }

	public void setParent(final PApplet parent) {
		this.GRAPHICS.setParent(parent);
	}

	public void setPath(final String path) {
		this.GRAPHICS.setPath(path);
	}

	public void setPrimary(final boolean primary) {
		this.GRAPHICS.setPrimary(primary);
	}

	public void setSize(final int w, final int h) {
		this.GRAPHICS.setSize(w, h);
	}

	// OpenGL-specific:
	// public void shader(final PShader shader) {
	// this.GRAPHICS.shader(shader);
	// }

	// public void shader(final PShader shader, final int kind) {
	// this.GRAPHICS.shader(shader, kind);
	// }

	public void shape(final PShape shape) {
		this.GRAPHICS.shape(shape);
	}

	public void shape(final PShape shape, final float x, final float y) {
		this.GRAPHICS.shape(shape, x, y);
	}

	public void shape(final PShape shape, final float a, final float b, final float c, final float d) {
		this.GRAPHICS.shape(shape, a, b, c, d);
	}

	public void shapeMode(final int mode) {
		this.GRAPHICS.shapeMode(mode);
	}

	public void shearX(final float angle) {
		this.GRAPHICS.shearX(angle);
	}

	public void shearY(final float angle) {
		this.GRAPHICS.shearY(angle);
	}

	public void shininess(final float shine) {
		this.GRAPHICS.shininess(shine);
	}

	public void smooth() {
		this.GRAPHICS.smooth();
	}

	public void smooth(final int quality) {
		this.GRAPHICS.smooth(quality);
	}

	public void specular(final int rgb) {
		this.GRAPHICS.specular(rgb);
	}

	public void specular(final float gray) {
		this.GRAPHICS.specular(gray);
	}

	public void specular(final float v1, final float v2, final float v3) {
		this.GRAPHICS.specular(v1, v2, v3);
	}

	// OpenGL-specific:
	// public void sphere(final float r) {
	// this.GRAPHICS.sphere(r);
	// }

	// public void sphereDetail(final int res) {
	// this.GRAPHICS.sphereDetail(res);
	// }

	// public void sphereDetail(final int ures, final int vres) {
	// this.GRAPHICS.sphereDetail(ures, vres);
	// }

	// OpenGL-specific:
	// public void spotLight(final float v1, final float v2, final float v3, final
	// float x, final float y, final float z,
	// final float nx, final float ny, final float nz, final float angle, final
	// float concentration) {
	// this.GRAPHICS.spotLight(v1, v2, v3, x, y, z, nx, ny, nz, angle,
	// concentration);
	// }

	public void square(final float x, final float y, final float extent) {
		this.GRAPHICS.square(x, y, extent);
	}

	public void stroke(final int rgb) {
		this.GRAPHICS.stroke(rgb);
	}

	public void stroke(final float gray) {
		this.GRAPHICS.stroke(gray);
	}

	public void stroke(final int rgb, final float alpha) {
		this.GRAPHICS.stroke(rgb, alpha);
	}

	public void stroke(final float gray, final float alpha) {
		this.GRAPHICS.stroke(gray, alpha);
	}

	public void stroke(final float v1, final float v2, final float v3) {
		this.GRAPHICS.stroke(v1, v2, v3);
	}

	public void stroke(final float v1, final float v2, final float v3, final float alpha) {
		this.GRAPHICS.stroke(v1, v2, v3, alpha);
	}

	public void strokeCap(final int cap) {
		this.GRAPHICS.strokeCap(cap);
	}

	public void strokeJoin(final int join) {
		this.GRAPHICS.strokeJoin(join);
	}

	public void strokeWeight(final float weight) {
		this.GRAPHICS.strokeWeight(weight);
	}

	public void style(final PStyle s) {
		this.GRAPHICS.style(s);
	}

	public void text(final char c, final float x, final float y) {
		this.GRAPHICS.text(c, x, y);
	}

	public void text(final String str, final float x, final float y) {
		this.GRAPHICS.text(str, x, y);
	}

	public void text(final int num, final float x, final float y) {
		this.GRAPHICS.text(num, x, y);
	}

	public void text(final float num, final float x, final float y) {
		this.GRAPHICS.text(num, x, y);
	}

	// OpenGL-specific:
	// public void text(final char c, final float x, final float y, final float z) {
	// this.GRAPHICS.text(c, x, y, z);
	// }

	// public void text(final String str, final float x, final float y, final float
	// z) {
	// this.GRAPHICS.text(str, x, y, z);
	// }

	// public void text(final int num, final float x, final float y, final float z)
	// {
	// this.GRAPHICS.text(num, x, y, z);
	// }

	// public void text(final float num, final float x, final float y, final float
	// z) {
	// this.GRAPHICS.text(num, x, y, z);
	// }

	public void text(final char[] chars, final int start, final int stop, final float x, final float y) {
		this.GRAPHICS.text(chars, start, stop, x, y);
	}

	public void text(final String str, final float x1, final float y1, final float x2, final float y2) {
		this.GRAPHICS.text(str, x1, y1, x2, y2);
	}

	// OpenGL-specific:
	// public void text(final char[] chars, final int start, final int stop, final
	// float x, final float y, final float z) {
	// this.GRAPHICS.text(chars, start, stop, x, y, z);
	// }

	public void textAlign(final int alignX) {
		this.GRAPHICS.textAlign(alignX);
	}

	public void textAlign(final int alignX, final int alignY) {
		this.GRAPHICS.textAlign(alignX, alignY);
	}

	public float textAscent() {
		return this.GRAPHICS.textAscent();
	}

	public float textDescent() {
		return this.GRAPHICS.textDescent();
	}

	public void textFont(final PFont which) {
		this.GRAPHICS.textFont(which);
	}

	public void textFont(final PFont which, final float size) {
		this.GRAPHICS.textFont(which, size);
	}

	public void textLeading(final float leading) {
		this.GRAPHICS.textLeading(leading);
	}

	public void textMode(final int mode) {
		this.GRAPHICS.textMode(mode);
	}

	public void textSize(final float size) {
		this.GRAPHICS.textSize(size);
	}

	public float textWidth(final char c) {
		return this.GRAPHICS.textWidth(c);
	}

	public float textWidth(final String str) {
		return this.GRAPHICS.textWidth(str);
	}

	public float textWidth(final char[] chars, final int start, final int length) {
		return this.GRAPHICS.textWidth(chars, start, length);
	}

	public void texture(final PImage image) {
		this.GRAPHICS.texture(image);
	}

	public void textureMode(final int mode) {
		this.GRAPHICS.textureMode(mode);
	}

	public void textureWrap(final int wrap) {
		this.GRAPHICS.textureWrap(wrap);
	}

	public void tint(final int rgb) {
		this.GRAPHICS.tint(rgb);
	}

	public void tint(final float gray) {
		this.GRAPHICS.tint(gray);
	}

	public void tint(final int rgb, final float alpha) {
		this.GRAPHICS.tint(rgb, alpha);
	}

	public void tint(final float gray, final float alpha) {
		this.GRAPHICS.tint(gray, alpha);
	}

	public void tint(final float v1, final float v2, final float v3) {
		this.GRAPHICS.tint(v1, v2, v3);
	}

	public void tint(final float v1, final float v2, final float v3, final float alpha) {
		this.GRAPHICS.tint(v1, v2, v3, alpha);
	}

	public void translate(final float x, final float y) {
		this.GRAPHICS.translate(x, y);
	}

	// OpenGL-specific:
	// public void translate(final float x, final float y, final float z) {
	// this.GRAPHICS.translate(x, y, z);
	// }

	public void triangle(final float x1, final float y1, final float x2, final float y2, final float x3,
			final float y3) {
		this.GRAPHICS.triangle(x1, y1, x2, y2, x3, y3);
	}

	public void vertex(final float[] v) {
		this.GRAPHICS.vertex(v);
	}

	public void vertex(final float x, final float y) {
		this.GRAPHICS.vertex(x, y);
	}

	// OpenGL-specific:
	// public void vertex(final float x, final float y, final float z) {
	// this.GRAPHICS.vertex(x, y, z);
	// }

	public void vertex(final float x, final float y, final float u, final float v) {
		this.GRAPHICS.vertex(x, y, u, v);
	}

	// OpenGL-specific:
	// public void vertex(final float x, final float y, final float z, final float
	// u, final float v) {
	// this.GRAPHICS.vertex(x, y, z, u, v);
	// }

	// Disallowed by `SVG`/`PDF`:
	// public void blend(final int sx, final int sy, final int sw, final int sh,
	// final int dx, final int dy, final int dw,
	// final int dh, final int mode) {
	// this.GRAPHICS.blend(sx, sy, sw, sh, dx, dy, dw, dh, mode);
	// }

	// Disallowed by `SVG`/`PDF`:
	// public void blend(final PImage src, final int sx, final int sy, final int sw,
	// final int sh, final int dx,
	// final int dy, final int dw, final int dh, final int mode) {
	// this.GRAPHICS.blend(src, sx, sy, sw, sh, dx, dy, dw, dh, mode);
	// }

	public PImage copy() {
		return this.GRAPHICS.copy();
	}

	// Disallowed by `SVG`/`PDF`:
	// public void copy(final int sx, final int sy, final int sw, final int sh,
	// final int dx, final int dy, final int dw,
	// final int dh) {
	// this.GRAPHICS.copy(sx, sy, sw, sh, dx, dy, dw, dh);
	// }

	// Disallowed by `SVG`/`PDF`:
	// public void copy(final PImage src, final int sx, final int sy, final int sw,
	// final int sh, final int dx,
	// final int dy, final int dw, final int dh) {
	// this.GRAPHICS.copy(src, sx, sy, sw, sh, dx, dy, dw, dh);
	// }

	// Disallowed by `SVG`/`PDF`:
	// public void filter(final int kind) {
	// this.GRAPHICS.filter(kind);
	// }

	// public void filter(final int kind, final float param) {
	// this.GRAPHICS.filter(kind, param);
	// }

	// Disallowed by `SVG`/`PDF`:
	// public PImage get() {
	// return this.GRAPHICS.get();
	// }

	// Disallowed by `SVG`/`PDF`:
	// public int get(final int x, final int y) {
	// return this.GRAPHICS.get(x, y);
	// }

	// Disallowed by `SVG`/`PDF`:
	// public PImage get(final int x, final int y, final int w, final int h) {
	// return this.GRAPHICS.get(x, y, w, h);
	// }

	public Image getImage() {
		return this.GRAPHICS.getImage();
	}

	public int getModifiedX1() {
		return this.GRAPHICS.getModifiedX1();
	}

	public int getModifiedX2() {
		return this.GRAPHICS.getModifiedX2();
	}

	public int getModifiedY1() {
		return this.GRAPHICS.getModifiedY1();
	}

	public int getModifiedY2() {
		return this.GRAPHICS.getModifiedY2();
	}

	public Object getNative() {
		return this.GRAPHICS.getNative();
	}

	public void init(final int width, final int height, final int format) {
		this.GRAPHICS.init(width, height, format);
	}

	public void init(final int width, final int height, final int format, final int factor) {
		this.GRAPHICS.init(width, height, format, factor);
	}

	public boolean isLoaded() {
		return this.GRAPHICS.isLoaded();
	}

	public boolean isModified() {
		return this.GRAPHICS.isModified();
	}

	// Disallowed by `SVG`/`PDF`:
	// public void loadPixels() {
	// this.GRAPHICS.loadPixels();
	// }

	// Disallowed by `SVG`/`PDF`:
	// public void mask(final int[] maskArray) {
	// this.GRAPHICS.mask(maskArray);
	// }

	// Disallowed by `SVG`/`PDF`:
	// public void mask(final PImage img) {
	// this.GRAPHICS.mask(img);
	// }

	public void resize(final int w, final int h) {
		this.GRAPHICS.resize(w, h);
	}

	// Disallowed by `SVG`/`PDF`:
	// public void set(final int x, final int y, final int c) {
	// this.GRAPHICS.set(x, y, c);
	// }

	// Disallowed by `SVG`/`PDF`:
	// public void set(final int x, final int y, final PImage img) {
	// this.GRAPHICS.set(x, y, img);
	// }

	public void setLoaded() {
		this.GRAPHICS.setLoaded();
	}

	public void setLoaded(final boolean l) {
		this.GRAPHICS.setLoaded(l);
	}

	public void setModified() {
		this.GRAPHICS.setModified();
	}

	public void setModified(final boolean m) {
		this.GRAPHICS.setModified(m);
	}

	// Disallowed by `SVG`/`PDF`:
	// public void updatePixels() {
	// this.GRAPHICS.updatePixels();
	// }

	// Disallowed by `SVG`/`PDF`:
	// public void updatePixels(final int x, final int y, final int w, final int h)
	// {
	// this.GRAPHICS.updatePixels(x, y, w, h);
	// }

	@Override
	public boolean equals(final Object obj) {
		return this.GRAPHICS.equals(obj);
	}

	@Override
	public int hashCode() {
		return this.GRAPHICS.hashCode();
	}

	@Override
	public String toString() {
		return this.GRAPHICS.toString();
	}
	// endregion

}
