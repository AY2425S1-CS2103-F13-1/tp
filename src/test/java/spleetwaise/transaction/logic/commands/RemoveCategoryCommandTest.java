package spleetwaise.transaction.logic.commands;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import spleetwaise.address.logic.commands.CommandResult;
import spleetwaise.address.model.person.Person;
import spleetwaise.address.testutil.TypicalPersons;
import spleetwaise.transaction.logic.commands.exceptions.CommandException;
import spleetwaise.transaction.model.ModelManager;
import spleetwaise.transaction.model.transaction.Amount;
import spleetwaise.transaction.model.transaction.Categories;
import spleetwaise.transaction.model.transaction.Date;
import spleetwaise.transaction.model.transaction.Description;
import spleetwaise.transaction.model.transaction.Transaction;

public class RemoveCategoryCommandTest {
    private static final Person testPerson = TypicalPersons.ALICE;
    private static final Amount testAmount = new Amount("1.23");
    private static final Description testDescription = new Description("description");
    private static final Date testDate = new Date("01012024");
    private static final Categories cat = new Categories("Food");
    private static final Transaction testTxn = new Transaction(testPerson, testAmount, testDescription, testDate, cat);
    private static final String category = "Food";
    @Test
    public void constructor_null_exceptionThrown() {
        assertThrows(NullPointerException.class, () -> new RemoveCategoryCommand(null, null));
    }

    @Test
    public void execute_invalidCategory_failure() {
        ModelManager modelManager = new ModelManager();
        String category = "";

        RemoveCategoryCommand cmd = new RemoveCategoryCommand(testTxn, category);

        assertThrows(CommandException.class, () -> cmd.execute(modelManager));
    }

    @Test
    public void execute_validCategory_success() {
        ModelManager modelManager = new ModelManager();

        RemoveCategoryCommand cmd = new RemoveCategoryCommand(testTxn, category);
        CommandResult cmdRes = assertDoesNotThrow(() -> cmd.execute(modelManager));

        String expectedString = String.format("Category removed from transaction: [%s] with [%s]",
                testTxn.getId(), category);
        assertEquals(expectedString, cmdRes.getFeedbackToUser());
    }

    @Test
    public void equals_diffTransaction_returnsFalse() {
        RemoveCategoryCommand cmd1 = new RemoveCategoryCommand(testTxn, category);
        Transaction testTxn2 = new Transaction(TypicalPersons.BOB, testAmount, testDescription, testDate);
        RemoveCategoryCommand cmd2 = new RemoveCategoryCommand(testTxn2, category);

        assertNotEquals(cmd1, cmd2);
    }

    @Test
    public void equals_null_returnsFalse() {
        RemoveCategoryCommand cmd1 = new RemoveCategoryCommand(testTxn, category);

        assertNotEquals(null, cmd1);
    }
}
