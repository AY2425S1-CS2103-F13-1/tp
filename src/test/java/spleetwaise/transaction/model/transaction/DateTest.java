package spleetwaise.transaction.model.transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class DateTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        String testString = null;

        assertThrows(NullPointerException.class, () -> new Date(testString));
    }

    @Test
    public void constructor_empty_throwsIllegalArgumentException() {
        String testString = "";

        assertThrows(IllegalArgumentException.class, () -> new Date(testString));
    }

    @Test
    public void isValidDate_null_exceptionThrown() {
        assertThrows(NullPointerException.class, () -> Date.isValidDate(null));
    }

    @Test
    public void isValidDate_invalidInput_returnsFalse() {
        assertFalse(Date.isValidDate(""));
        assertFalse(Date.isValidDate("01/01/2024"));
        assertFalse(Date.isValidDate("012025"));
        assertFalse(Date.isValidDate("0101"));
        assertFalse(Date.isValidDate("112024"));
        assertFalse(Date.isValidDate("31/02/2024"));
    }

    @Test
    public void isValidDate_validInput_returnsTrue() {
        assertTrue(Date.isValidDate("01012024"));
    }

    @Test
    public void toString_correctFormat() {
        Date date = new Date("02012024");
        assertEquals("02/01/2024", date.toString());
    }

    @Test
    public void equals_validArgument_returnsTrue() {
        Date date1 = new Date("02012024");
        Date date2 = new Date("02012024");
        assertTrue(date1.equals(date2));

        assertTrue(date1.equals(date1));
    }

    @Test
    public void equals_invalidArgument_returnsFalse() {
        Date date1 = new Date("01012024");
        Date date2 = new Date("02012024");
        assertFalse(date1.equals(date2));

        assertFalse(date1.equals(null));
    }
}
