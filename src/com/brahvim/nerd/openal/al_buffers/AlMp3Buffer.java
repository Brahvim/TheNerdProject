package com.brahvim.nerd.openal.al_buffers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.ArrayList;

import javax.sound.sampled.AudioFormat;

import org.lwjgl.openal.AL11;

import com.brahvim.nerd.openal.NerdAl;

import fr.delthas.javamp3.Mp3InputStream;

@Deprecated
public class AlMp3Buffer extends AlBuffer<IntBuffer> {
	public static final ArrayList<AlMp3Buffer> ALL_INSTANCES = new ArrayList<>();

	// region Constructors.
	public AlMp3Buffer(final NerdAl p_alMan) {
		super(p_alMan);
		AlMp3Buffer.ALL_INSTANCES.add(this);
	}

	public AlMp3Buffer(final AlBuffer<?> p_buffer) {
		super(p_buffer);
		AlMp3Buffer.ALL_INSTANCES.add(this);
	}

	public AlMp3Buffer(final NerdAl p_alMan, final int p_id) {
		super(p_alMan, p_id);
		AlMp3Buffer.ALL_INSTANCES.add(this);
	}

	public AlMp3Buffer(final NerdAl p_alInst, final IntBuffer p_data) {
		super(p_alInst, p_data);
		AlMp3Buffer.ALL_INSTANCES.add(this);
	}
	// endregion

	@Override
	protected void disposeImpl() {
		super.disposeImpl();
		AlMp3Buffer.ALL_INSTANCES.remove(this);
	}

	@Override
	protected void setDataImpl(final int p_format, final IntBuffer p_buffer, final int p_sampleRate) {
		AL11.alBufferData(this.id, p_format, p_buffer, p_sampleRate);
	}

	@Override
	@Deprecated
	public AlBuffer<?> loadFrom(final String p_path) {
		return super.loadFrom(p_path);
	}

	@Override
	@Deprecated
	protected AlMp3Buffer loadFromImpl(final File p_file) {
		AudioFormat format = null;
		final ByteArrayOutputStream bytes = new ByteArrayOutputStream(
				(int) Math.min((long) Integer.MAX_VALUE, p_file.length()));

		try (final Mp3InputStream stream = new Mp3InputStream(new FileInputStream(p_file))) {
			format = stream.getAudioFormat();
			for (int b = 0; (b = stream.read()) != -1;)
				bytes.write(b);
		} catch (final IOException e) {
			e.printStackTrace();
		}

		AL11.alBufferData(this.id,
				super.alFormat = format.getChannels() == 1
						? AL11.AL_FORMAT_MONO16
						: AL11.AL_FORMAT_STEREO16,
				ByteBuffer.wrap(bytes.toByteArray())
						.order(ByteOrder.nativeOrder()).asIntBuffer(),
				(int) format.getSampleRate());

		return this;
	}

}
