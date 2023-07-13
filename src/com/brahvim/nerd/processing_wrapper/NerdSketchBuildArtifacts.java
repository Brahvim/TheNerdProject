package com.brahvim.nerd.processing_wrapper;

import java.util.HashMap;

public class NerdSketchBuildArtifacts {

	private final NerdSketch SKETCH;
	private final HashMap<String, NerdExtension> EXTENSIONS;

	/* `package` */ NerdSketchBuildArtifacts(final NerdSketch p_sketch) {
		this.SKETCH = p_sketch;
		this.EXTENSIONS = this.SKETCH.EXTENSIONS;
	}

	public NerdSketch getSketch() {
		return this.SKETCH;
	}

	@SuppressWarnings("unchecked")
	public <RetT> RetT getExtObject(final String p_extName) {
		return (RetT) this.EXTENSIONS.get(p_extName).getExtObject();
	}

}
