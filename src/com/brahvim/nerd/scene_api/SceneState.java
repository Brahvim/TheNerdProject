package com.brahvim.nerd.scene_api;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.brahvim.nerd.io.NerdByteSerial;

public class SceneState {
    private final HashMap<String, Object> DATA = new HashMap<>(0);

    // region From `HashMap`.
    public void clear() {
        this.DATA.clear();
    }

    public Object compute(final String key,
            final BiFunction<? super String, ? super Object, ? extends Object> remappingFunction) {
        return this.DATA.compute(key, remappingFunction);
    }

    public Object computeIfAbsent(final String key, final Function<? super String, ? extends Object> mappingFunction) {
        return this.DATA.computeIfAbsent(key, mappingFunction);
    }

    public Object computeIfPresent(final String key,
            final BiFunction<? super String, ? super Object, ? extends Object> remappingFunction) {
        return this.DATA.computeIfPresent(key, remappingFunction);
    }

    public boolean containsKey(final Object key) {
        return this.DATA.containsKey(key);
    }

    public boolean containsValue(final Object value) {
        return this.DATA.containsValue(value);
    }

    public Set<Map.Entry<String, Object>> entrySet() {
        return this.DATA.entrySet();
    }

    public void forEach(final BiConsumer<? super String, ? super Object> action) {
        this.DATA.forEach(action);
    }

    public boolean isEmpty() {
        return this.DATA.isEmpty();
    }

    public Set<String> keySet() {
        return this.DATA.keySet();
    }

    public Object merge(final String key, final Object value,
            final BiFunction<? super Object, ? super Object, ? extends Object> remappingFunction) {
        return this.DATA.merge(key, value, remappingFunction);
    }

    public int size() {
        return this.DATA.size();
    }

    public Collection<Object> values() {
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
     * If an object with the given key is already saved, it is updated.<br>
     * <br>
     * Else, a new key-value pair is created and saved.
     *
     */
    public <T> SceneState set(final String p_key, final T p_value) {
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
     * 
     * For this, the class will need to store a `HashSet` of `Queue`s for each
     * thread using these getter methods.
     * 
     * ...another idea, would be to offer an iterator instead.
     */

    // region Serialization.
    public byte[] toByteArray() {
        return NerdByteSerial.toBytes(this.DATA);
    }

    public void toFile(final File p_file) {
        NerdByteSerial.toFile(this.DATA, p_file);
    }

    public void toFile(final String p_fileName) {
        NerdByteSerial.toFile(this.DATA, p_fileName);
    }
    // endregion

}
