package com.brahvim.nerd.ext;

import com.brahvim.nerd.papplet_wrapper.Sketch;

public abstract class NerdExt {

	protected final Sketch SKETCH;

	public NerdExt(final Sketch p_sketch) {
		this.SKETCH = p_sketch;
	}

	protected abstract void initExt();

}
