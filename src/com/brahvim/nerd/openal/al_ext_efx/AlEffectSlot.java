package com.brahvim.nerd.openal.al_ext_efx;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.openal.AL11;
import org.lwjgl.openal.EXTEfx;
import org.lwjgl.system.MemoryStack;

import com.brahvim.nerd.openal.NerdAl;

public class AlEffectSlot {

	/*
	 * let arr = [
	 * "DENSITY",
	 * "DIFFUSION",
	 * "GAIN",
	 * "GAINHF",
	 * "GAINLF",
	 * "DECAY_TIME",
	 * "DECAY_HFRATIO",
	 * "DECAY_LFRATIO",
	 * "REFLECTIONS_GAIN",
	 * "REFLECTIONS_DELAY",
	 * "REFLECTIONS_PAN",
	 * "LATE_REVERB_GAIN",
	 * "LATE_REVERB_DELAY",
	 * "LATE_REVERB_PAN",
	 * "ECHO_TIME",
	 * "ECHO_DEPTH",
	 * "MODULATION_TIME",
	 * "MODULATION_DEPTH",
	 * "AIR_ABSORPTION_GAINHF",
	 * "HFREFERENCE",
	 * "ROOM_ROLLOFF_FACTOR",
	 * "DECAYHF_LIMIT"
	 * ];
	 * 
	 * let camelCaseArr = [];
	 * 
	 * for (let i = 0; i < arr.length; i++) {
	 * let s = arr[i];
	 * let split = s.split('_');
	 * 
	 * if (split.length == 1)
	 * camelCaseArr[i] = s.toLowerCase();
	 * continue;
	 * 
	 * let result;
	 * for (let j = 0; j < split.length; j++) {
	 * let t = split[j].toLowerCase();
	 * result = '';
	 * 
	 * if (j > 0)
	 * result.charAt(1).toUpperCase()
	 * .concat(t.substring(1, t.length));
	 * }
	 * }
	 * 
	 * for (let i = 0; i < arr.length; i++)
	 * console.log(`public void set${camelCaseArr[i]}(float p_value) {
	 * super.setFloat(EXTEfx.AL_${"REVERB"}_${arr[i]}, p_value);
	 * }`);
	 */

	// region Fields.
	private int id;
	private NerdAl alMan;
	private AlEffect effect;
	// endregion

	public AlEffectSlot(NerdAl p_alMan) {
		this.alMan = p_alMan;
		this.id = EXTEfx.alGenAuxiliaryEffectSlots();

		this.alMan.checkAlErrors();
		this.alMan.checkAlcErrors();
	}

	public AlEffectSlot(NerdAl p_alMan, AlEffect p_effect) {
		this.alMan = p_alMan;
		this.id = EXTEfx.alGenAuxiliaryEffectSlots();

		this.alMan.checkAlErrors();
		this.alMan.checkAlcErrors();

		this.setEffect(p_effect);
	}

	// region Getters.
	public int getId() {
		return this.id;
	}

	public AlEffect getEffect() {
		return this.effect;
	}
	// endregion

	// region Setters.
	public void setGain(float p_value) {
		this.setFloat(EXTEfx.AL_EFFECTSLOT_GAIN, p_value);
	}

	public void setEffect(AlEffect p_effect) {
		this.effect.dispose();
		EXTEfx.alAuxiliaryEffectSloti(this.id, EXTEfx.AL_EFFECT_TYPE, p_effect.id);
	}

	public void setAutoSend(boolean p_value) {
		EXTEfx.alAuxiliaryEffectSloti(this.id, EXTEfx.AL_EFFECTSLOT_AUXILIARY_SEND_AUTO,
				p_value ? AL11.AL_TRUE : AL11.AL_FALSE);
	}
	// endregion

	// region C-style OpenAL getters.
	public int getInt(int p_alEnum) {
		MemoryStack.stackPush();
		IntBuffer buffer = MemoryStack.stackMallocInt(1);

		EXTEfx.alGetAuxiliaryEffectSloti(this.id, p_alEnum, buffer);

		MemoryStack.stackPop();
		this.alMan.checkAlErrors();
		this.alMan.checkAlcErrors();

		return buffer.get();
	}

	public int[] getIntVector(int p_alEnum, int p_vecSize) {
		MemoryStack.stackPush();
		IntBuffer buffer = MemoryStack.stackMallocInt(p_vecSize);

		EXTEfx.alGetAuxiliaryEffectSlotiv(this.id, p_alEnum, buffer);

		MemoryStack.stackPop();
		this.alMan.checkAlErrors();
		this.alMan.checkAlcErrors();

		return buffer.array();
	}

	public float getFloat(int p_alEnum, float p_value) {
		MemoryStack.stackPush();
		FloatBuffer buffer = MemoryStack.stackMallocFloat(1);

		EXTEfx.alGetAuxiliaryEffectSlotf(this.id, p_alEnum, buffer);

		MemoryStack.stackPop();
		this.alMan.checkAlErrors();
		this.alMan.checkAlcErrors();

		return buffer.get();
	}

	public float[] getFloatVector(int p_alEnum, int p_vecSize) {
		MemoryStack.stackPush();
		FloatBuffer buffer = MemoryStack.stackMallocFloat(p_vecSize);

		EXTEfx.alGetAuxiliaryEffectSlotfv(this.id, p_alEnum, buffer);

		MemoryStack.stackPop();
		this.alMan.checkAlErrors();
		this.alMan.checkAlcErrors();

		return buffer.array();
	}
	// endregion

	// region C-style OpenAL setters.
	public void setInt(int p_alEnum, int p_value) {
		EXTEfx.alAuxiliaryEffectSloti(this.id, p_alEnum, p_value);

		this.alMan.checkAlErrors();
		this.alMan.checkAlcErrors();
	}

	public void setIntVector(int p_alEnum, int... p_values) {
		EXTEfx.alAuxiliaryEffectSlotiv(this.id, p_alEnum, p_values);

		this.alMan.checkAlErrors();
		this.alMan.checkAlcErrors();
	}

	public void setFloat(int p_alEnum, float p_value) {
		EXTEfx.alAuxiliaryEffectSlotf(this.id, p_alEnum, p_value);

		this.alMan.checkAlErrors();
		this.alMan.checkAlcErrors();
	}

	public void setFloatVector(int p_alEnum, float... p_values) {
		EXTEfx.alAuxiliaryEffectSlotfv(this.id, p_alEnum, p_values);

		this.alMan.checkAlErrors();
		this.alMan.checkAlcErrors();
	}
	// endregion

	public void dispose() {
		EXTEfx.alDeleteAuxiliaryEffectSlots(this.id);
	}

}
