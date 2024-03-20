package com.brahvim.nerd.framework.lights;

import java.util.ArrayList;

import com.brahvim.nerd.framework.lights.NerdSpotLightQueue.NerdSpotLight;

import processing.core.PVector;

public class NerdPointLightQueue implements NerdLightSlotEntry {

	public static class NerdPointLight implements NerdLightSlotEntry {

		public final PVector
		/*   */ POSITION = new PVector(),
				COLOR = new PVector();

	}

	private final ArrayList<NerdSpotLight> QUEUE = new ArrayList<>(2);

}
