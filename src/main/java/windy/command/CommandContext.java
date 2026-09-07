package windy.command;

import java.io.IOException;

import windy.storage.Storage;
import windy.task.TaskList;
import windy.ui.Ui;

/**
 * Provides the application state and services needed to execute a command.
 */
public class CommandContext {
    private final TaskList tasks;
    private final Ui ui;
    private final Storage storage;
    private boolean isExitRequested;

    /**
     * Creates a command context with the current application dependencies.
     *
     * @param tasks the current task list.
     * @param ui the application UI.
     * @param storage the task storage.
     */
    public CommandContext(TaskList tasks, Ui ui, Storage storage) {
        this.tasks = tasks;
        this.ui = ui;
        this.storage = storage;
    }

    public TaskList getTasks() {
        return tasks;
    }

    public Ui getUi() {
        return ui;
    }

    /**
     * Saves the current tasks and reports any storage error to the user.
     */
    public void saveTasks() {
        try {
            storage.saveTasks(tasks.getTasks());
        } catch (IOException exception) {
            ui.showError("Unable to save tasks: " + exception.getMessage());
        }
    }

    /**
     * Records that the application should exit after the current command.
     */
    public void requestExit() {
        isExitRequested = true;
    }

    /**
     * Checks whether the current command requested application exit.
     *
     * @return {@code true} if the application should exit.
     */
    public boolean isExitRequested() {
        return isExitRequested;
    }
}
