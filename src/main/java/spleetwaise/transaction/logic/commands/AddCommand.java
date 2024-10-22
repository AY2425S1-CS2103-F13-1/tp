package spleetwaise.transaction.logic.commands;

import static java.util.Objects.requireNonNull;
import static spleetwaise.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static spleetwaise.transaction.logic.parser.CliSyntax.PREFIX_AMOUNT;
import static spleetwaise.transaction.logic.parser.CliSyntax.PREFIX_DATE;
import static spleetwaise.transaction.logic.parser.CliSyntax.PREFIX_DESCRIPTION;

import spleetwaise.address.commons.util.ToStringBuilder;
import spleetwaise.commons.logic.commands.Command;
import spleetwaise.commons.logic.commands.CommandResult;
import spleetwaise.commons.logic.commands.exceptions.CommandException;
import spleetwaise.commons.model.CommonModel;
import spleetwaise.transaction.model.transaction.Transaction;

/**
 * This class represents a command for adding transactions.
 */
public class AddCommand extends Command {

    /**
     * The word used to trigger this command in the input.
     */
    public static final String COMMAND_WORD = "addTxn";

    /**
     * The message that is displayed upon successful execution of this command.
     */
    public static final String MESSAGE_SUCCESS = "Transaction added: %s";

    public static final String MESSAGE_DUPLICATE_TXN = "Transaction already exists in the transaction book";

    /**
     * The message usage string that explains how to use this command.
     */
    public static final String MESSAGE_USAGE =
            COMMAND_WORD + ": Add a new transaction.\n" + "Parameters: " + PREFIX_PHONE + "CONTACT " + PREFIX_AMOUNT
                    + "AMOUNT " + PREFIX_DESCRIPTION + "DESCRIPTION " + "[" + PREFIX_DATE + "DATE ]\n" + "Example: "
                    + COMMAND_WORD + " " + PREFIX_PHONE + "88888888 " + PREFIX_AMOUNT + "10.00 " + PREFIX_DESCRIPTION
                    + "Paid John for lunch" + PREFIX_DATE + "23012024 ";


    private final Transaction transactionToAdd;

    /**
     * Creates an AddCommand to add the specified {@code Transaction}.
     *
     * @param transaction The transaction to be added.
     */
    public AddCommand(Transaction transaction) {
        requireNonNull(transaction);
        transactionToAdd = transaction;
    }

    /**
     * This method executes the add command.
     *
     * @return the result of the execution.
     */
    @Override
    public CommandResult execute() throws CommandException {
        CommonModel model = CommonModel.getInstance();

        if (model.hasTransaction(transactionToAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_TXN);
        }

        model.addTransaction(transactionToAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, transactionToAdd));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof AddCommand otherAddCommand)) {
            return false;
        }

        return transactionToAdd.equals(otherAddCommand.transactionToAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("txnToAdd", transactionToAdd).toString();
    }

}
