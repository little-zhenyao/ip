import java.util.ArrayList;
import java.util.Scanner;

/**
 * The main class for the Windy chatbot application.
 */
public class Windy {

    private static final String NAME = "Windy";
    private final ArrayList<Task> tasks;

    private Windy() {
        tasks = new ArrayList<>();
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
     * Otherwise, add a task named as user's input to the list
     *      the type of task is defined by user's first input word
     */
    private void runCommandLoop() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine().trim();
            if (input.equals("bye")) {
                break;
            }

            String[] command = input.split("\\s+");
            printSeparator();
            try {
                if (input.equals("list")) {
                    this.listTasks();
                } else if (command[0].equals("mark") || command[0].equals("unmark")) {
                    boolean mark = command[0].equals("mark");
                    if (command.length != 2) {
                        throw new InvalidInputFormatException("     Invalid format. Please use: mark TASK_NUMBER");
                    }
                    this.markTask(command[1], mark);
                } else if (command[0].equals("delete")) {
                    if (command.length != 2) {
                        throw new InvalidInputFormatException("     Invalid format. Please use: delete TASK_NUMBER");
                    }
                    this.deleteTask(command[1]);
                } else {
                    this.addTask(input);
                }
            } catch (InvalidInputFormatException e) {
                System.out.println(e.getMessage());
            }

            printSeparator();
        }
        printSeparator();
    }

    private int getValidTaskIndex(String taskNumber) throws InvalidInputFormatException {
        int num;
        try {
            num = Integer.parseInt(taskNumber) - 1;
        } catch (NumberFormatException e) {
            throw new InvalidInputFormatException("     The number must be a positive integer");
        }
        if (num < 0 || num >= tasks.size()) {
            if (tasks.isEmpty()) {
                throw new InvalidInputFormatException("     There are no tasks in the list.");
            } else {
                throw new InvalidInputFormatException("     Invalid number of task, " +
                        "please try the number between 1 and " + tasks.size());
            }
        }
        return num;
    }

    private void deleteTask(String taskNumber) throws InvalidInputFormatException {
        int num =  getValidTaskIndex(taskNumber);

        System.out.println("     Noted. I've removed this task:");
        System.out.println("       " + tasks.get(num));
        tasks.remove(num);
        System.out.println("     Now you have " + tasks.size() + " tasks in the list.");
    }

    private void markTask(String taskNumber, boolean mark) throws InvalidInputFormatException {
        int num = getValidTaskIndex(taskNumber);
        tasks.get(num).setDone(mark);
        if (mark) {
            System.out.println("     Nice! I've marked this task as done:");
        } else {
            System.out.println("     OK, I've marked this task as not done yet:");
        }
        System.out.println("       " + tasks.get(num));
    }

    private void addTask(String input) throws InvalidInputFormatException {
        String[] command = input.split("\\s+");
        if (command.length == 1 && !command[0].equals("todo") &&
                !command[0].equals("deadline") && !command[0].equals("event")) {
            throw new InvalidInputFormatException("     Invalid command, please try another one");
        }
        if (command.length == 1) {
            throw new InvalidInputFormatException("     The description of task cannot be empty");
        }
        String details = input.substring(command[0].length() + 1).trim();
        switch (command[0]) {
            case "todo" -> {
                this.tasks.add(new Todo(details, false));
            }
            case "deadline" -> {
                String[] parts = details.split("\\s+/by\\s+", 2);
                if (parts.length != 2) {
                    throw new InvalidInputFormatException("     The format of deadline is wrong. Please use description /by date");
                }
                this.tasks.add(new Deadline(parts[0], false, parts[1]));
            }
            case "event" -> {
                String[] fromParts = details.split("\\s+/from\\s+", 2);
                if (fromParts.length != 2) {
                    throw new InvalidInputFormatException("     The format of event is wrong. Please use description /from date1 /to date2");
                }
                String name = fromParts[0].trim();

                String[] timeParts = fromParts[1].split("\\s+/to\\s+", 2);
                if (timeParts.length != 2) {
                    throw new InvalidInputFormatException("The format of event is wrong. Please use description /from date1 /to date2");
                }
                String from = timeParts[0].trim();
                String to = timeParts[1].trim();
                this.tasks.add(new Event(name, false, from, to));
            }
            default -> {
                throw new InvalidInputFormatException("     Invalid command, please try another one");
            }
        }
        System.out.println("     Got it. I've added this task:");
        System.out.println("       " + tasks.get(tasks.size() - 1));
        System.out.println("     Now you have " + tasks.size() + " tasks in the list.");
    }

    private void listTasks() {
        System.out.println("     Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println("     " + (i + 1) + "." + tasks.get(i));
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
