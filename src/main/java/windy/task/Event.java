package windy.task;

import windy.exception.InvalidInputFormatException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;

/**
 * Represents a task that occurs between a start and an end date or time.
 */
public class Event extends Task {

    private final LocalDate start;
    private final LocalDate end;
    private static final DateTimeFormatter INPUT_FORMATTER
            = DateTimeFormatter.ofPattern("uuuu-M-d").withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter OUTPUT_FORMATTER =
            DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);

    /**
     * Creates a task that occurs over a date range.
     *
     * @param name the task description
     * @param done whether the task has been completed
     * @param start the first date of the event, in {@code yyyy-M-d} format
     * @param end the last date of the event, in {@code yyyy-M-d} format
     * @throws InvalidInputFormatException if either date is invalid
     */
    public Event(String name, boolean done, String start, String end)  throws InvalidInputFormatException {
        super(name, done);
        try {
            this.start = LocalDate.parse(start, INPUT_FORMATTER);
            this.end = LocalDate.parse(end, INPUT_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new InvalidInputFormatException
                    ("     The format of event is wrong. Please use description /from yyyy-M-d /to yyyy-M-d");
        }

    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "[E][" + this.getStatus() + "] " + this.getName()
                + " (from: " + this.start.format(OUTPUT_FORMATTER)
                + " to: " + this.end.format(OUTPUT_FORMATTER) + ")";
    }

    /** {@inheritDoc} */
    @Override
    public String toDataString() {
        return "E | " + (this.isDone() ? "1" : "0") + " | " + this.getName() + " | " + this.start + " | " + this.end;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isOccur(LocalDate date) {
        return !date.isBefore(this.start) && !date.isAfter(this.end) && !isDone();
    }
}
