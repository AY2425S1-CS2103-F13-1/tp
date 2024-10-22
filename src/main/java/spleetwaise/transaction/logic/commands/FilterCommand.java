package spleetwaise.transaction.logic.commands;

import static spleetwaise.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static spleetwaise.transaction.logic.parser.CliSyntax.PREFIX_AMOUNT;
import static spleetwaise.transaction.logic.parser.CliSyntax.PREFIX_DATE;
import static spleetwaise.transaction.logic.parser.CliSyntax.PREFIX_DESCRIPTION;

import java.util.function.Predicate;

import spleetwaise.address.commons.util.ToStringBuilder;
import spleetwaise.address.model.person.Person;
import spleetwaise.commons.logic.commands.Command;
import spleetwaise.commons.logic.commands.CommandResult;
import spleetwaise.commons.logic.commands.exceptions.CommandException;
import spleetwaise.commons.model.CommonModel;
import spleetwaise.transaction.model.transaction.Amount;
import spleetwaise.transaction.model.transaction.Date;
import spleetwaise.transaction.model.transaction.Description;
import spleetwaise.transaction.model.transaction.Transaction;

/**
 * This class represents a command for filtering transactions.
 */
public class FilterCommand extends Command {

    /**
     * The word used to trigger this command in the input.
     */
    public static final String COMMAND_WORD = "filterTxn";

    /**
     * The message that is displayed upon successful execution of this command.
     */
    public static final String MESSAGE_SUCCESS = "Transaction book filtered.";

    /**
     * The message usage string that explains how to use this command.
     */
    public static final String MESSAGE_USAGE =
            COMMAND_WORD + ": Filter the transaction book.\n" + "Parameters: "
                    + "[" + PREFIX_PHONE + "CONTACT] " + "[" + PREFIX_AMOUNT + "AMOUNT] " + "[" + PREFIX_DESCRIPTION
                    + "DESCRIPTION] " + "[" + PREFIX_DATE + "DATE ]\n"
                    + "At least one of the above filtering criteria is needed\n"
                    + "Example: " + COMMAND_WORD + " " + PREFIX_PHONE + "88888888";

    private final Person contact;
    private final Amount amount;
    private final Description description;
    private final Date date;

    /**
     * Creates an AddCommand to add the specified {@code Transaction}.
     *
     * @param contact     The person to filter the transaction by.
     * @param amount      The amount to filter the transaction by.
     * @param description The description to filter the transaction by.
     * @param date        The date to filter the transaction by.
     */
    public FilterCommand(Person contact, Amount amount, Description description, Date date) {
        if (contact == null && amount == null && description == null && date == null) {
            throw new NullPointerException();
        }

        this.contact = contact;
        this.amount = amount;
        this.description = description;
        this.date = date;
    }

    /**
     * This method executes the filter command.
     *
     * @return the result of the execution.
     */
    @Override
    public CommandResult execute() throws CommandException {
        CommonModel model = CommonModel.getInstance();

        Predicate<Transaction> filterPredicate = (txn) -> {
            if (contact != null && !txn.getPerson().equals(contact)) {
                return false;
            }

            if (amount != null && !txn.getAmount().equals(amount)) {
                return false;
            }

            if (description != null
                    && !txn.getDescription().toString().toLowerCase()
                    .contains(description.toString().toLowerCase())) {
                return false;
            }

            if (date != null && !txn.getDate().equals(date)) {
                return false;
            }

            return true;
        };

        model.updateFilteredTransactionList(filterPredicate);
        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof FilterCommand)) {
            return false;
        }

        FilterCommand otherFilterCommand = (FilterCommand) other;
        return (contact == null ? otherFilterCommand.contact == null : contact.equals(otherFilterCommand.contact))
                && (amount == null ? otherFilterCommand.amount == null : amount.equals(otherFilterCommand.amount))
                && (description == null ? otherFilterCommand.description == null
                : description.equals(otherFilterCommand.description))
                && (date == null ? otherFilterCommand.date == null : date.equals(otherFilterCommand.date));
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("contact", contact)
                .add("amount", amount)
                .add("description", description)
                .add("date", date)
                .toString();
    }
}
