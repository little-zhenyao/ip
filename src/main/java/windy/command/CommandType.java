package windy.command;

import java.util.Locale;

/**
 * Represents a command supported by the Windy chatbot.
 */
public enum CommandType {
    BYE,
    LIST,
    MARK,
    UNMARK,
    DELETE,
    TODO,
    DEADLINE,
    EVENT,
    FIND,
    UNKNOWN;

    /**
     * Converts the first word of a user's input into a command type.
     *
     * @param commandWord the first word entered by the user
     * @return the corresponding command type, or {@link #UNKNOWN} if it is unsupported
     */
    public static CommandType from(String commandWord) {
        try {
            return CommandType.valueOf(commandWord.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            return UNKNOWN;
        }
    }
}
