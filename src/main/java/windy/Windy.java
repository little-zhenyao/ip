package windy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import windy.command.Command;
import windy.command.Parser;
import windy.exception.InvalidInputFormatException;
import windy.storage.Storage;
import windy.task.Task;
import windy.task.TaskList;
import windy.ui.Ui;

/**
 * The main class for the Windy chatbot application.
 */
public class Windy {

    private final TaskList tasks;
    private final Storage storage;
    private final Ui ui;

    /**
     * Creates the application and restores tasks from the default data file.
     */
    private Windy() {
        List<Task> loadedTasks;
        ui = new Ui();
        storage = new Storage("data/windy.txt");
        try {
            loadedTasks = storage.loadTasks();
        } catch (IOException exception) {
            ui.showError("     Unable to load saved tasks: " + exception.getMessage());
            loadedTasks = new ArrayList<>();
        }
        tasks = new TaskList(loadedTasks);
    }

    /**
     * Reads, validates, and executes commands until the user exits or input ends.
     */
    private void runCommandLoop() {
        commandLoop:
        while (ui.hasNextCommand()) {
            String input = ui.readCommand();

            ui.showLine();
            try {
                Command command = Parser.parseCommand(input, tasks.getNumTasks());
                if (command.isExit()) {
                    break commandLoop;
                }
                command.execute(tasks, ui, storage);
            } catch (InvalidInputFormatException exception) {
                ui.showError(exception.getMessage());
            }

            ui.showLine();
        }
    }

    /**
     * Starts the Windy command-line application.
     *
     * @param args command-line arguments; currently unused.
     */
    public static void main(String[] args) {
        Windy windy = new Windy();
        windy.ui.showWelcome();
        windy.runCommandLoop();
        windy.ui.showBye();
    }
}
