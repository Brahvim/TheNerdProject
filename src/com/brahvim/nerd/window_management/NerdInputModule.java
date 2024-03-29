package com.brahvim.nerd.window_management;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import com.brahvim.nerd.processing_wrapper.NerdModule;
import com.brahvim.nerd.processing_wrapper.NerdSketch;

import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PVector;
import processing.event.MouseEvent;

public class NerdInputModule<SketchPGraphicsT extends PGraphics> extends NerdModule<SketchPGraphicsT> {

	// region Fields.
	// region Standard keyboard codes.
	public static final List<Character> STANDARD_KEYBOARD_SYMBOLS = List.of(
			'\'', '\"', '-', '=', '`', '~', '!', '@', '#', '$',
			'%', '^', '&', '*', '(', ')', '{', '}', '[',
			']', ';', ',', '.', '/', '\\', ':', '|', '<',
			'>', '_', '+', '?');

	public static final List<Integer> STANDARD_KEYBOARD_SPECIAL_KEYS = List.of(
			0, // `Fn`, plus any function key.
			2, // `Home`.
			3, // `End`.
			5, // `Sys Rq` ("System Request") or `Prt Sc` ("Print Screen").
			8, // `Backspace`.
			9, // `Tab`.
			10, // Both `Return`s/`Enter`s (old name first).
			11, // `PageDown`.
			12, // Registered when a button is pressed on the numpad with `NumLock` off.
			16, // `PageUp`.
			19, // "`Alt`-Graph".
			20, // `Caps Lock`.
			23, // `Scroll Lock`.
			26, // `Insert`/`Ins` (old name first).
			27, // `Escape`/`Esc` (old name first).

			// `97` to `108` are for all function keys
			// [regardless of whether `Shift` or `Ctrl` are pressed]:
			97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109,

			147, // Both `Delete` keys.
			148, // `Pause`/`Break` and also `Num Lock`.
			153, // `Menu`/`Application` AKA "RightClick" key.
			157 // "Meta"/"Super", AKA the "OS key".
	);
	// endregion

	/** Position of the mouse relative to the monitor. Updated framely. */
	public final Point GLOBAL_MOUSE_POINT = new Point(),
			PREV_FRAME_GLOBAL_MOUSE_POINT = new Point();

	/** Position of the mouse relative to the monitor. Updated framely. */
	public final PVector
	/*   */ GLOBAL_MOUSE_VECTOR = new PVector(),
			PREV_FRAME_GLOBAL_MOUSE_VECTOR = new PVector();

	// region Frame-wise states, Processing style (thus mutable).
	// ...yeah, *these* ain't mutable, are they?:
	public final List<PVector>
	/*   */ UNPROJ_TOUCHES = new ArrayList<>(10),
			PREV_UNPROJ_TOUCHES = new ArrayList<>(10),
			PREV_FRAME_UNPROJ_TOUCHES = new ArrayList<>(10);

	/** Updated during appropriate event callbacks - not in a loop. */
	public final PVector
	/*   */ MOUSE_VECTOR = new PVector(),
			PREV_MOUSE_VECTOR = new PVector(),
			MOUSE_CENTER_OFFSET = new PVector(),
			PREV_MOUSE_CENTER_OFFSET = new PVector();

	/** Updated framely! Generally, don't use this (look at that long name!). */
	public final PVector
	/*   */ CURR_FRAME_MOUSE_VECTOR = new PVector(),
			PREV_FRAME_MOUSE_VECTOR = new PVector(),
			CURR_FRAME_MOUSE_CENTER_OFFSET_VECTOR = new PVector(),
			PREV_FRAME_MOUSE_CENTER_OFFSET_VECTOR = new PVector();

	/** Updated during appropriate event callbacks - not in a loop. */
	public char key, pkey;

	/** Updated during appropriate event callbacks - not in a loop. */
	public int keyCode, pkeyCode;

	/** Updated during appropriate event callbacks - not in a loop. */
	public int mouseButton, pmouseButton;

	/** Updated during appropriate event callbacks - not in a loop. */
	public boolean keyPressed, pkeyPressed; // NOSONAR! Processing calls them the same!...

	/** Updated during appropriate event callbacks - not in a loop. */
	public boolean mousePressed, pmousePressed; // NOSONAR! Processing calls them the same!...

	/** Updated during appropriate event callbacks - not in a loop. */
	public float mouseX, mouseY, pmouseX, pmouseY;

	/** Updated during appropriate event callbacks - not in a loop. */
	public boolean mouseLeft, mouseMid, mouseRight, pmouseLeft, pmouseMid, pmouseRight;

	/** Updated during appropriate event callbacks - not in a loop. */
	public float mouseScroll, pmouseScroll,
			mouseScrollDelta, pmouseScrollDelta,
			totalMouseScroll, ptotalMouseScroll;
	// endregion

	/** Updates framely. */
	protected final List<Integer>
	/*   */ KEYS_HELD = new ArrayList<>(5),
			PREV_FRAME_KEYS_HELD = new ArrayList<>(5);
	protected NerdWindowModule<?> window;
	// endregion

	public NerdInputModule(final NerdSketch<SketchPGraphicsT> p_sketch) {
		super(p_sketch);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void preSetup() {
		this.window = super.SKETCH.getNerdModule(NerdWindowModule.class);
	}

	@Override
	public void pre() {
		this.GLOBAL_MOUSE_POINT.setLocation(MouseInfo.getPointerInfo().getLocation());
		this.GLOBAL_MOUSE_VECTOR.set(this.GLOBAL_MOUSE_POINT.x, this.GLOBAL_MOUSE_POINT.y);

		this.CURR_FRAME_MOUSE_CENTER_OFFSET_VECTOR.set(
				super.SKETCH.mouseX - super.SKETCH.width * 0.5f,
				super.SKETCH.mouseY - super.SKETCH.height * 0.5f);
		this.CURR_FRAME_MOUSE_VECTOR.set(super.SKETCH.mouseX, super.SKETCH.mouseY);
	}

	@Override
	public void post() {
		this.PREV_FRAME_GLOBAL_MOUSE_POINT.setLocation(this.GLOBAL_MOUSE_POINT);
		this.PREV_FRAME_GLOBAL_MOUSE_VECTOR.set(this.GLOBAL_MOUSE_VECTOR);

		this.PREV_FRAME_MOUSE_CENTER_OFFSET_VECTOR.set(
				super.SKETCH.mouseX - super.SKETCH.width * 0.5f,
				super.SKETCH.mouseY - super.SKETCH.height * 0.5f);
		this.PREV_FRAME_MOUSE_VECTOR.set(super.SKETCH.mouseX, super.SKETCH.mouseY);

		this.PREV_FRAME_KEYS_HELD.clear();
		this.PREV_FRAME_KEYS_HELD.addAll(this.KEYS_HELD);
	}

	@Override
	public void focusLost() {
		this.KEYS_HELD.clear();
	}

	@Override
	public void focusGained() {
		this.KEYS_HELD.clear();
	}

	// region Input event callbacks from Processing.
	// Copying is better since we don't know what decisions the caller makes!
	// This is why methods here only *copy* fields from `NerdSketch`.

	// region Keyboard events.
	@Override
	public void keyTyped() {
		this.key = super.SKETCH.key;
		this.keyCode = super.SKETCH.keyCode;
		this.keyPressed = super.SKETCH.keyPressed;
	}

	@Override
	public void keyPressed() {
		// Set the previous states,
		this.pkey = this.key;
		this.pkeyCode = this.keyCode;
		this.pkeyPressed = this.keyPressed;

		// ...And get the latest states!:
		this.key = super.SKETCH.key;
		this.keyCode = super.SKETCH.keyCode;
		this.keyPressed = super.SKETCH.keyPressed;

		synchronized (this.KEYS_HELD) {
			if (!this.KEYS_HELD.contains(this.keyCode))
				this.KEYS_HELD.add(this.keyCode);
		}
	}

	@Override
	public void keyReleased() {
		this.key = super.SKETCH.key;
		this.keyCode = super.SKETCH.keyCode;
		this.keyPressed = super.SKETCH.keyPressed;

		try {
			synchronized (this.KEYS_HELD) {
				this.KEYS_HELD.remove((Integer) this.keyCode);
			}
		} catch (final IndexOutOfBoundsException e) { // NOSONAR!
		}
	}
	// endregion

	// region Mouse events.
	private void literallyEveryMouseButtonCallback() {
		this.pmouseButton = this.mouseButton;
		this.pmousePressed = this.mousePressed;

		this.mouseButton = super.SKETCH.mouseButton;
		this.mousePressed = super.SKETCH.mousePressed;

		this.mouseLeft = this.mouseButton == PConstants.LEFT && this.mousePressed;
		this.mouseMid = this.mouseButton == PConstants.CENTER && this.mousePressed;
		this.mouseRight = this.mouseButton == PConstants.RIGHT && this.mousePressed;
	}

	@Override
	public void mousePressed() {
		this.literallyEveryMouseButtonCallback();
	}

	@Override
	public void mouseReleased() {
		this.literallyEveryMouseButtonCallback();
	}

	// Not called on Android!:
	@Override
	public void mouseClicked() {
		this.literallyEveryMouseButtonCallback();
	}

	@Override
	public void mouseMoved() {
		this.pmouseX = this.mouseX;
		this.pmouseY = this.mouseY;

		this.PREV_MOUSE_VECTOR.set(this.MOUSE_VECTOR);
		this.PREV_MOUSE_CENTER_OFFSET.set(this.MOUSE_CENTER_OFFSET);

		this.mouseX = super.SKETCH.mouseX;
		this.mouseY = super.SKETCH.mouseY;

		this.MOUSE_VECTOR.set(this.mouseX, this.mouseY);
		this.MOUSE_CENTER_OFFSET.set(
				super.SKETCH.mouseX - this.window.cx,
				super.SKETCH.mouseY - this.window.cy);
	}

	@Override
	public void mouseDragged() {
		// Should've included stuff from `NerdInputModule::mouseMoved()`, but hey - if
		// the mouse is dragged, it also moves, right?!
		this.literallyEveryMouseButtonCallback();

		// this.pmouseX = this.mouseX; this.pmouseY = this.mouseY;
		// this.mouseX = super.SKETCH.mouseX; this.mouseY = super.SKETCH.mouseY;
	}

	@Override
	public void mouseWheel(final MouseEvent p_event) {
		this.pmouseScroll = this.mouseScroll;
		this.ptotalMouseScroll = this.totalMouseScroll;
		this.pmouseScrollDelta = this.mouseScrollDelta;

		this.mouseScroll = p_event.getCount(); // Fetch the latest and greatest!
		this.mouseScrollDelta = this.mouseScroll - this.pmouseScroll; // Delta!~
		this.totalMouseScroll += this.mouseScrollDelta; // ...Then we add it in.
	}
	// endregion
	// endregion

	// region Keyboard utilities!
	public char getTypedKey() {
		if (NerdInputModule.isTypeable(super.SKETCH.key))
			return super.SKETCH.key;

		// New way to do this in Java!:
		// (...as seen in [`java.lang.`]`Long.class`, on line 217, in OpenJDK `17`!)
		return switch (super.SKETCH.keyCode) {
			case PConstants.BACKSPACE -> '\b';
			case PConstants.RETURN -> '\n';
			case PConstants.ENTER -> '\n';
			default -> '\0';
		};

		// """"""""Slow"""""""":
		/*
		 * if (keyCode == BACKSPACE)
		 * return '\b';
		 * else if (keyCode == RETURN || keyCode == ENTER)
		 * return '%n';
		 * else if (isTypeable(key))
		 * return key;
		 * else return '\0';
		 */
	}

	public Integer[] getHeldKeysArray() {
		return this.KEYS_HELD.toArray(new Integer[0]);
	}

	public String getHeldKeysDebugString() {
		final int numStrings = this.KEYS_HELD.size();

		if (numStrings == 0)
			return "";

		String toRet = "[ ";
		final int lastId = this.KEYS_HELD.size() - 1;

		for (int i = 0; i < numStrings; ++i)
			toRet = toRet.concat(KeyEvent.getKeyText(this.KEYS_HELD.get(i)))
					.concat(i == lastId ? " ]" : ", ");

		return toRet;
	}

	public String getLastFrameHeldKeysDebugString() {
		return this.PREV_FRAME_KEYS_HELD.toString();
	}

	// region Current frame.
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

	public boolean keyPressedIsOnly(final int p_keyCode) {
		return this.KEYS_HELD.size() == 1 && this.KEYS_HELD.contains(p_keyCode);
	}

	public boolean keyGivenIsPressed(final int p_keyCode) {
		return this.KEYS_HELD.contains(p_keyCode);
	}

	public boolean keysPressedAreOnly(final int... p_keyCodes) {
		boolean toRet = this.KEYS_HELD.size() == p_keyCodes.length;

		if (!toRet)
			return false;

		for (final var i : p_keyCodes)
			toRet &= this.KEYS_HELD.contains(i);

		return toRet;
	}

	public boolean keysGivenArePressed(final int... p_keyCodes) {
		// this.keysHeld.contains(p_keyCodes); // Causes a totally unique error
		// :O

		for (final var i : p_keyCodes)
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

	public boolean keysPressedIsEither(final int... p_keyCodes) {
		for (final var i : p_keyCodes)
			if (this.KEYS_HELD.contains(i))
				return true;

		return false;
	}

	public boolean keysPressedAreOrdered(final int... p_keyCodes) {
		final int paramArrLen = p_keyCodes.length;
		final int ownArrLen = this.KEYS_HELD.size();

		int paramArrId = 0;

		for (int i = 0; paramArrId < paramArrLen && i < ownArrLen; ++i) {
			if (p_keyCodes[paramArrId] == this.KEYS_HELD.get(i))
				paramArrId++;
		}

		return paramArrId == paramArrLen;
	}
	// endregion

	// region Previous frame.
	public boolean keyGivenWasPressed(final int p_keyCode) {
		return this.PREV_FRAME_KEYS_HELD.contains(p_keyCode);
	}

	public boolean keyPressedWasOnly(final int p_keyCode) {
		return this.PREV_FRAME_KEYS_HELD.size() == 1 && this.PREV_FRAME_KEYS_HELD.contains(p_keyCode);
	}

	public boolean keysPressedWereOnly(final int... p_keyCodes) {
		boolean toRet = this.PREV_FRAME_KEYS_HELD.size() == p_keyCodes.length;

		if (!toRet)
			return false;

		for (final var i : p_keyCodes)
			toRet &= this.PREV_FRAME_KEYS_HELD.contains(i);

		return toRet;
	}

	public boolean keysGivenWerePressed(final int... p_keyCodes) {
		for (final var i : p_keyCodes)
			if (!this.PREV_FRAME_KEYS_HELD.contains(i))
				return false;
		return true;
	}

	public boolean keyPressedWasEither(final int... p_keyCodes) {
		for (final var i : p_keyCodes)
			if (this.PREV_FRAME_KEYS_HELD.contains(i))
				return true;

		return false;
	}

	public boolean keysPressedWereOrdered(final int... p_keyCodes) {
		final int paramArrLen = p_keyCodes.length;
		final int ownArrLen = this.PREV_FRAME_KEYS_HELD.size();

		int paramArrId = 0;

		for (int i = 0; paramArrId < paramArrLen && i < ownArrLen; ++i) {
			if (p_keyCodes[paramArrId] == this.PREV_FRAME_KEYS_HELD.get(i))
				paramArrId++;
		}

		return paramArrId == paramArrLen;
	}
	// endregion

	// region "No Longer" (previous frame but not current frame).
	public boolean keyGivenIsNoLongerPressed(final int p_keyCode) {
		return !this.keyGivenIsPressed(p_keyCode) && this.keyGivenWasPressed(p_keyCode);
	}

	public boolean keyNoLongerPressedIsOnly(final int p_keyCode) {
		return !this.keyPressedIsOnly(p_keyCode) && this.keyPressedWasOnly(p_keyCode);
	}

	public boolean keyPressedNoLongerIsEither(final int... p_keyCodes) {
		return !this.keysPressedIsEither(p_keyCodes) && this.keyPressedWasEither(p_keyCodes);
	}

	public boolean keysPressedNoLongerAreOnly(final int... p_keyCodes) {
		return !this.keysPressedAreOnly(p_keyCodes) && this.keysPressedWereOnly(p_keyCodes);
	}

	public boolean keysGivenAreNoLongerPressed(final int... p_keyCodes) {
		return !this.keysGivenArePressed(p_keyCodes) && this.keysGivenWerePressed(p_keyCodes);
	}

	public boolean keysPressedAreNoLongerOrdered(final int... p_keyCodes) {
		return !this.keysPressedAreOrdered(p_keyCodes) && this.keysPressedWereOrdered(p_keyCodes);
	}
	// endregion

	// region "`is*()`"".
	public static boolean isTypeable(final char p_char) {
		return Character.isDigit(p_char) ||
				Character.isLetter(p_char) ||
				Character.isWhitespace(p_char) ||
				NerdInputModule.isStandardKeyboardSymbol(p_char);
	}

	/**
	 * To be used for checking if a certain key can be typed as a character
	 * <b>using no escape sequence</b>.
	 *
	 * @param p_keyCode is the code of the key to check.
	 * @return Can the key can be used in typing?
	 */
	public boolean isSpecialKey(final int p_keyCode) {
		// I just didn't want to make an array 😂:
		return (p_keyCode > 96 && p_keyCode < 109
				// ^^^ `97` to `108` for all function keys
				// [regardless of whether `Shift` or `Ctrl` are pressed]:
				|| p_keyCode == 0 // `Fn`, plus any function key.
				|| p_keyCode == 2 // `Home`,
				|| p_keyCode == 3 // `End`,
				|| p_keyCode == 8 // `Backspace`,
				|| p_keyCode == 9 // `Tab`,
				|| p_keyCode == 10 // Both `Enter`s/`return`s.
				|| p_keyCode == 11 // `PageDown`,
				|| p_keyCode == 12 // Registered when a button is pressed on the numpad with `NumLock` off.
				|| p_keyCode == 16 // `PageUp`,
				|| p_keyCode == 19 // "`Alt`-Graph',
				|| p_keyCode == 20 // `CapsLock`,
				|| p_keyCode == 23 // `ScrollLock`,
				|| p_keyCode == 26 // `Insert`,
				|| p_keyCode == 147 // Both `Delete` keys,
				|| p_keyCode == 148 // `Pause`/`Break` and also `NumLock`,
				|| p_keyCode == 153 // `Menu`/`Application` AKA "RightClick" key.
				|| p_keyCode == 157 // "Meta"/"Super", AKA the "OS key".
		);
	}

	public static boolean isStandardKeyboardSymbol(final char p_char) {
		// boolean is = false;
		for (final var c : NerdInputModule.STANDARD_KEYBOARD_SYMBOLS)
			// Can't use this!:
			// return ch == p_char;
			// What if the array being examined is empty?!

			if (c == p_char)
				return true;

		// These used to be in the loop:
		// is = ch == p_char;
		// is |= ch == p_char;
		// return is;

		return false;
	}
	// endregion
	// endregion

}
