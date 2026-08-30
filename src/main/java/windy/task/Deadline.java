package windy.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;

import windy.exception.InvalidInputFormatException;

/**
 * Represents a task that must be completed by a specific date or time.
 */
public class Deadline extends Task {

    private static final DateTimeFormatter INPUT_FORMATTER =
            DateTimeFormatter.ofPattern("uuuu-M-d").withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter OUTPUT_FORMATTER =
            DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);
    private final LocalDate deadline;

    /**
     * Creates a task that must be completed by a deadline.
     *
     * @param name the task description
     * @param isDone whether the task has been completed
     * @param deadline the deadline in {@code yyyy-M-d} format
     * @throws InvalidInputFormatException if the deadline is not a valid date
     */
    public Deadline(String name, boolean isDone, String deadline) throws InvalidInputFormatException {
        super(name, isDone);
        try {
            this.deadline = LocalDate.parse(deadline, INPUT_FORMATTER);
        } catch (DateTimeParseException exception) {
            throw new InvalidInputFormatException(
                    "     The format of deadline is wrong. Please use description /by yyyy-M-d");
        }
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "[D][" + this.getStatus() + "] " + this.getName()
                + " (by: " + this.deadline.format(OUTPUT_FORMATTER) + ")";
    }

    /** {@inheritDoc} */
    @Override
    public String toDataString() {
        return "D | " + (this.isDone() ? "1" : "0") + " | " + this.getName()
                + " | " + this.deadline;
    }

    /** {@inheritDoc} */
    @Override
    public boolean occursOnDate(LocalDate date) {
        return !date.isAfter(this.deadline) && !isDone();
    }
}
