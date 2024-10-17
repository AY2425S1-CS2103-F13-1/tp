package spleetwaise.transaction.logic.commands;

import static java.util.Objects.requireNonNull;
import static spleetwaise.address.logic.parser.CliSyntax.PREFIX_CATEGORY;
import static spleetwaise.address.logic.parser.CliSyntax.PREFIX_TXN;

import spleetwaise.address.logic.commands.CommandResult;
import spleetwaise.transaction.logic.commands.exceptions.CommandException;
import spleetwaise.transaction.model.Model;
import spleetwaise.transaction.model.transaction.Transaction;

/**
 * This class represents a command for removing categories to transactions.
 */
public class RemoveCategoryCommand extends Command {

    /**
     * The word used to trigger this command in the input.
     */
    public static final String COMMAND_WORD = "removeTxnCat";

    public static final String MESSAGE_SUCCESS = "Category removed from transaction: [%s] with [%s]";

    public static final String MESSAGE_MISSING_CATEGORY = "No such category found in the specified transaction.";

    /**
     * The message usage string that explains how to use this command.
     */
    public static final String MESSAGE_USAGE =
            COMMAND_WORD + ": Remove a category from transaction.\n" + "Parameters: " + PREFIX_TXN
                    + "TRANSACTION_INDEX " + PREFIX_CATEGORY + "CATEGORY " + "Example: " + COMMAND_WORD + " "
                    + PREFIX_TXN + "1 " + PREFIX_CATEGORY + "Transport\n\n";

    private final Transaction transaction;
    private final String category;

    /**
     * Creates an RemoveCategoryCommand to remove tag to specified {@code Transaction}.
     *
     * @param transaction The transaction where the category is removed.
     * @param category The category to be removed
     */
    public RemoveCategoryCommand(Transaction transaction, String category) {
        requireNonNull(transaction);
        requireNonNull(category);

        this.transaction = transaction;
        this.category = category;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (!transaction.containsCategory(category)) {
            throw new CommandException(MESSAGE_MISSING_CATEGORY);
        }

        transaction.removeCategory(category);

        return new CommandResult(String.format(MESSAGE_SUCCESS, transaction.getId(), category));
    }
}
