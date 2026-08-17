/**
 * Represents a task with a name and a completion status.
 */
public class Task {
    private final String name;
    private boolean isDone;
    public Task(String name, boolean done) {
        this.name = name;
        this.isDone = done;
    }

    public String getStatus() {
        return isDone ? "X" : " ";
    }

    public String getName() {
        return name;
    }

    public void setDone(boolean done) {
        isDone = done;
    }
}
