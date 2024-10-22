package spleetwaise.transaction.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import spleetwaise.address.commons.core.index.Index;
import spleetwaise.commons.logic.commands.Command;
import spleetwaise.commons.logic.commands.CommandResult;
import spleetwaise.commons.logic.commands.exceptions.CommandException;
import spleetwaise.commons.model.CommonModel;
import spleetwaise.transaction.model.TransactionBookModel;
import spleetwaise.transaction.model.transaction.Transaction;
import spleetwaise.transaction.model.transaction.TransactionIdPredicate;

/**
 * Contains helper methods for testing transaction commands.
 */
public class CommandTestUtil {

    /**
     * Executes the given {@code command}, confirms that <br> - the returned {@link CommandResult} matches
     * {@code expectedCommandResult} <br> - the {@code actualModel} matches {@code expectedModel}
     */
    public static void assertCommandSuccess(
            Command command, TransactionBookModel actualModel, CommandResult expectedCommandResult,
            TransactionBookModel expectedModel
    ) {
        try {
            CommonModel.initialise(null, actualModel);
            CommandResult result = command.execute();
            assertEquals(expectedCommandResult, result);
            assertEquals(expectedModel, actualModel);
        } catch (CommandException ce) {
            throw new AssertionError("Execution of txn command should not fail.", ce);
        }
    }

    /**
     * Convenience wrapper to
     * {@link #assertCommandSuccess(Command, TransactionBookModel, CommandResult, TransactionBookModel)} that takes a
     * string {@code expectedMessage}.
     */
    public static void assertCommandSuccess(
            Command command, TransactionBookModel actualModel, String expectedMessage,
            TransactionBookModel expectedModel
    ) {
        CommandResult expectedCommandResult = new CommandResult(expectedMessage);
        assertCommandSuccess(command, actualModel, expectedCommandResult, expectedModel);
    }

    /**
     * Updates {@code model}'s filtered list to show only the transaction at the given {@code targetIndex} in the
     * {@code model}'s transaction book.
     */
    public static void showTransactionAtIndex(TransactionBookModel model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredTransactionList().size());

        Transaction txn = model.getFilteredTransactionList().get(targetIndex.getZeroBased());
        final String txnId = txn.getId();
        model.updateFilteredTransactionList(new TransactionIdPredicate(txnId));

        assertEquals(1, model.getFilteredTransactionList().size());
    }
}
