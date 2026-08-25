import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private final Path filepath;

    public Storage(String filepath) {
        this.filepath = Path.of(filepath);
    }

    public List<Task> loadTasks() throws IOException {
        List<Task> tasks = new ArrayList<>();

        if (!Files.exists(this.filepath)) {
            return tasks;
        }

        for (String line : Files.readAllLines(this.filepath)) {
            tasks.add((parseTask(line)));
        }

        return tasks;
    }

    public void saveTasks(List<Task> tasks) throws IOException {
        Path parent =  this.filepath.getParent();
        if (!Files.exists(parent)) {
            Files.createDirectories(parent);
        }
        List<String> lines = tasks.stream().map(Task::toDataString).toList();
        Files.write(this.filepath, lines);
    }

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
        }catch (ArrayIndexOutOfBoundsException e) {
            throw new IOException("Invalid task line: " + line, e);
        }
    }
}
