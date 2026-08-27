package windy.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import windy.exception.InvalidInputFormatException;
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

        for (String line : Files.readAllLines(this.filePath)) {
            tasks.add(parseTask(line));
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
     * @return the task represented by the line
     * @throws IOException if the record is incomplete or contains invalid data
     */
    private Task parseTask(String line) throws IOException {
        String[] parts = line.split("\\s*\\|\\s*", -1);

        try {
            String type = parts[0];
            boolean isDone = parts[1].equals("1");
            String name = parts[2];

            return switch (type) {
                case "T" -> new Todo(name, isDone);
                case "D" -> new Deadline(name, isDone, parts[3]);
                case "E" -> new Event(name, isDone, parts[3], parts[4]);
                default -> throw new IOException("Unknown task type: " + type);
            };
        } catch (ArrayIndexOutOfBoundsException | InvalidInputFormatException exception) {
            throw new IOException("Invalid task line: " + line, exception);
        }
    }
}
