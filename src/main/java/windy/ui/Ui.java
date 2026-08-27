package windy.ui;

import java.util.List;
import java.util.Scanner;

import windy.task.Task;

/**
 * Handles all console input and output for the Windy application.
 */
public class Ui {
    private static final String NAME = "Windy";
    private final Scanner scanner;

    /**
     * Creates a user interface that reads commands from standard input.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Checks whether another command is available from standard input.
     *
     * @return {@code true} if another line can be read
     */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /**
     * Reads and trims the next command from standard input.
     *
     * @return the next user command
     */
    public String readCommand() {
        return scanner.nextLine().trim();
    }

    /**
     * Displays the application banner and greeting.
     */
    public void showWelcome() {
        showLine();
        showBanner();
        System.out.println("     Hello! I'm " + NAME + ".");
        System.out.println("     What can I do for you?");
        showLine();
    }

    /**
     * Displays the farewell message.
     */
    public void showBye() {
        System.out.println("     Bye. Hope to see you again soon!");
        showLine();
    }

    /**
     * Displays an error message.
     *
     * @param message the message to display
     */
    public void showError(String message) {
        System.out.println(message);
    }

    /**
     * Displays every task with its one-based task number.
     *
     * @param tasks the tasks to display
     */
    public void showTaskList(List<Task> tasks) {
        System.out.println("     Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println("     " + (i + 1) + "." + tasks.get(i));
        }
    }

    /**
     * Displays the deleted task and the remaining task count.
     *
     * @param task the task that was removed
     * @param taskCount the number of tasks remaining
     */
    public void showDeleteTask(Task task, int taskCount) {
        System.out.println("     Noted. I've removed this task:");
        System.out.println("       " + task);
        System.out.println("     Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Displays confirmation of a task's updated completion status.
     *
     * @param isMarked {@code true} if the task was marked done; {@code false} otherwise
     * @param task the updated task
     */
    public void showMarkTask(boolean isMarked, Task task) {
        if (isMarked) {
            System.out.println("     Nice! I've marked this task as done:");
        } else {
            System.out.println("     OK, I've marked this task as not done yet:");
        }
        System.out.println("       " + task);
    }

    /**
     * Displays the added task and the updated task count.
     *
     * @param task the task that was added
     * @param taskCount the number of tasks after the addition
     */
    public void showAddTask(Task task, int taskCount) {
        System.out.println("     Got it. I've added this task:");
        System.out.println("       " + task);
        System.out.println("     Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Displays tasks found by a date search, or a message when none are found.
     *
     * @param tasks the matching tasks
     */
    public void showFoundTasks(List<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("     No such task found");
            return;
        }
        System.out.println("     There are " + tasks.size() + " tasks that meet the requirements:");
        for (Task task : tasks) {
            System.out.println("     " + task);
        }
    }

    /**
     * Displays the horizontal separator used between command responses.
     */
    public void showLine() {
        System.out.println("    ____________________________________________________________");
    }

    private void showBanner() {
        String banner = "     __        ___           _       \n"
                + "     \\ \\      / (_)_ __   __| |_   _ \n"
                + "      \\ \\ /\\ / /| | '_ \\ / _` | | | |\n"
                + "       \\ V  V / | | | | | (_| | |_| |\n"
                + "        \\_/\\_/  |_|_| |_|\\__,_|\\__, |\n"
                + "                               |___/ \n";
        System.out.print(banner);
    }
}
