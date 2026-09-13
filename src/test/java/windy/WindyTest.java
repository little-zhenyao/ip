package windy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests command responses exposed by Windy to the GUI.
 */
public class WindyTest {

    @TempDir
    Path tempDirectory;

    @Test
    public void getWelcomeMessage_returnsConsoleWelcomeContent() {
        Windy windy = createWindy();

        String welcomeMessage = windy.getWelcomeMessage();

        assertTrue(welcomeMessage.contains("Hello! I'm Windy."));
        assertTrue(welcomeMessage.contains("What can I do for you?"));
        assertTrue(welcomeMessage.contains("__        ___"));
    }

    @Test
    public void getResponse_addAndListCommands_returnsCommandResponses() {
        Windy windy = createWindy();

        String addResponse = windy.getResponse("todo read book");
        String listResponse = windy.getResponse("list");

        assertEquals(String.join(System.lineSeparator(),
                "Got it. I've added this task:",
                "  [T][ ] read book",
                "Now you have 1 tasks in the list."), addResponse);
        assertEquals(String.join(System.lineSeparator(),
                "Here are the tasks in your list:",
                "1.[T][ ] read book"), listResponse);
    }

    @Test
    public void getResponse_invalidCommand_returnsErrorMessage() {
        Windy windy = createWindy();

        String response = windy.getResponse("nonsense");

        assertEquals("Unknown command 'nonsense'. Available commands: bye, list, mark, "
                + "unmark, delete, todo, deadline, event, date, find, undo, redo.", response);
        assertFalse(windy.isExitRequested());
    }

    @Test
    public void getResponse_byeCommand_returnsFarewellMessage() {
        Windy windy = createWindy();

        String response = windy.getResponse("bye");

        assertEquals(String.join(System.lineSeparator(),
                "Bye. Hope to see you again soon!",
                "______________________________________________"), response);
        assertTrue(windy.isExitRequested());
    }

    private Windy createWindy() {
        String filePath = tempDirectory.resolve("data/windy.txt").toString();
        return new Windy(filePath);
    }
}
