package windy.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import windy.exception.InvalidInputFormatException;
import windy.storage.TaskDataFormat.TaskType;
import windy.task.Deadline;
import windy.task.Event;
import windy.task.Task;
import windy.task.Todo;

/**
 * Loads and saves tasks in Windy's plain-text data format.
 */
public class Storage {
    private final Path filePath;

    /**
     * Creates a storage manager for the specified file.
     *
     * @param filePath the path of the task data file
     */
    public Storage(String filePath) {
        this.filePath = Path.of(filePath);
    }

    /**
     * Loads tasks from the data file, or returns an empty list if the file does not exist.
     *
     * @return the tasks stored in the file
     * @throws IOException if the file cannot be read or contains an invalid task record
     */
    public List<Task> loadTasks() throws IOException {
        List<Task> tasks = new ArrayList<>();

        if (!Files.exists(this.filePath)) {
            return tasks;
        }

        List<String> lines = Files.readAllLines(this.filePath);
        for (int i = 0; i < lines.size(); i++) {
            tasks.add(parseTask(lines.get(i), i + 1));
        }

        return tasks;
    }

    /**
     * Writes all tasks to the data file, creating its parent directory when necessary.
     *
     * @param tasks the tasks to save
     * @throws IOException if the tasks cannot be written
     */
    public void saveTasks(List<Task> tasks) throws IOException {
        Path parent = this.filePath.getParent();
        if (!Files.exists(parent)) {
            Files.createDirectories(parent);
        }
        List<String> lines = tasks.stream().map(Task::toDataString).toList();
        Files.write(this.filePath, lines);
    }

    /**
     * Converts one stored record into its corresponding task object.
     *
     * @param line a line from the data file
     * @param lineNumber the one-based position of the line in the data file
     * @return the task represented by the line
     * @throws IOException if the record is incomplete or contains invalid data
     */
    private Task parseTask(String line, int lineNumber) throws IOException {
        String[] recordFields = TaskDataFormat.splitRecord(line);
        TaskType taskType = parseTaskType(recordFields[0], lineNumber);
        int expectedFieldCount = taskType.getFieldCount();
        if (recordFields.length != expectedFieldCount) {
            throw createInvalidRecordException(lineNumber,
                    "expected " + expectedFieldCount + " fields but found " + recordFields.length);
        }

        boolean isDone = parseCompletionStatus(recordFields[1], lineNumber);
        String taskDescription = recordFields[2];

        try {
            return switch (taskType) {
                case TODO -> new Todo(taskDescription, isDone);
                case DEADLINE -> new Deadline(taskDescription, isDone, recordFields[3]);
                case EVENT -> new Event(taskDescription, isDone, recordFields[3], recordFields[4]);
            };
        } catch (InvalidInputFormatException exception) {
            throw createInvalidRecordException(lineNumber, "invalid task data", exception);
        }
    }

    private TaskType parseTaskType(String typeCode, int lineNumber) throws IOException {
        try {
            return TaskType.fromCode(typeCode);
        } catch (IllegalArgumentException exception) {
            throw createInvalidRecordException(lineNumber, exception.getMessage());
        }
    }

    private boolean parseCompletionStatus(String status, int lineNumber) throws IOException {
        try {
            return TaskDataFormat.parseCompletionStatus(status);
        } catch (IllegalArgumentException exception) {
            throw createInvalidRecordException(lineNumber, exception.getMessage());
        }
    }

    private IOException createInvalidRecordException(int lineNumber, String reason) {
        return new IOException("Invalid task record at line " + lineNumber + ": " + reason);
    }

    private IOException createInvalidRecordException(int lineNumber, String reason, Exception cause) {
        return new IOException("Invalid task record at line " + lineNumber + ": " + reason, cause);
    }
}
