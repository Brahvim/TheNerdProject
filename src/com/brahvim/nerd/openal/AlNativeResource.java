package com.brahvim.nerd.openal;

import java.util.ArrayList;

public abstract class AlNativeResource {

	// region Fields and constructor.
	protected boolean hasDisposed, willDispose = true;

	private static ArrayList<AlNativeResource> ALL_INSTANCES = new ArrayList<>();

	protected AlNativeResource() {
		AlNativeResource.ALL_INSTANCES.add(this);
	}
	// endregion

	// region Instance collection queries.
	public static int getNumInstances() {
		return AlNativeResource.ALL_INSTANCES.size();
	}

	public static ArrayList<AlNativeResource> getAllResources() {
		return new ArrayList<>(AlNativeResource.ALL_INSTANCES);
	}
	// endregion

	// Yes, there's no `getId()` method here - IDs can be either `long`s or `int`s!

	public final boolean shouldDispose(final boolean p_value) {
		final boolean toRet = this.willDispose;
		this.willDispose = p_value;
		return toRet;
	}

	public final boolean isDisposed() {
		return this.hasDisposed;
	}

	public final void dispose() {
		if (!this.willDispose)
			return;

		// They're the same, so-uhh,
		this.disposeForcibly();
	}

	public final void disposeForcibly() {
		if (this.hasDisposed)
			return;

		this.hasDisposed = true;
		this.disposeImpl();
	}

	protected abstract void disposeImpl();

}
