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

    public Task deleteTask(int index) {
        return tasks.remove(index);
    }

    public int  getNumTasks() {
        return tasks.size();
    }

    public Task getTask(int num) {
        return tasks.get(num);
    }

    public void markTask(int num, boolean mark) {
        tasks.get(num).setDone(mark);
    }

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
