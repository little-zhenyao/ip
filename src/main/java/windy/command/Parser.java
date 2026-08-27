package windy.command;

import windy.exception.InvalidInputFormatException;
import windy.task.Deadline;
import windy.task.Event;
import windy.task.Task;
import windy.task.Todo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class Parser {

    private Parser() {}

    public static Task parseNewTask(String input, CommandType commandType) throws InvalidInputFormatException {
        String[] command = input.split("\\s+");
        if (command.length == 1) {
            throw new InvalidInputFormatException("     The description of task cannot be empty");
        }
        String details = input.substring(command[0].length() + 1).trim();
        switch (commandType) {
            case TODO -> {
                return new Todo(details, false);
            }
            case DEADLINE -> {
                String[] parts = details.split("\\s+/by\\s+", 2);
                if (parts.length != 2) {
                    throw new InvalidInputFormatException
                            ("     The format of deadline is wrong. Please use description /by yyyy-M-d");
                }
                return new Deadline(parts[0], false, parts[1]);
            }
            case EVENT -> {
                String[] fromParts = details.split("\\s+/from\\s+", 2);
                if (fromParts.length != 2) {
                    throw new InvalidInputFormatException
                            ("     The format of event is wrong. Please use description /from yyyy-M-d /to yyyy-M-d");
                }
                String name = fromParts[0].trim();

                String[] timeParts = fromParts[1].split("\\s+/to\\s+", 2);
                if (timeParts.length != 2) {
                    throw new InvalidInputFormatException
                            ("     The format of event is wrong. Please use description /from yyyy-M-d /to yyyy-M-d");
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

    public static int parseTaskNumber(String taskNumber, int taskSize) throws InvalidInputFormatException {
        int num;
        try {
            num = Integer.parseInt(taskNumber) - 1;
        } catch (NumberFormatException e) {
            throw new InvalidInputFormatException("     The number must be a positive integer");
        }
        if (num < 0 || num >= taskSize) {
            if (taskSize == 0) {
                throw new InvalidInputFormatException("     There are no tasks in the list.");
            } else {
                throw new InvalidInputFormatException("     Invalid number of task, " +
                        "please try the number between 1 and " + taskSize + ".");
            }
        }
        return num;
    }

    public static LocalDate parseDate(String date) throws InvalidInputFormatException {
        LocalDate localDate;
        try {
            localDate = LocalDate.parse(date
                    , DateTimeFormatter.ofPattern("uuuu-M-d").withResolverStyle(ResolverStyle.STRICT));
        } catch (DateTimeParseException e) {
            throw new InvalidInputFormatException("     Invalid date format. Please write like date yyyy-M-d");
        }
        return localDate;
    }

    public static String[] splitCommand(String input) {
        return input.split("\\s+");
    }

    public static CommandType parseCommandType(String command) {
        return CommandType.from(command);
    }

    public static void parseInvalidCommand(CommandType commandType, int length) throws InvalidInputFormatException {
        switch (commandType) {
            case BYE, LIST ->  {
                if (length != 1) {
                    throw new InvalidInputFormatException("     Invalid command, please try another one");
                }
            }
            case MARK, UNMARK, DELETE ->  {
                if (length != 2) {
                    throw new InvalidInputFormatException
                            ("     Invalid format. Please use: " + commandType.name().toLowerCase() + " TASK_NUMBER");
                }
            }
            case FIND -> {
                if (length != 2) {
                    throw new InvalidInputFormatException("     Invalid format. Please use: find keyword");
                }
            }
            case DATE ->  {
                if (length != 2) {
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
