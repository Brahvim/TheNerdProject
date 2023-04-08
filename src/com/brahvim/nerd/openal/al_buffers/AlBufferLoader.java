package com.brahvim.nerd.openal.al_buffers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.lwjgl.stb.STBVorbis;
import org.lwjgl.system.MemoryStack;

public class AlBufferLoader {

	public static ShortBuffer loadOgg(final File p_file) {
		try {
			MemoryStack.stackPush();
			final IntBuffer channelsBuffer = MemoryStack.stackMallocInt(1);

			MemoryStack.stackPush();
			final IntBuffer sampleRateBuffer = MemoryStack.stackMallocInt(1);

			// The bigger data (the audio) we're loading. Definitely goes on the heap!

			final ShortBuffer toRet = STBVorbis.stb_vorbis_decode_filename(
					p_file.getCanonicalPath(), channelsBuffer, sampleRateBuffer);

			if (toRet == null) {
				// System.err.println("STB failed to load audio data!");
				MemoryStack.stackPop();
				MemoryStack.stackPop();
			}

			// We're done. Remove the previous two allocations.
			MemoryStack.stackPop();
			MemoryStack.stackPop();

			return toRet;
		} catch (final IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	// https://stackoverflow.com/a/70050617/
	@Deprecated
	public static ByteBuffer loadWav(final File p_file) {
		// If the size is more than an `int` can handle, wrap!
		final ByteArrayOutputStream bytes = new ByteArrayOutputStream(
				(int) Math.min((long) Integer.MAX_VALUE, p_file.length()));

		try (AudioInputStream ais = AudioSystem.getAudioInputStream(p_file)) {
			for (int b = 0; (b = ais.read()) != -1;)
				bytes.write(b);
			return ByteBuffer.wrap(bytes.toByteArray());

		} catch (final UnsupportedAudioFileException e) {
			e.printStackTrace();
			return null;

		} catch (final IOException e) {
			e.printStackTrace();
			return null;
		}

	}

}
