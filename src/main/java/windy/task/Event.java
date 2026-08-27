package windy.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;

import windy.exception.InvalidInputFormatException;

/**
 * Represents a task that occurs between a start and an end date or time.
 */
public class Event extends Task {

    private static final DateTimeFormatter INPUT_FORMATTER
            = DateTimeFormatter.ofPattern("uuuu-M-d").withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter OUTPUT_FORMATTER
            = DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);
    private final LocalDate startDate;
    private final LocalDate endDate;

    /**
     * Creates a task that occurs over a date range.
     *
     * @param name the task description
     * @param isDone whether the task has been completed
     * @param startDate the first date of the event, in {@code yyyy-M-d} format
     * @param endDate the last date of the event, in {@code yyyy-M-d} format
     * @throws InvalidInputFormatException if either date is invalid
     */
    public Event(String name, boolean isDone, String startDate, String endDate)
            throws InvalidInputFormatException {
        super(name, isDone);
        try {
            this.startDate = LocalDate.parse(startDate, INPUT_FORMATTER);
            this.endDate = LocalDate.parse(endDate, INPUT_FORMATTER);
        } catch (DateTimeParseException exception) {
            throw new InvalidInputFormatException(
                    "     The format of event is wrong. Please use description /from yyyy-M-d /to yyyy-M-d");
        }
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "[E][" + this.getStatus() + "] " + this.getName()
                + " (from: " + this.startDate.format(OUTPUT_FORMATTER)
                + " to: " + this.endDate.format(OUTPUT_FORMATTER) + ")";
    }

    /** {@inheritDoc} */
    @Override
    public String toDataString() {
        return "E | " + (this.isDone() ? "1" : "0") + " | " + this.getName()
                + " | " + this.startDate + " | " + this.endDate;
    }

    /** {@inheritDoc} */
    @Override
    public boolean occursOnDate(LocalDate date) {
        return !date.isBefore(this.startDate) && !date.isAfter(this.endDate) && !isDone();
    }
}
