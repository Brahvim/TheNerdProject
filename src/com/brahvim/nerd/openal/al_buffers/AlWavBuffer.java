package com.brahvim.nerd.openal.al_buffers;

import java.nio.ByteBuffer;

import com.brahvim.nerd.openal.NerdAl;

public class AlWavBuffer extends AlTypedBuffer<ByteBuffer> {

	public AlWavBuffer(NerdAl p_alInst) {
		super(p_alInst);
	}

	public AlWavBuffer(NerdAl p_alInst, ByteBuffer p_data) {
		super(p_alInst, p_data);
	}

	@Override
	public void setData(int p_dataType, ByteBuffer p_buffer, int p_sampleRate) {
		
	}

}
