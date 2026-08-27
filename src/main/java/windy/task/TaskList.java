package windy.task;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TaskList {
    private final List<Task> tasks;

    public TaskList(List<Task> tasks) {
        this.tasks = tasks;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public List<Task> getTasks() {
        return List.copyOf(tasks);
    }

    public Task deleteTask(int taskIndex) {
        return tasks.remove(taskIndex);
    }

    public int getNumTasks() {
        return tasks.size();
    }

    public Task getTask(int taskIndex) {
        return tasks.get(taskIndex);
    }

    public void markTask(int taskIndex, boolean isMarked) {
        tasks.get(taskIndex).setDone(isMarked);
    }

    public List<Task> findTasksByDate(LocalDate localDate) {
        List<Task> foundTasks = new ArrayList<>();
        for (Task task : this.tasks) {
            if (task.occursOnDate(localDate)) {
                foundTasks.add(task);
            }
        }
        return foundTasks;
    }
}
