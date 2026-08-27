package windy.task;

import java.time.LocalDate;

/**
 * Represents a task with a name and a completion status.
 */
public abstract class Task {
    private final String name;
    private boolean isDone;

    public Task(String name, boolean isDone) {
        this.name = name;
        this.isDone = isDone;
    }

    public String getStatus() {
        return isDone ? "X" : " ";
    }

    public boolean isDone() {
        return isDone;
    }

    public String getName() {
        return name;
    }

    public void setDone(boolean isDone) {
        this.isDone = isDone;
    }

    /**
     * Method to convert data to the format to storage
     * @return the data of tasks that should be saved in the file
     */
    public abstract String toDataString();

    public abstract boolean occursOnDate(LocalDate date);
}
