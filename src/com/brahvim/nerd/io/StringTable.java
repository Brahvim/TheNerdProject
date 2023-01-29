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
            int firstQuotePosPlusOne = 0, lineLen = 0, lineLenMinusOne = 0,
                    newLineCharPos = 0, lastQuotePos = 0, eqPos = 0;

            // Remember that this loop goes through EACH LINE!
            // Not each *character!* :joy::
            for (String line; (line = bufferedReader.readLine()) != null;) {
                lineLen = line.length();
                lineLenMinusOne = lineLen - 1;

                // region Cases demanding skipping this iteration.
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
                // endregion

                // Find where the `=` sign is!:
                eqPos = line.indexOf('=');

                // Finding an index since some people might prefer
                // putting spaces between the property name and `=`:
                firstQuotePosPlusOne = line.indexOf('"', eqPos);

                // Find a `"` symbol *without* a `\` before it:
                lastQuotePos = lineLen; // We assume it's at the end.

                // region Find the quote that ends the property's value.
                // Go backwards through the string. When you see the first double-quote
                // character, stop!:
                for (int i = lineLenMinusOne; i != firstQuotePosPlusOne; i--) {
                    // If the character you currently see is a double-quote,
                    if (line.charAt(i) == '"') {
                        // ...and the character *before* it isn't a backslash,
                        if (line.charAt(i - 1) != '\\') {
                            // ...then it must be the quote marking the end of the property definition!
                            lastQuotePos = i;
                            break;
                        }
                    }
                }
                // endregion

                content = line.substring(firstQuotePosPlusOne + 1, lastQuotePos);

                // region Parse out `\n`s!:
                parsedContent = new StringBuilder(content);

                while ((newLineCharPos = parsedContent.indexOf("\\n")) != -1) {
                    // Causes an infinite loop:
                    // if (parsedContent.charAt(newLineCharPos - 1) == '\\')
                    // continue;

                    // Better solution:
                    for (int i = 0; i < 2; i++)
                        parsedContent.deleteCharAt(newLineCharPos);
                    parsedContent.insert(newLineCharPos, '\n');
                }

                // if (content.contains("<br>"))
                // content = content.replace("\\\\n", App.NEWLINE);
                // endregion

                // region ...put it into the map (while it is locked)!
                synchronized (this.TABLE) {
                    this.TABLE.put(
                            // Format: `SectionName.propertyName`:
                            section.concat(".")
                                    .concat(line.substring(0, eqPos)),
                            parsedContent.toString());
                }
                // endregion

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
