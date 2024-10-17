package spleetwaise.transaction.logic.commands;

import static java.util.Objects.requireNonNull;

import spleetwaise.address.logic.commands.CommandResult;
import spleetwaise.transaction.logic.commands.exceptions.CommandException;
import spleetwaise.transaction.model.Model;
import spleetwaise.transaction.model.transaction.Transaction;

/**
 * This class represents a command for removing tags to transactions.
 */
public class RemoveTagCommand extends Command {
    /**
     * The word used to trigger this command in the input.
     */
    public static final String COMMAND_WORD = "removeTag";

    public static final String MESSAGE_SUCCESS = "Tag removed from transaction: %s";

    private final Transaction transactionToUntag;
    private final String tag;

    /**
     * Creates an RemoveTagCommand to remove tag to specified {@code Transaction}.
     *
     * @param transaction The transaction where the tag is removed.
     * @param tag The tag to be removed
     */
    public RemoveTagCommand (Transaction transaction, String tag) {
        requireNonNull(transaction);
        requireNonNull(tag);

        transactionToUntag = transaction;
        this.tag = tag;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        transactionToUntag.removeTag(tag);

        return new CommandResult(String.format(MESSAGE_SUCCESS, transactionToUntag));
    }
}
