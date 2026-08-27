package windy.command;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

import windy.exception.InvalidInputFormatException;
import windy.task.Deadline;
import windy.task.Event;
import windy.task.Task;
import windy.task.Todo;

/**
 * Parses user input and validates command arguments for the Windy application.
 */
public class Parser {

    private Parser() {
    }

    /**
     * Creates a task from the details supplied with a task-creation command.
     *
     * @param input the complete command entered by the user
     * @param commandType the type of task to create
     * @return the task described by the command
     * @throws InvalidInputFormatException if required task details are missing or malformed
     */
    public static Task parseNewTask(String input, CommandType commandType) throws InvalidInputFormatException {
        String[] commandParts = input.split("\\s+");
        if (commandParts.length == 1) {
            throw new InvalidInputFormatException("     The description of task cannot be empty");
        }
        String details = input.substring(commandParts[0].length() + 1).trim();
        switch (commandType) {
            case TODO -> {
                return new Todo(details, false);
            }
            case DEADLINE -> {
                String[] parts = details.split("\\s+/by\\s+", 2);
                if (parts.length != 2) {
                    throw new InvalidInputFormatException(
                            "     The format of deadline is wrong. Please use description /by yyyy-M-d");
                }
                return new Deadline(parts[0], false, parts[1]);
            }
            case EVENT -> {
                String[] fromParts = details.split("\\s+/from\\s+", 2);
                if (fromParts.length != 2) {
                    throw new InvalidInputFormatException(
                            "     The format of event is wrong. Please use description /from yyyy-M-d /to yyyy-M-d");
                }
                String name = fromParts[0].trim();

                String[] timeParts = fromParts[1].split("\\s+/to\\s+", 2);
                if (timeParts.length != 2) {
                    throw new InvalidInputFormatException(
                            "     The format of event is wrong. Please use description /from yyyy-M-d /to yyyy-M-d");
                }
                String from = timeParts[0].trim();
                String to = timeParts[1].trim();
                return new Event(name, false, from, to);
            }
            default -> {
                throw new InvalidInputFormatException("     Invalid command, please try another one");
            }
        }
    }

    /**
     * Converts a one-based task number from user input to a valid list index.
     *
     * @param taskNumber the task number entered by the user
     * @param taskCount the current number of tasks
     * @return the corresponding zero-based task index
     * @throws InvalidInputFormatException if the number is not an integer or is outside the task list
     */
    public static int parseTaskNumber(String taskNumber, int taskCount) throws InvalidInputFormatException {
        int taskIndex;
        try {
            taskIndex = Integer.parseInt(taskNumber) - 1;
        } catch (NumberFormatException exception) {
            throw new InvalidInputFormatException("     The number must be a positive integer");
        }
        if (taskIndex < 0 || taskIndex >= taskCount) {
            if (taskCount == 0) {
                throw new InvalidInputFormatException("     There are no tasks in the list.");
            } else {
                throw new InvalidInputFormatException("     Invalid number of task, "
                        + "please try the number between 1 and " + taskCount + ".");
            }
        }
        return taskIndex;
    }

    /**
     * Parses a date in the {@code yyyy-M-d} format.
     *
     * @param date the date text to parse
     * @return the parsed date
     * @throws InvalidInputFormatException if the date is invalid or uses an unsupported format
     */
    public static LocalDate parseDate(String date) throws InvalidInputFormatException {
        LocalDate localDate;
        try {
            localDate = LocalDate.parse(date,
                    DateTimeFormatter.ofPattern("uuuu-M-d").withResolverStyle(ResolverStyle.STRICT));
        } catch (DateTimeParseException exception) {
            throw new InvalidInputFormatException("     Invalid date format. Please write like date yyyy-M-d");
        }
        return localDate;
    }

    /**
     * Splits a command into whitespace-separated words.
     *
     * @param input the command entered by the user
     * @return the words in the command
     */
    public static String[] splitCommand(String input) {
        return input.split("\\s+");
    }

    /**
     * Identifies the command represented by a command word.
     *
     * @param command the first word of the user's input
     * @return the matching command type, or {@link CommandType#UNKNOWN} if none matches
     */
    public static CommandType parseCommandType(String command) {
        return CommandType.from(command);
    }

    /**
     * Validates that a command contains the expected number of words.
     *
     * @param commandType the command being validated
     * @param commandLength the number of words in the command
     * @throws InvalidInputFormatException if the command has an unsupported type or argument count
     */
    public static void parseInvalidCommand(CommandType commandType, int commandLength)
            throws InvalidInputFormatException {
        switch (commandType) {
            case BYE, LIST -> {
                if (commandLength != 1) {
                    throw new InvalidInputFormatException("     Invalid command, please try another one");
                }
            }
            case MARK, UNMARK, DELETE -> {
                if (commandLength != 2) {
                    throw new InvalidInputFormatException("     Invalid format. Please use: "
                            + commandType.name().toLowerCase() + " TASK_NUMBER");
                }
            }
            case FIND -> {
                if (commandLength != 2) {
                    throw new InvalidInputFormatException("     Invalid format. Please use: find keyword");
                }
            }
            case DATE -> {
                if (commandLength != 2) {
                    throw new InvalidInputFormatException("     Invalid format. Please use: date yyyy-M-d");
                }
            }
            case UNKNOWN -> {
                throw new InvalidInputFormatException("     Invalid command, please try another one");
            }
            default -> {}
        }
    }
}
