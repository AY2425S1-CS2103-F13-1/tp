package spleetwaise.transaction.logic.commands;

import static java.util.Objects.requireNonNull;

import spleetwaise.address.logic.commands.CommandResult;
import spleetwaise.transaction.model.TransactionBook;
import spleetwaise.transaction.model.TransactionBookModel;

/**
 * Represents a command to clear the transaction book.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clearTxn";
    public static final String MESSAGE_SUCCESS = "Transaction book has been cleared!";


    @Override
    public CommandResult execute(TransactionBookModel model) {
        requireNonNull(model);
        model.setTransactionBook(new TransactionBook());
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
