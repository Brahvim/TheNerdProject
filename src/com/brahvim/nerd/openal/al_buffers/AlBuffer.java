package com.brahvim.nerd.openal.al_buffers;

import java.io.File;
import java.nio.Buffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.openal.AL11;
import org.lwjgl.openal.ALC11;
import org.lwjgl.system.MemoryStack;

import com.brahvim.nerd.openal.NerdAl;

public abstract class AlBuffer<BufferT extends Buffer> {

	// region Fields.
	protected int id;
	protected NerdAl manager;

	// LWJGL does not provide `AL_DATA` anywhere, :/
	// Storing it here for access.
	protected BufferT data;

	private final static ArrayList<AlBuffer<?>> buffers = new ArrayList<>();
	// endregion

	// region Constructors.
	public AlBuffer(NerdAl p_alInst) {
		this.manager = p_alInst;

		this.id = AL11.alGenBuffers();
		this.manager.checkAlErrors();
	}

	public AlBuffer(NerdAl p_alInst, BufferT p_data) {
		this.manager = p_alInst;

		this.id = AL11.alGenBuffers();
		this.manager.checkAlErrors();
	}
	// endregion

	@SuppressWarnings("unchecked")
	public static ArrayList<AlBuffer<?>> getEveryBufferEver() {
		return (ArrayList<AlBuffer<?>>) AlBuffer.buffers.clone();
	}

	// region `abstract` methods.
	public abstract void setData(int p_dataType, BufferT p_buffer, int p_sampleRate);

	public abstract AlBuffer<?> loadFrom(File p_file);
	// endregion

	public AlBuffer<?> loadFrom(String p_path) {
		this.loadFrom(new File(p_path));
		return this;
	}

	// region Getters.
	public int getId() {
		return this.id;
	}

	public int getSize() {
		// Not using `MemoryStack` to allocate here! This stuff is being returned!
		// ...but WAIT, it works..?!
		MemoryStack.stackPush();
		IntBuffer retVal = MemoryStack.stackMallocInt(1);
		AL11.alGetBufferiv(this.id, AL11.AL_SIZE, retVal);
		MemoryStack.stackPop();

		return retVal.get();
	}

	public int getBits() {
		// Not using `MemoryStack` to allocate here! This stuff is being returned!
		// ...but WAIT, it works..?!
		MemoryStack.stackPush();
		IntBuffer retVal = MemoryStack.stackMallocInt(1);
		AL11.alGetBufferiv(this.id, AL11.AL_BITS, retVal);
		MemoryStack.stackPop();

		return retVal.get();
	}

	public int getChannels() {
		// Not using `MemoryStack` to allocate here! This stuff is being returned!
		// ...but WAIT, it works..?!
		MemoryStack.stackPush();
		IntBuffer retVal = MemoryStack.stackMallocInt(1);
		AL11.alGetBufferiv(this.id, AL11.AL_CHANNELS, retVal);
		MemoryStack.stackPop();

		return retVal.get();
	}

	public BufferT getData() {
		return this.data;
	}

	public int getSampleRate() {
		MemoryStack.stackPush();
		IntBuffer sampleRateBuffer = MemoryStack.stackMallocInt(1);
		AL11.alBufferiv(this.id, ALC11.ALC_FREQUENCY, sampleRateBuffer);
		MemoryStack.stackPop();

		return sampleRateBuffer.get();
	}
	// endregion

	// region Setters.

	public void setBits(int p_bits) {
		AL11.alBufferi(this.id, AL11.AL_BITS, p_bits);
	}

	public void setSampleRate(int p_sampleRate) {
		AL11.alBufferi(this.id, AL11.AL_FREQUENCY, p_sampleRate);
	}

	public void setChannels(int p_channels) {
		AL11.alBufferi(this.id, AL11.AL_CHANNELS, p_channels);
	}

	// Older `setData()` overloads. No longer used thanks to generics!:
	/*
	 * public void setData(int p_dataType, float[] p_data, int p_sampleRate) {
	 * AL11.alBufferData(this.bufId, p_dataType, p_data, p_sampleRate);
	 * this.alInst.checkForErrors();
	 * 
	 * }
	 * 
	 * public void setData(int p_dataType, FloatBuffer p_data, int p_sampleRate) {
	 * AL11.alBufferData(this.bufId, p_dataType, p_data, p_sampleRate);
	 * this.alInst.checkForErrors();
	 * 
	 * }
	 * 
	 * public void setData(int p_dataType, byte[] p_data, int p_sampleRate) {
	 * this.setData(p_dataType, ByteBuffer.wrap(p_data), p_sampleRate);
	 * }
	 * 
	 * public void setData(int p_dataType, ByteBuffer p_data, int p_sampleRate) {
	 * AL11.alBufferData(this.bufId, p_dataType, p_data, p_sampleRate);
	 * this.alInst.checkForErrors();
	 * 
	 * }
	 * 
	 * public void setData(int p_dataType, short[] p_data, int p_sampleRate) {
	 * AL11.alBufferData(this.bufId, p_dataType, p_data, p_sampleRate);
	 * this.alInst.checkForErrors();
	 * 
	 * }
	 * 
	 * public void setData(int p_dataType, ShortBuffer p_data, int p_sampleRate) {
	 * AL11.alBufferData(this.bufId, p_dataType, p_data, p_sampleRate);
	 * this.alInst.checkForErrors();
	 * 
	 * }
	 * 
	 * public void setData(int p_dataType, int[] p_data, int p_sampleRate) {
	 * AL11.alBufferData(this.bufId, p_dataType, p_data, p_sampleRate);
	 * this.alInst.checkForErrors();
	 * 
	 * }
	 * 
	 * public void setData(int p_dataType, IntBuffer p_data, int p_sampleRate) {
	 * AL11.alBufferData(this.bufId, p_dataType, p_data, p_sampleRate);
	 * this.alInst.checkForErrors();
	 * 
	 * }
	 */

	// endregion

	public void dispose() {
		this.manager.getDeviceBuffers().remove(this);
		AlBuffer.buffers.remove(this);
		AL11.alDeleteBuffers(this.id);
		this.manager.checkAlErrors();
	}

}
