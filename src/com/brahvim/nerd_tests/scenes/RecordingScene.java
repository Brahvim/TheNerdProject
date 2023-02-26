package com.brahvim.nerd_tests.scenes;

import com.brahvim.nerd.openal.AlCapture;
import com.brahvim.nerd.openal.AlSource;
import com.brahvim.nerd.openal.al_buffers.AlWavBuffer;
import com.brahvim.nerd.scene_api.NerdScene;
import com.brahvim.nerd.scene_api.SceneState;

import processing.core.PConstants;

public class RecordingScene extends NerdScene {
	private AlCapture capture;
	private AlSource capturePlayback;
	private AlWavBuffer capturedAudio;

	@Override
	protected void setup(SceneState p_state) {
		this.capture = new AlCapture(SKETCH.AL);
		this.capturePlayback = new AlSource(SKETCH.AL);
		this.capturedAudio = new AlWavBuffer(SKETCH.AL);
	}

	@Override
	public void mouseClicked() {
		switch (SKETCH.mouseButton) {
			case PConstants.LEFT -> {
				this.capturePlayback.stop();
				this.capture.startCapturing();
			}

			case PConstants.RIGHT -> {
				this.capturePlayback.setBuffer(
						this.capture.stopCapturing(this.capturedAudio));
				this.capturePlayback.play();
			}
		}
	}

}
