package windy.task;

import java.time.LocalDate;
import java.util.Locale;

/**
 * Represents a task with a name and a completion status.
 */
public abstract class Task {
    private final String name;
    private boolean isDone;

    /**
     * Creates a task with the given description and completion status.
     *
     * @param name the task description.
     * @param isDone whether the task has been completed.
     */
    public Task(String name, boolean isDone) {
        this.name = name;
        this.isDone = isDone;
    }

    /**
     * Returns the symbol used to display the task's completion status.
     *
     * @return {@code X} if the task is done, or a space otherwise.
     */
    public String getStatus() {
        return isDone ? "X" : " ";
    }

    /**
     * Checks whether the task has been completed.
     *
     * @return {@code true} if the task is done.
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Returns the task description.
     *
     * @return the task description.
     */
    public String getName() {
        return name;
    }

    /**
     * Changes the task's completion status.
     *
     * @param isDone the new completion status.
     */
    public void setDone(boolean isDone) {
        this.isDone = isDone;
    }

    /**
     * Returns whether this task's description contains the specified keyword.
     *
     * @param keyword the keyword to search for.
     * @return {@code true} if the task description contains the keyword.
     */
    public boolean containsKeyword(String keyword) {
        String normalizedName = name.toLowerCase(Locale.ROOT);
        String normalizedKeyword = keyword.toLowerCase(Locale.ROOT);
        return normalizedName.contains(normalizedKeyword);
    }

    /**
     * Converts the task to the format used in the data file.
     *
     * @return the serialized task.
     */
    public abstract String toDataString();

    /**
     * Checks whether this incomplete task is relevant on the specified date.
     *
     * @param date the date to check.
     * @return {@code true} if the task occurs on the date and is not completed.
     */
    public abstract boolean occursOnDate(LocalDate date);
}
