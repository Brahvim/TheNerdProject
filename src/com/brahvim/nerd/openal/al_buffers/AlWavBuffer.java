package com.brahvim.nerd.openal.al_buffers;

import java.nio.ByteBuffer;

import org.lwjgl.openal.AL11;

import com.brahvim.nerd.openal.NerdAl;

public class AlWavBuffer extends NerdAlTypedBuffer<ByteBuffer> {

	// region Constructors.
	public AlWavBuffer(NerdAl p_alInst) {
		super(p_alInst);
	}

	public AlWavBuffer(NerdAl p_alInst, ByteBuffer p_data) {
		super(p_alInst, p_data);
	}
	// endregion

	@Override
	public void setData(int p_dataType, ByteBuffer p_buffer, int p_sampleRate) {
		super.data = p_buffer;
		AL11.alBufferData(super.bufId, p_dataType, p_buffer, p_sampleRate);
		super.alInst.checkAlErrors();
	}

}
