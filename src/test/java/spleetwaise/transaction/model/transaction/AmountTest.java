package spleetwaise.transaction.model.transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class AmountTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        String testString = null;

        assertThrows(NullPointerException.class, () -> new Amount(testString));
    }

    @Test
    public void constructor_empty_throwsIllegalArgumentException() {
        String testString = "";

        assertThrows(IllegalArgumentException.class, () -> new Amount(testString));
    }

    @Test
    public void isValidAmount_null_throwsExcecption() {
        String testString = null;

        assertThrows(NullPointerException.class, () -> Amount.isValidAmount(testString));
    }

    @Test
    public void isValidAmount_invalidArgument_returnsFalse() {
        // Test empty inputs
        assertFalse(Amount.isValidAmount(""));
        assertFalse(Amount.isValidAmount(" "));

        // Test non-numeric inputs
        assertFalse(Amount.isValidAmount("+"));
        assertFalse(Amount.isValidAmount("-"));
        assertFalse(Amount.isValidAmount("alphanumeric"));

        // Test numeric input with wrong format
        assertFalse(Amount.isValidAmount("100"));
        assertFalse(Amount.isValidAmount("100.001"));

        // Test zero inputs
        assertFalse(Amount.isValidAmount("+0"));
        assertFalse(Amount.isValidAmount("-0"));
        assertFalse(Amount.isValidAmount("+0.0"));
        assertFalse(Amount.isValidAmount("-0.0"));
        assertFalse(Amount.isValidAmount("+0.00"));
        assertFalse(Amount.isValidAmount("-0.00"));
    }

    @Test
    public void isValidAmount_validArgument_returnsTrue() {
        assertTrue(Amount.isValidAmount("+100"));
        assertTrue(Amount.isValidAmount("-100"));
        assertTrue(Amount.isValidAmount("+100.0"));
        assertTrue(Amount.isValidAmount("-100.0"));
        assertTrue(Amount.isValidAmount("+100.00"));
        assertTrue(Amount.isValidAmount("-100.00"));
        assertTrue(Amount.isValidAmount("+0.01"));
        assertTrue(Amount.isValidAmount("-0.01"));
        assertTrue(Amount.isValidAmount("+0.1"));
        assertTrue(Amount.isValidAmount("-0.1"));
    }

    @Test
    public void isNegative_success() {
        Amount amt1 = new Amount("+100");
        assertFalse(amt1.isNegative());

        Amount amt2 = new Amount("-100");
        assertTrue(amt2.isNegative());
    }

    @Test
    public void equals_validArgument_returnsTrue() {
        Amount amt1 = new Amount("+100");
        Amount amt2 = new Amount("+100.0");
        assertTrue(amt1.equals(amt2));

        amt1 = new Amount("+100");
        assertTrue(amt1.equals(amt1));
    }

    @Test
    public void equals_invalidArgument_returnsFalse() {
        Amount amt1 = new Amount("+100");
        assertFalse(amt1.equals(null));
    }

    @Test
    public void toString_correctFormat() {
        Amount amt1 = new Amount("+0.12");
        assertEquals("+0.12", amt1.toString());

        amt1 = new Amount("-0.12");
        assertEquals("-0.12", amt1.toString());
    }



}
