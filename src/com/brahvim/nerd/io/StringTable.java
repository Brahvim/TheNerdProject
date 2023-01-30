package com.brahvim.nerd.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class StringTable {
    // ...I guess these tables should be specific to each scene.

    // String tables exist for making it easy for TRANSLATORS to work with them.
    // Use a file!

    // TODO: Multiline strings! Please! Please add them!
    // TODO PS That backslash converter is not acting correctly either.

    // NOTTODO: Declare this `public`, and try to save a modified table!
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
            String section = null, propertyName = null;
            StringBuilder content = new StringBuilder(), parsedContent = new StringBuilder();
            int firstQuotePosPlusOne = 0, lineLen = 0, lineLenMinusOne = 0,
                    delimiterCharPos = 0, lastQuotePos = 0, eqPos = 0;
            boolean valueIsMultiLine = false, previousLineWasPartOfValue = false;

            // Remember that this loop goes through EACH LINE!
            // Not each *character!* :joy::
            for (String line; (line = bufferedReader.readLine()) != null;) {
                lineLen = line.length();
                lineLenMinusOne = lineLen - 1;

                // Following, are cases demanding skipping this iteration:

                // Skip over empty lines:
                if (line.isBlank())
                    continue;

                if (!valueIsMultiLine) {
                    // If you're not a multi-line value, it should be safe to free memory.
                    content.delete(0, content.length());
                    // Skipping comments and registering sections,
                    // and even this iteration if they exist:
                    switch (line.charAt(0)) {
                        case ';': // Semicolons are also comments in INI files, apparently!
                        case '#':
                            continue;
                        case '[':
                            section = line.substring(1, line.indexOf(']'));
                            continue;
                    }

                }

                // Find where the `=` sign is!
                // I don't care if the value is multi-line xD:
                eqPos = line.indexOf('=');

                // ...but don't derive any property-name :|
                if (!valueIsMultiLine)
                    propertyName = line.substring(0, eqPos);
                // I'm not shifting away this assignment to some other `if` for performance.
                // Who knows? Night need to modify this parser even more someday :joy:

                // Finding an index since some people might prefer
                // putting spaces between the property name and `=`:
                firstQuotePosPlusOne = line.indexOf('"', eqPos);

                // If the value is a multi-liner, the current line of the value won't have a
                // beginning quote.
                if (valueIsMultiLine)
                    firstQuotePosPlusOne = 0;

                // Find a `"` symbol *without* a `\` before it:
                lastQuotePos = lineLen; // We assume it's at the end.
                previousLineWasPartOfValue = valueIsMultiLine; // Will use this in just a bit.
                valueIsMultiLine = true; // We also assume this till we find proof!

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
                            valueIsMultiLine = false;
                            break;
                        }
                    }
                }
                // endregion

                // If the value *is* a multi-liner, don't perform parsing and saving.
                // We need to finish scanning it!
                if (valueIsMultiLine) {
                    if (previousLineWasPartOfValue)
                        content.append(line);
                    else
                        content.append(line.substring(firstQuotePosPlusOne + 1, lastQuotePos));

                    String contentString = content.toString();
                    System.out.printf("`contentString`: `%s`.\n", contentString);
                    continue;
                }

                // If the value isn't a multi-liner, go on!
                // Parse it, and add it to the map!

                content.append(line.substring(firstQuotePosPlusOne, lastQuotePos));

                String contentString = content.toString();
                System.out.printf("`contentString`: `%s`.\n", contentString);

                // `parsedContent` will experience changes according to delimiters:
                parsedContent.delete(0, parsedContent.length());
                parsedContent.append(content);

                // region Parse out `\n`s!:
                while ((delimiterCharPos = parsedContent.indexOf("\\n")) != -1) {
                    // Causes an infinite loop:
                    // if (parsedContent.charAt(newLineCharPos - 1) == '\\')
                    // continue;

                    // Better solution:

                    // Remove the `\n`:
                    for (int i = 0; i < 2; i++)
                        parsedContent.deleteCharAt(delimiterCharPos);

                    // Insert a real one in its place:
                    parsedContent.insert(delimiterCharPos, '\n');
                }

                // if (content.contains("<br>"))
                // content = content.replace("\\\\n", App.NEWLINE);
                // endregion

                // region Parse out `\t`s!:
                while ((delimiterCharPos = parsedContent.indexOf("\\t")) != -1) {
                    // Remove the `\t`:
                    for (int i = 0; i < 2; i++)
                        parsedContent.deleteCharAt(delimiterCharPos);

                    // Insert a real one in its place:
                    parsedContent.insert(delimiterCharPos, '\t');
                }
                // endregion

                // region Parse out backslashes last! This is better:
                while ((delimiterCharPos = parsedContent.indexOf("\\\\")) != -1) {
                    if (parsedContent.charAt(delimiterCharPos - 1) == 0)
                        ;

                    // Remove one of the backslashes. This gives us a single backslash,
                    // which is what we need:
                    parsedContent.deleteCharAt(delimiterCharPos);
                }
                // endregion

                // region ...put `parsedContent` into the map (while the map is locked)!
                synchronized (this.TABLE) {
                    // Format: `SectionName.propertyName`:
                    this.TABLE.put(
                            section.concat(".").concat(propertyName),
                            parsedContent.toString());
                }
                // endregion

            }

        } catch (

        IOException e) {
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
