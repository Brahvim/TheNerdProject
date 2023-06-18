package com.brahvim.nerd.processing_wrapper;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedHashSet;

import com.brahvim.nerd.processing_wrapper.NerdSketch.NerdSketchKeyboardListener;

import processing.core.PConstants;
import processing.core.PVector;
import processing.event.MouseEvent;

// A (currently very messy) class to hold input event states from 
// the previous frame and previous event encounters.

public class NerdInputManager {

	// region Fields.
	/** Position of the mouse relative to the monitor. */
	public final Point GLOBAL_MOUSE_POINT = new Point(),
			PREV_FRAME_GLOBAL_MOUSE_POINT = new Point();

	/** Position of the mouse relative to the monitor. */
	public final PVector GLOBAL_MOUSE_VECTOR = new PVector(),
			PREV_FRAME_GLOBAL_MOUSE_VECTOR = new PVector();

	// region Frame-wise states, Processing style (thus mutable).
	// ...yeah, *these* ain't mutable, are they?:
	public final ArrayList<PVector> UNPROJ_TOUCHES = new ArrayList<>(10),
			PREV_UNPROJ_TOUCHES = new ArrayList<>(10),
			PREV_FRAME_UNPROJ_TOUCHES = new ArrayList<>(10);

	/** Updated each time events changing this variable occur. */
	public final PVector MOUSE_VECTOR = new PVector(),
			PREV_MOUSE_VECTOR = new PVector(),
			MOUSE_CENTER_OFFSET = new PVector(),
			PREV_MOUSE_CENTER_OFFSET = new PVector();
	/** Updated framely! Generally, don't use this (look at that long name!). */
	public final PVector CURR_FRAME_MOUSE_VECTOR = new PVector(),
			PREV_FRAME_MOUSE_VECTOR = new PVector(),
			CURR_FRAME_MOUSE_CENTER_OFFSET = new PVector(),
			PREV_FRAME_MOUSE_CENTER_OFFSET = new PVector();

	/** Updated each time events changing this variable occur. */
	public char key, pkey;

	/** Updated each time events changing this variable occur. */
	public int keyCode, pkeyCode;

	/** Updated each time events changing this variable occur. */
	public int mouseButton, pmouseButton;

	/** Updated each time events changing this variable occur. */
	public boolean keyPressed, pkeyPressed;

	/** Updated each time events changing this variable occur. */
	public boolean mousePressed, pmousePressed;

	/** Updated each time events changing this variable occur. */
	public float mouseX, mouseY, pmouseX, pmouseY;

	/** Updated each time events changing this variable occur. */
	public boolean mouseLeft, mouseMid, mouseRight, pmouseLeft, pmouseMid, pmouseRight;

	/** Updated each time events changing this variable occur. */
	public float mouseScroll, pmouseScroll,
			mouseScrollDelta, pmouseScrollDelta,
			totalMouseScroll, ptotalMouseScroll;

	protected final LinkedHashSet<NerdSketchKeyboardListener> KEYBOARD_LISTENERS = new LinkedHashSet<>(1);
	protected final LinkedHashSet<Integer> KEYS_HELD = new LinkedHashSet<>(5);
	// endregion

	private final NerdSketch SKETCH; // ...It's best to keep this `private`.
	// endregion

	public NerdInputManager(final NerdSketch p_sketch) {
		this.SKETCH = p_sketch;
	}

	/* `package` */ void preCallback() {
		this.CURR_FRAME_MOUSE_CENTER_OFFSET.set(
				this.SKETCH.mouseX - this.SKETCH.width * 0.5f,
				this.SKETCH.mouseY - this.SKETCH.height * 0.5f);
		this.CURR_FRAME_MOUSE_VECTOR.set(this.SKETCH.mouseX, this.SKETCH.mouseY);
	}

	/* `package` */ void postCallback() {
		this.PREV_FRAME_MOUSE_CENTER_OFFSET.set(
				this.SKETCH.mouseX - this.SKETCH.width * 0.5f,
				this.SKETCH.mouseY - this.SKETCH.height * 0.5f);
		this.PREV_FRAME_MOUSE_VECTOR.set(this.SKETCH.mouseX, this.SKETCH.mouseY);
	}

	// region Input event callbacks from Processing.
	/* `package` */ void keyTyped() {
		this.key = this.SKETCH.key;
		this.keyCode = this.SKETCH.keyCode;
		this.keyPressed = this.SKETCH.keyPressed;
	}

	/* `package` */ void keyPressed() {
		this.key = this.SKETCH.key;
		this.keyCode = this.SKETCH.keyCode;
		this.keyPressed = this.SKETCH.keyPressed;

		for (final NerdSketchKeyboardListener l : this.KEYBOARD_LISTENERS)
			l.keyPressed();

		this.pkey = this.key;
		this.pkeyCode = this.keyCode;
	}

	/* `package` */ void keyReleased() {
		this.pmouseButton = this.mouseButton;

		this.key = this.SKETCH.key;
		this.keyCode = this.SKETCH.keyCode;
		this.keyPressed = this.SKETCH.keyPressed;
	}

	/* `package` */ void mousePressed() {
		this.pmouseButton = this.mouseButton;
		this.pmousePressed = this.mousePressed;

		this.mouseButton = this.SKETCH.mouseButton;
		this.mousePressed = this.SKETCH.mousePressed;
	}

	/* `package` */ void mouseReleased() {
		this.pmouseButton = this.mouseButton;
		this.pmousePressed = this.mousePressed;

		this.mouseButton = this.SKETCH.mouseButton;
		this.mousePressed = this.SKETCH.mousePressed;
	}

	/* `package` */ void mouseClicked() {
		this.pmouseButton = this.mouseButton;
		this.pmousePressed = this.mousePressed;

		this.mouseButton = this.SKETCH.mouseButton;
		this.mousePressed = this.SKETCH.mousePressed;
	}

	/* `package` */ void mouseMoved() {
		this.pmouseX = this.mouseX;
		this.pmouseY = this.mouseY;

		this.PREV_MOUSE_VECTOR.set(this.MOUSE_VECTOR);
		this.PREV_MOUSE_CENTER_OFFSET.set(this.MOUSE_CENTER_OFFSET);

		this.mouseX = this.SKETCH.mouseX;
		this.mouseY = this.SKETCH.mouseY;
		this.MOUSE_VECTOR.set(this.mouseX, this.mouseY);
		this.MOUSE_CENTER_OFFSET.set(
				this.SKETCH.mouseX - this.SKETCH.WINDOW.cx,
				this.SKETCH.mouseY - this.SKETCH.WINDOW.cy);
	}

	/* `package` */ void mouseDragged() {
		this.pmouseX = this.mouseX;
		this.pmouseY = this.mouseY;
		this.pmouseButton = this.mouseButton;

		this.mouseX = this.SKETCH.mouseX;
		this.mouseY = this.SKETCH.mouseY;
		this.mouseButton = this.SKETCH.mouseButton;
	}

	/* `package` */ void mouseWheel(final MouseEvent p_event) {
		this.pmouseScroll = this.mouseScroll;
		this.ptotalMouseScroll = this.totalMouseScroll;
		this.pmouseScrollDelta = this.mouseScrollDelta;

		this.mouseScroll = p_event.getCount(); // Fetch the latest and greatest!
		this.mouseScrollDelta = this.mouseScroll - this.pmouseScroll; // Delta!~
		this.totalMouseScroll += this.mouseScrollDelta; // ...Then we add it in.
	}
	// endregion

	// region Keyboard utilities!
	public Integer[] getHeldKeys() {
		return this.KEYS_HELD.toArray(new Integer[0]);
	}

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

	public String addTypedKeyTo(final String p_str) {
		final char typedChar = this.getTypedKey();
		final int strLen = p_str.length();

		if (typedChar == '\b' && strLen > 0)
			return p_str.substring(strLen - 1, strLen);
		else
			return p_str.concat(Character.toString(typedChar));
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
