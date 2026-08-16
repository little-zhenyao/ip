import java.util.Scanner;

/**
 * The main class for the Windy chatbot application.
 */
public class Windy {

    private static final String NAME = "Windy";
    private static final int MAX_TASKS = 100;
    private final String[] tasks;
    private int sizeOfTasks;

    private Windy() {
        tasks = new String[MAX_TASKS];
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
        String input = scanner.nextLine();
        while (!input.equals("bye")) {
            printSeparator();
            if (input.equals("list")) {
                this.listTasks();
            } else {
                this.addTask(input);
            }
            printSeparator();
            input = scanner.nextLine();
        }
        printSeparator();
    }

    private void addTask(String input) {
        System.out.println("     add: " + input);
        this.tasks[sizeOfTasks] = input;
        sizeOfTasks++;
    }

    private void listTasks() {
        for (int i = 0; i < sizeOfTasks; i++) {
            System.out.println("     " + (i + 1) + ". " + tasks[i]);
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
