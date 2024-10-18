package spleetwaise.address.logic.commands;

import spleetwaise.address.model.AddressBook;
import spleetwaise.commons.CommonModel;

/**
 * Clears the address book.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "Address book has been cleared!";


    @Override
    public CommandResult execute() {
        CommonModel model = CommonModel.getInstance();
        model.setAddressBook(new AddressBook());
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
