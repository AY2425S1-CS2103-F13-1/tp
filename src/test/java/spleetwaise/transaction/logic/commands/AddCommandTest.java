package spleetwaise.transaction.logic.commands;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import spleetwaise.address.commons.core.index.Index;
import spleetwaise.address.logic.Messages;
import spleetwaise.address.model.AddressBookModelManager;
import spleetwaise.address.model.UserPrefs;
import spleetwaise.address.model.person.Person;
import spleetwaise.address.testutil.Assert;
import spleetwaise.address.testutil.TypicalPersons;
import spleetwaise.commons.logic.commands.CommandResult;
import spleetwaise.commons.logic.commands.exceptions.CommandException;
import spleetwaise.commons.model.CommonModel;
import spleetwaise.transaction.model.TransactionBookModelManager;
import spleetwaise.transaction.model.transaction.Amount;
import spleetwaise.transaction.model.transaction.Category;
import spleetwaise.transaction.model.transaction.Date;
import spleetwaise.transaction.model.transaction.Description;
import spleetwaise.transaction.model.transaction.Status;
import spleetwaise.transaction.model.transaction.Transaction;

public class AddCommandTest {

    private static final Person testPerson = TypicalPersons.ALICE;
    private static final Index testIndex = Index.fromOneBased(1);
    private static final Amount testAmount = new Amount("1.23");
    private static final Description testDescription = new Description("description");
    private static final Date testDate = new Date("01012024");
    private static final Status testStatus = new Status(false);
    private static final Set<Category> testCategories = new HashSet<>(List.of(new Category("FOOD")));
    private static final Transaction testTxn = new Transaction(
            testPerson, testAmount, testDescription, testDate, testCategories);

    @BeforeEach
    void setup() {
        CommonModel.initialise(
                new AddressBookModelManager(TypicalPersons.getTypicalAddressBook(), new UserPrefs()),
                new TransactionBookModelManager()
        );
    }

    @Test
    public void constructor_null_exceptionThrown() {
        assertThrows(NullPointerException.class, () -> new AddCommand(null, testAmount,
                testDescription, testDate, testCategories
        ));
        assertThrows(NullPointerException.class, () -> new AddCommand(testIndex, null,
                testDescription, testDate, testCategories
        ));
        assertThrows(NullPointerException.class, () -> new AddCommand(testIndex, testAmount,
                null, testDate, testCategories
        ));
        assertThrows(NullPointerException.class, () -> new AddCommand(testIndex, testAmount,
                testDescription, null, testCategories
        ));
        assertThrows(NullPointerException.class, () -> new AddCommand(testIndex, testAmount,
                testDescription, testDate, null
        ));
    }

    @Test
    public void execute_validTransaction_success() {
        AddCommand cmd = new AddCommand(testIndex, testAmount, testDescription, testDate, testCategories);
        CommandResult cmdRes = assertDoesNotThrow(cmd::execute);

        assertFalse(CommonModel.getInstance().getTransactionBook().getTransactionList().isEmpty());

        String expectedString = String.format(
                "%s [%s] (%s): %s on %s for $%s with categories: %s",
                testPerson.getName(),
                testStatus,
                testPerson.getPhone(),
                testDescription, testDate, testAmount, testCategories
        );
        assertEquals(
                String.format(AddCommand.MESSAGE_SUCCESS, expectedString),
                cmdRes.getFeedbackToUser()
        );
        assertTrue(CommonModel.getInstance().hasTransaction(testTxn));
    }

    @Test
    public void execute_invalidPersonIndex_throwsCommandException() {
        Index invalidIndex = Index.fromZeroBased(TypicalPersons.getTypicalAddressBook().getPersonList().size());
        AddCommand cmd = new AddCommand(invalidIndex, testAmount,
                testDescription, testDate, testCategories
        );

        Assert.assertThrows(CommandException.class, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX,
                cmd::execute
        );
    }

    @Test
    public void equals_sameTransaction_returnsTrue() {
        AddCommand cmd1 = new AddCommand(testIndex, testAmount, testDescription, testDate, testCategories);
        AddCommand cmd2 = new AddCommand(testIndex, testAmount, testDescription, testDate, testCategories);

        assertEquals(cmd1, cmd1);
        assertEquals(cmd1, cmd2);
        assertEquals(cmd1.toString(), cmd1.toString());
        assertEquals(cmd2.toString(), cmd2.toString());
    }

    @Test
    public void equals_diffTransaction_returnsFalse() {
        AddCommand cmdBase = new AddCommand(testIndex, testAmount, testDescription, testDate, testCategories);

        Index newIndex = Index.fromOneBased(2);
        AddCommand cmdOther = new AddCommand(newIndex, testAmount, testDescription, testDate, testCategories);
        assertNotEquals(cmdBase, cmdOther);

        Amount newAmount = new Amount("12.3");
        cmdOther = new AddCommand(testIndex, newAmount, testDescription, testDate, testCategories);
        assertNotEquals(cmdBase, cmdOther);

        Description newDesc = new Description("different description");
        cmdOther = new AddCommand(testIndex, testAmount, newDesc, testDate, testCategories);
        assertNotEquals(cmdBase, cmdOther);

        Date newDate = new Date("01022024");
        cmdOther = new AddCommand(testIndex, testAmount, testDescription, newDate, testCategories);
        assertNotEquals(cmdBase, cmdOther);

        cmdOther = new AddCommand(testIndex, testAmount, testDescription, testDate,
                new HashSet<>(List.of(new Category("EXTRA")))
        );
        assertNotEquals(cmdBase, cmdOther);
    }

    @Test
    public void equals_null_returnsFalse() {
        AddCommand cmd = new AddCommand(testIndex, testAmount, testDescription, testDate, testCategories);

        assertNotEquals(cmd, null);
        assertNotEquals(null, cmd);
    }

    @Test
    public void execute_duplicateTransaction_exceptionThrown() {
        CommonModel.getInstance().addTransaction(testTxn);

        AddCommand cmd = new AddCommand(testIndex, testAmount, testDescription, testDate, testCategories);
        Assert.assertThrows(CommandException.class, AddCommand.MESSAGE_DUPLICATE_TXN, cmd::execute);
    }
}
