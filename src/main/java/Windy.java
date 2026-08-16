import java.util.Scanner;

/**
 * The main class for the Windy chatbot application.
 */
public class Windy {

    private static final String NAME = "Windy";

    private static void greet() {
        printSeparator();
        printWindyBanner();
        System.out.println("Hello! I'm " + NAME + ".");
        System.out.println("What can I do for you?");
        printSeparator();
    }

    private static void sayBye() {
        System.out.println("Bye. Hope to see you again soon!");
        printSeparator();
    }

    /**
     * Repeat the words that user inputs until user inputs the word "bye"
     */
    private static void echo() {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        while (!input.equals("bye")) {
            printSeparator();
            System.out.println(input);
            printSeparator();
            input = scanner.nextLine();
        }
        printSeparator();
    }

    private static void printWindyBanner() {
        String banner = "__        ___           _       \n"
                + "\\ \\      / (_)_ __   __| |_   _ \n"
                + " \\ \\ /\\ / /| | '_ \\ / _` | | | |\n"
                + "  \\ V  V / | | | | | (_| | |_| |\n"
                + "   \\_/\\_/  |_|_| |_|\\__,_|\\__, |\n"
                + "                          |___/ \n";
        System.out.print(banner);
    }
    private static void printSeparator() {
        System.out.println("____________________________________________________________");
    }

    public static void main(String[] args) {
        greet();
        echo();
        sayBye();
    }
}
