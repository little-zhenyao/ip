package windy.command;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import windy.exception.InvalidInputFormatException;
import windy.task.Deadline;
import windy.task.Event;
import windy.task.Task;
import windy.task.TaskDateParser;
import windy.task.Todo;

/**
 * Parses user input and validates command arguments for the Windy application.
 */
public class Parser {

    private static final String INVALID_COMMAND_MESSAGE =
            "Invalid command, please try another one";
    private Parser() {
    }

    /**
     * Parses a complete input line into an executable command.
     *
     * @param input the complete command entered by the user.
     * @param taskCount the current number of tasks.
     * @return the parsed command.
     * @throws InvalidInputFormatException if the command or its arguments are invalid.
     */
    public static Command parseCommand(String input, int taskCount) throws InvalidInputFormatException {
        String[] commandParts = splitCommand(input);
        CommandType commandType = parseCommandType(commandParts[0]);
        validateCommandArgumentCount(commandType, commandParts.length);

        return createCommand(input, commandParts, commandType, taskCount);
    }

    private static Command createCommand(String input, String[] commandParts,
            CommandType commandType, int taskCount) throws InvalidInputFormatException {
        return switch (commandType) {
            case BYE -> new ByeCommand();
            case LIST -> new ListCommand();
            case MARK -> new MarkCommand(parseTaskNumber(commandParts[1], taskCount));
            case UNMARK -> new UnmarkCommand(parseTaskNumber(commandParts[1], taskCount));
            case DELETE -> new DeleteCommand(parseTaskNumber(commandParts[1], taskCount));
            case TODO, DEADLINE, EVENT -> new AddTaskCommand(parseNewTask(input, commandType));
            case FIND -> new FindCommand(commandParts[1]);
            case DATE -> new DateCommand(parseDate(commandParts[1]));
            case UNKNOWN -> throw new InvalidInputFormatException(INVALID_COMMAND_MESSAGE);
        };
    }

    /**
     * Creates a task from the details supplied with a task-creation command.
     *
     * @param input the complete command entered by the user.
     * @param commandType the type of task to create.
     * @return the task described by the command.
     * @throws InvalidInputFormatException if required task details are missing or malformed.
     */
    public static Task parseNewTask(String input, CommandType commandType) throws InvalidInputFormatException {
        assert commandType == CommandType.TODO
                || commandType == CommandType.DEADLINE
                || commandType == CommandType.EVENT
                : "Command type should represent a task-creation command";

        String taskDetails = extractTaskDetails(input);

        return switch (commandType) {
            case TODO -> new Todo(taskDetails, false);
            case DEADLINE -> parseDeadline(taskDetails);
            case EVENT -> parseEvent(taskDetails);
            default -> throw new InvalidInputFormatException(INVALID_COMMAND_MESSAGE);
        };
    }

    private static String extractTaskDetails(String input) throws InvalidInputFormatException {
        String[] commandParts = input.split("\\s+", 2);
        if (commandParts.length < 2 || commandParts[1].isBlank()) {
            throw new InvalidInputFormatException("The description of task cannot be empty");
        }
        return commandParts[1].trim();
    }

    private static Deadline parseDeadline(String taskDetails) throws InvalidInputFormatException {
        String[] deadlineParts = taskDetails.split("\\s+/by\\s+", 2);
        if (deadlineParts.length != 2) {
            throw new InvalidInputFormatException(TaskDateParser.DEADLINE_FORMAT_ERROR_MESSAGE);
        }
        return new Deadline(deadlineParts[0], false, deadlineParts[1]);
    }

    private static Event parseEvent(String taskDetails) throws InvalidInputFormatException {
        String[] eventParts = taskDetails.split("\\s+/from\\s+", 2);
        if (eventParts.length != 2) {
            throw new InvalidInputFormatException(TaskDateParser.EVENT_FORMAT_ERROR_MESSAGE);
        }

        String[] dateParts = eventParts[1].split("\\s+/to\\s+", 2);
        if (dateParts.length != 2) {
            throw new InvalidInputFormatException(TaskDateParser.EVENT_FORMAT_ERROR_MESSAGE);
        }

        String taskDescription = eventParts[0].trim();
        String startDate = dateParts[0].trim();
        String endDate = dateParts[1].trim();
        return new Event(taskDescription, false, startDate, endDate);
    }

    /**
     * Converts a one-based task number from user input to a valid list index.
     *
     * @param taskNumber the task number entered by the user.
     * @param taskCount the current number of tasks.
     * @return the corresponding zero-based task index.
     * @throws InvalidInputFormatException if the number is not an integer or is outside the task list.
     */
    public static int parseTaskNumber(String taskNumber, int taskCount) throws InvalidInputFormatException {
        int taskIndex;
        try {
            taskIndex = Integer.parseInt(taskNumber) - 1;
        } catch (NumberFormatException exception) {
            throw new InvalidInputFormatException("The number must be a positive integer");
        }
        if (taskIndex < 0 || taskIndex >= taskCount) {
            if (taskCount == 0) {
                throw new InvalidInputFormatException("There are no tasks in the list.");
            } else {
                throw new InvalidInputFormatException("Invalid number of task, "
                        + "please try the number between 1 and " + taskCount + ".");
            }
        }
        assert taskIndex >= 0 && taskIndex < taskCount
                : "Parsed task index should be within the task list";
        return taskIndex;
    }

    /**
     * Parses a date in the {@code yyyy-M-d} format.
     *
     * @param date the date text to parse.
     * @return the parsed date.
     * @throws InvalidInputFormatException if the date is invalid or uses an unsupported format.
     */
    public static LocalDate parseDate(String date) throws InvalidInputFormatException {
        try {
            return TaskDateParser.parse(date);
        } catch (DateTimeParseException exception) {
            throw new InvalidInputFormatException(TaskDateParser.SEARCH_DATE_FORMAT_ERROR_MESSAGE);
        }
    }

    /**
     * Splits a command into whitespace-separated words.
     *
     * @param input the command entered by the user.
     * @return the words in the command.
     */
    public static String[] splitCommand(String input) {
        return input.split("\\s+");
    }

    /**
     * Identifies the command represented by a command word.
     *
     * @param command the first word of the user's input.
     * @return the matching command type, or {@link CommandType#UNKNOWN} if none matches.
     */
    public static CommandType parseCommandType(String command) {
        return CommandType.from(command);
    }

    /**
     * Validates that a command contains the expected number of words.
     *
     * @param commandType the command being validated.
     * @param commandLength the number of words in the command.
     * @throws InvalidInputFormatException if the command has an unsupported type or argument count.
     */
    public static void validateCommandArgumentCount(CommandType commandType, int commandLength)
            throws InvalidInputFormatException {
        switch (commandType) {
            case BYE, LIST -> requireArgumentCount(commandLength, 1, INVALID_COMMAND_MESSAGE);
            case MARK, UNMARK, DELETE -> requireArgumentCount(commandLength, 2,
                    "Invalid format. Please use: "
                            + commandType.name().toLowerCase() + " TASK_NUMBER");
            case FIND -> requireArgumentCount(commandLength, 2,
                    "Invalid format. Please use: find keyword");
            case DATE -> requireArgumentCount(commandLength, 2,
                    "Invalid format. Please use: date yyyy-M-d");
            case UNKNOWN -> throw new InvalidInputFormatException(INVALID_COMMAND_MESSAGE);
            default -> {
            }
        }
    }

    private static void requireArgumentCount(int actualCount, int expectedCount, String errorMessage)
            throws InvalidInputFormatException {
        if (actualCount != expectedCount) {
            throw new InvalidInputFormatException(errorMessage);
        }
    }
}
