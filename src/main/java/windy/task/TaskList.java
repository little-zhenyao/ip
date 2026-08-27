package windy.task;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the application's collection of tasks.
 */
public class TaskList {
    private final List<Task> tasks;

    /**
     * Creates a task list backed by the supplied list.
     *
     * @param tasks the initial tasks
     */
    public TaskList(List<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param task the task to add
     */
    public void addTask(Task task) {
        tasks.add(task);
    }

    /**
     * Returns an unmodifiable snapshot of the current tasks.
     *
     * @return a copy of the task list
     */
    public List<Task> getTasks() {
        return List.copyOf(tasks);
    }

    /**
     * Removes and returns the task at the specified index.
     *
     * @param index the zero-based index of the task
     * @return the removed task
     */
    public Task deleteTask(int index) {
        return tasks.remove(index);
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return the task count
     */
    public int  getNumTasks() {
        return tasks.size();
    }

    /**
     * Returns the task at the specified index.
     *
     * @param num the zero-based index of the task
     * @return the selected task
     */
    public Task getTask(int num) {
        return tasks.get(num);
    }

    /**
     * Changes the completion status of a selected task.
     *
     * @param num the zero-based index of the task
     * @param mark {@code true} to mark it done; {@code false} to mark it not done
     */
    public void markTask(int num, boolean mark) {
        tasks.get(num).setDone(mark);
    }

    /**
     * Finds incomplete tasks that occur on the specified date.
     *
     * @param localDate the date to search for
     * @return tasks that occur on the date
     */
    public List<Task> findTaskByDate(LocalDate localDate) {
        List<Task> tasksFound = new ArrayList<>();
        for (Task task : this.tasks) {
            if (task.isOccur(localDate)) {
                tasksFound.add(task);
            }
        }
        return tasksFound;
    }
}
