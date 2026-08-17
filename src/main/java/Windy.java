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
     *      the type of task is defined by user's first input word
     */
    private void runCommandLoop() {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine().trim();
        while (!input.equals("bye")) {
            String[] command = input.split("\\s+");
            printSeparator();
            if (input.equals("list")) {
                this.listTasks();
            } else if (command.length == 2 && (command[0].equals("mark") || command[0].equals("unmark"))) {
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
            return;
        }
        tasks[num].setDone(mark);
        if (mark) {
            System.out.println("     Nice! I've marked this task as done:");
        } else {
            System.out.println("     OK, I've marked this task as not done yet:");
        }
        System.out.println("       " + tasks[num].toString());
    }

    private void addTask(String input) {
        String[] command = input.split("\\s+");
        String details = input.substring(command[0].length() + 1).trim();
        switch (command[0]) {
            case "todo" -> this.tasks[sizeOfTasks] = new Todo(details, false);
            case "deadline" -> {
                String[] parts = details.split("\\s+/by\\s+", 2);
                this.tasks[sizeOfTasks] = new Deadline(parts[0], false, parts[1]);
            }
            case "event" -> {
                String[] fromParts = details.split("\\s+/from\\s+", 2);
                String name = fromParts[0].trim();

                String[] timeParts = fromParts[1].split("\\s+/to\\s+", 2);
                String from = timeParts[0].trim();
                String to = timeParts[1].trim();
                this.tasks[sizeOfTasks] = new Event(name, false, from, to);
            }
            default -> {
                System.out.println("Invalid command");
                return;
            }
        }
        sizeOfTasks++;
        System.out.println("     Got it. I've added this task:");
        System.out.println("       " +  tasks[sizeOfTasks - 1].toString());
        System.out.println("     Now you have " + sizeOfTasks + " tasks in the list.");
    }

    private void listTasks() {
        System.out.println("     Here are the tasks in your list:");
        for (int i = 0; i < sizeOfTasks; i++) {
            System.out.println("     " + (i + 1) + "." + tasks[i].toString());
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
