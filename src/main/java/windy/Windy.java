package windy;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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

    private static final String DEFAULT_FILE_PATH = "data/windy.txt";

    private final TaskList tasks;
    private final Storage storage;
    private final Ui ui;
    private boolean isExitRequested;

    /**
     * Creates the application and restores tasks from the default data file.
     */
    public Windy() {
        this(DEFAULT_FILE_PATH);
    }

    /**
     * Creates the application and restores tasks from the specified data file.
     *
     * @param filePath path of the task data file.
     */
    Windy(String filePath) {
        List<Task> loadedTasks;
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            loadedTasks = storage.loadTasks();
        } catch (IOException exception) {
            ui.showError("     Unable to load saved tasks: " + exception.getMessage());
            loadedTasks = new ArrayList<>();
        }
        tasks = new TaskList(loadedTasks);
    }

    /**
     * Returns the welcome output used by the console interface.
     *
     * @return Windy's welcome message.
     */
    public String getWelcomeMessage() {
        return captureUiOutput(Ui::showWelcome);
    }

    /**
     * Executes a command and returns the response for display in the GUI.
     *
     * @param input command entered by the user.
     * @return Windy's response to the command.
     */
    public String getResponse(String input) {
        isExitRequested = false;
        ByteArrayOutputStream responseBytes = new ByteArrayOutputStream();
        try (PrintStream responseOutput = new PrintStream(
                responseBytes, true, StandardCharsets.UTF_8)) {
            Ui responseUi = new Ui(responseOutput);
            try {
                Command command = Parser.parseCommand(input.trim(), tasks.getNumTasks());
                if (command.isExit()) {
                    isExitRequested = true;
                    responseUi.showBye();
                } else {
                    command.execute(tasks, responseUi, storage);
                }
            } catch (InvalidInputFormatException exception) {
                responseUi.showError(exception.getMessage());
            }
        }
        return removeConsoleIndentation(responseBytes.toString(StandardCharsets.UTF_8));
    }

    /**
     * Checks whether the latest valid command requested application exit.
     *
     * @return {@code true} if the latest command was {@code bye}.
     */
    public boolean isExitRequested() {
        return isExitRequested;
    }

    /**
     * Captures output produced by one of the console UI's display methods.
     *
     * @param displayAction display method to invoke.
     * @return captured output formatted for a dialog box.
     */
    private String captureUiOutput(Consumer<Ui> displayAction) {
        ByteArrayOutputStream responseBytes = new ByteArrayOutputStream();
        try (PrintStream responseOutput = new PrintStream(
                responseBytes, true, StandardCharsets.UTF_8)) {
            displayAction.accept(new Ui(responseOutput));
        }
        return removeConsoleIndentation(responseBytes.toString(StandardCharsets.UTF_8));
    }

    /**
     * Removes console-only indentation while retaining relative indentation within a response.
     *
     * @param response response formatted for the console.
     * @return response formatted for a dialog box.
     */
    private String removeConsoleIndentation(String response) {
        return response.lines()
                .map(line -> {
                    if (line.startsWith("     ")) {
                        return line.substring(5);
                    }
                    if (line.startsWith("    ")) {
                        return line.substring(4);
                    }
                    return line;
                })
                .collect(Collectors.joining("\n"));
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
