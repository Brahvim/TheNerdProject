package com.brahvim.nerd.openal.al_buffers;

import java.io.File;
import java.nio.Buffer;
import java.nio.ByteBuffer;

import org.lwjgl.openal.AL11;

import com.brahvim.nerd.openal.NerdAl;

public class AlNoTypeBuffer extends AlBuffer<Buffer> {

	// region Constructors.
	public AlNoTypeBuffer(final NerdAl p_alMan) {
		super(p_alMan);
	}

	public AlNoTypeBuffer(final AlBuffer<?> p_buffer) {
		super(p_buffer);
	}

	public AlNoTypeBuffer(final NerdAl p_alMan, final int p_id) {
		super(p_alMan, p_id);
	}

	public AlNoTypeBuffer(final NerdAl p_alInst, final Buffer p_data) {
		super(p_alInst, p_data);
	}
	// endregion

	@Override
	protected AlBuffer<Buffer> loadFromImpl(final File p_file) {
		throw new UnsupportedOperationException("""
				`AlNativeBuffer` exists for types you may add yourself!
				`AlNativeBuffer::loadFrom(File)` has no idea what you're trying to do.
				""");
		// return switch (PApplet.getExtension(p_file.getAbsolutePath())) {
		// case "wav" ->
		// case "ogg": ->
		// default: // ?!??!?
		// }
	}

	@Override
	protected void setDataImpl(final int p_format, final Buffer p_buffer, final int p_sampleRate) {
		AL11.alBufferData(this.id, p_format, (ByteBuffer) p_buffer, p_sampleRate);
	}

}
