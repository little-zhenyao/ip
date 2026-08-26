import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;

/**
 * Represents a task that must be completed by a specific date or time.
 */
public class Deadline extends Task{

    private final LocalDate deadline;
    private static final DateTimeFormatter INPUT_FORMATTER
            = DateTimeFormatter.ofPattern("uuuu-M-d").withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter OUTPUT_FORMATTER =
            DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);

    public Deadline(String name, boolean done, String deadline) throws InvalidInputFormatException {
        super(name, done);
        try {
            this.deadline = LocalDate.parse(deadline, INPUT_FORMATTER);
        }  catch (DateTimeParseException e) {
            throw new InvalidInputFormatException
                    ("     The format of deadline is wrong. Please use description /by yyyy-M-d");
        }

    }

    @Override
    public String toString() {
        return "[D][" + this.getStatus() + "] " + this.getName()
                + " (by: " + this.deadline.format(OUTPUT_FORMATTER) + ")";
    }

    @Override
    public String toDataString() {
        return "D | " + (this.isDone() ? "1" : "0") + " | " + this.getName() + " | " + this.deadline;
    }

    @Override
    public boolean isOccur(LocalDate date) {
        return !date.isAfter(this.deadline) && !isDone();
    }
}
