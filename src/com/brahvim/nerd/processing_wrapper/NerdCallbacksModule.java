package com.brahvim.nerd.processing_wrapper;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.function.Consumer;

import com.brahvim.nerd.processing_callback_interfaces.keyboard.NerdKeyboardListener;
import com.brahvim.nerd.processing_callback_interfaces.mouse.NerdMouseListener;
import com.brahvim.nerd.processing_callback_interfaces.touch.NerdTouchListener;
import com.brahvim.nerd.processing_callback_interfaces.window.NerdWindowListener;

public class NerdCallbacksModule extends NerdModule {

	// region Callback listeners,
	protected final LinkedHashSet<NerdMouseListener> MOUSE_LISTENERS = new LinkedHashSet<>(1);
	protected final LinkedHashSet<NerdTouchListener> TOUCH_LISTENERS = new LinkedHashSet<>(1);
	protected final LinkedHashSet<NerdWindowListener> WINDOW_LISTENERS = new LinkedHashSet<>(1);
	protected final LinkedHashSet<NerdKeyboardListener> KEYBOARD_LISTENERS = new LinkedHashSet<>(1);
	// ...to remove!:
	protected final HashSet<Consumer<NerdSketch>> CALLBACK_LISTENERS_TO_REMOVE = new HashSet<>(1);

	protected final LinkedHashSet<Consumer<NerdSketch>> SETTINGS_LISTENERS;
	protected final LinkedHashSet<Consumer<NerdSketch>> SETUP_LISTENERS;
	protected final LinkedHashSet<Consumer<NerdSketch>> PRE_LISTENERS;
	protected final LinkedHashSet<Consumer<NerdSketch>> PRE_DRAW_LISTENERS;
	protected final LinkedHashSet<Consumer<NerdSketch>> DRAW_LISTENERS;
	protected final LinkedHashSet<Consumer<NerdSketch>> POST_DRAW_LISTENERS;
	protected final LinkedHashSet<Consumer<NerdSketch>> POST_LISTENERS;
	protected final LinkedHashSet<Consumer<NerdSketch>> EXIT_LISTENERS;
	protected final LinkedHashSet<Consumer<NerdSketch>> DISPOSAL_LISTENERS;

	// Adding a new callbacks list to `NerdSketch`, or a subclass? REGISTER IT IN
	// THIS!:
	protected final LinkedHashSet<?>[] LIST_OF_CALLBACK_LISTS;
	// See the end of the constructor!
	// endregion

	public NerdCallbacksModule(final NerdSketch p_sketch, final NerdSketchBuilderSettings p_settings) {
		super(p_sketch);

		// Intializing these listeners:
		this.PRE_LISTENERS = p_settings.preListeners;
		this.POST_LISTENERS = p_settings.postListeners;
		this.DRAW_LISTENERS = p_settings.drawListeners;
		this.PRE_DRAW_LISTENERS = p_settings.preDrawListeners;
		this.POST_DRAW_LISTENERS = p_settings.postDrawListeners;

		// Intializing these listeners as well, haha!:
		this.EXIT_LISTENERS = p_settings.exitListeners;
		this.SETUP_LISTENERS = p_settings.setupListeners;
		this.SETTINGS_LISTENERS = p_settings.settingsListeners;
		this.DISPOSAL_LISTENERS = p_settings.disposalListeners;
		// this. .addAll(this.sketchConstructedListeners);

		this.LIST_OF_CALLBACK_LISTS = new LinkedHashSet<?>[] {
				this.DRAW_LISTENERS, this.PRE_DRAW_LISTENERS, this.POST_DRAW_LISTENERS,
				this.SETTINGS_LISTENERS, this.SETUP_LISTENERS, this.EXIT_LISTENERS, this.DISPOSAL_LISTENERS,
				this.PRE_LISTENERS, this.POST_LISTENERS };
	}

	// region Adding calback listeners.
	public void addPreListener(final Consumer<NerdSketch> p_preListener) {
		this.PRE_LISTENERS.add(Objects.requireNonNull(
				p_preListener, NerdSketch.NULL_LISTENER_ERROR_MESSAGE));
	}

	public void addPostListener(final Consumer<NerdSketch> p_postListener) {
		this.POST_LISTENERS.add(Objects.requireNonNull(
				p_postListener, NerdSketch.NULL_LISTENER_ERROR_MESSAGE));
	}

	public void addPreDrawListener(final Consumer<NerdSketch> p_preDrawListener) {
		this.PRE_DRAW_LISTENERS.add(Objects.requireNonNull(
				p_preDrawListener, NerdSketch.NULL_LISTENER_ERROR_MESSAGE));
	}

	public void addDrawListener(final Consumer<NerdSketch> p_drawListener) {
		this.DRAW_LISTENERS.add(Objects.requireNonNull(
				p_drawListener, NerdSketch.NULL_LISTENER_ERROR_MESSAGE));
	}

	public void addPostDrawListener(final Consumer<NerdSketch> p_postDrawListener) {
		this.POST_DRAW_LISTENERS.add(Objects.requireNonNull(
				p_postDrawListener, NerdSketch.NULL_LISTENER_ERROR_MESSAGE));
	}

	public void addSketchExitListener(final Consumer<NerdSketch> p_exitListener) {
		this.EXIT_LISTENERS.add(Objects.requireNonNull(
				p_exitListener, NerdSketch.NULL_LISTENER_ERROR_MESSAGE));
	}

	public void addSketchDisposalListener(final Consumer<NerdSketch> p_disposalListener) {
		this.DISPOSAL_LISTENERS.add(Objects.requireNonNull(
				p_disposalListener, NerdSketch.NULL_LISTENER_ERROR_MESSAGE));
	}
	// endregion

	// region Removing callback listeners.
	// Don't need all of these, but still will have them around in case internal
	// workings change!
	public void removePreListener(final Consumer<NerdSketch> p_listener) {
		this.CALLBACK_LISTENERS_TO_REMOVE.add(p_listener);
	}

	public void removePostListener(final Consumer<NerdSketch> p_listener) {
		this.CALLBACK_LISTENERS_TO_REMOVE.add(p_listener);
	}

	public void removePreDrawListener(final Consumer<NerdSketch> p_listener) {
		this.CALLBACK_LISTENERS_TO_REMOVE.add(p_listener);
	}

	public void removeDrawListener(final Consumer<NerdSketch> p_listener) {
		this.CALLBACK_LISTENERS_TO_REMOVE.add(p_listener);
	}

	public void removePostDrawListener(final Consumer<NerdSketch> p_listener) {
		this.CALLBACK_LISTENERS_TO_REMOVE.add(p_listener);
	}

	public void removeSketchExitListener(final Consumer<NerdSketch> p_listener) {
		this.CALLBACK_LISTENERS_TO_REMOVE.add(p_listener);
	}

	public void removeSketchDisposalListener(final Consumer<NerdSketch> p_listener) {
		this.CALLBACK_LISTENERS_TO_REMOVE.add(p_listener);
	}
	// endregion

}
