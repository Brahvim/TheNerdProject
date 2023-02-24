package com.brahvim.nerd_tests;

import com.brahvim.nerd.io.class_loaders.SceneClassLoader;
import com.brahvim.nerd.papplet_wrapper.Sketch;
import com.brahvim.nerd.scene_api.NerdScene;

public enum LoadedSceneClass {

	TEST_SCENE_5(
			// ...for some reason I can use only a JAR file now. No `.class` ones!
			"file:/" + Sketch.DATA_DIR_PATH + "TestScene5.jar",
			"com.brahvim.nerd_tests.scenes.TestScene5");

	// I asked ChatGPT about naming conventions for `final` fields, and it said
	// *this* was fine. Yes, I already knew about `System.out`:
	private final SceneClassLoader sceneClassLoader;

	private LoadedSceneClass(String p_urlString, String p_fullyQualifiedName) {
		this.sceneClassLoader = new SceneClassLoader(p_urlString, p_fullyQualifiedName);
	}

	public Class<? extends NerdScene> getSceneClassLoader() {
		return this.sceneClassLoader.getLoadedClass();
	}

}
