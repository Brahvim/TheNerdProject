package com.brahvim.nerd.io;

import java.io.File;
import java.io.FileNotFoundException;

import processing.core.PApplet;
import processing.data.JSONObject;

public class StringTable {
    // ...I guess these tables should be specific to each scene.

    // String tables exist for making it easy for TRANSLATORS to work with them.
    // Use a file!

    // region Fields.
    private File file;
    private JSONObject json;
    private String langauge;
    // endregion

    // region Constructors.
    public StringTable(File p_file, String p_lang) throws FileNotFoundException {
        this.file = p_file;
        this.langauge = p_lang;

        if (this.file == null)
            throw new NullPointerException("`StringTable`s cannot use `null` `File`s!");

        if (this.langauge == null)
            throw new NullPointerException(
                    "`StringTable`s cannot use `null` for language identifier `String`!");

        this.refresh();
    }

    public StringTable(String p_filePath, String p_lang) throws FileNotFoundException {
        this(new File(p_filePath), p_lang);
    }

    public StringTable(File p_file) throws FileNotFoundException {
        this.file = p_file;
        this.langauge = "en";

        if (this.file == null)
            throw new NullPointerException("`StringTable`s cannot use `null` `File`s!");

        this.refresh();
    }

    public StringTable(String p_filePath) throws FileNotFoundException {
        this(new File(p_filePath));
    }
    // endregion

    // region Language settings.
    public void setLanguage(String p_lang) {
        this.langauge = p_lang;
    }

    public void getLanguage(String p_lang) {
        this.langauge = p_lang;
    }
    // endregion

    // region `refresh()` overloads.
    public void refresh(File p_file) throws FileNotFoundException {
        if (p_file == null)
            throw new NullPointerException(
                    "`StringTable::refresh(File)` cannot take a `null` `File`!");

        this.file = p_file;
        this.refresh();
    }

    // Actual implementation:
    public void refresh() throws FileNotFoundException {
        // This check should be run.
        // The purpose behind calling `refresh()` *is*
        // to reflect upon changes in the file.

        if (!this.file.exists()) {
            throw new FileNotFoundException(
                    "No string table file `"
                            + this.file.getAbsolutePath()
                            + "` exists.");
        }

        this.json = PApplet.loadJSONObject(this.file);
    }
    // endregion

    // region `getString()` overloads.
    public String getString(String p_key) {
        return this.getString(p_key, "");
    }

    public String getString(String p_key, String p_default) {
        // Split all the keys!
        final String[] KEYS = PApplet.split(p_key, '.');

        // Index of last object in the JSON tree:
        final int LAST_OBJECT_ID = KEYS.length - 1;
        JSONObject lastObject = null;

        // Iterate till we see our query's last object,
        for (int i = 0; i != LAST_OBJECT_ID; i++) {
            synchronized (this.json) {
                lastObject = this.json.getJSONObject(KEYS[i]);
            }
        }

        lastObject = lastObject.getJSONObject(KEYS[LAST_OBJECT_ID]);

        // ...get the string of the specified langauge!
        String toRet = null;
        synchronized (this.json) {
            toRet = lastObject.getString(this.langauge);
        }

        if (toRet == null) {
            System.err.printf("""
                    `StringTable` key `%s` not found!
                    \tGiving default value, `%s`.""",
                    p_key, p_default);
            return p_default;
        }

        return toRet;
    }
    // endregion

    // region `getStringArray()` overloads.
    /*
     * public String[] getStringArray(String p_key) {
     * return null;
     * }
     * 
     * public String[] getStringArray(String p_key, String[] p_default) {
     * return p_default;
     * }
     */
    // endregion

    public JSONObject getUnderlyingJSON() {
        return this.json;
    }

}
