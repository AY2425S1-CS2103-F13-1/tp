package spleetwaise.transaction.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import spleetwaise.address.model.person.Person;
import spleetwaise.address.testutil.TypicalPersons;
import spleetwaise.transaction.model.transaction.Amount;
import spleetwaise.transaction.model.transaction.Date;
import spleetwaise.transaction.model.transaction.Description;
import spleetwaise.transaction.model.transaction.Transaction;

public class ModelManagerTest {

    private static Person testPerson = TypicalPersons.ALICE;
    private static Amount testAmount = new Amount("+1.23");
    private static Description testDescription = new Description("1");
    private static Date testDate = new Date("01012024");

    private static Transaction testTxn = new Transaction(testPerson, testAmount, testDescription, testDate);
    private static Transaction testTxn2 = new Transaction(testPerson, testAmount, new Description("2"), testDate);
    private static Transaction testTxn3 = new Transaction(testPerson, testAmount, new Description("3"), testDate);


    private spleetwaise.transaction.model.Model transactionModel = new spleetwaise.transaction.model.ModelManager();
    private spleetwaise.address.model.Model addressBookModel = new spleetwaise.address.model.ModelManager();

    @Test
    void transactionModelIsNotAddressBookModel() {
        assert !(transactionModel.equals(addressBookModel));
        assert !(addressBookModel.equals(transactionModel));
    }

    @Test
    public void constructor_emptyParam_success() {
        ModelManager manager = new ModelManager();
        assertEquals(new TransactionBook(), manager.getTransactionBook());
    }

    @Test
    public void constructor_nullParams_exceptionThrown() {
        assertThrows(NullPointerException.class, () -> new ModelManager(null));
    }

    @Test
    public void constructor_useExistingBook_success() {
        TransactionBook transactionBook = new TransactionBook();
        ModelManager manager = new ModelManager(transactionBook);

        assertEquals(transactionBook, manager.getTransactionBook());
    }

    @Test
    public void setTransactionBook_null_exceptionThrown() {
        ModelManager manager = new ModelManager();

        assertThrows(NullPointerException.class, () -> manager.setTransactionBook(null));
    }

    @Test
    public void setTransactionBook_validParams_success() {
        TransactionBook existingTransactionBook = new TransactionBook();
        existingTransactionBook.addTransaction(testTxn);
        ModelManager manager = new ModelManager(existingTransactionBook);

        TransactionBook newTransactionBook = new TransactionBook();
        newTransactionBook.addTransaction(testTxn2);
        manager.setTransactionBook(newTransactionBook);

        assertFalse(manager.getTransactionBook().getTransactionList().contains(testTxn));
        assertTrue(manager.getTransactionBook().getTransactionList().contains(testTxn2));
    }

    @Test
    public void getTransactionBook_success() {
        TransactionBook existingTransactionBook = new TransactionBook();
        existingTransactionBook.addTransaction(testTxn);
        ModelManager manager = new ModelManager(existingTransactionBook);

        assertEquals(existingTransactionBook, manager.getTransactionBook());
    }

    @Test
    public void addTransaction_success() {
        ModelManager manager = new ModelManager();

        manager.addTransaction(testTxn);

        assertTrue(manager.getTransactionBook().getTransactionList().contains(testTxn));
    }

    @Test
    public void addTransaction_null_exceptionThrown() {
        ModelManager manager = new ModelManager();

        assertThrows(NullPointerException.class, () -> manager.addTransaction(null));

        assertEquals(Collections.emptyList(), manager.getTransactionBook().getTransactionList());
    }

    @Test
    public void hasTransaction_contains_returnsTrue() {
        ModelManager manager = new ModelManager();
        manager.addTransaction(testTxn);

        assertTrue(manager.hasTransaction(testTxn));
    }

    @Test
    public void hasTransaction_doNotContain_returnsFalse() {
        ModelManager manager = new ModelManager();

        assertFalse(manager.hasTransaction(testTxn));
    }

    @Test
    public void getFilteredTransactionList_noPredicate_shouldEqual() {
        ModelManager manager = new ModelManager();
        manager.addTransaction(testTxn);
        manager.addTransaction(testTxn2);

        assertTrue(manager.getFilteredTransactionList().contains(testTxn));
        assertTrue(manager.getFilteredTransactionList().contains(testTxn2));
    }

    @Test
    public void updateFilteredTransactionList_withPredicate_shouldFilter() {
        ModelManager manager = new ModelManager();
        manager.addTransaction(testTxn);
        manager.addTransaction(testTxn2);

        Predicate<Transaction> pred = (t) -> t.getDescription().equals(testDescription);
        manager.updateFilteredTransactionList(pred);

        assertTrue(manager.getFilteredTransactionList().contains(testTxn));
        assertFalse(manager.getFilteredTransactionList().contains(testTxn2));
    }

    @Test
    public void equals_null_returnsFalse() {
        ModelManager manager = new ModelManager();

        assertFalse(manager.equals(null));
    }

    @Test
    public void equals_notSame_returnsFalse() {
        ModelManager manager1 = new ModelManager();
        manager1.addTransaction(testTxn);
        ModelManager manager2 = new ModelManager();
        manager2.addTransaction(testTxn2);

        assertFalse(manager1.equals(manager2));
    }

    @Test
    public void equals_diffPredicate_returnsFalse() {
        ModelManager manager1 = new ModelManager();
        manager1.addTransaction(testTxn2);
        manager1.addTransaction(testTxn3);
        Predicate<Transaction> pred1 = (t) -> t.getDescription().equals(new Description("2"));
        manager1.updateFilteredTransactionList(pred1);

        ModelManager manager2 = new ModelManager();
        manager2.addTransaction(testTxn2);
        manager2.addTransaction(testTxn3);
        Predicate<Transaction> pred2 = (t) -> t.getDescription().equals(new Description("3"));
        manager1.updateFilteredTransactionList(pred2);

        assertFalse(manager1.equals(manager2));
    }

    @Test
    public void equals_same_returnsTrue() {
        ModelManager manager1 = new ModelManager();
        manager1.addTransaction(testTxn);

        assertTrue(manager1.equals(manager1));

        ModelManager manager2 = new ModelManager();
        manager2.addTransaction(testTxn);

        assertTrue(manager1.equals(manager2));
    }



}
