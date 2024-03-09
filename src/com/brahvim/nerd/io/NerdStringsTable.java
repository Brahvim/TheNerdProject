package com.brahvim.nerd.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.random.RandomGenerator;

import com.brahvim.nerd.processing_wrapper.NerdSketch;

import processing.core.PApplet;
import processing.data.JSONArray;
import processing.data.JSONObject;

/**
 * <h2>A class for string tables, which are used for localization.</h2>
 * <p>
 * A "global" namespace for these strings is provided through
 * {@linkplain NerdSketch#getGlobalStringsTable()
 * NerdSketch::getGlobalStringTable()}, so you can access
 * string table data across scenes!
 */
public class NerdStringsTable {

	// region Fields.
	private File file;
	private JSONObject json;
	private String language;
	// endregion

	// region Constructors.
	@SuppressWarnings("unchecked")
	public NerdStringsTable(final NerdStringsTable p_table) {
		this.file = p_table.file.getAbsoluteFile();
		this.language = p_table.language;
		this.json = new JSONObject();

		final Iterator<String> keysItr = p_table.json.keyIterator();
		while (keysItr.hasNext()) {
			final String s = keysItr.next();
			p_table.json.getJSONObject(s);
		}
	}

	public NerdStringsTable(final File p_file, final String p_lang) throws FileNotFoundException {
		this.file = p_file;
		this.language = p_lang;

		if (this.file == null)
			throw new NullPointerException("`NerdStringTable`s cannot use `null` `File`s!");

		if (this.language == null)
			throw new NullPointerException("`NerdStringTable`s cannot use `null` for language identifier `String`!");

		this.refresh();
	}

	public NerdStringsTable(final String p_filePath, final String p_lang) throws FileNotFoundException {
		this(new File(p_filePath), p_lang);
	}

	public NerdStringsTable(final File p_file) throws FileNotFoundException {
		this.file = p_file;
		this.language = "en";

		if (this.file == null)
			throw new NullPointerException("`NerdStringTable`s cannot use `null` `File`s!");

		this.refresh();
	}

	public NerdStringsTable(final String p_filePath) throws FileNotFoundException {
		this(new File(p_filePath));
	}
	// endregion

	// region Language settings.
	public void setLanguage(final String p_lang) {
		this.language = p_lang;
	}

	public void getLanguage(final String p_lang) {
		this.language = p_lang;
	}
	// endregion

	// region `refresh()` overloads.
	public void refresh(final File p_file) throws FileNotFoundException {
		if (p_file == null)
			throw new NullPointerException("`StringTable::refresh(File)` cannot take a `null` `File`!");

		this.file = p_file;
		this.refresh();
	}

	// Actual implementation:
	public void refresh() throws FileNotFoundException {
		// This check should be run.
		// The purpose behind calling `refresh()` *is*
		// to reflect upon changes in the file.

		if (!this.file.exists()) {
			throw new FileNotFoundException("No string table file `" + this.file.getAbsolutePath() + "` exists.");
		}

		this.json = PApplet.loadJSONObject(this.file);
	}
	// endregion

	// region `getString()` overloads.
	public String get(final String p_key) {
		return this.get(p_key, "");
	}

	public String get(final String p_key, final String p_default) {
		// Split all the keys!
		final String[] keys = PApplet.split(p_key, '.');

		// Index of second-last object in the JSON tree:
		final int secondLastObjectId = keys.length - 1;
		JSONObject lastObject = null;

		// Iterate till we see our query's second-last object,
		for (int i = 0; i != secondLastObjectId; ++i)
			synchronized (this.json) {
				lastObject = this.json.getJSONObject(keys[i]);
			}

		// Get the very last object from here:
		if (lastObject != null)
			lastObject = lastObject.getJSONObject(keys[secondLastObjectId]);
		// Oh - and yes, I too, have ***no*** idea why we can get the "last object" only
		// from outside the loop!

		// ...get the string of the specified language!
		final String toRet = lastObject == null ? "" : lastObject.getString(this.language);
		/* NOSONAR! */ lastObject = null; // GC, do you wish to collect it? Please do it now!

		if (toRet == null) {
			System.err.printf("""
					`NerdStringTable` key `%s` not found!
					\tGiving default value, `%s`.""", p_key, p_default);
			return p_default;
		}

		return toRet;
	}
	// endregion

	// region `fromArray()` overloads.
	public String fromArray(final String p_key, final int p_id) {
		return this.fromArray(p_key, p_id, "");
	}

	public String fromArray(final String p_key, final int p_id, final String p_default) {
		// Split all the keys!
		final String[] keys = PApplet.split(p_key, '.');

		// Index of second-last object in the JSON tree:
		final int secondLastObjectId = keys.length - 1;
		JSONObject lastObject = null;

		// Iterate till we see our query's second-last object,
		for (int i = 0; i != secondLastObjectId; ++i) {
			synchronized (this.json) {
				lastObject = this.json.getJSONObject(keys[i]);
			}
		}

		// Get the very last object from here:
		if (lastObject != null)
			lastObject = lastObject.getJSONObject(keys[secondLastObjectId]);
		// Oh - and yes, I too, have ***no*** idea why we can get the "last object" only
		// from outside the loop!

		JSONArray stringArray = null;
		// region Get the array, else return `p_default`.
		try {
			if (lastObject != null)
				stringArray = lastObject.getJSONArray(this.language);
			// ...get the string-array of the specified language!
		} catch (final Exception e) {
			System.err.printf("""
					There is no array called `%s` in the `NerdStringTable` file!
					\tGiving default value, `%s`.""", p_key, p_default);
			return p_default;
		}
		// endregion

		/* NOSONAR! */ lastObject = null; // GC, do you wish to collect it? Please do it now!

		String toRet = p_default;
		// region Get the element if it is available.
		try {
			toRet = stringArray == null ? "" : stringArray.getString(p_id);
		} catch (final RuntimeException e) {
			System.err.printf("""
					`JSONArray` `%s` does not have any element at index `%d`!
					\tGiving default value, `%s`.""", p_key, p_id, p_default);
		}
		// endregion

		return toRet;
	}
	// endregion

	// region `randomFromArray()` overloads.
	public String randomFromArray(final String p_key) {
		return this.randomFromArray(p_key, "");
	}

	public String randomFromArray(final String p_key, final String p_default) {
		// Split all the keys!
		final String[] keys = PApplet.split(p_key, '.');

		// Index of second-last object in the JSON tree:
		final int secondLastObjectId = keys.length - 1;
		JSONObject lastObject = null;

		// Iterate till we see our query's second-last object,
		for (int i = 0; i != secondLastObjectId; ++i)
			synchronized (this.json) {
				lastObject = this.json.getJSONObject(keys[i]);
			}

		// Get the very last object from here:
		if (lastObject != null)
			lastObject = lastObject.getJSONObject(keys[secondLastObjectId]);
		// Oh - and yes, I too, have ***no*** idea why we can get the "last object" only
		// from outside the loop!

		JSONArray stringArray = null;
		// region Get the array, else return `p_default`.
		try {
			if (lastObject != null)
				stringArray = lastObject.getJSONArray(this.language);
			// ...get the string-array of the specified language!
		} catch (final Exception e) {
			System.err.printf("""
					There is no array called `%s` in the `NerdStringTable` file!
					\tGiving default value, `%s`.""", p_key, p_default);
			return p_default;
		}
		// endregion

		// ~~Probably a useless, extra instruction:~~ We DON'T know how large this guy
		// is in memory.
		// Must clean him upas quick as possible. May benefit from parallel GC timings,
		// right?
		/* NOSONAR! */ lastObject = null; // GC, do you wish to collect it? Please do it now!

		return stringArray == null ? ""
				: stringArray.getString(RandomGenerator.getDefault().nextInt(0, stringArray.size()));
	}
	// endregion

	public JSONObject getUnderlyingJSON() {
		return this.json;
	}

}
