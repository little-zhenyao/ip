package windy.command;

import windy.storage.Storage;
import windy.task.TaskList;
import windy.ui.Ui;

/**
 * Displays every task in the task list.
 */
public class ListCommand extends Command {

    /** {@inheritDoc} */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showTaskList(tasks.getTasks());
    }
}
