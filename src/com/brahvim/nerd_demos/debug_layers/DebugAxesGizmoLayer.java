package com.brahvim.nerd_demos.debug_layers;

import com.brahvim.nerd.framework.scene_api.NerdLayer;

public class DebugAxesGizmoLayer extends NerdLayer {

	@Override
	protected void draw() {
		GRAPHICS.translate(WINDOW.cx, WINDOW.cy);
	}

}
