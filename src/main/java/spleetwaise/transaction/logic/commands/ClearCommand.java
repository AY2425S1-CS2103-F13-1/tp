package spleetwaise.transaction.logic.commands;

import spleetwaise.commons.logic.commands.Command;
import spleetwaise.commons.logic.commands.CommandResult;
import spleetwaise.commons.model.CommonModel;
import spleetwaise.transaction.model.TransactionBook;

/**
 * Represents a command to clear the transaction book.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clearTxn";
    public static final String MESSAGE_SUCCESS = "Transaction book has been cleared!";


    @Override
    public CommandResult execute() {
        CommonModel model = CommonModel.getInstance();
        model.setTransactionBook(new TransactionBook());
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
