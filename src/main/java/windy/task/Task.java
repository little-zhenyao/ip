package windy.task;

import java.time.LocalDate;

/**
 * Represents a task with a name and a completion status.
 */
public abstract class Task {
    private final String name;
    private boolean isDone;

    /**
     * Creates a task with the given description and completion status.
     *
     * @param name the task description
     * @param done whether the task has been completed
     */
    public Task(String name, boolean done) {
        this.name = name;
        this.isDone = done;
    }

    /**
     * Returns the symbol used to display the task's completion status.
     *
     * @return {@code X} if the task is done, or a space otherwise
     */
    public String getStatus() {
        return isDone ? "X" : " ";
    }

    /**
     * Checks whether the task has been completed.
     *
     * @return {@code true} if the task is done
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Returns the task description.
     *
     * @return the task description
     */
    public String getName() {
        return name;
    }

    /**
     * Changes the task's completion status.
     *
     * @param done the new completion status
     */
    public void setDone(boolean done) {
        isDone = done;
    }

    /**
     * Converts the task to the format used in the data file.
     *
     * @return the serialized task
     */
    public abstract String toDataString();

    /**
     * Checks whether this incomplete task is relevant on the specified date.
     *
     * @param date the date to check
     * @return {@code true} if the task occurs on the date and is not completed
     */
    public abstract boolean isOccur(LocalDate date);
}
