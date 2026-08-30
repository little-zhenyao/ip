package windy.command;

import windy.storage.Storage;
import windy.task.Task;
import windy.task.TaskList;
import windy.ui.Ui;

/**
 * Deletes a task from the task list.
 */
public class DeleteCommand extends Command {
    private final int taskIndex;

    /**
     * Creates a command that deletes the task at the specified index.
     *
     * @param taskIndex the zero-based index of the task to delete.
     */
    public DeleteCommand(int taskIndex) {
        this.taskIndex = taskIndex;
    }

    /** {@inheritDoc} */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        Task deletedTask = tasks.deleteTask(taskIndex);
        ui.showDeleteTask(deletedTask, tasks.getNumTasks());
        saveTasks(tasks, ui, storage);
    }
}
