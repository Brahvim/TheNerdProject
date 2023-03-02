package com.brahvim.nerd.openal.al_ext_efx;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.openal.EXTEfx;
import org.lwjgl.system.MemoryStack;

import com.brahvim.nerd.openal.AlNativeResource;
import com.brahvim.nerd.openal.NerdAl;

public abstract class AlEffect extends AlNativeResource {

	// region Fields.
	public final static ArrayList<AlEffect> ALL_INSTANCES = new ArrayList<>();

	protected int id;
	protected NerdAl alMan;
	protected AlAuxiliaryEffectSlot slot;
	// endregion

	public AlEffect(NerdAl p_nerdAl) {
		AlEffect.ALL_INSTANCES.add(this);

		this.alMan = p_nerdAl;
		this.id = EXTEfx.alGenEffects();
		this.setInt(EXTEfx.AL_EFFECT_TYPE, this.getEffectType());
		this.alMan.checkAlError();
	}

	// region Getters!
	protected abstract int getEffectType();

	public int getId() {
		return this.id;
	}

	public boolean isUsed() {
		return this.slot != null;
	}
	// endregion

	// region C-style OpenAL getters.
	public int getInt(int p_alEnum) {
		MemoryStack.stackPush();
		IntBuffer buffer = MemoryStack.stackMallocInt(1);

		EXTEfx.alGetEffecti(this.id, p_alEnum, buffer);

		MemoryStack.stackPop();
		this.alMan.checkAlError();

		return buffer.get();
	}

	public int[] getIntVector(int p_alEnum, int p_vecSize) {
		MemoryStack.stackPush();
		IntBuffer buffer = MemoryStack.stackMallocInt(p_vecSize);

		EXTEfx.alGetEffectiv(this.id, p_alEnum, buffer);

		MemoryStack.stackPop();
		this.alMan.checkAlError();

		return buffer.array();
	}

	public float getFloat(int p_alEnum) {
		MemoryStack.stackPush();
		FloatBuffer buffer = MemoryStack.stackMallocFloat(1);

		EXTEfx.alGetEffectf(this.id, p_alEnum, buffer);

		MemoryStack.stackPop();
		this.alMan.checkAlError();

		return buffer.get();
	}

	public float[] getFloatVector(int p_alEnum, int p_vecSize) {
		MemoryStack.stackPush();
		FloatBuffer buffer = MemoryStack.stackMallocFloat(p_vecSize);

		EXTEfx.alGetEffectfv(this.id, p_alEnum, buffer);

		MemoryStack.stackPop();
		this.alMan.checkAlError();

		return buffer.array();
	}
	// endregion

	// region C-style OpenAL setters.
	public void setInt(int p_alEnum, int p_value) {
		EXTEfx.alEffecti(this.id, p_alEnum, p_value);
		if (this.slot != null)
			this.slot.setEffect(this);
		this.alMan.checkAlError();
	}

	public void setIntVector(int p_alEnum, int... p_values) {
		EXTEfx.alEffectiv(this.id, p_alEnum, p_values);
		if (this.slot != null)
			this.slot.setEffect(this);
		this.alMan.checkAlError();
	}

	public void setFloat(int p_alEnum, float p_value) {
		EXTEfx.alEffectf(this.id, p_alEnum, p_value);
		if (this.slot != null)
			this.slot.setEffect(this);
		this.alMan.checkAlError();
	}

	public void setFloatVector(int p_alEnum, float... p_values) {
		EXTEfx.alEffectfv(this.id, p_alEnum, p_values);
		if (this.slot != null)
			this.slot.setEffect(this);
		this.alMan.checkAlError();
	}
	// endregion

	public AlAuxiliaryEffectSlot getSlot() {
		return this.slot;
	}

	@Override
	protected void disposeImpl() {
		this.slot.setEffect(null);
		EXTEfx.alDeleteEffects(this.id);
		AlEffect.ALL_INSTANCES.remove(this);
	}

}
