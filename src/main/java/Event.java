/**
 * Represents a task that occurs between a start and an end date or time.
 */
public class Event extends Task{

    private final String start;
    private final String end;

    public Event(String name, boolean done, String start, String end) {
        super(name, done);
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return "[E][" + this.getStatus() + "] " + this.getName()
                + " (from: " + this.start + " to: " + this.end + ")";
    }
}
