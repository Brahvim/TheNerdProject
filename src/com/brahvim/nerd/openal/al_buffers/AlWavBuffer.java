package com.brahvim.nerd.openal.al_buffers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.lwjgl.openal.AL11;

import com.brahvim.nerd.openal.NerdAl;

public class AlWavBuffer extends AlBuffer<ByteBuffer> {

	// region Constructors.
	public AlWavBuffer(NerdAl p_alMan) {
		super(p_alMan);
	}

	public AlWavBuffer(AlBuffer<?> p_buffer) {
		super(p_buffer);
	}

	public AlWavBuffer(NerdAl p_alMan, int p_id) {
		super(p_alMan, p_id);
	}

	public AlWavBuffer(NerdAl p_alInst, ByteBuffer p_data) {
		super(p_alInst, p_data);
	}
	// endregion

	@Override
	protected void setDataImpl(int p_format, ByteBuffer p_buffer, int p_sampleRate) {
		AL11.alBufferData(this.id, p_format, p_buffer, p_sampleRate);
	}

	@Override
	@Deprecated
	public AlBuffer<?> loadFrom(String p_path) {
		return super.loadFrom(p_path);
	}

	@Override
	@Deprecated
	protected AlWavBuffer loadFromImpl(File p_file) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream(
				(int) Math.min((long) Integer.MAX_VALUE, p_file.length()));

		try (AudioInputStream ais = AudioSystem.getAudioInputStream(p_file)) {
			for (int b = 0; (b = ais.read()) != -1;)
				bytes.write(b);

			// Give the OpenAL buffer the data:
			AL11.alBufferData(this.id,
					super.dataType = ais.getFormat().getChannels() == 1
							? AL11.AL_FORMAT_MONO16
							: AL11.AL_FORMAT_STEREO16,
					AlBufferLoader.loadWav(p_file),
					(int) ais.getFormat().getSampleRate());
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return this;
	}

}
