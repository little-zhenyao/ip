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
     * @param isDone whether the task has been completed
     */
    public Todo(String name, boolean isDone) {
        super(name, isDone);
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
    public boolean occursOnDate(LocalDate date) {
        return false;
    }
}
