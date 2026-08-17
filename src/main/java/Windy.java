import java.util.Scanner;

/**
 * The main class for the Windy chatbot application.
 */
public class Windy {

    private static final String NAME = "Windy";
    private static final int MAX_TASKS = 100;
    private final Task[] tasks;
    private int sizeOfTasks;

    private Windy() {
        tasks = new Task[MAX_TASKS];
        sizeOfTasks = 0;
    }

    private void greet() {
        printSeparator();

        printWindyBanner();
        System.out.println("     Hello! I'm " + NAME + ".");
        System.out.println("     What can I do for you?");
        printSeparator();
    }

    private void sayBye() {
        System.out.println("     Bye. Hope to see you again soon!");
        printSeparator();
    }

    /**
     * If user's input is "list", list all the task
     * If user's input is "bye", exit
     * Otherwise, add a task named as user's input to the array
     */
    private void runCommandLoop() {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine().trim();
        while (!input.equals("bye")) {
            String[] command = input.split("\\s+");
            printSeparator();
            if (input.equals("list")) {
                this.listTasks();
            } else if (command.length == 2 && command[0].equals("mark") || command[0].equals("unmark")) {
                boolean mark = command[0].equals("mark");
                this.markTask(command[1], mark);
            } else {
                this.addTask(input);
            }
            printSeparator();
            input = scanner.nextLine().trim();
        }
        printSeparator();
    }

    private void markTask(String taskNumber, boolean mark) {
        int num = Integer.parseInt(taskNumber) - 1;
        if (num < 0 || num >= sizeOfTasks) {
            System.out.println("Invalid task number");
        }
        tasks[num].setDone(mark);
        if (mark) {
            System.out.println("     Nice! I've marked this task as done:");
        } else {
            System.out.println("     OK, I've marked this task as not done yet:");
        }
        System.out.println("       [" + tasks[num].getStatus() + "] " + tasks[num].getName());
    }

    private void addTask(String input) {
        System.out.println("     add: " + input);
        this.tasks[sizeOfTasks] = new Task(input, false);
        sizeOfTasks++;
    }

    private void listTasks() {
        System.out.println("     Here are the tasks in your list:");
        for (int i = 0; i < sizeOfTasks; i++) {
            System.out.println("     " + (i + 1) + ".[" + tasks[i].getStatus() + "] " + tasks[i].getName());
        }
    }

    private void printWindyBanner() {
        String banner = "     __        ___           _       \n"
                + "     \\ \\      / (_)_ __   __| |_   _ \n"
                + "      \\ \\ /\\ / /| | '_ \\ / _` | | | |\n"
                + "       \\ V  V / | | | | | (_| | |_| |\n"
                + "        \\_/\\_/  |_|_| |_|\\__,_|\\__, |\n"
                + "                               |___/ \n";
        System.out.print(banner);
    }
    private void printSeparator() {
        System.out.println("    ____________________________________________________________");
    }

    public static void main(String[] args) {
        Windy windy = new Windy();
        windy.greet();
        windy.runCommandLoop();
        windy.sayBye();
    }
}
