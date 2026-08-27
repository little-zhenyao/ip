package windy.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import windy.exception.InvalidInputFormatException;

/**
 * Tests the task management and date-based filtering behavior of {@link TaskList}.
 */
public class TaskListTest {

    @Test
    public void addTask_newTask_addsTaskToList() {
        TaskList taskList = new TaskList(new ArrayList<>());
        Todo todo = new Todo("buy groceries", false);

        taskList.addTask(todo);

        assertEquals(1, taskList.getNumTasks());
        assertSame(todo, taskList.getTask(0));
    }

    @Test
    public void deleteTask_existingTask_removesAndReturnsTask() {
        Todo firstTask = new Todo("buy groceries", false);
        Todo secondTask = new Todo("read book", false);
        TaskList taskList = new TaskList(new ArrayList<>(List.of(firstTask, secondTask)));

        Task deletedTask = taskList.deleteTask(0);

        assertSame(firstTask, deletedTask);
        assertEquals(List.of(secondTask), taskList.getTasks());
    }

    @Test
    public void findTaskByDate_multipleTasks_returnsMatchingIncompleteTasks()
            throws InvalidInputFormatException {
        LocalDate searchDate = LocalDate.of(2026, 8, 27);
        Deadline matchingDeadline = new Deadline("submit assignment", false, "2026-8-27");
        Event matchingEvent = new Event("project meeting", false, "2026-8-26", "2026-8-28");
        Event nonMatchingEvent = new Event("holiday", false, "2026-9-1", "2026-9-2");
        Deadline completedDeadline = new Deadline("finished work", true, "2026-8-27");
        Todo todo = new Todo("buy groceries", false);
        TaskList taskList = new TaskList(List.of(
                matchingDeadline, matchingEvent, nonMatchingEvent, completedDeadline, todo));

        List<Task> tasksFound = taskList.findTaskByDate(searchDate);

        assertEquals(List.of(matchingDeadline, matchingEvent), tasksFound);
    }

    @Test
    public void findTaskByDate_noMatchingTasks_returnsEmptyList()
            throws InvalidInputFormatException {
        TaskList taskList = new TaskList(List.of(
                new Event("holiday", false, "2026-9-1", "2026-9-2"),
                new Todo("buy groceries", false)));

        List<Task> tasksFound = taskList.findTaskByDate(LocalDate.of(2026, 8, 27));

        assertTrue(tasksFound.isEmpty());
    }
}
