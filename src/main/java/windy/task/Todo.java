package windy.task;

import java.time.LocalDate;

/**
 * Represents a task without an associated date or time.
 */
public class Todo extends Task {

    public Todo(String name, boolean isDone) {
        super(name, isDone);
    }

    @Override
    public String toString() {
        return "[T][" + this.getStatus() + "] " + this.getName();
    }

    @Override
    public String toDataString() {
        return "T | " + (this.isDone() ? "1" : "0") + " | " + this.getName();
    }

    @Override
    public boolean occursOnDate(LocalDate date) {
        return false;
    }
}
