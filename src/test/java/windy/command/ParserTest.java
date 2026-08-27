package windy.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import windy.exception.InvalidInputFormatException;

public class ParserTest {

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
        assertThrows(InvalidInputFormatException.class,
                () -> Parser.parseTaskNumber("0", 3));
    }

    @Test
    public void parseTaskNumber_emptyTaskList_throwsInvalidInputFormatException() {
        assertThrows(InvalidInputFormatException.class,
                () -> Parser.parseTaskNumber("1", 0));
    }
}