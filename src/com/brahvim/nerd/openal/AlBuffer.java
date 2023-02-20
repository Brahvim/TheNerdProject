package com.brahvim.nerd.openal;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import org.lwjgl.openal.AL11;
import org.lwjgl.system.MemoryStack;

public class AlBuffer {
	private int bufId;
	private NerdAl alInst;

	// region Constructors.
	public AlBuffer(NerdAl p_alInst) {
		this.alInst = p_alInst;

		this.bufId = AL11.alGenBuffers();
		this.alInst.checkForErrors();
	}

	public AlBuffer(NerdAl p_alInst, Buffer p_data) {
		this.alInst = p_alInst;

		this.bufId = AL11.alGenBuffers();
		this.alInst.checkForErrors();
	}
	// endregion

	// region Getters.
	public int getId() {
		return this.bufId;
	}

	public int getSize() {
		// Not using `MemoryStack` to allocate here! This stuff is being returned!
		MemoryStack.stackPush();
		IntBuffer retVal = MemoryStack.stackMallocInt(1);
		AL11.alGetBufferiv(this.bufId, AL11.AL_SIZE, retVal);
		MemoryStack.stackPop();

		return retVal.get();
	}

	public void getData() {
		// TODO Retain the type of data loaded an fe
		// AL11.getBuffer
	}
	// endregion

	// region Setters.
	public void setBits() {
	}

	public void setFreq() {
	}

	public void setChannels() {
	}

	// region `setData()` overloads.
	public void setData(int p_dataType, float[] p_data, int p_sampleRate) {
		AL11.alBufferData(this.bufId, p_dataType, p_data, p_sampleRate);
		this.alInst.checkForErrors();

	}

	public void setData(int p_dataType, FloatBuffer p_data, int p_sampleRate) {
		AL11.alBufferData(this.bufId, p_dataType, p_data, p_sampleRate);
		this.alInst.checkForErrors();

	}

	public void setData(int p_dataType, byte[] p_data, int p_sampleRate) {
		this.setData(p_dataType, ByteBuffer.wrap(p_data), p_sampleRate);
	}

	public void setData(int p_dataType, ByteBuffer p_data, int p_sampleRate) {
		AL11.alBufferData(this.bufId, p_dataType, p_data, p_sampleRate);
		this.alInst.checkForErrors();

	}

	public void setData(int p_dataType, short[] p_data, int p_sampleRate) {
		AL11.alBufferData(this.bufId, p_dataType, p_data, p_sampleRate);
		this.alInst.checkForErrors();

	}

	public void setData(int p_dataType, ShortBuffer p_data, int p_sampleRate) {
		AL11.alBufferData(this.bufId, p_dataType, p_data, p_sampleRate);
		this.alInst.checkForErrors();

	}

	public void setData(int p_dataType, int[] p_data, int p_sampleRate) {
		AL11.alBufferData(this.bufId, p_dataType, p_data, p_sampleRate);
		this.alInst.checkForErrors();

	}

	public void setData(int p_dataType, IntBuffer p_data, int p_sampleRate) {
		AL11.alBufferData(this.bufId, p_dataType, p_data, p_sampleRate);
		this.alInst.checkForErrors();

	}

	// endregion
	// endregion

	public void dispose() {
		AL11.alDeleteBuffers(this.bufId);
	}

}
