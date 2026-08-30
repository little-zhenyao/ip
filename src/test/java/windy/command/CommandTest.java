package windy.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import windy.storage.Storage;
import windy.task.Task;
import windy.task.TaskList;
import windy.task.Todo;
import windy.ui.Ui;

public class CommandTest {
    @TempDir
    Path tempDirectory;

    @Test
    public void execute_addTaskCommand_addsAndSavesTask()
            throws Exception {
        TaskList tasks = new TaskList(new ArrayList<>());
        Storage storage = createStorage();

        new AddTaskCommand(new Todo("read book", false))
                .execute(tasks, new Ui(), storage);

        assertEquals(1, tasks.getNumTasks());
        assertEquals(List.of("T | 0 | read book"),
                storage.loadTasks().stream().map(Task::toDataString).toList());
    }

    @Test
    public void execute_markUnmarkAndDeleteCommands_updateTaskList() {
        Todo todo = new Todo("read book", false);
        TaskList tasks = new TaskList(new ArrayList<>(List.of(todo)));
        Storage storage = createStorage();
        Ui ui = new Ui();

        new MarkCommand(0).execute(tasks, ui, storage);
        assertTrue(todo.isDone());

        new UnmarkCommand(0).execute(tasks, ui, storage);
        assertFalse(todo.isDone());

        new DeleteCommand(0).execute(tasks, ui, storage);
        assertEquals(0, tasks.getNumTasks());
    }

    @Test
    public void isExit_byeCommand_returnsTrue() {
        assertTrue(new ByeCommand().isExit());
        assertFalse(new ListCommand().isExit());
    }

    private Storage createStorage() {
        return new Storage(tempDirectory.resolve("data/windy.txt").toString());
    }
}
