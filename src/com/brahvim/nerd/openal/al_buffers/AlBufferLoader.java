package com.brahvim.nerd.openal.al_buffers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.lwjgl.openal.AL11;
import org.lwjgl.openal.ALC11;
import org.lwjgl.stb.STBVorbis;
import org.lwjgl.system.MemoryStack;

import com.brahvim.nerd.openal.al_exceptions.AlException;

public class AlBufferLoader {

	public static ShortBuffer loadOgg(File p_file) {
		try {
			return STBVorbis.stb_vorbis_decode_filename(p_file.getCanonicalPath(), null, null);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static ShortBuffer loadOgg(File p_file, long p_ctxId, int p_bufId) {
		// A note about the use of `org.lwjgl.system.MemoryStack`:
		/*
		 * LWJGL's `MemoryStack` class allows for stack allocations.
		 * They're faster for this case - where we are allocating amounts of memory the
		 * stack can handle into buffers, which is why we use them here. Java would
		 * otherwise use the heap for any object, including these `java.nio.Buffer`
		 * subclass instances used here, which is slower for a case like this.
		 * 
		 * After all, the reason why we're using buffers is just that we're supposed to
		 * use pointers with C API calls to simulate multiple return values. Why not
		 * also make them faster to access?
		 */

		try {
			MemoryStack.stackPush();
			IntBuffer channelsBuffer = MemoryStack.stackMallocInt(1);

			MemoryStack.stackPush();
			IntBuffer sampleRateBuffer = MemoryStack.stackMallocInt(1);

			// The bigger data (the audio) we're loading. Definitely goes on the heap!

			ShortBuffer rawAudioBuffer = STBVorbis.stb_vorbis_decode_filename(
					p_file.getCanonicalPath(), channelsBuffer, sampleRateBuffer);

			if (rawAudioBuffer == null) {
				System.err.println("STB failed to load audio data!");
				MemoryStack.stackPop();
				MemoryStack.stackPop();
			}

			// Give the OpenAL buffer the data if needed.
			if (!(p_ctxId == 0 || p_bufId == 0))
				AlBufferLoader.onAlc(p_ctxId, () -> {
					AL11.alBufferData(p_bufId,
							channelsBuffer.get() == 1 ? AL11.AL_FORMAT_MONO16 : AL11.AL_FORMAT_STEREO16,
							rawAudioBuffer, sampleRateBuffer.get());
				});

			// We're done. Remove the previous two allocations.
			MemoryStack.stackPop();
			MemoryStack.stackPop();

			return rawAudioBuffer;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	// [https://stackoverflow.com/a/70050617/]
	public static ByteBuffer loadWav(File p_file) {
		// If the size is more than an `int` can handle, wrap!
		ByteArrayOutputStream bytes = new ByteArrayOutputStream(
				(int) Math.min((long) Integer.MAX_VALUE, p_file.length()));

		try (AudioInputStream ais = AudioSystem.getAudioInputStream(p_file)) {
			for (int b = 0; (b = ais.read()) != -1;)
				bytes.write(b);
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ByteBuffer.wrap(bytes.toByteArray());
	}

	private static ByteBuffer loadWavImpl(ByteArrayOutputStream p_bytes, AudioInputStream p_ais) {
		if (p_bytes == null)
			throw new IllegalArgumentException(
					"`AlBufferLoader.loadWavImpl()` cannot use a `null` `ByteArrayOutputStream`!");

		for (int b = 0; (b = p_ais.read()) != -1;)
			p_bytes.write(b);

		return ByteBuffer.wrap(p_bytes.toByteArray());
	}

	public static ByteBuffer loadWav(File p_file, long p_ctxId, int p_bufId) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream(
				(int) Math.min((long) Integer.MAX_VALUE, p_file.length()));

		try (AudioInputStream ais = AudioSystem.getAudioInputStream(p_file)) {
			AlBufferLoader.loadWavImpl(bytes, ais);

			// Give the OpenAL buffer the data if needed.
			if (!(p_ctxId == 0 || p_bufId == 0))
				AlBufferLoader.onAlc(p_ctxId, () -> {
					AL11.alBufferData(p_bufId,
							ais.getFormat().getChannels() == 1
									? AL11.AL_FORMAT_MONO16
									: AL11.AL_FORMAT_STEREO16,
							AlBufferLoader.loadWav(p_file),
							(int) ais.getFormat().getSampleRate());
				});

		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Nope! I'm not supporting multithreading here O_O
	private static /* `synchronized` */ void onAlc(long p_ctx, Runnable p_runnable) {
		long prevCtx = ALC11.alcGetCurrentContext();

		if (!ALC11.alcMakeContextCurrent(p_ctx))
			throw new AlException(0);

		p_runnable.run();

		if (!ALC11.alcMakeContextCurrent(prevCtx))
			throw new AlException(0);
	}

}
