package windy.command;

import windy.storage.Storage;
import windy.task.TaskList;
import windy.ui.Ui;

/**
 * Marks a task as completed.
 */
public class MarkCommand extends Command {
    private final int taskIndex;

    /**
     * Creates a command that marks the task at the specified index.
     *
     * @param taskIndex the zero-based index of the task to mark.
     */
    public MarkCommand(int taskIndex) {
        this.taskIndex = taskIndex;
    }

    /** {@inheritDoc} */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        tasks.markTask(taskIndex, true);
        saveTasks(tasks, ui, storage);
        ui.showMarkTask(true, tasks.getTask(taskIndex));
    }
}
