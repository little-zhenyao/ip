package windy.command;

import windy.storage.Storage;
import windy.task.TaskList;
import windy.ui.Ui;

/**
 * Marks a task as not completed.
 */
public class UnmarkCommand extends Command {
    private final int taskIndex;

    /**
     * Creates a command that unmarks the task at the specified index.
     *
     * @param taskIndex the zero-based index of the task to unmark.
     */
    public UnmarkCommand(int taskIndex) {
        this.taskIndex = taskIndex;
    }

    /** {@inheritDoc} */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        tasks.markTask(taskIndex, false);
        saveTasks(tasks, ui, storage);
        ui.showMarkTask(false, tasks.getTask(taskIndex));
    }
}
