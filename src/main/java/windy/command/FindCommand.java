package windy.command;

import java.util.List;

import windy.storage.Storage;
import windy.task.Task;
import windy.task.TaskList;
import windy.ui.Ui;

/**
 * Displays tasks whose descriptions contain a keyword.
 */
public class FindCommand extends Command {
    private final String keyword;

    /**
     * Creates a command that searches task descriptions for a keyword.
     *
     * @param keyword the keyword to search for.
     */
    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    /** {@inheritDoc} */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        List<Task> foundTasks = tasks.findTasksContainingKeyword(keyword);
        ui.showFoundTasks(foundTasks);
    }
}
