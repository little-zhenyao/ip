package windy.command;

import java.time.LocalDate;
import java.util.List;

import windy.storage.Storage;
import windy.task.Task;
import windy.task.TaskList;
import windy.ui.Ui;

/**
 * Displays incomplete tasks relevant on a specified date.
 */
public class DateCommand extends Command {
    private final LocalDate date;

    /**
     * Creates a command that finds tasks relevant on the specified date.
     *
     * @param date the date to search for.
     */
    public DateCommand(LocalDate date) {
        this.date = date;
    }

    /** {@inheritDoc} */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        List<Task> foundTasks = tasks.findTasksByDate(date);
        ui.showFoundTasks(foundTasks);
    }
}
