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

    public String getStart() {
        return start;
    }
    public String getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return "[E][" + this.getStatus() + "] " + this.getName()
                + " (from: " + this.start + " to: " + this.end + ")";
    }

    @Override
    public String toDataString() {
        return "E | " + (this.isDone() ? "1" : "0") + " | " + this.getName() + " | " + this.start + " | " + this.end;
    }
}
