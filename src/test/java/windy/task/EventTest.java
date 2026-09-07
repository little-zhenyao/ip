package windy.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import windy.exception.InvalidInputFormatException;

public class EventTest {

    @Test
    public void constructor_endDateBeforeStartDate_throwsInvalidInputFormatException() {
        Executable createEvent = () -> new Event("holiday", false, "2026-9-3", "2026-8-27");

        InvalidInputFormatException exception = assertThrows(InvalidInputFormatException.class, createEvent);

        assertEquals("The event end date cannot be before its start date", exception.getMessage());
    }
}
