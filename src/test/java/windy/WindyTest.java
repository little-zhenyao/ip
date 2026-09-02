package windy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

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

        assertEquals("Got it. I've added this task:\n"
                + "  [T][ ] read book\n"
                + "Now you have 1 tasks in the list.", addResponse);
        assertEquals("Here are the tasks in your list:\n"
                + "1.[T][ ] read book", listResponse);
    }

    @Test
    public void getResponse_invalidCommand_returnsErrorMessage() {
        Windy windy = createWindy();

        String response = windy.getResponse("nonsense");

        assertEquals("Invalid command, please try another one", response);
        assertFalse(windy.isExitRequested());
    }

    @Test
    public void getResponse_byeCommand_returnsFarewellMessage() {
        Windy windy = createWindy();

        String response = windy.getResponse("bye");

        assertEquals("Bye. Hope to see you again soon!\n"
                + "______________________________________________", response);
        assertTrue(windy.isExitRequested());
    }

    private Windy createWindy() {
        String filePath = tempDirectory.resolve("data/windy.txt").toString();
        return new Windy(filePath);
    }
}
