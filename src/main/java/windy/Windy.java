package windy;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import windy.command.CommandType;
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
     * Saves the current task list and reports any storage error to the user.
     */
    private void saveTasks() {
        try {
            storage.saveTasks(tasks.getTasks());
        } catch (IOException exception) {
            ui.showError("     Unable to save tasks: " + exception.getMessage());
        }
    }

    /**
     * Reads, validates, and executes commands until the user exits or input ends.
     */
    private void runCommandLoop() {
        commandLoop:
        while (ui.hasNextCommand()) {
            String input = ui.readCommand();
            String[] commandParts = Parser.splitCommand(input);
            CommandType commandType = Parser.parseCommandType(commandParts[0]);

            ui.showLine();
            try {
                Parser.parseInvalidCommand(commandType, commandParts.length);
                switch (commandType) {
                    case BYE -> {
                        break commandLoop;
                    }
                    case LIST -> {
                        this.listTasks();
                    }
                    case MARK, UNMARK -> {
                        this.markTask(commandParts[1], commandType == CommandType.MARK);
                    }
                    case DELETE -> {
                        this.deleteTask(commandParts[1]);
                    }
                    case TODO, DEADLINE, EVENT -> {
                        this.addTask(input, commandType);
                    }
                    case FIND -> {
                        this.findTasksContainingKeyword(commandParts[1]);
                    }
                    case DATE -> {
                        this.findTasksOnDate(commandParts[1]);
                    }
                    case UNKNOWN -> {}
                }
            } catch (InvalidInputFormatException exception) {
                ui.showError(exception.getMessage());
            }

            ui.showLine();
        }
    }

    /**
     * Displays tasks whose descriptions contain the specified keyword.
     *
     * @param keyword the keyword to search for.
     */
    private void findTasksContainingKeyword(String keyword) {
        List<Task> foundTasks = tasks.findTasksContainingKeyword(keyword);
        ui.showFoundTasks(foundTasks);
    }

    /**
     * Displays incomplete tasks that occur on the specified date.
     *
     * @param date the date to search for, in {@code yyyy-M-d} format.
     * @throws InvalidInputFormatException if the date is invalid.
     */
    private void findTasksOnDate(String date) throws InvalidInputFormatException {
        LocalDate localDate = Parser.parseDate(date);
        List<Task> foundTasks = tasks.findTasksByDate(localDate);
        ui.showFoundTasks(foundTasks);
    }

    /**
     * Deletes the selected task and saves the updated task list.
     *
     * @param taskNumber the one-based number of the task to delete.
     * @throws InvalidInputFormatException if the task number is invalid.
     */
    private void deleteTask(String taskNumber) throws InvalidInputFormatException {
        int taskIndex = Parser.parseTaskNumber(taskNumber, tasks.getNumTasks());
        Task deletedTask = tasks.deleteTask(taskIndex);
        ui.showDeleteTask(deletedTask, tasks.getNumTasks());
        saveTasks();
    }

    /**
     * Updates a task's completion status and saves the task list.
     *
     * @param taskNumber the one-based number of the task to update.
     * @param isMarked {@code true} to mark the task done; {@code false} to mark it not done.
     * @throws InvalidInputFormatException if the task number is invalid.
     */
    private void markTask(String taskNumber, boolean isMarked) throws InvalidInputFormatException {
        int taskIndex = Parser.parseTaskNumber(taskNumber, tasks.getNumTasks());
        tasks.markTask(taskIndex, isMarked);
        saveTasks();
        ui.showMarkTask(isMarked, tasks.getTask(taskIndex));
    }

    /**
     * Parses and adds a new task, then saves the task list.
     *
     * @param input the complete task-creation command.
     * @param commandType the type of task to create.
     * @throws InvalidInputFormatException if the task details are invalid.
     */
    private void addTask(String input, CommandType commandType) throws InvalidInputFormatException {
        Task newTask = Parser.parseNewTask(input, commandType);
        tasks.addTask(newTask);
        saveTasks();
        ui.showAddTask(newTask, tasks.getNumTasks());
    }

    /**
     * Displays all tasks currently in the task list.
     */
    private void listTasks() {
        ui.showTaskList(tasks.getTasks());
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
