package com.brahvim.nerd.openal.al_buffers;

import java.io.File;
import java.nio.Buffer;
import java.nio.ByteBuffer;

import org.lwjgl.openal.AL11;

import com.brahvim.nerd.openal.NerdAl;

public class AlNativeBuffer extends AlBuffer<Buffer> {

	// region Constructors.
	public AlNativeBuffer(NerdAl p_alInst) {
		super(p_alInst);
	}

	public AlNativeBuffer(NerdAl p_alMan, int p_id) {
		super(p_alMan, p_id);
	}

	public AlNativeBuffer(AlBuffer<?> p_buffer) {
		super(p_buffer);
	}

	public AlNativeBuffer(NerdAl p_alInst, Buffer p_data) {
		super(p_alInst, p_data);
	}
	// endregion

	@Override
	public AlBuffer<?> loadFrom(File p_file) {
		throw new UnsupportedOperationException("""
				`AlNativeBuffer` exists for types you may add yourself!
				`AlNativeBuffer::loadFrom(File)` has no idea what you're trying to do.
				""");
		// return switch (PApplet.getExtension(p_file.getAbsolutePath())) {
		// case "wav" ->
		// case "ogg": ->
		// }
	}

	@Override
	public void setData(int p_format, Buffer p_buffer, int p_sampleRate) {
		super.data = p_buffer;
		super.dataType = p_format;
		AL11.alBufferData(super.id, p_format, ((ByteBuffer) p_buffer), p_sampleRate);
		super.alMan.checkAlErrors();
	}

}
