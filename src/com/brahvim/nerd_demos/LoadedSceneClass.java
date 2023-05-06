package com.brahvim.nerd_demos;

import com.brahvim.nerd.io.class_loaders.NerdSceneClassLoader;
import com.brahvim.nerd.papplet_wrapper.NerdSketch;
import com.brahvim.nerd.scene_api.NerdScene;

public enum LoadedSceneClass {

	DEMO_SCENE_5(
			// JARs only!
			"file:/" + NerdSketch.DATA_DIR_PATH + "DemoScene5.jar",
			"com.brahvim.nerd_tests.scenes.DemoScene5");

	// region Non-`enum` stuff.
	// I asked ChatGPT about naming conventions for `final` fields, and it said
	// *this* was fine. Yes, I already knew about `System.out`:
	private final NerdSceneClassLoader sceneClassLoader;

	private LoadedSceneClass(final String p_urlString, final String p_fullyQualifiedName) {
		this.sceneClassLoader = new NerdSceneClassLoader(p_urlString, p_fullyQualifiedName);
	}

	public Class<? extends NerdScene> getSceneClassLoader() {
		return this.sceneClassLoader.getLoadedClass();
	}
	// endregion

}
