package windy.command;

import windy.storage.Storage;
import windy.task.Task;
import windy.task.TaskList;
import windy.ui.Ui;

/**
 * Adds a task to the task list.
 */
public class AddTaskCommand extends Command {
    private final Task task;

    /**
     * Creates a command that adds the specified task.
     *
     * @param task the task to add.
     */
    public AddTaskCommand(Task task) {
        this.task = task;
    }

    /** {@inheritDoc} */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        tasks.addTask(task);
        saveTasks(tasks, ui, storage);
        ui.showAddTask(task, tasks.getNumTasks());
    }
}
