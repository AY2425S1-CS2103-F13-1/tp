package spleetwaise.transaction.model.transaction;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import spleetwaise.address.model.person.Person;
import spleetwaise.address.testutil.TypicalPersons;

public class TransactionTest {

    private static Person testPerson = TypicalPersons.ALICE;
    private static Amount testAmount = new Amount("+1.23");
    private static Description testDescription = new Description("description");
    private static Date testDate = new Date("01012024");

    @Test
    public void constructor_nullParams_exceptionThrown() {
        assertThrows(NullPointerException.class, () -> new Transaction(null, testAmount, testDescription, testDate));
        assertThrows(NullPointerException.class, () -> new Transaction(testPerson, null, testDescription, testDate));
        assertThrows(NullPointerException.class, () -> new Transaction(testPerson, testAmount, null, testDate));
        assertThrows(NullPointerException.class, () -> new Transaction(testPerson, testAmount, testDescription, null));
    }

    @Test
    public void constructor_validParams_success() {
        assertDoesNotThrow(() -> new Transaction(testPerson, testAmount, testDescription, testDate));
    }

    @Test
    public void constructor_noDateParam_success() {
        Date nowDate = Date.getNowDate();
        Transaction noDateTxn = new Transaction(testPerson, testAmount, testDescription);
        assertEquals(nowDate, noDateTxn.getDate());

    }

    @Test
    public void equals_validArgument_returnsTrue() {
        Transaction txn1 = new Transaction(testPerson, testAmount, testDescription, testDate);
        Transaction txn2 = new Transaction(testPerson, testAmount, testDescription, testDate);

        assertTrue(txn1.equals(txn2));

        assertTrue(txn1.equals(txn1));
    }

    @Test
    public void equals_invalidArgument_returnsFalse() {
        Transaction txn1 = new Transaction(testPerson, testAmount, testDescription, testDate);

        Person testPerson2 = TypicalPersons.BOB;
        Transaction txn2 = new Transaction(testPerson2, testAmount, testDescription, testDate);
        assertFalse(txn1.equals(txn2));

        Amount testAmount2 = new Amount("-1.23");
        txn2 = new Transaction(testPerson, testAmount2, testDescription, testDate);
        assertFalse(txn1.equals(txn2));

        Description testDescription2 = new Description("description2");
        txn2 = new Transaction(testPerson, testAmount, testDescription2, testDate);
        assertFalse(txn1.equals(txn2));

        Date testDate2 = new Date("02012024");
        txn2 = new Transaction(testPerson, testAmount, testDescription, testDate2);
        assertFalse(txn1.equals(txn2));

        assertFalse(txn1.equals(null));
    }

    @Test
    public void toString_success() {
        Transaction txn1 = new Transaction(testPerson, testAmount, testDescription, testDate);

        assertEquals(String.format("[%s] Alice Pauline(94351253): description on 01/01/2024 for $+1.23", txn1.getId()),
                txn1.toString());
    }



}
