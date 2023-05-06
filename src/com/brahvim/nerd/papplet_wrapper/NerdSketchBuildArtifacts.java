package com.brahvim.nerd.papplet_wrapper;

import java.util.HashMap;

public class NerdSketchBuildArtifacts {

	private final NerdSketch SKETCH;
	private final HashMap<String, Object> EXTENSIONS;

	/* `package` */ NerdSketchBuildArtifacts(final NerdSketch p_sketch) {
		this.SKETCH = p_sketch;
		this.EXTENSIONS = this.SKETCH.EXTENSIONS;
	}

	public NerdSketch getSketch() {
		return this.SKETCH;
	}

	public HashMap<String, Object> getExtensionsMap() {
		return this.EXTENSIONS;
	}

	@SuppressWarnings("unchecked")
	public <RetT> RetT getExtObject(final String p_extName) {
		return (RetT) this.EXTENSIONS.get(p_extName);
	}

	/* `package` */ void addExtObject(final String p_extName, final Object p_extObject) {
		this.EXTENSIONS.put(p_extName, p_extObject);
	}

}
