package com.brahvim.nerd.openal.al_buffers;

import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import org.lwjgl.openal.AL11;
import org.lwjgl.stb.STBVorbis;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.libc.LibCStdlib;

import com.brahvim.nerd.openal.NerdAl;

public class AlOggBuffer extends AlBuffer<ShortBuffer> {

	// region Constructors.
	public AlOggBuffer(NerdAl p_alInst) {
		super(p_alInst);
	}

	public AlOggBuffer(NerdAl p_alInst, ShortBuffer p_data) {
		super(p_alInst, p_data);
	}
	// endregion

	@Override
	public void setData(int p_dataType, ShortBuffer p_buffer, int p_sampleRate) {
		super.data = p_buffer;
		super.dataType = p_dataType;
		AL11.alBufferData(super.id, p_dataType, p_buffer.array(), p_sampleRate);
		super.alMan.checkAlErrors();
	}

	@Override // STBVorbis needs to free memory!!!
	public void dispose() {
		super.dispose();
		LibCStdlib.free(super.data); // Yep, we literally made Java, C. "Welcome to JavaC!" :joy:
	}

	@Override
	public AlOggBuffer loadFrom(File p_file) {
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
				//System.err.println("STB failed to load audio data!");
				MemoryStack.stackPop();
				MemoryStack.stackPop();
			}

			// Give the OpenAL buffer the data:

			AL11.alBufferData(super.id,
					super.dataType = channelsBuffer.get() == 1
							? AL11.AL_FORMAT_MONO16
							: AL11.AL_FORMAT_STEREO16,
					rawAudioBuffer, sampleRateBuffer.get());

			// We're done. Remove the previous two allocations.
			MemoryStack.stackPop();
			MemoryStack.stackPop();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return this;
	}

}
