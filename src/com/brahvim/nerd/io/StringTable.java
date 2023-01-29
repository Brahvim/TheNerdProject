package com.brahvim.nerd.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;

public class StringTable {
    // ...I guess these tables should be specific to each scene.

    // String tables exist for making it easy for TRANSLATORS to work with them.
    // Use a file!

    // TODO: Declare this `public`, then try to give a way to save a modified table!
    // ..and please try not to mess up the file's comments...
    private final HashMap<String, String> TABLE = new HashMap<>();
    private File file;

    // region Constructors.
    public StringTable(File p_file) throws FileNotFoundException {
        this.file = p_file;

        if (this.file != null)
            this.refresh();
    }

    public StringTable(String p_filePath) throws FileNotFoundException {
        this(new File(p_filePath));
    }
    // endregion

    public void refresh(File p_file) throws FileNotFoundException {
        // This check should still be run.
        // The purpose behind calling `refresh()` *is*
        // to reflect upon changes in the file.
        if (!p_file.exists()) {
            System.out.printf("""
                    No string table file `%s` exists.
                    """, this.file.getAbsolutePath());
        }

        this.file = p_file;
        this.refresh(this.file);
    }

    // Actual implementation:
    public void refresh() throws FileNotFoundException {
        this.TABLE.clear();

        try (FileReader fileReader = new FileReader(this.file);
                BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String section = null, content = null;
            StringBuilder parsedContent;
            int eqPos = 0, lineLen = 0, newLineCharPos = 0, contentLastCharPos = 0;

            // Remember that this loop goes through EACH LINE!
            // Not each *character!* :joy::
            for (String line; (line = bufferedReader.readLine()) != null;) {
                lineLen = line.length();

                // Leave empty lines alone!:
                if (line.isBlank())
                    continue;

                // Skipping comments and registering sections,
                // and skip this iteration if they exist:
                switch (line.charAt(0)) {
                    case ';': // Semicolons are also comments in INI files, apparently!
                    case '#':
                        continue;
                    case '[':
                        section = line.substring(1, line.indexOf(']'));
                        continue;
                }

                // Find where the `=` sign is!:
                eqPos = line.indexOf('=');

                // Find a `"` symbol *without* a `\` before it:

                contentLastCharPos = lineLen; // We assume it's at the end.

                // If the character before isn't a backslash,
                while (line.charAt(contentLastCharPos - 1) != '\\') {
                    contentLastCharPos = line.lastIndexOf("\"", contentLastCharPos);
                }

                content = line.substring(eqPos + 1, contentLastCharPos);

                // Parse out `\n`s!:
                parsedContent = new StringBuilder(content);

                while ((newLineCharPos = parsedContent.indexOf("\\n")) != -1) {
                    // Causes an infinite loop, and I won't be writing `\n` anywhere, anyway:
                    // if (parsedContent.charAt(newLineCharPos - 1) == '\\')
                    // continue;

                    for (int i = 0; i < 2; i++)
                        parsedContent.deleteCharAt(newLineCharPos);
                    parsedContent.insert(newLineCharPos, '\n');
                }

                // if (content.contains("<br>"))
                // content = content.replace("\\\\n", App.NEWLINE);

                synchronized (this.TABLE) {
                    this.TABLE.put(
                            // Format: `SectionName.propertyName`:
                            section.concat(".")
                                    .concat(line.substring(0, eqPos)),
                            parsedContent.toString());
                }
            }
        } catch (Exception e) {
            System.out.printf("""
                    Failed to parse string table in file: `%s`.
                    """, this.file.getAbsolutePath());
            e.printStackTrace();
        }

    }

    public String getString(String p_key) {
        String ret = null;

        synchronized (this.TABLE) {
            ret = this.TABLE.get(p_key);
        }

        if (ret == null) {
            System.err.printf("String table key `%s` not found!\n", p_key);
            return "";
        }

        return ret;
    }

}
