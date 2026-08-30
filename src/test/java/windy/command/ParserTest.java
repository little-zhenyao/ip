package windy.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import windy.exception.InvalidInputFormatException;

public class ParserTest {

    @Test
    public void parseCommand_supportedCommands_returnsMatchingCommandObjects()
            throws InvalidInputFormatException {
        assertInstanceOf(ByeCommand.class, Parser.parseCommand("bye", 0));
        assertInstanceOf(ListCommand.class, Parser.parseCommand("list", 0));
        assertInstanceOf(MarkCommand.class, Parser.parseCommand("mark 1", 1));
        assertInstanceOf(UnmarkCommand.class, Parser.parseCommand("unmark 1", 1));
        assertInstanceOf(DeleteCommand.class, Parser.parseCommand("delete 1", 1));
        assertInstanceOf(AddTaskCommand.class, Parser.parseCommand("todo read book", 0));
        assertInstanceOf(AddTaskCommand.class,
                Parser.parseCommand("deadline return book /by 2026-9-1", 0));
        assertInstanceOf(AddTaskCommand.class,
                Parser.parseCommand("event holiday /from 2026-9-1 /to 2026-9-2", 0));
        assertInstanceOf(FindCommand.class, Parser.parseCommand("find book", 0));
        assertInstanceOf(DateCommand.class, Parser.parseCommand("date 2026-9-1", 0));
    }

    @Test
    public void parseCommand_unknownCommand_throwsInvalidInputFormatException() {
        assertThrows(InvalidInputFormatException.class, () -> Parser.parseCommand("dance", 0));
    }

    @Test
    public void parseTaskNumber_validNumber_returnsZeroBasedIndex() throws InvalidInputFormatException {
        assertEquals(1, Parser.parseTaskNumber("2", 4));
    }

    @Test
    public void parseTaskNumber_invalidNumber_throwsInvalidInputFormatException() {
        assertThrows(InvalidInputFormatException.class, () -> Parser.parseTaskNumber("adffa", 3));
    }

    @Test
    public void parseTaskNumber_tooLargeNumber_throwsInvalidInputFormatException() {
        assertThrows(InvalidInputFormatException.class, () -> Parser.parseTaskNumber("5", 3));
    }

    @Test
    public void parseTaskNumber_zero_throwsInvalidInputFormatException() {
        assertThrows(InvalidInputFormatException.class, () -> Parser.parseTaskNumber("0", 3));
    }

    @Test
    public void parseTaskNumber_emptyTaskList_throwsInvalidInputFormatException() {
        assertThrows(InvalidInputFormatException.class, () -> Parser.parseTaskNumber("1", 0));
    }
}
