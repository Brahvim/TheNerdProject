package com.brahvim.nerd.openal;

import java.io.File;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import org.lwjgl.openal.AL11;
import org.lwjgl.openal.ALC11;
import org.lwjgl.stb.STBVorbis;
import org.lwjgl.system.MemoryStack;

public class AlBufferDataLoader {

	public void loadOgg(File p_file, long p_ctxId, int p_bufId) {
		AlBufferDataLoader.loadOgg(p_file.getAbsolutePath(), p_ctxId, p_bufId);
	}

	public static void loadOgg(String p_fileName, long p_ctxId, int p_bufId) {
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

		MemoryStack.stackPush();
		IntBuffer channelsBuffer = MemoryStack.stackMallocInt(1);

		MemoryStack.stackPush();
		IntBuffer sampleRateBuffer = MemoryStack.stackMallocInt(1);

		// The bigger data (the audio) we're loading. Definitely goes on the heap!
		ShortBuffer rawAudioBuffer = STBVorbis.stb_vorbis_decode_filename(
				"path\\to\\file.ogg", channelsBuffer, sampleRateBuffer);

		if (rawAudioBuffer == null) {
			System.out.println("STB failed to load!");
			System.exit(0);
			MemoryStack.stackPop();
			MemoryStack.stackPop();
		}

		AlBufferDataLoader.onAlc(p_ctxId, () -> {
			AL11.alBufferData(p_bufId,
					channelsBuffer.get() == 1 ? AL11.AL_FORMAT_MONO16 : AL11.AL_FORMAT_STEREO16,
					rawAudioBuffer, sampleRateBuffer.get());
		});

		// We're done. Remove the previous two allocations.
		MemoryStack.stackPop();
		MemoryStack.stackPop();
	}

	private static synchronized void onAlc(long p_ctx, Runnable p_runnable) {
		long prevCtx = ALC11.alcGetCurrentContext();

		ALC11.alcMakeContextCurrent(p_ctx);
		p_runnable.run();
		ALC11.alcMakeContextCurrent(prevCtx);
	}

}
