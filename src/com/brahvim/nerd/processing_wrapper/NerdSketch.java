package com.brahvim.nerd.processing_wrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.brahvim.nerd.processing_callback_interfaces.workflow.NerdSketchAllWorkflowsListener;
import processing.core.PApplet;

public class NerdSketch extends PApplet implements NerdSketchAllWorkflowsListener {

	private final NerdSketchSettings SKETCH_SETTINGS;
	private final List<NerdModule> MODULES = new ArrayList<>(3);
	private final Map<NerdModule, Class<? extends NerdModule>>
	//////////////////////////////////////////////////////////
	MODULES_TO_CLASSES_MAP = new HashMap<>(3);

	// region Constructions.
	@SuppressWarnings("unused")
	private NerdSketch() {
		throw new IllegalAccessError("Please instantiate `" + this.getClass().getSimpleName()
				+ "`es the way they're supposed to be! Sorry...");
	}

	public NerdSketch(final NerdSketchSettings p_settings) {
		this.SKETCH_SETTINGS = p_settings;
	}
	// endregion

	@Override
	public void settings() {
		if (this.SKETCH_SETTINGS.fullScreen)
			super.fullScreen();
		else
			super.size(this.SKETCH_SETTINGS.width, this.SKETCH_SETTINGS.height);
	}

	@Override
	public void setup() {
	}

	@Override
	public void draw() {
	}

	@Override
	public void exit() {
	}

	@Override
	public void dispose() {
	}

	// region Hardware callbacks!
	@Override
	public void mouseClicked() {
	}

	@Override
	public void mouseDragged() {
	}

	@Override
	public void mousePressed() {
	}

	@Override
	public void mouseReleased() {
	}

	@Override
	public void keyPressed() {
	}

	@Override
	public void keyReleased() {
	}

	@Override
	public void keyTyped() {
	}
	// endregion

}
