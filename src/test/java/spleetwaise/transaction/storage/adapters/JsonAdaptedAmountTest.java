package spleetwaise.transaction.storage.adapters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import spleetwaise.address.commons.exceptions.IllegalValueException;
import spleetwaise.transaction.model.transaction.Amount;


public class JsonAdaptedAmountTest {

    private static final String VALID_AMOUNT_POSITIVE = "10.99";
    private static final String VALID_AMOUNT_NEGATIVE = "-10.99";
    private static final String INVALID_AMOUNT = "10.0099";

    @Test
    public void testConstructor_string_positiveAmount() {
        // Test creating an instance with a valid string amount
        assertEquals(VALID_AMOUNT_POSITIVE, (new JsonAdaptedAmount(VALID_AMOUNT_POSITIVE)).getAmount());
    }

    @Test
    public void testConstructor_string_negativeAmount() {
        // Test creating an instance with a valid string amount
        assertEquals(VALID_AMOUNT_NEGATIVE, (new JsonAdaptedAmount(VALID_AMOUNT_NEGATIVE)).getAmount());
    }

    @Test
    public void testConstructor_string_invalidAmount() {
        // Test creating an instance with a valid string amount
        assertThrows(IllegalValueException.class, () -> (new JsonAdaptedAmount(INVALID_AMOUNT)).toModelType());
    }

    @Test
    public void testConstructor_amount_positiveAmount() {
        // Create an Amount object
        Amount amount = new Amount(VALID_AMOUNT_POSITIVE);
        // Create a JsonAdaptedAmount instance from the Amount
        assertEquals(VALID_AMOUNT_POSITIVE, (new JsonAdaptedAmount(amount)).getAmount());
    }

    @Test
    public void testConstructor_amount_negativeAmount() {
        // Create an Amount object
        Amount amount = new Amount(VALID_AMOUNT_NEGATIVE);
        // Create a JsonAdaptedAmount instance from the Amount
        assertEquals(VALID_AMOUNT_NEGATIVE, (new JsonAdaptedAmount(amount)).getAmount());
    }

    @Test
    public void testToModelType() throws IllegalValueException {
        // Test converting a valid string amount to an Amount object
        Amount modelAmount = (new JsonAdaptedAmount(VALID_AMOUNT_POSITIVE)).toModelType();
        assertEquals(VALID_AMOUNT_POSITIVE, modelAmount.getAmount().toString());
    }
}
