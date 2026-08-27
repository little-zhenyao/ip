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

    private void saveTasks() {
        try {
            storage.saveTasks(tasks.getTasks());
        } catch (IOException e) {
            ui.showError("     Unable to save tasks: " + e.getMessage());
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
                        this.findTasksContainingKeyword(command[1]);
                    }
                    case DATE -> {
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

    private void findTasksContainingKeyword(String keyword) {
        List<Task> tasksFound = tasks.findTasksContainingKeyword(keyword);
        ui.showFindTask(tasksFound);
    }

    private void findTaskOnDate(String date) throws InvalidInputFormatException {
        LocalDate localDate = Parser.parseDate(date);
        List<Task> tasksFound = tasks.findTaskByDate(localDate);
        ui.showFindTask(tasksFound);
    }

    private void deleteTask(String taskNumber) throws InvalidInputFormatException {
        int num = Parser.parseTaskNumber(taskNumber, tasks.getNumTasks());
        Task deletedTask = tasks.deleteTask(num);
        ui.showDeleteTask(deletedTask, tasks.getNumTasks());
        saveTasks();
    }

    private void markTask(String taskNumber, boolean mark) throws InvalidInputFormatException {
        int num = Parser.parseTaskNumber(taskNumber, tasks.getNumTasks());
        tasks.markTask(num, mark);
        saveTasks();
        ui.showMarkTask(mark, tasks.getTask(num));
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
