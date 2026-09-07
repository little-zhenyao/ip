package windy.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import windy.task.Deadline;
import windy.task.Event;
import windy.task.Task;
import windy.task.Todo;

/** Tests task serialization and safe file replacement. */
public class StorageTest {

    @TempDir
    Path tempDirectory;

    @Test
    public void saveTasks_allTaskTypes_preservesDataFormat() throws Exception {
        Path filePath = tempDirectory.resolve("windy.txt");
        Storage storage = new Storage(filePath.toString());
        List<Task> tasks = List.of(
                new Todo("read book", false),
                new Deadline("return book", true, "2026-9-1"),
                new Event("holiday", false, "2026-9-2", "2026-9-3"));

        storage.saveTasks(tasks);

        assertEquals(List.of(
                "T | 0 | read book",
                "D | 1 | return book | 2026-09-01",
                "E | 0 | holiday | 2026-09-02 | 2026-09-03"), Files.readAllLines(filePath));
    }

    @Test
    public void loadTasks_invalidCompletionStatus_throwsIoException() throws IOException {
        Storage storage = createStorageWithRecord("T | x | read book");

        IOException exception = assertThrows(IOException.class, storage::loadTasks);

        assertTrue(exception.getMessage().contains("completion status must be 0 or 1"));
    }

    @Test
    public void loadTasks_extraField_throwsIoException() throws IOException {
        Storage storage = createStorageWithRecord("T | 0 | read book | unexpected");

        IOException exception = assertThrows(IOException.class, storage::loadTasks);

        assertTrue(exception.getMessage().contains("expected 3 fields but found 4"));
    }

    @Test
    public void loadTasks_missingField_throwsIoException() throws IOException {
        Storage storage = createStorageWithRecord("D | 0 | return book");

        IOException exception = assertThrows(IOException.class, storage::loadTasks);

        assertTrue(exception.getMessage().contains("expected 4 fields but found 3"));
    }

    @Test
    public void loadTasks_unknownTaskType_throwsIoException() throws IOException {
        Storage storage = createStorageWithRecord("X | 0 | read book");

        IOException exception = assertThrows(IOException.class, storage::loadTasks);

        assertTrue(exception.getMessage().contains("unsupported task type 'X'"));
    }

    @Test
    public void saveTasks_failedReplacement_cleansTemporaryFileAndPreservesTarget() throws IOException {
        Path target = tempDirectory.resolve("windy.txt");
        Files.createDirectory(target);
        Path original = target.resolve("original.txt");
        Files.writeString(original, "preserved");
        Storage storage = new Storage(target.toString());

        assertThrows(IOException.class, () -> storage.saveTasks(List.of(new Todo("A", false))));

        assertEquals("preserved", Files.readString(original));
        try (var files = Files.list(tempDirectory)) {
            assertEquals(List.of(target), files.toList());
        }
    }

    private Storage createStorageWithRecord(String record) throws IOException {
        Path filePath = tempDirectory.resolve("windy.txt");
        Files.writeString(filePath, record);
        return new Storage(filePath.toString());
    }
}
