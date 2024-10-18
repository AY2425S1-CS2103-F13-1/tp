package spleetwaise.transaction.logic.commands;

import static spleetwaise.transaction.model.ModelManager.PREDICATE_SHOW_ALL_TXNS;

import spleetwaise.address.logic.commands.CommandResult;
import spleetwaise.commons.CommonModel;

/**
 * Lists all transactions in the transaction book to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "listTxn";

    public static final String MESSAGE_SUCCESS = "Listed all transactions";


    @Override
    public CommandResult execute() {
        CommonModel model = CommonModel.getInstance();
        model.updateFilteredTransactionList(PREDICATE_SHOW_ALL_TXNS);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
