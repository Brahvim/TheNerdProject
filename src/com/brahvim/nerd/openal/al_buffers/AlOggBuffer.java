package com.brahvim.nerd.openal.al_buffers;

import java.nio.ShortBuffer;

import org.lwjgl.openal.AL11;
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
		AL11.alBufferData(super.bufId, p_dataType, p_buffer.array(), p_sampleRate);
		super.alInst.checkAlErrors();
	}

	@Override // STBVorbis needs to free memory!!!
	public void dispose() {
		super.dispose();
		LibCStdlib.free(super.data); // Yep, we literally made Java, C. "Welcome to JavaC!" :joy:
	}

}
