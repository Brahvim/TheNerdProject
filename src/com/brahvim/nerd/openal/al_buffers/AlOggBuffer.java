package com.brahvim.nerd.openal.al_buffers;

import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

import org.lwjgl.openal.AL11;
import org.lwjgl.stb.STBVorbis;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.libc.LibCStdlib;

import com.brahvim.nerd.openal.NerdAl;

public class AlOggBuffer extends AlBuffer<ShortBuffer> {
	public final static ArrayList<AlOggBuffer> ALL_INSTANCES = new ArrayList<>();

	// region Constructors.
	public AlOggBuffer(NerdAl p_alMan) {
		super(p_alMan);
		AlOggBuffer.ALL_INSTANCES.add(this);
	}

	public AlOggBuffer(AlBuffer<?> p_buffer) {
		super(p_buffer);
		AlOggBuffer.ALL_INSTANCES.add(this);
	}

	public AlOggBuffer(NerdAl p_alMan, int p_id) {
		super(p_alMan, p_id);
		AlOggBuffer.ALL_INSTANCES.add(this);
	}

	public AlOggBuffer(NerdAl p_alInst, ShortBuffer p_data) {
		super(p_alInst, p_data);
		AlOggBuffer.ALL_INSTANCES.add(this);
	}
	// endregion

	// Free the buffer (or not) :D
	@Override
	protected void disposeImpl() {
		super.disposeImpl();
		LibCStdlib.free(super.data); // Yep, we literally made Java, C. "Welcome to JavaC!" :joy:
		AlOggBuffer.ALL_INSTANCES.remove(this);
	}

	@Override
	protected void setDataImpl(int p_format, ShortBuffer p_buffer, int p_sampleRate) {
		AL11.alBufferData(this.id, p_format, p_buffer, p_sampleRate);
	}

	@Override
	protected AlOggBuffer loadFromImpl(File p_file) {
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
				// System.err.println("STB failed to load audio data!");
				MemoryStack.stackPop();
				MemoryStack.stackPop();
			}

			// Give the OpenAL buffer the data:

			AL11.alBufferData(this.id,
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
