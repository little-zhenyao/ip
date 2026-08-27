package windy.ui;

import java.util.List;
import java.util.Scanner;

import windy.task.Task;

public class Ui {
    private static final String NAME = "Windy";
    private final Scanner scanner;

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    public String readCommand() {
        return scanner.nextLine().trim();
    }

    public void showWelcome() {
        showLine();
        showBanner();
        System.out.println("     Hello! I'm " + NAME + ".");
        System.out.println("     What can I do for you?");
        showLine();
    }

    public void showBye() {
        System.out.println("     Bye. Hope to see you again soon!");
        showLine();
    }

    public void showError(String message) {
        System.out.println(message);
    }

    public void showTaskList(List<Task> tasks) {
        System.out.println("     Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println("     " + (i + 1) + "." + tasks.get(i));
        }
    }

    public void showDeleteTask(Task task, int taskCount) {
        System.out.println("     Noted. I've removed this task:");
        System.out.println("       " + task);
        System.out.println("     Now you have " + taskCount + " tasks in the list.");
    }

    public void showMarkTask(boolean isMarked, Task task) {
        if (isMarked) {
            System.out.println("     Nice! I've marked this task as done:");
        } else {
            System.out.println("     OK, I've marked this task as not done yet:");
        }
        System.out.println("       " + task);
    }

    public void showAddTask(Task task, int taskCount) {
        System.out.println("     Got it. I've added this task:");
        System.out.println("       " + task);
        System.out.println("     Now you have " + taskCount + " tasks in the list.");
    }

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
