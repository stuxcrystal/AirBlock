package net.stuxcrystal.airblock.commands.arguments.split;

import java.util.LinkedList;

/**
 * Splitter that supports quotes.
 */
public class QuotedSplit implements ArgumentSplitter {

    /**
     * Executes the action.
     */
    private static class QuotedSplitExecutor {

        /**
         * The raw string.
         */
        private final String raw;

        /**
         * Should flags be parsed.
         */
        private final boolean parseFlags;

        /**
         * The parsed arguments.
         */
        private LinkedList<String> arguments = new LinkedList<String>();

        /**
         * Holds the current value of the argument.
         */
        private StringBuilder cArgument = new StringBuilder();

        /**
         * Contains the currently recognized flags.
         */
        private StringBuilder flags = new StringBuilder();

        /**
         * Is the next character is escaped?
         */
        private boolean isEscaped = false;

        /**
         * Does the parser currently parse flags?
         */
        private boolean inFlag = false;

        /**
         * Checks if the current splitter has been escaped?
         */
        private boolean wasQuoted = false;

        /**
         * Does the parser expect flags now?
         */
        private boolean expectFlags = true;

        /**
         * The current quote-char.
         */
        private Character quoteChar = null;

        /**
         * Prepares the quoted split executor.
         */
        private QuotedSplitExecutor(String raw, boolean parseFlags) {
            this.raw = raw;
            this.parseFlags = parseFlags;
        }

        /**
         * Adds a character to the appropriate StringBuilder-Instance.
         * @param c The character to add.
         */
        public void addCharacter(char c) {
            // If the character is escaped, remove the escape-mode.
            if (this.isEscaped) this.isEscaped = false;

            // If the parser is currently parsing flags,
            if (this.inFlag && this.expectFlags) {
                // Check if the flag is already known.
                if (!this.flags.toString().contains(new String(new char[]{c})))
                    this.flags.append(c);

                // If the parser is not parsing a flag,
            } else {
                // But is currently expecting a flag, make the parser don't expect a flag.
                if (this.expectFlags) this.expectFlags = false;

                // Add the character to the argument.
                this.cArgument.append(c);
            }
        }

        /**
         * Adds the new attribute to the argument list.
         */
        public void newAttribute() {
            if (this.inFlag) {
                this.inFlag = false;
            } else {
                this.arguments.add(this.cArgument.toString());
            }
            this.wasQuoted = false;
            this.cArgument = new StringBuilder();
        }

        /**
         * Executes the split algorithm.
         */
        public void execute() {

            for (char c : this.raw.toCharArray()) {

                switch (c) {

                    // Handle spaces.
                    case ' ':
                        // Just add the space if it is escaped.
                        if (this.isEscaped || this.quoteChar != null) {
                            this.addCharacter(c);
                            continue;
                        }

                        // Ignore the space if the current argument is empty.
                        if (!this.inFlag)
                            if (this.cArgument.length() == 0 && !this.wasQuoted) continue;

                        // Parse next attribute after this character.
                        this.newAttribute();
                        continue;

                    case '\\':
                        if (this.isEscaped) {
                            this.addCharacter(c);
                        } else {
                            this.isEscaped = true;

                        }
                        continue;

                    // Handle quotes.
                    case '"':
                    case '\'':
                        // Just add the character if it is escaped or quoted with the other quote character.
                        if (this.isEscaped || (!new Character(c).equals(this.quoteChar) && this.quoteChar!=null)) {
                            this.addCharacter(c);
                            continue;
                        }

                        // If there is no quote character, set the quote-character to this one.
                        if (this.quoteChar == null) {
                            this.wasQuoted = true;
                            this.quoteChar = c;

                            // Otherwise disable the quote-mode.
                        } else {
                            this.quoteChar = null;
                        }
                        continue;

                    // Handle flags.
                    case '-':
                        // Parse the flags.
                        if (this.parseFlags) {
                            // Sets In-Flag attribute.
                            if (this.expectFlags && !this.inFlag && !this.isEscaped) {
                                this.inFlag = true;
                                continue;
                            }
                        }

                        // Adds a character to the flag (or attribute).
                        this.addCharacter(c);
                        continue;

                    // Handle other characters.
                    default:
                        this.addCharacter(c);
                }
            }

            // If there is an argument left, just add the argument.
            if (this.cArgument.length() > 0) this.newAttribute();
        }

        /**
         * Parses the value.
         * @param value The value to parse.
         * @return The new value.
         */
        public static String[] getParserResult(String value, boolean parseFlags) {
            QuotedSplitExecutor executor = new QuotedSplitExecutor(value, parseFlags);
            executor.execute();

            if (parseFlags)
                executor.arguments.addFirst(executor.flags.toString());
            return executor.arguments.toArray(new String[executor.arguments.size()]);
        }
    }

    @Override
    public String[] split(String args, boolean parseFlags) {
        return QuotedSplitExecutor.getParserResult(args, parseFlags);
    }
}
