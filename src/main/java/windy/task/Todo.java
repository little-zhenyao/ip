package windy.task;

import java.time.LocalDate;

/**
 * Represents a task without an associated date or time.
 */
public class Todo extends Task {

    /**
     * Creates a task without an associated date.
     *
     * @param name the task description
     * @param done whether the task has been completed
     */
    public Todo(String name, boolean done) {
        super(name, done);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "[T][" + this.getStatus() + "] " + this.getName();
    }

    /** {@inheritDoc} */
    @Override
    public String toDataString() {
        return "T | " + (this.isDone() ? "1" : "0") + " | " + this.getName();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isOccur(LocalDate date) {
        return false;
    }
}
