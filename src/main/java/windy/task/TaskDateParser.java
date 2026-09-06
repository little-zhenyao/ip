package windy.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

/**
 * Parses task dates using Windy's supported input format.
 */
public final class TaskDateParser {
    public static final String DEADLINE_FORMAT_ERROR_MESSAGE =
            "The format of deadline is wrong. Please use description /by yyyy-M-d";
    public static final String EVENT_FORMAT_ERROR_MESSAGE =
            "The format of event is wrong. Please use description /from yyyy-M-d /to yyyy-M-d";
    public static final String SEARCH_DATE_FORMAT_ERROR_MESSAGE =
            "Invalid date format. Please write like date yyyy-M-d";

    private static final DateTimeFormatter INPUT_FORMATTER =
            DateTimeFormatter.ofPattern("uuuu-M-d").withResolverStyle(ResolverStyle.STRICT);

    private TaskDateParser() {
    }

    /**
     * Parses a date using the supported {@code yyyy-M-d} input format.
     *
     * @param dateText the date text to parse.
     * @return the parsed date.
     * @throws DateTimeParseException if the date is invalid.
     */
    public static LocalDate parse(String dateText) {
        return LocalDate.parse(dateText, INPUT_FORMATTER);
    }
}
