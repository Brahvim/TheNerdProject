package com.brahvim.nerd.processing_wrapper;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedHashSet;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;
import processing.opengl.PGraphics3D;

public class NerdInputManager {

	// region Fields.
	/** Position of the mouse relative to the monitor. */
	public final Point GLOBAL_MOUSE_POINT = new Point(),
			PREV_GLOBAL_MOUSE_POINT = new Point();

	/** Position of the mouse relative to the monitor. */
	public final PVector GLOBAL_MOUSE_VECTOR = new PVector();
	public final PVector PREV_GLOBAL_MOUSE_VECTOR = new PVector();

	// region Frame-wise states, Processing style (thus mutable).
	// ...yeah, *these* ain't mutable, are they?:
	// TODO: Make all 4 of these non-framely!:
	public final ArrayList<PVector> UNPROJ_TOUCHES = new ArrayList<>(10);
	public final ArrayList<PVector> PREV_UNPROJ_TOUCHES = new ArrayList<>(10);

	public final PVector MOUSE_VEC = new PVector(), MOUSE_CENTER_OFFSET = new PVector();
	public final PVector PREV_MOUSE_VEC = new PVector(), PREV_MOUSE_CENTER_OFFSET = new PVector();

	public char key, pkey;
	public int keyCode, pkeyCode;
	public boolean keyPressed, pkeyPressed;
	public boolean mousePressed, pmousePressed;
	public float mouseX, mouseY, pmouseX, pmouseY;
	public int mouseButton, framelyMouseButton, pmouseButton, pframelyMouseButton;

	/** Updated each time the event callbacks are called. */
	public boolean mouseLeft, mouseMid, mouseRight,
			pmouseLeft, pmouseMid, pmouseRight;

	/** Updated framely! Generally, don't use this (look at the name!). */
	public boolean framelyMouseLeft, framelyMouseMid, framelyMouseRight,
			pframelyMouseLeft, pframelyMouseMid, pframelyMouseRight;

	// public float framelyMouseScrollDelta, pframelyMouseScrollDelta;
	/** Updated in `NerdSketch::mouseWheel()`. */
	// public float lastMouseScroll, totalMouseScroll, pframeTotalMouseScroll;

	private final NerdSketch SKETCH;
	private final LinkedHashSet<Integer> KEYS_HELD;
	// endregion

	public NerdInputManager(final NerdSketch p_sketch, final LinkedHashSet<Integer> p_keysHeldListRef) {
		this.SKETCH = p_sketch;
		this.KEYS_HELD = p_keysHeldListRef;
	}

	protected void preDraw() {
		this.key = this.SKETCH.key;
		this.mouseX = this.SKETCH.mouseX;
		this.mouseY = this.SKETCH.mouseY;
		this.pmouseX = this.SKETCH.pmouseX;
		this.pmouseY = this.SKETCH.pmouseY;
		this.keyCode = this.SKETCH.keyCode;
		this.keyPressed = this.SKETCH.keyPressed;
		this.mouseButton = this.SKETCH.mouseButton;
		this.mousePressed = this.SKETCH.mousePressed;
	}

	protected void postCallback() {
		for (final PVector v : this.UNPROJ_TOUCHES)
			this.PREV_UNPROJ_TOUCHES.add(v);

		this.pkey = this.SKETCH.key;
		this.pkeyCode = this.SKETCH.keyCode;
		this.PREV_MOUSE_VEC.set(this.MOUSE_VEC);
		this.pkeyPressed = this.SKETCH.keyPressed;
		this.pframelyMouseMid = this.framelyMouseMid;
		this.pmousePressed = this.SKETCH.mousePressed;
		// this.pframeTotalMouseScroll = this.totalMouseScroll;
		this.pframelyMouseLeft = this.framelyMouseLeft;
		this.pframelyMouseRight = this.framelyMouseRight;
		this.pframelyMouseButton = this.SKETCH.mouseButton;
		// this.pframelyMouseScrollDelta = this.framelyMouseScrollDelta;
	}

	// region Mouse and coordinate conversion utilities.
	// region Sketch overloads for `screen*()`.
	public void screenX(final float p_x, final float p_y, final float p_z) {
		this.SKETCH.screenX(p_x, p_y, p_z);
	}

	public void screenY(final float p_x, final float p_y, final float p_z) {
		this.SKETCH.screenY(p_x, p_y, p_z);
	}

	public void screenZ(final float p_x, final float p_y, final float p_z) {
		this.SKETCH.screenZ(p_x, p_y, p_z);
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
				this.SKETCH.modelX(p_vec.x, p_vec.y, p_vec.z),
				this.SKETCH.modelY(p_vec.x, p_vec.y, p_vec.z),
				this.SKETCH.modelZ(p_vec.x, p_vec.y, p_vec.z));
	}

	public PVector modelVec(final float p_x, final float p_y, final float p_z) {
		return new PVector(
				this.SKETCH.modelX(p_x, p_y, p_z),
				this.SKETCH.modelY(p_x, p_y, p_z),
				this.SKETCH.modelZ(p_x, p_y, p_z));
	}

	public PVector screenVec() {
		return new PVector(
				this.screenX(),
				this.screenY(),
				this.screenZ());
	}

	public PVector screenVec(final PVector p_vec) {
		return new PVector(
				this.SKETCH.screenX(p_vec.x, p_vec.y, p_vec.z),
				this.SKETCH.screenY(p_vec.x, p_vec.y, p_vec.z),
				this.SKETCH.screenZ(p_vec.x, p_vec.y, p_vec.z));
	}

	public PVector screenVec(final float p_x, final float p_y, final float p_z) {
		return new PVector(
				this.SKETCH.screenX(p_x, p_y, p_z),
				this.SKETCH.screenY(p_x, p_y, p_z),
				this.SKETCH.screenZ(p_x, p_y, p_z));
	}
	// endregion

	// region `modelX()`-`modelY()`-`modelZ()` `PVector` and no-parameter overloads.
	// region Parameterless overloads.
	public float modelX() {
		return this.SKETCH.modelX(0, 0, 0);
	}

	public float modelY() {
		return this.SKETCH.modelY(0, 0, 0);
	}

	public float modelZ() {
		return this.SKETCH.modelZ(0, 0, 0);
	}
	// endregion

	// region `p_vec`?
	// ...how about `p_modelMatInvMulter`? :rofl:!
	public float modelX(final PVector p_vec) {
		return this.SKETCH.modelX(p_vec.x, p_vec.y, p_vec.z);
	}

	public float modelY(final PVector p) {
		return this.SKETCH.modelY(p.x, p.y, p.z);
	}

	public float modelZ(final PVector p) {
		return this.SKETCH.modelZ(p.x, p.y, p.z);
	}
	// endregion
	// endregion

	// region `screenX()`-`screenY()`-`screenZ()`, `PVector`, plus no-arg overloads.
	// "Oh! And when the `z` is `-1`, you just add this and sub that. Optimization!"
	// - That ONE Mathematician.

	// region Parameterless overloads.
	public float screenX() {
		return this.SKETCH.screenX(0, 0, 0);
	}

	public float screenY() {
		return this.SKETCH.screenY(0, 0, 0);
	}

	public float screenZ() {
		return this.SKETCH.screenY(0, 0, 0);
	}
	// endregion

	// region `p_vec`!
	// The following two were going to disclude the `z` if it was `0`.
	// And later, I felt this was risky.
	// This two-`float` overload ain't in the docs, that scares me!

	// ...ACTUALLY,
	// https://github.com/processing/processing/blob/459853d0dcdf1e1648b1049d3fdbb4bf233fded8/core/src/processing/opengl/PGraphicsOpenGL.java#L4611
	// ..."they rely on the JIT too!" (no, they don't optimize this at all. They
	// just put the `0` themselves, LOL.) :joy:

	public float screenX(final PVector p_vec) {
		return this.SKETCH.screenX(p_vec.x, p_vec.y, p_vec.z);

		// return p_vec.z == 0
		// ? this.SKETCH.screenX(p_vec.x, p_vec.y)
		// : this.SKETCH.screenX(p_vec.x, p_vec.y, p_vec.z);
	}

	public float screenY(final PVector p_vec) {
		return this.SKETCH.screenY(p_vec.x, p_vec.y, p_vec.z);

		// return p_vec.z == 0
		// ? this.SKETCH.screenY(p_vec.x, p_vec.y)
		// : this.SKETCH.screenY(p_vec.x, p_vec.y, p_vec.z);
	}

	public float screenZ(final PVector p_vec) {
		// Hmmm...
		// ..so `z` cannot be `0` here.
		// ..and `x` and `y` cannot be ignored!
		// "No room for optimization here!"
		return this.SKETCH.screenZ(p_vec.x, p_vec.y, p_vec.z);
	}
	// endregion
	// endregion

	// region Unprojection via `world*()` and `getMouseInWorld*()`!
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
		this.SKETCH.UNPROJECTOR.captureViewMatrix((PGraphics3D) this.SKETCH.getGraphics());
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
		return this.worldVec(this.mouseX, this.mouseY,
				this.SKETCH.getCamera().far - p_distanceFromFarPlane + this.SKETCH.getCamera().near);
	}

	public PVector getMouseInWorldAtZ(final float p_distanceFromCamera) {
		return this.worldVec(this.mouseX, this.mouseY, p_distanceFromCamera);
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
	// final TouchEvent.Pointer touch = super.touches[p_touchId];
	// return this.worldVec(touch.x, touch.y,
	// this.SKETCH.getCamera().far - p_distanceFromFarPlane +
	// this.SKETCH.getCamera().near);
	// }

	// public PVector getTouchInWorldAtZ(final int p_touchId, final float
	// p_distanceFromCamera) {
	// final TouchEvent.Pointer touch = super.touches[p_touchId];
	// return this.worldVec(touch.x, touch.y, p_distanceFromCamera);
	// }
	// endregion
	// endregion
	// endregion

	// region Keyboard utilities!
	public boolean onlyKeyPressedIs(final int p_keyCode) {
		return this.KEYS_HELD.size() == 1 && this.KEYS_HELD.contains(p_keyCode);
	}

	public boolean onlyKeysPressedAre(final int... p_keyCodes) {
		boolean toRet = this.KEYS_HELD.size() == p_keyCodes.length;

		if (!toRet)
			return false;

		for (final int i : p_keyCodes)
			toRet &= this.KEYS_HELD.contains(i);

		return toRet;
	}

	public boolean keysPressed(final int... p_keyCodes) {
		// this.keysHeld.contains(p_keyCodes); // Causes a totally unique error
		// :O

		for (final int i : p_keyCodes)
			if (!this.KEYS_HELD.contains(i))
				return false;
		return true;

		// I have no idea why Nerd still uses this. Didn't I change that..?:
		/*
		 * boolean flag = true;
		 * for (int i : p_keyCodes)
		 * flag &= this.keysHeld.contains(i); // ...yeah, `|=` and not `&=`...
		 * return flag;
		 */
		// An article once said: `boolean` flags are bad.
	}

	public boolean keyIsPressed(final int p_keyCode) {
		return this.KEYS_HELD.contains(p_keyCode);
	}

	public boolean anyGivenKeyIsPressed(final int... p_keyCodes) {
		for (final int i : p_keyCodes)
			if (this.KEYS_HELD.contains(i))
				return true;
		return false;
	}

	public static boolean isStandardKeyboardSymbol(final char p_char) {
		// boolean is = false;
		for (final char ch : NerdSketch.STANDARD_KEYBOARD_SYMBOLS)
			// Can't use this!:
			// return ch == p_char;
			// What if the array being examined is empty?!

			if (ch == p_char)
				return true;

		// These used to be in the loop:
		// is = ch == p_char;
		// is |= ch == p_char;
		// return is;

		return false;
	}

	public static boolean isTypeable(final char p_char) {
		return Character.isDigit(p_char) ||
				Character.isLetter(p_char) ||
				Character.isWhitespace(p_char) ||
				NerdSketch.isStandardKeyboardSymbol(p_char);
	}

	public char getTypedKey() {
		if (NerdSketch.isTypeable(this.SKETCH.key))
			return this.SKETCH.key;

		// New way to do this in Java!:
		// (...as seen in [`java.lang.`]`Long.class`, on line 217, in OpenJDK `17`!)
		return switch (this.SKETCH.keyCode) {
			case PConstants.BACKSPACE -> '\b';
			case PConstants.RETURN -> '\n';
			case PConstants.ENTER -> '\n';
			default -> '\0';
		};

		// """"""""Slow"""""""":
		/*
		 * if (keyCode == BACKSPACE)
		 * return '\b';
		 * else if (keyCode == retURN || keyCode == ENTER)
		 * return '\n';
		 * else if (isTypeable(key))
		 * return key;
		 * else return '\0';
		 */

	}

	public void addTypedKeyTo(final String p_str) {
		final char typedChar = this.getTypedKey();
		final int strLen = p_str.length();

		if (typedChar == '\b' && strLen > 0)
			p_str.substring(strLen - 1, strLen);
		else
			p_str.concat(Character.toString(typedChar));
	}

	public void addTypedKeyTo(final StringBuilder p_str) {
		final char typedChar = this.getTypedKey();
		final int strLen = p_str.length();

		if (typedChar == '\b' && strLen > 0)
			p_str.substring(strLen - 1, strLen);
		else
			p_str.append(Character.toString(typedChar));
	}

	// To be used for checking if a certain key can be typed:
	public boolean isNotSpecialKey(final int p_keyCode) {
		// I just didn't want to make an array :joy::
		return !(
		// For all function keys [regardless of whether `Shift` or `Ctrl` are pressed]:
		p_keyCode > 96 && p_keyCode < 109 ||
				p_keyCode == 0 || // `Fn`, plus a function key.
				p_keyCode == 2 || // `Home`,
				p_keyCode == 3 || // `End`,
				p_keyCode == 8 || // `Backspace`,
				p_keyCode == 10 || // Both `Enter`s/`return`s.
				p_keyCode == 11 || // `PageDown`,
				p_keyCode == 12 || // Resistered when a button is pressed on the numpad with `NumLock` off.
				p_keyCode == 16 || // `PageUp`,
				p_keyCode == 19 || // "`Alt`-Graph',
				p_keyCode == 20 || // `CapsLock`,
				p_keyCode == 23 || // `ScrollLock`,
				p_keyCode == 26 || // `Insert`,
				p_keyCode == 147 || // Both `Delete` keys,
				p_keyCode == 148 || // `Pause`/`Break` and also `NumLock`,
				p_keyCode == 153 || // `Menu`/`Application` AKA "RightClick" key.
				p_keyCode == 157 // "Meta", AKA the "OS key".
		);
	}
	// endregion

}
