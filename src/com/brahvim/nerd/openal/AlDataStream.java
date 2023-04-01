package com.brahvim.nerd.openal;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.lwjgl.openal.AL11;

import com.brahvim.nerd.openal.al_buffers.AlWavBuffer;

public class AlDataStream {

	// region Fields.
	protected static final ArrayList<AlDataStream> ALL_INSTANCES = new ArrayList<>();

	private NerdAl alMan;
	private AlSource source;
	private final ArrayList<AlWavBuffer> buffers = new ArrayList<>(3),
			unusedBuffersPool = new ArrayList<>(5);
	// endregion

	public AlDataStream(NerdAl p_alMan, AlSource p_source) {
		this.alMan = p_alMan;
		this.source = p_source;
		AlDataStream.ALL_INSTANCES.add(this);
	}

	public synchronized void addBytes(int p_alFormat, byte[] p_bytes, int p_sampleRate) {
		// This is fine - `ArrayList`s don't decrease their size anyway.
		if (this.unusedBuffersPool.isEmpty())
			this.unusedBuffersPool.add(new AlWavBuffer(this.alMan));

		final AlWavBuffer toQueue = this.unusedBuffersPool.remove(0);
		toQueue.setData(p_alFormat, ByteBuffer.wrap(p_bytes), p_sampleRate);
		this.source.queueBuffers(toQueue);
		this.alMan.checkAlError();
		this.buffers.add(toQueue);
	}

	// Yo! Did this break?
	// ...you might wanna check out that loop in this method!:
	/* `package` */ void framelyCallback() {
		for (int i = this.source.getBuffersProcessed() - 1; i != 0; i--) {
			AlWavBuffer b = this.buffers.get(i);
			this.source.unqueueBuffers(b);
			this.alMan.checkAlError();
			this.unusedBuffersPool.add(this.buffers.remove(i));
		}
	}

	public synchronized void stop() {
		// Page `14` of the "OpenAL Programmer's Guide" mentions this
		// nice shortcut to remove all attached buffers from a source:
		this.source.setInt(AL11.AL_BUFFER, 0);
		this.alMan.checkAlError();

		// Should I actually be doing this...?
		this.alMan.checkAlError();
	}

	public ArrayList<AlWavBuffer> getAlBuffers() {
		return new ArrayList<>(this.buffers);
	}

}
