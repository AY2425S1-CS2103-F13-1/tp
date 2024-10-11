package spleetwaise.transaction.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import spleetwaise.address.model.person.Person;
import spleetwaise.address.testutil.TypicalPersons;
import spleetwaise.transaction.model.transaction.Amount;
import spleetwaise.transaction.model.transaction.Date;
import spleetwaise.transaction.model.transaction.Description;
import spleetwaise.transaction.model.transaction.Transaction;
import spleetwaise.transaction.model.transaction.exceptions.DuplicateTransactionException;

public class TransactionBookTest {

    private static Person testPerson = TypicalPersons.ALICE;
    private static Amount testAmount = new Amount("+1.23");
    private static Description testDescription = new Description("description");
    private static Date testDate = new Date("01012024");

    private static Transaction testTxn = new Transaction(testPerson, testAmount, testDescription, testDate);
    private static Transaction testTxn2 = new Transaction(testPerson, testAmount, new Description("2"), testDate);
    private static Transaction testTxn3 = new Transaction(testPerson, testAmount, new Description("3"), testDate);

    @Test
    public void constructor_noParams_success() {
        TransactionBook testBook = new TransactionBook();
        assertEquals(Collections.EMPTY_LIST, testBook.getTransactionList());
    }

    @Test
    public void constructor_withParams_success() {
        TransactionBook testExistingBook = new TransactionBook();
        TransactionBook testBook = new TransactionBook(testExistingBook);
        assertEquals(testExistingBook.getTransactionList(), testBook.getTransactionList());
    }

    @Test
    public void containsTransaction_isInList_returnsTrue() {
        TransactionBook book = new TransactionBook();
        book.addTransaction(testTxn);

        assertTrue(book.containsTransaction(testTxn));
    }

    @Test
    public void containsTransaction_notInList_returnsFalse() {
        TransactionBook book = new TransactionBook();

        assertFalse(book.containsTransaction(testTxn));
    }

    @Test
    public void addTransaction_noDuplicate_success() {
        TransactionBook book = new TransactionBook();

        assertDoesNotThrow(() -> book.addTransaction(testTxn));
    }

    @Test
    public void addTransaction_duplicate_exceptionThrown() {
        TransactionBook book = new TransactionBook();
        book.addTransaction(testTxn);

        assertThrows(DuplicateTransactionException.class, () -> book.addTransaction(testTxn));
    }

    @Test
    public void addTransaction_null_exceptionThrown() {
        TransactionBook book = new TransactionBook();

        assertThrows(NullPointerException.class, () -> book.addTransaction(null));
    }

    @Test
    public void setTransaction_null_exceptionThrown() {
        TransactionBook book = new TransactionBook();

        assertThrows(NullPointerException.class, () -> book.setTransactions(null));
    }

    @Test
    public void setTransaction_validParams_success() {
        TransactionBook book = new TransactionBook();
        book.addTransaction(testTxn);

        TransactionBook replacementBook = new TransactionBook();
        replacementBook.addTransaction(testTxn2);
        replacementBook.addTransaction(testTxn3);

        book.setTransactions(replacementBook);

        assertFalse(book.containsTransaction(testTxn));
        assertTrue(book.containsTransaction(testTxn2));
        assertTrue(book.containsTransaction(testTxn3));
    }

    @Test
    public void equals_isEqual_returnsTrue() {
        TransactionBook book = new TransactionBook();
        book.addTransaction(testTxn);

        assertTrue(book.equals(book));

        TransactionBook book2 = new TransactionBook();
        book2.addTransaction(testTxn);

        assertTrue(book.equals(book2));
    }

    @Test
    public void equals_notEqual_returnsFalse() {
        TransactionBook book = new TransactionBook();
        book.addTransaction(testTxn);

        assertFalse(book.equals(null));

        TransactionBook book2 = new TransactionBook();
        book2.addTransaction(testTxn2);

        assertFalse(book.equals(book2));
    }

}
