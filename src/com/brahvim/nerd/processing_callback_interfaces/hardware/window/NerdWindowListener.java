package com.brahvim.nerd.processing_callback_interfaces.hardware.window;

public interface NerdWindowListener
		extends NerdFocusLostCallback, NerdFocusGainedCallback,
		NerdFullscreenChangedCallback, NerdMonitorChangedCallback, NerdResizedCallback {

	@Override
	public default void focusLost() {
	}

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

}
