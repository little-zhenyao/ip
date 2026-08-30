package windy.command;

import java.io.IOException;

import windy.storage.Storage;
import windy.task.TaskList;
import windy.ui.Ui;

/**
 * Represents a command that Windy can execute.
 */
public abstract class Command {

    /**
     * Executes this command using the application's task list, UI, and storage.
     *
     * @param tasks the current task list.
     * @param ui the application UI.
     * @param storage the task storage.
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage);

    /**
     * Checks whether this command exits the application.
     *
     * @return {@code true} if the command exits the application.
     */
    public boolean isExit() {
        return false;
    }

    /**
     * Saves the current task list and reports any storage error to the user.
     *
     * @param tasks the current task list.
     * @param ui the application UI.
     * @param storage the task storage.
     */
    protected void saveTasks(TaskList tasks, Ui ui, Storage storage) {
        try {
            storage.saveTasks(tasks.getTasks());
        } catch (IOException exception) {
            ui.showError("     Unable to save tasks: " + exception.getMessage());
        }
    }
}
