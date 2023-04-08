package com.brahvim.nerd.openal.al_ext_efx;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.openal.AL11;
import org.lwjgl.openal.EXTEfx;
import org.lwjgl.system.MemoryStack;

import com.brahvim.nerd.openal.AlNativeResource;
import com.brahvim.nerd.openal.AlSource;
import com.brahvim.nerd.openal.NerdAl;
import com.brahvim.nerd.openal.al_exceptions.NerdAlException;
import com.brahvim.nerd.openal.al_ext_efx.al_effects.AlAutowah;
import com.brahvim.nerd.openal.al_ext_efx.al_effects.AlChorus;
import com.brahvim.nerd.openal.al_ext_efx.al_effects.AlCompressor;
import com.brahvim.nerd.openal.al_ext_efx.al_effects.AlDistortion;
import com.brahvim.nerd.openal.al_ext_efx.al_effects.AlEaxReverb;
import com.brahvim.nerd.openal.al_ext_efx.al_effects.AlEcho;
import com.brahvim.nerd.openal.al_ext_efx.al_effects.AlEqualizer;
import com.brahvim.nerd.openal.al_ext_efx.al_effects.AlFlanger;
import com.brahvim.nerd.openal.al_ext_efx.al_effects.AlFrequencyShifter;
import com.brahvim.nerd.openal.al_ext_efx.al_effects.AlPitchShifter;
import com.brahvim.nerd.openal.al_ext_efx.al_effects.AlReverb;
import com.brahvim.nerd.openal.al_ext_efx.al_effects.AlRingModulator;

public class AlAuxiliaryEffectSlot extends AlNativeResource {

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
	protected static final ArrayList<AlAuxiliaryEffectSlot> ALL_INSTANCES = new ArrayList<>();

	private final NerdAl alMan;
	private AlEffect effect;
	private AlSource source;
	private int id = EXTEfx.AL_EFFECT_NULL;
	// endregion

	// region Constructors.
	public AlAuxiliaryEffectSlot(final NerdAl p_alMan) {
		this.alMan = p_alMan;
		this.id = EXTEfx.alGenAuxiliaryEffectSlots();
		this.alMan.checkAlError();
		AlAuxiliaryEffectSlot.ALL_INSTANCES.add(this);
	}

	public AlAuxiliaryEffectSlot(final NerdAl p_alMan, final AlEffect p_effect) {
		this.alMan = p_alMan;
		this.id = EXTEfx.alGenAuxiliaryEffectSlots();
		this.alMan.checkAlError();

		this.setEffect(p_effect);
		AlAuxiliaryEffectSlot.ALL_INSTANCES.add(this);
	}
	// endregion

	// region Instance collection queries.
	public static int getNumInstances() {
		return AlAuxiliaryEffectSlot.ALL_INSTANCES.size();
	}

	public static ArrayList<AlAuxiliaryEffectSlot> getAllInstances() {
		return new ArrayList<>(AlAuxiliaryEffectSlot.ALL_INSTANCES);
	}
	// endregion

	// region Getters.
	public int getId() {
		return this.id;
	}

	public float getGain() {
		return this.getFloat(EXTEfx.AL_EFFECTSLOT_GAIN);
	}

	public AlSource getSource() {
		return this.source;
	}

	@SuppressWarnings("unchecked")
	public <T extends AlEffect> T getEffect() {
		final int ID = this.getInt(EXTEfx.AL_EFFECTSLOT_EFFECT);

		if (ID == this.effect.id)
			return (T) this.effect;
		else
			for (final AlEffect e : AlEffect.ALL_INSTANCES) {
				if (e.id == ID)
					return (T) e;
			}

		final int EFFECT_TYPE = EXTEfx.alGetEffecti(ID, EXTEfx.AL_EFFECTSLOT_EFFECT);

		// region Construct new effect according to `EFFECT_TYPE`.
		// The JS code that generated the next part. Hee-hee!:
		/*
		 * // I literally copied these from the documentation.
		 * // Edited 'em using VSCode's selection features LOL.
		 * 
		 * let arr = [
		 * "AL_EFFECT_EAXREVERB",
		 * "AL_EFFECT_REVERB",
		 * "AL_EFFECT_CHORUS",
		 * "AL_EFFECT_DISTORTION",
		 * "AL_EFFECT_ECHO",
		 * "AL_EFFECT_FLANGER",
		 * "AL_EFFECT_FREQUENCY_SHIFTER",
		 * "AL_EFFECT_VOCAL_MORPHER",
		 * "AL_EFFECT_PITCH_SHIFTER",
		 * "AL_EFFECT_RING_MODULATOR",
		 * "AL_EFFECT_AUTOWAH",
		 * "AL_EFFECT_COMPRESSOR",
		 * "AL_EFFECT_EQUALIZER",
		 * ];
		 * 
		 * for (let x of arr)
		 * console.log(
		 * 
		 * `else if (EFFECT_TYPE == EXTEfx.${x})
		 * return (T) new ${capitalizeFirstChar(
		 * upperSnakeToCamelCase(x))}() {
		 * 
		 * @Override
		 * protected int getEffectType() {
		 * return EFFECT_TYPE;
		 * }
		 * 
		 * };`);
		 * 
		 * function upperSnakeToCamelCase(p_str) {
		 * p_str = p_str.toLowerCase();
		 * let build = '';
		 * 
		 * const STR_LEN = p_str.length;
		 * let lastUn = 0, secLastUn = 0;
		 * 
		 * for (let i = 0; i != STR_LEN; i++) {
		 * if (p_str.charAt(i) != '_')
		 * continue;
		 * 
		 * secLastUn = lastUn;
		 * lastUn = i;
		 * 
		 * if (secLastUn == 0)
		 * build += p_str.substring(0, i);
		 * else build += capitalizeFirstChar(p_str.substring(
		 * secLastUn + 1, lastUn
		 * ));
		 * }
		 * 
		 * build += capitalizeFirstChar(p_str.substring(1 + p_str.lastIndexOf('_')));
		 * 
		 * return build;
		 * }
		 * 
		 * function capitalizeFirstChar(p_str) {
		 * return p_str.charAt(0).toUpperCase() + p_str.substring(1);
		 * }
		 */

		if (EFFECT_TYPE == EXTEfx.AL_EFFECT_EAXREVERB)
			return (T) new AlEaxReverb(this.alMan);

		else if (EFFECT_TYPE == EXTEfx.AL_EFFECT_REVERB)
			return (T) new AlReverb(this.alMan);

		else if (EFFECT_TYPE == EXTEfx.AL_EFFECT_CHORUS)
			return (T) new AlChorus(this.alMan);

		else if (EFFECT_TYPE == EXTEfx.AL_EFFECT_DISTORTION)
			return (T) new AlDistortion(this.alMan);

		else if (EFFECT_TYPE == EXTEfx.AL_EFFECT_ECHO)
			return (T) new AlEcho(this.alMan);

		else if (EFFECT_TYPE == EXTEfx.AL_EFFECT_FLANGER)
			return (T) new AlFlanger(this.alMan);

		else if (EFFECT_TYPE == EXTEfx.AL_EFFECT_FREQUENCY_SHIFTER)
			return (T) new AlFrequencyShifter(this.alMan);

		else if (EFFECT_TYPE == EXTEfx.AL_EFFECT_VOCAL_MORPHER) {
			System.err.println(
					"LWJGL has not yet implemented `AL_VOCAL_MORPHER`, sorry.");
			// `return (T) new AlVocalMorpher();`
			return (T) new AlEffect(this.alMan) {
				@Override
				protected int getEffectType() {
					return EFFECT_TYPE;
				}
			};
		}

		else if (EFFECT_TYPE == EXTEfx.AL_EFFECT_PITCH_SHIFTER)
			return (T) new AlPitchShifter(this.alMan);

		else if (EFFECT_TYPE == EXTEfx.AL_EFFECT_RING_MODULATOR)
			return (T) new AlRingModulator(this.alMan);

		else if (EFFECT_TYPE == EXTEfx.AL_EFFECT_AUTOWAH)
			return (T) new AlAutowah(this.alMan);

		else if (EFFECT_TYPE == EXTEfx.AL_EFFECT_COMPRESSOR)
			return (T) new AlCompressor(this.alMan);

		else if (EFFECT_TYPE == EXTEfx.AL_EFFECT_EQUALIZER)
			return (T) new AlEqualizer(this.alMan);

		// Instead of throwing this exception, I could just use reflection to see
		// what classes extend `AlEffect` with the assumption that the default
		// constructor exists in them...
		else
			throw new NerdAlException("""
					No idea what this OpenAL effect is...
					Come to this line and modify Nerd's source to fix this!""");
		// endregion

	}
	// endregion

	// region Setters.
	public AlAuxiliaryEffectSlot setGain(final float p_value) {
		this.setFloat(EXTEfx.AL_EFFECTSLOT_GAIN, p_value);
		return this;
	}

	public AlAuxiliaryEffectSlot setSource(final AlSource p_source) {
		this.source = p_source;
		return this;
	}

	public AlEffect setEffect(final AlEffect p_effect) {
		final AlEffect toRet = this.effect;

		if (p_effect == null) {
			this.effect = null;
			EXTEfx.alAuxiliaryEffectSloti(this.id, EXTEfx.AL_EFFECTSLOT_EFFECT, EXTEfx.AL_EFFECT_NULL);
			return toRet;
		}

		this.effect = p_effect;
		this.effect.slot = this;
		EXTEfx.alAuxiliaryEffectSloti(this.id, EXTEfx.AL_EFFECTSLOT_EFFECT, p_effect.id);
		return toRet;
	}

	public AlAuxiliaryEffectSlot setAutoSend(final boolean p_value) {
		EXTEfx.alAuxiliaryEffectSloti(this.id, EXTEfx.AL_EFFECTSLOT_AUXILIARY_SEND_AUTO,
				p_value ? AL11.AL_TRUE : AL11.AL_FALSE);
		return this;
	}
	// endregion

	// region C-style OpenAL getters.
	public int getInt(final int p_alEnum) {
		MemoryStack.stackPush();
		final IntBuffer buffer = MemoryStack.stackMallocInt(1);

		EXTEfx.alGetAuxiliaryEffectSloti(this.id, p_alEnum, buffer);

		MemoryStack.stackPop();
		this.alMan.checkAlError();

		return buffer.get();
	}

	public int[] getIntVector(final int p_alEnum, final int p_vecSize) {
		MemoryStack.stackPush();
		final IntBuffer buffer = MemoryStack.stackMallocInt(p_vecSize);

		EXTEfx.alGetAuxiliaryEffectSlotiv(this.id, p_alEnum, buffer);

		MemoryStack.stackPop();
		this.alMan.checkAlError();

		return buffer.array();
	}

	public float getFloat(final int p_alEnum) {
		MemoryStack.stackPush();
		final FloatBuffer buffer = MemoryStack.stackMallocFloat(1);

		EXTEfx.alGetAuxiliaryEffectSlotf(this.id, p_alEnum, buffer);

		MemoryStack.stackPop();
		this.alMan.checkAlError();

		return buffer.get();
	}

	public float[] getFloatVector(final int p_alEnum, final int p_vecSize) {
		MemoryStack.stackPush();
		final FloatBuffer buffer = MemoryStack.stackMallocFloat(p_vecSize);

		EXTEfx.alGetAuxiliaryEffectSlotfv(this.id, p_alEnum, buffer);

		MemoryStack.stackPop();
		this.alMan.checkAlError();

		return buffer.array();
	}
	// endregion

	// region C-style OpenAL setters.
	public void setInt(final int p_alEnum, final int p_value) {
		EXTEfx.alAuxiliaryEffectSloti(this.id, p_alEnum, p_value);
		this.alMan.checkAlError();
	}

	public void setIntVector(final int p_alEnum, final int... p_values) {
		EXTEfx.alAuxiliaryEffectSlotiv(this.id, p_alEnum, p_values);
		this.alMan.checkAlError();
	}

	public void setFloat(final int p_alEnum, final float p_value) {
		EXTEfx.alAuxiliaryEffectSlotf(this.id, p_alEnum, p_value);
		this.alMan.checkAlError();
	}

	public void setFloatVector(final int p_alEnum, final float... p_values) {
		EXTEfx.alAuxiliaryEffectSlotfv(this.id, p_alEnum, p_values);
		this.alMan.checkAlError();
	}
	// endregion

	@Override
	protected void disposeImpl() {
		this.setEffect(null);
		this.source.setEffectSlot(null);
		EXTEfx.alDeleteAuxiliaryEffectSlots(this.id);
		AlAuxiliaryEffectSlot.ALL_INSTANCES.remove(this);
	}

}
