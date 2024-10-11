package spleetwaise.transaction.testutil;

import spleetwaise.transaction.logic.commands.AddCommand;

/**
 * A utility class for Transaction.
 */
public class TransactionUtil {

    /**
     * Retrieves a test add transaction command.
     * @return A test add transaction command.
     */
    public static String getAddCommand() {
        // Using TypicalPerson.ALICE contact details
        return AddCommand.COMMAND_WORD + " p/94351253 amt/+12.3 desc/Test";
    }
}
