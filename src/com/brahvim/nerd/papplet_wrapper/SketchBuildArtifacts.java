package com.brahvim.nerd.papplet_wrapper;

import java.util.HashMap;

public class SketchBuildArtifacts {

	public final Sketch SKETCH;
	private final HashMap<String, Object> EXTENSIONS;

	/* `package` */ SketchBuildArtifacts(final Sketch p_sketch) {
		this.SKETCH = p_sketch;
		this.EXTENSIONS = this.SKETCH.EXTENSIONS;
	}

	public Sketch getSketch() {
		return this.SKETCH;
	}

	public HashMap<String, Object> getExtensionsMap() {
		return this.EXTENSIONS;
	}

	@SuppressWarnings("unchecked")
	public <RetT> RetT getExtObject(final String p_extName) {
		return (RetT) this.EXTENSIONS.get(p_extName);
	}

}
