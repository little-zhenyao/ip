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
     * @param taskIndex the zero-based index of the task
     * @return the removed task
     */
    public Task deleteTask(int taskIndex) {
        return tasks.remove(taskIndex);
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return the task count
     */
    public int getNumTasks() {
        return tasks.size();
    }

    /**
     * Returns the task at the specified index.
     *
     * @param taskIndex the zero-based index of the task
     * @return the selected task
     */
    public Task getTask(int taskIndex) {
        return tasks.get(taskIndex);
    }

    /**
     * Changes the completion status of a selected task.
     *
     * @param taskIndex the zero-based index of the task
     * @param isMarked {@code true} to mark it done; {@code false} to mark it not done
     */
    public void markTask(int taskIndex, boolean isMarked) {
        tasks.get(taskIndex).setDone(isMarked);
    }

    /**
     * Finds incomplete tasks that occur on the specified date.
     *
     * @param localDate the date to search for
     * @return tasks that occur on the date
     */
    public List<Task> findTasksByDate(LocalDate localDate) {
        List<Task> foundTasks = new ArrayList<>();
        for (Task task : this.tasks) {
            if (task.occursOnDate(localDate)) {
                foundTasks.add(task);
            }
        }
        return foundTasks;
    }

    /**
     * Finds all tasks whose descriptions contain the specified keyword.
     *
     * @param keyword the keyword to search for
     * @return tasks with descriptions that contain the keyword
     */
    public List<Task> findTasksContainingKeyword(String keyword) {
        List<Task> foundTasks = new ArrayList<>();
        for (Task task : this.tasks) {
            if (task.containsKeyword(keyword)) {
                foundTasks.add(task);
            }
        }
        return foundTasks;
    }
}
