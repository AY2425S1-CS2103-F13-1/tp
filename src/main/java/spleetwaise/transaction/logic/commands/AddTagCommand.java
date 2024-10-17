package spleetwaise.transaction.logic.commands;

import static java.util.Objects.requireNonNull;

import spleetwaise.address.logic.commands.CommandResult;
import spleetwaise.transaction.logic.commands.exceptions.CommandException;
import spleetwaise.transaction.model.Model;
import spleetwaise.transaction.model.transaction.Transaction;

/**
 * This class represents a command for adding tags to transactions.
 */
public class AddTagCommand extends Command {

    /**
     * The word used to trigger this command in the input.
     */
    public static final String COMMAND_WORD = "addTag";

    public static final String MESSAGE_SUCCESS = "Tag added to transaction: %s";

    private final Transaction transactionToTag;
    private final String tag;

    /**
     * Creates an AddTagCommand to add tag to specified {@code Transaction}.
     *
     * @param transaction The transaction where the tag is added.
     * @param tag The tag to be added
     */
    public AddTagCommand (Transaction transaction, String tag) {
        requireNonNull(transaction);
        requireNonNull(tag);

        transactionToTag = transaction;
        this.tag = tag;
    }

    /**
     * This method executes the add tag command.
     *
     * @param model the model of the transactions.
     * @return the result of the execution.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        transactionToTag.addTag(tag);

        return new CommandResult(String.format(MESSAGE_SUCCESS, transactionToTag));
    }
}
