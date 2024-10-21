package spleetwaise.transaction.logic.commands;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import spleetwaise.address.logic.commands.CommandResult;
import spleetwaise.address.model.AddressBookModel;
import spleetwaise.address.model.person.Person;
import spleetwaise.address.testutil.TypicalPersons;
import spleetwaise.commons.CommonModel;
import spleetwaise.transaction.model.ModelManager;
import spleetwaise.transaction.model.TransactionBookModel;
import spleetwaise.transaction.model.transaction.Amount;
import spleetwaise.transaction.model.transaction.Date;
import spleetwaise.transaction.model.transaction.Description;
import spleetwaise.transaction.testutil.TypicalTransactions;

public class FilterCommandTest {

    private static final Person testPerson = TypicalTransactions.SEANOWESME.getPerson();
    private static final Amount testAmount = TypicalTransactions.SEANOWESME.getAmount();
    private static final Description testDescription = TypicalTransactions.SEANOWESME.getDescription();
    private static final Date testDate = TypicalTransactions.SEANOWESME.getDate();
    private static final AddressBookModel abModel = new spleetwaise.address.model.ModelManager();
    private static final TransactionBookModel txnModel = new ModelManager();

    @BeforeAll
    public static void setUp() {
        abModel.setAddressBook(TypicalPersons.getTypicalAddressBook());
        txnModel.setTransactionBook(TypicalTransactions.getTypicalTransactionBook());
        CommonModel.initialise(abModel, txnModel);
    }

    @BeforeEach
    public void setUpEach() {
        txnModel.updateFilteredTransactionList(TransactionBookModel.PREDICATE_SHOW_ALL_TXNS);
        assertTrue(txnModel.hasTransaction(TypicalTransactions.SEANOWESME));
        assertTrue(txnModel.hasTransaction(TypicalTransactions.BOBOWES));
    }

    @Test
    public void constructor_null_exceptionThrown() {
        assertThrows(NullPointerException.class, () -> new FilterCommand(null, null, null, null));
    }

    @Test
    public void execute_personOnly_success() {
        FilterCommand cmd = new FilterCommand(testPerson, null, null, null);
        CommandResult cmdRes = assertDoesNotThrow(cmd::execute);

        assertEquals(FilterCommand.MESSAGE_SUCCESS, cmdRes.getFeedbackToUser());
        assertEquals(1, txnModel.getFilteredTransactionList().size());
        assertTrue(txnModel.getFilteredTransactionList().contains(TypicalTransactions.SEANOWESME));
        assertFalse(txnModel.getFilteredTransactionList().contains(TypicalTransactions.BOBOWES));
    }

    @Test
    public void execute_amountOnly_success() {
        FilterCommand cmd = new FilterCommand(null, testAmount, null, null);
        CommandResult cmdRes = assertDoesNotThrow(cmd::execute);

        assertEquals(FilterCommand.MESSAGE_SUCCESS, cmdRes.getFeedbackToUser());
        assertEquals(1, txnModel.getFilteredTransactionList().size());
        assertTrue(txnModel.getFilteredTransactionList().contains(TypicalTransactions.SEANOWESME));
        assertFalse(txnModel.getFilteredTransactionList().contains(TypicalTransactions.BOBOWES));
    }

    @Test
    public void execute_descriptionOnly_success() {
        FilterCommand cmd = new FilterCommand(null, null, testDescription, null);
        CommandResult cmdRes = assertDoesNotThrow(cmd::execute);

        assertEquals(FilterCommand.MESSAGE_SUCCESS, cmdRes.getFeedbackToUser());
        assertEquals(1, txnModel.getFilteredTransactionList().size());
        assertTrue(txnModel.getFilteredTransactionList().contains(TypicalTransactions.SEANOWESME));
        assertFalse(txnModel.getFilteredTransactionList().contains(TypicalTransactions.BOBOWES));
    }

    @Test
    public void execute_descriptionDifferentCaseOnly_success() {
        FilterCommand cmd = new FilterCommand(null, null,
                new Description(testDescription.toString().toLowerCase()), null);
        CommandResult cmdRes = assertDoesNotThrow(cmd::execute);

        assertEquals(FilterCommand.MESSAGE_SUCCESS, cmdRes.getFeedbackToUser());
        assertEquals(1, txnModel.getFilteredTransactionList().size());
        assertTrue(txnModel.getFilteredTransactionList().contains(TypicalTransactions.SEANOWESME));
        assertFalse(txnModel.getFilteredTransactionList().contains(TypicalTransactions.BOBOWES));
    }

    @Test
    public void execute_dateOnly_success() {
        FilterCommand cmd = new FilterCommand(null, null, null, testDate);
        CommandResult cmdRes = assertDoesNotThrow(cmd::execute);

        assertEquals(FilterCommand.MESSAGE_SUCCESS, cmdRes.getFeedbackToUser());
        assertEquals(1, txnModel.getFilteredTransactionList().size());
        assertTrue(txnModel.getFilteredTransactionList().contains(TypicalTransactions.SEANOWESME));
        assertFalse(txnModel.getFilteredTransactionList().contains(TypicalTransactions.BOBOWES));
    }

    @Test
    public void execute_allParams_success() {
        FilterCommand cmd = new FilterCommand(testPerson, testAmount, testDescription, testDate);
        CommandResult cmdRes = assertDoesNotThrow(cmd::execute);

        assertEquals(FilterCommand.MESSAGE_SUCCESS, cmdRes.getFeedbackToUser());
        assertEquals(1, txnModel.getFilteredTransactionList().size());
        assertTrue(txnModel.getFilteredTransactionList().contains(TypicalTransactions.SEANOWESME));
        assertFalse(txnModel.getFilteredTransactionList().contains(TypicalTransactions.BOBOWES));
    }

    @Test
    public void equals_sameTransaction_returnsTrue() {
        FilterCommand cmd1 = new FilterCommand(testPerson, testAmount, testDescription, testDate);
        FilterCommand cmd2 = new FilterCommand(testPerson, testAmount, testDescription, testDate);

        assertEquals(cmd1, cmd1);
        assertEquals(cmd1, cmd2);
    }

    @Test
    public void equals_diffTransaction_returnsFalse() {
        FilterCommand cmd1 = new FilterCommand(null, testAmount, testDescription, testDate);
        FilterCommand cmd2 = new FilterCommand(testPerson, null, testDescription, testDate);
        FilterCommand cmd3 = new FilterCommand(testPerson, testAmount, null, testDate);
        FilterCommand cmd4 = new FilterCommand(testPerson, testAmount, testDescription, null);

        assertNotEquals(cmd1, cmd2);
        assertNotEquals(cmd1, cmd3);
        assertNotEquals(cmd1, cmd4);
        assertNotEquals(cmd2, cmd3);
        assertNotEquals(cmd2, cmd4);
        assertNotEquals(cmd3, cmd4);
    }

    @Test
    public void equals_genericObject_returnsFalse() {
        FilterCommand cmd1 = new FilterCommand(testPerson, testAmount, testDescription, testDate);

        assertNotEquals(new Object(), cmd1);
    }

    @Test
    public void equals_null_returnsFalse() {
        FilterCommand cmd1 = new FilterCommand(testPerson, testAmount, testDescription, testDate);

        assertNotEquals(null, cmd1);
    }

    @Test
    public void toString_success() {
        FilterCommand cmd = new FilterCommand(null, null, testDescription, null);
        assertEquals("spleetwaise.transaction.logic.commands.FilterCommand{contact=null, amount=null, "
                + "description=Sean owes me a lot for a landed property in Sentosa, date=null}",
                cmd.toString());
    }
}

