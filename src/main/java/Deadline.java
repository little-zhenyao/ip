/**
 * Represents a task that must be completed by a specific date or time.
 */
public class Deadline extends Task{

    private final String deadline;

    public Deadline(String name, boolean done, String deadline) {
        super(name, done);
        this.deadline = deadline;
    }

    public String getDeadline() {
        return deadline;
    }

    @Override
    public String toString() {
        return "[D][" + this.getStatus() + "] " + this.getName()
                + " (by: " + this.deadline + ")";
    }

    @Override
    public String toDataString() {
        return "D | " + (this.isDone() ? "1" : "0") + " | " + this.getName() + " | " + this.deadline;
    }
}
