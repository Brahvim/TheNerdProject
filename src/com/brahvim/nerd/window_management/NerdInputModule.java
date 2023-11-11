package com.brahvim.nerd.window_management;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import com.brahvim.nerd.processing_wrapper.NerdModule;
import com.brahvim.nerd.processing_wrapper.NerdSketch;

import processing.core.PConstants;
import processing.core.PVector;
import processing.event.MouseEvent;

public class NerdInputModule extends NerdModule {

	// region Fields.
	public static final List<Character> STANDARD_KEYBOARD_SYMBOLS = List.of(
			'\'', '\"', '-', '=', '`', '~', '!', '@', '#', '$',
			'%', '^', '&', '*', '(', ')', '{', '}', '[',
			']', ';', ',', '.', '/', '\\', ':', '|', '<',
			'>', '_', '+', '?');

	/** Position of the mouse relative to the monitor. */
	public final Point GLOBAL_MOUSE_POINT = new Point(),
			PREV_FRAME_GLOBAL_MOUSE_POINT = new Point();

	/** Position of the mouse relative to the monitor. */
	public final PVector GLOBAL_MOUSE_VECTOR = new PVector(),
			PREV_FRAME_GLOBAL_MOUSE_VECTOR = new PVector();

	// region Frame-wise states, Processing style (thus mutable).
	// ...yeah, *these* ain't mutable, are they?:
	public final List<PVector> UNPROJ_TOUCHES = new ArrayList<>(10),
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
			CURR_FRAME_MOUSE_CENTER_OFFSET_VECTOR = new PVector(),
			PREV_FRAME_MOUSE_CENTER_OFFSET_VECTOR = new PVector();

	/** Updated each time events changing this variable occur. */
	public char key, pkey;

	/** Updated each time events changing this variable occur. */
	public int keyCode, pkeyCode;

	/** Updated each time events changing this variable occur. */
	public int mouseButton, pmouseButton;

	/** Updated each time events changing this variable occur. */
	public boolean keyPressed, pkeyPressed; // NOSONAR

	/** Updated each time events changing this variable occur. */
	public boolean mousePressed, pmousePressed; // NOSONAR

	/** Updated each time events changing this variable occur. */
	public float mouseX, mouseY, pmouseX, pmouseY;

	/** Updated each time events changing this variable occur. */
	public boolean mouseLeft, mouseMid, mouseRight, pmouseLeft, pmouseMid, pmouseRight;

	/** Updated each time events changing this variable occur. */
	public float mouseScroll, pmouseScroll,
			mouseScrollDelta, pmouseScrollDelta,
			totalMouseScroll, ptotalMouseScroll;

	protected NerdWindowModule<?> window;
	protected final List<Integer> KEYS_HELD = new ArrayList<>(5), PREV_FRAME_KEYS_HELD = new ArrayList<>(5);
	// endregion
	// endregion

	public NerdInputModule(final NerdSketch<?> p_sketch) {
		super(p_sketch);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void preSetup() {
		this.window = (NerdWindowModule<?>) super.SKETCH.getNerdModule(NerdWindowModule.class);
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
	public void focusGained() {
		this.KEYS_HELD.clear();
	}

	@Override
	public void focusLost() {
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
			this.KEYS_HELD.add(this.keyCode);
		}
	}

	@Override
	public void keyReleased() {
		this.pmouseButton = this.mouseButton;

		this.key = super.SKETCH.key;
		this.keyCode = super.SKETCH.keyCode;
		this.keyPressed = super.SKETCH.keyPressed;

		try {
			synchronized (this.KEYS_HELD) {
				this.KEYS_HELD.remove((Integer) this.keyCode);
			}
		} catch (final IndexOutOfBoundsException e) { // NOSONAR
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
	public Integer[] getHeldKeysArray() {
		return this.KEYS_HELD.toArray(new Integer[0]);
	}

	public String getHeldKeysDebugString() {
		final int numStrings = this.KEYS_HELD.size();

		if (numStrings == 0)
			return "";

		String toRet = "[ ";
		final int iCheck = this.KEYS_HELD.size() - 1;

		for (int i = 0; i < numStrings; i++)
			toRet = toRet.concat(KeyEvent.getKeyText(this.KEYS_HELD.get(i)))
					.concat(i == iCheck ? " ]" : ", ");

		return toRet;
	}

	public String getLastFrameHeldKeysDebugString() {
		return this.PREV_FRAME_KEYS_HELD.toString();
	}

	public boolean onlyKeyPressedIs(final int p_keyCode) {
		return this.KEYS_HELD.size() == 1 && this.KEYS_HELD.contains(p_keyCode);
	}

	public boolean onlyKeyPressedWas(final int p_keyCode) {
		return this.PREV_FRAME_KEYS_HELD.size() == 1 && this.PREV_FRAME_KEYS_HELD.contains(p_keyCode);
	}

	public boolean onlyKeysPressedAre(final int... p_keyCodes) {
		boolean toRet = this.KEYS_HELD.size() == p_keyCodes.length;

		if (!toRet)
			return false;

		for (final int i : p_keyCodes)
			toRet &= this.KEYS_HELD.contains(i);

		return toRet;
	}

	public boolean onlyKeysPressedWere(final int... p_keyCodes) {
		boolean toRet = this.PREV_FRAME_KEYS_HELD.size() == p_keyCodes.length;

		if (!toRet)
			return false;

		for (final int i : p_keyCodes)
			toRet &= this.PREV_FRAME_KEYS_HELD.contains(i);

		return toRet;
	}

	public boolean keysPressedAreOrdered(final int... p_keyCodes) {
		final int paramArrLen = p_keyCodes.length;
		final int ownArrLen = this.KEYS_HELD.size();

		int paramArrId = 0;

		for (int i = 0; paramArrId < paramArrLen && i < ownArrLen; i++) {
			if (p_keyCodes[paramArrId] == this.KEYS_HELD.get(i))
				paramArrId++;
		}

		return paramArrId == paramArrLen;
	}

	public boolean keysPressedWereOrdered(final int... p_keyCodes) {
		final int paramArrLen = p_keyCodes.length;
		final int ownArrLen = this.PREV_FRAME_KEYS_HELD.size();

		int paramArrId = 0;

		for (int i = 0; paramArrId < paramArrLen && i < ownArrLen; i++) {
			if (p_keyCodes[paramArrId] == this.PREV_FRAME_KEYS_HELD.get(i))
				paramArrId++;
		}

		return paramArrId == paramArrLen;
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

	public boolean keysPressedLastFrame(final int... p_keyCodes) {
		for (final int i : p_keyCodes)
			if (!this.PREV_FRAME_KEYS_HELD.contains(i))
				return false;
		return true;
	}

	public boolean keyIsPressed(final int p_keyCode) {
		return this.KEYS_HELD.contains(p_keyCode);
	}

	public boolean keyWasPressed(final int p_keyCode) {
		return this.PREV_FRAME_KEYS_HELD.contains(p_keyCode);
	}

	public boolean anyGivenKeyIsPressed(final int... p_keyCodes) {
		for (final int i : p_keyCodes)
			if (this.KEYS_HELD.contains(i))
				return true;
		return false;
	}

	public boolean anyGivenKeyWasPressed(final int... p_keyCodes) {
		for (final int i : p_keyCodes)
			if (this.PREV_FRAME_KEYS_HELD.contains(i))
				return true;
		return false;
	}

	public static boolean isStandardKeyboardSymbol(final char p_char) {
		// boolean is = false;
		for (final char ch : NerdInputModule.STANDARD_KEYBOARD_SYMBOLS)
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
				NerdInputModule.isStandardKeyboardSymbol(p_char);
	}

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
				p_keyCode == 12 || // Registered when a button is pressed on the numpad with `NumLock` off.
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
