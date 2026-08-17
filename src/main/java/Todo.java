/**
 * Represents a task without an associated date or time.
 */
public class Todo extends Task {

    public Todo(String name, boolean done) {
        super(name, done);
    }

    @Override
    public String toString() {
        return "[T][" + this.getStatus() + "] " + this.getName();
    }
}
