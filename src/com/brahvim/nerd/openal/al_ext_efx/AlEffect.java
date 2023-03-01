package com.brahvim.nerd.openal.al_ext_efx;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.openal.EXTEfx;
import org.lwjgl.system.MemoryStack;

import com.brahvim.nerd.openal.AlObject;
import com.brahvim.nerd.openal.NerdAl;

public abstract class AlEffect extends AlObject {

	// region Fields.
	public final static ArrayList<AlEffect> effects = new ArrayList<>();

	protected int id;
	protected NerdAl alMan;
	protected boolean used, hasDisposed;
	protected AlAuxiliaryEffectSlot slot;
	// endregion

	public AlEffect(NerdAl p_NerdAl) {
		AlEffect.effects.add(this);

		this.alMan = p_NerdAl;
		this.id = EXTEfx.alGenEffects();
		this.setInt(EXTEfx.AL_EFFECT_TYPE, this.getEffectType());

		this.alMan.checkAlErrors();
		this.alMan.checkAlcErrors();
	}

	// region Getters!
	protected abstract int getEffectType();

	public int getId() {
		return this.id;
	}

	public boolean isUsed() {
		return this.used;
	}
	// endregion

	// region C-style OpenAL getters.
	public int getInt(int p_alEnum) {
		MemoryStack.stackPush();
		IntBuffer buffer = MemoryStack.stackMallocInt(1);

		EXTEfx.alGetEffecti(this.id, p_alEnum, buffer);

		MemoryStack.stackPop();
		this.alMan.checkAlErrors();
		this.alMan.checkAlcErrors();

		return buffer.get();
	}

	public int[] getIntVector(int p_alEnum, int p_vecSize) {
		MemoryStack.stackPush();
		IntBuffer buffer = MemoryStack.stackMallocInt(p_vecSize);

		EXTEfx.alGetEffectiv(this.id, p_alEnum, buffer);

		MemoryStack.stackPop();
		this.alMan.checkAlErrors();
		this.alMan.checkAlcErrors();

		return buffer.array();
	}

	public float getFloat(int p_alEnum) {
		MemoryStack.stackPush();
		FloatBuffer buffer = MemoryStack.stackMallocFloat(1);

		EXTEfx.alGetEffectf(this.id, p_alEnum, buffer);

		MemoryStack.stackPop();
		this.alMan.checkAlErrors();
		this.alMan.checkAlcErrors();

		return buffer.get();
	}

	public float[] getFloatVector(int p_alEnum, int p_vecSize) {
		MemoryStack.stackPush();
		FloatBuffer buffer = MemoryStack.stackMallocFloat(p_vecSize);

		EXTEfx.alGetEffectfv(this.id, p_alEnum, buffer);

		MemoryStack.stackPop();
		this.alMan.checkAlErrors();
		this.alMan.checkAlcErrors();

		return buffer.array();
	}
	// endregion

	// region C-style OpenAL setters.
	public void setInt(int p_alEnum, int p_value) {
		EXTEfx.alEffecti(this.id, p_alEnum, p_value);
		if (this.slot != null)
			this.slot.setEffect(this);

		this.alMan.checkAlErrors();
		this.alMan.checkAlcErrors();
	}

	public void setIntVector(int p_alEnum, int... p_values) {
		EXTEfx.alEffectiv(this.id, p_alEnum, p_values);
		if (this.slot != null)
			this.slot.setEffect(this);

		this.alMan.checkAlErrors();
		this.alMan.checkAlcErrors();
	}

	public void setFloat(int p_alEnum, float p_value) {
		EXTEfx.alEffectf(this.id, p_alEnum, p_value);
		if (this.slot != null)
			this.slot.setEffect(this);

		this.alMan.checkAlErrors();
		this.alMan.checkAlcErrors();
	}

	public void setFloatVector(int p_alEnum, float... p_values) {
		EXTEfx.alEffectfv(this.id, p_alEnum, p_values);
		if (this.slot != null)
			this.slot.setEffect(this);

		this.alMan.checkAlErrors();
		this.alMan.checkAlcErrors();
	}
	// endregion

	public AlAuxiliaryEffectSlot getSlot() {
		return this.slot;
	}

	// region Disposal.
	public boolean isDisposed() {
		return this.hasDisposed;
	}

	public void disposeImpl() {
		if (this.hasDisposed)
			return;
		this.hasDisposed = true;

		this.slot.setEffect(null);
		AlEffect.effects.remove(this);
		EXTEfx.alDeleteEffects(this.id);
	}
	// endregion

}
