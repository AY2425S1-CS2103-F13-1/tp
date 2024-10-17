package spleetwaise.transaction.model.transaction;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class DescriptionTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        String testString = null;

        assertThrows(NullPointerException.class, () -> new Description(testString));
    }

    @Test
    public void constructor_empty_throwsIllegalArgumentException() {
        String testString = "";

        assertThrows(IllegalArgumentException.class, () -> new Description(testString));
    }

    @Test
    public void isValidDate_null_exceptionThrown() {
        assertThrows(NullPointerException.class, () -> Description.isValidDescription(null));
    }

    @Test
    public void isValidDescription_invalidInput_returnsFalse() {
        assertFalse(Description.isValidDescription(""));

        StringBuilder longStringBuilder = new StringBuilder();
        for (int i = 0; i <= 120; i++) {
            longStringBuilder.append("+");
        }
        String longString = longStringBuilder.toString();
        assertFalse(Description.isValidDescription(longString));
    }

    @Test
    public void isValidDescription_validInput_returnsTrue() {
        assertTrue(Description.isValidDescription("valid"));
    }

    @Test
    public void equals_validArgument_returnsTrue() {
        Description desc1 = new Description("same");
        Description desc2 = new Description("same");

        assertTrue(desc1.equals(desc2));

        assertTrue(desc1.equals(desc1));
    }

    @Test
    public void equals_invalidArgument_returnsFalse() {
        Description desc1 = new Description("same");
        Description desc2 = new Description("not same");

        assertFalse(desc1.equals(desc2));

        assertFalse(desc1.equals(null));
    }
}
