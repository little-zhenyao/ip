package windy.command;

import windy.storage.Storage;
import windy.task.TaskList;
import windy.ui.Ui;

/**
 * Exits the application.
 */
public class ByeCommand extends Command {

    /**
     * Performs no work because the application loop handles exiting.
     *
     * @param tasks the current task list.
     * @param ui the application UI.
     * @param storage the task storage.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        // The application loop exits before executing this command.
    }

    /** {@inheritDoc} */
    @Override
    public boolean isExit() {
        return true;
    }
}
