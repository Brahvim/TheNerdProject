package com.brahvim.nerd.processing_callback_interfaces;

import com.brahvim.nerd.processing_callback_interfaces.keyboard.NerdKeyboardListener;
import com.brahvim.nerd.processing_callback_interfaces.mouse.NerdMouseListener;
import com.brahvim.nerd.processing_callback_interfaces.touch.NerdTouchListener;
import com.brahvim.nerd.processing_callback_interfaces.window.NerdWindowListener;

import processing.event.MouseEvent;

public interface NerdSketchHardwareListener
		extends NerdWindowListener, NerdMouseListener, NerdKeyboardListener, NerdTouchListener {

	@Override
	public default void focusGained() {
	}

	@Override
	public default void fullscreenChanged(final boolean p_state) {
	}

	@Override
	public default void monitorChanged() {
	}

	@Override
	public default void resized() {
	}

	@Override
	public default void mousePressed() {
	}

	@Override
	public default void mouseMoved() {
	}

	@Override
	public default void mouseDragged() {
	}

	@Override
	public default void mouseWheel(final MouseEvent p_mouseEvent) {
	}

	@Override
	public default void mouseReleased() {
	}

	@Override
	public default void mouseClicked() {
	}

	@Override
	public default void keyTyped() {
	}

	@Override
	public default void keyPressed() {
	}

	@Override
	public default void touchStarted() {
	}

	@Override
	public default void touchMoved() {
	}

	@Override
	public default void touchEnded() {
	}

}
