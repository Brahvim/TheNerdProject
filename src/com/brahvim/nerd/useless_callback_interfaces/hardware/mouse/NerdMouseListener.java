package com.brahvim.nerd.useless_callback_interfaces.hardware.mouse;

public interface NerdMouseListener extends NerdMousePressedCallback, NerdMouseMovedCallback, NerdMouseDraggedCallback,
		NerdMouseWheelCallback, NerdMouseReleasedCallback, NerdMouseClickedCallback {

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
	public default void mouseWheel(final processing.event.MouseEvent p_mouseEvent) {
	}

	@Override
	public default void mouseReleased() {
	}

	@Override
	public default void mouseClicked() {
	}

}
