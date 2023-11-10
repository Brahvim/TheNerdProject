package com.brahvim.nerd.framework.scene_api;

import java.io.File;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.brahvim.nerd.utils.NerdByteSerialUtils;

public class NerdSceneState {

	private final HashMap<String, Serializable> DATA = new HashMap<>(0);

	// region From `HashMap`.
	public void clear() {
		this.DATA.clear();
	}

	public Serializable compute(final String key,
			final BiFunction<? super String, ? super Serializable, ? extends Serializable> remappingFunction) {
		return this.DATA.compute(key, remappingFunction);
	}

	public Serializable computeIfAbsent(final String key,
			final Function<? super String, ? extends Serializable> mappingFunction) {
		return this.DATA.computeIfAbsent(key, mappingFunction);
	}

	public Serializable computeIfPresent(final String key,
			final BiFunction<? super String, ? super Serializable, ? extends Serializable> remappingFunction) {
		return this.DATA.computeIfPresent(key, remappingFunction);
	}

	public boolean containsKey(final Serializable key) {
		return this.DATA.containsKey(key);
	}

	public boolean containsValue(final Serializable value) {
		return this.DATA.containsValue(value);
	}

	public Set<Map.Entry<String, Serializable>> entrySet() {
		return this.DATA.entrySet();
	}

	public void forEach(final BiConsumer<? super String, ? super Serializable> action) {
		this.DATA.forEach(action);
	}

	public boolean isEmpty() {
		return this.DATA.isEmpty();
	}

	public Set<String> keySet() {
		return this.DATA.keySet();
	}

	public Serializable merge(final String key, final Serializable value,
			final BiFunction<? super Serializable, ? super Serializable, ? extends Serializable> remappingFunction) {
		return this.DATA.merge(key, value, remappingFunction);
	}

	public int size() {
		return this.DATA.size();
	}

	public Collection<Serializable> values() {
		return this.DATA.values();
	}
	// endregion

	// region Methods to modify the saved state's data.
	// Don't waste time casting.
	// But, don't muddle with types.
	// It'll start throwing exceptions!
	/**
	 * Returns {@code null} if the key is not contained.
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(final String p_key) {
		return (T) this.DATA.get(p_key);
	}

	@SuppressWarnings("unchecked")
	public <T> T get(final String p_key, final T p_default) {
		final T toRet = (T) this.get(p_key); // ...let's actually rely *too much* on the JIT.
		return toRet == null ? p_default : toRet;
	}

	/**
	 * If an object with the given key is already saved, it is updated.
	 * <p>
	 * Else, a new key-value pair is created and saved.
	 */
	public NerdSceneState set(final String p_key, final Serializable p_value) {
		this.DATA.put(p_key, p_value);
		return this;
	}

	@SuppressWarnings("unchecked")
	public <T> T remove(final String p_key) {
		return (T) this.DATA.remove(p_key);
	}
	// endregion

	// Useless idea: How about a "queue getter"? 🤪
	/*
	 * You nest calls to "`queueGet()`", and at last, call "`getQueue()`", which
	 * gives the all objects requested as an array or `ArrayList`.
	 * For this, the class will need to store a `HashSet` of `Queue`s for each
	 * thread using these getter methods.
	 * ...another idea, would be to offer an iterator instead.
	 */

	// region Serialization.
	public byte[] toByteArray() {
		return NerdByteSerialUtils.toBytes(this.DATA);
	}

	public void toFile(final File p_file) {
		NerdByteSerialUtils.toFile(this.DATA, p_file);
	}

	public void toFile(final String p_fileName) {
		NerdByteSerialUtils.toFile(this.DATA, p_fileName);
	}
	// endregion

}
