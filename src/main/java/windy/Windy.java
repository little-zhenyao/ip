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

    private void saveTasks() {
        try {
            storage.saveTasks(tasks.getTasks());
        } catch (IOException exception) {
            ui.showError("     Unable to save tasks: " + exception.getMessage());
        }
    }

    /**
     * If user's input is "list", list all the task
     * If user's input is "bye", exit
     * Otherwise, add a task named as user's input to the list
     *      the type of task is defined by user's first input word
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

    private void findTasksOnDate(String date) throws InvalidInputFormatException {
        LocalDate localDate = Parser.parseDate(date);
        List<Task> foundTasks = tasks.findTasksByDate(localDate);
        ui.showFoundTasks(foundTasks);
    }

    private void deleteTask(String taskNumber) throws InvalidInputFormatException {
        int taskIndex = Parser.parseTaskNumber(taskNumber, tasks.getNumTasks());
        Task deletedTask = tasks.deleteTask(taskIndex);
        ui.showDeleteTask(deletedTask, tasks.getNumTasks());
        saveTasks();
    }

    private void markTask(String taskNumber, boolean isMarked) throws InvalidInputFormatException {
        int taskIndex = Parser.parseTaskNumber(taskNumber, tasks.getNumTasks());
        tasks.markTask(taskIndex, isMarked);
        saveTasks();
        ui.showMarkTask(isMarked, tasks.getTask(taskIndex));
    }

    private void addTask(String input, CommandType commandType) throws InvalidInputFormatException {
        Task newTask = Parser.parseNewTask(input, commandType);
        tasks.addTask(newTask);
        saveTasks();
        ui.showAddTask(newTask, tasks.getNumTasks());
    }

    private void listTasks() {
        ui.showTaskList(tasks.getTasks());
    }

    public static void main(String[] args) {
        Windy windy = new Windy();
        windy.ui.showWelcome();
        windy.runCommandLoop();
        windy.ui.showBye();
    }
}
