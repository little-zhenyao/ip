package windy;

import windy.command.CommandType;
import windy.command.Parser;
import windy.exception.InvalidInputFormatException;
import windy.storage.Storage;
import windy.task.Task;
import windy.task.TaskList;
import windy.ui.Ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

/**
 * The main class for the Windy chatbot application.
 */
public class Windy {

    private static final String NAME = "Windy";
    private final TaskList tasks;
    private final Storage storage;
    private final Ui ui;

    /**
     * Creates the application and restores tasks from the default data file.
     */
    private Windy() {
        List<Task> tasks1;
        ui = new Ui();
        storage = new Storage("data/windy.txt");
        try {
            tasks1 = storage.loadTasks();
        } catch (IOException e) {
            ui.showError("     Unable to load saved tasks: " + e.getMessage());
            tasks1 = new ArrayList<>();
        }
        tasks = new TaskList(tasks1);
    }

    /**
     * Saves the current task list and reports any storage error to the user.
     */
    private void saveTasks() {
        try {
            storage.saveTasks(tasks.getTasks());
        } catch (IOException e) {
            ui.showError("     Unable to save tasks: " + e.getMessage());
        }
    }

    /**
     * Reads, validates, and executes commands until the user exits or input ends.
     */
    private void runCommandLoop() {
        commandLoop:
        while (ui.hasNextCommand()) {
            String input = ui.readCommand();
            String[] command = Parser.splitCommand(input);
            CommandType commandType = Parser.parseCommandType(command[0]);

            ui.showLine();
            try {
                Parser.parseInvalidCommand(commandType, command.length);
                switch (commandType) {
                    case BYE -> {
                        break commandLoop;
                    }
                    case LIST -> {
                        this.listTasks();
                    }
                    case MARK, UNMARK -> {
                        this.markTask(command[1], commandType == CommandType.MARK);
                    }
                    case DELETE -> {
                        this.deleteTask(command[1]);
                    }
                    case TODO, DEADLINE, EVENT -> {
                        this.addTask(input, commandType);
                    }
                    case FIND -> {
                        this.findTaskOnDate(command[1]);
                    }
                    case UNKNOWN -> {}
                }
            } catch (InvalidInputFormatException e) {
                ui.showError(e.getMessage());
            }

            ui.showLine();
        }
    }

    /**
     * Displays incomplete tasks that occur on the specified date.
     *
     * @param date the date to search for, in {@code yyyy-M-d} format
     * @throws InvalidInputFormatException if the date is invalid
     */
    private void findTaskOnDate(String date) throws InvalidInputFormatException {
        LocalDate localDate = Parser.parseDate(date);
        List<Task> tasksFound = tasks.findTaskByDate(localDate);
        ui.showFindTask(tasksFound);
    }

    /**
     * Deletes the selected task and saves the updated task list.
     *
     * @param taskNumber the one-based number of the task to delete
     * @throws InvalidInputFormatException if the task number is invalid
     */
    private void deleteTask(String taskNumber) throws InvalidInputFormatException {
        int num = Parser.parseTaskNumber(taskNumber, tasks.getNumTasks());
        Task deletedTask = tasks.deleteTask(num);
        ui.showDeleteTask(deletedTask, tasks.getNumTasks());
        saveTasks();
    }

    /**
     * Updates a task's completion status and saves the task list.
     *
     * @param taskNumber the one-based number of the task to update
     * @param mark {@code true} to mark the task done; {@code false} to mark it not done
     * @throws InvalidInputFormatException if the task number is invalid
     */
    private void markTask(String taskNumber, boolean mark) throws InvalidInputFormatException {
        int num = Parser.parseTaskNumber(taskNumber, tasks.getNumTasks());
        tasks.markTask(num, mark);
        saveTasks();
        ui.showMarkTask(mark, tasks.getTask(num));
    }

    /**
     * Parses and adds a new task, then saves the task list.
     *
     * @param input the complete task-creation command
     * @param commandType the type of task to create
     * @throws InvalidInputFormatException if the task details are invalid
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
     * @param args command-line arguments; currently unused
     */
    public static void main(String[] args) {
        Windy windy = new Windy();
        windy.ui.showWelcome();
        windy.runCommandLoop();
        windy.ui.showBye();
    }
}
