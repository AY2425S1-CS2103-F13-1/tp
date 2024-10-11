package spleetwaise.address.logic;

import static spleetwaise.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import spleetwaise.address.commons.core.GuiSettings;
import spleetwaise.address.commons.core.LogsCenter;
import spleetwaise.address.logic.commands.CommandResult;
import spleetwaise.address.logic.commands.exceptions.CommandException;
import spleetwaise.address.logic.parser.AddressBookParser;
import spleetwaise.address.logic.parser.exceptions.ParseException;
import spleetwaise.address.model.ReadOnlyAddressBook;
import spleetwaise.address.model.person.Person;
import spleetwaise.address.storage.Storage;
import spleetwaise.commons.exceptions.SpleetWaiseCommandException;
import spleetwaise.transaction.logic.parser.ParserUtil;
import spleetwaise.transaction.logic.parser.TransactionParser;

/**
 * The main LogicManager of the app.
 */
public class LogicManager implements Logic {

    public static final String FILE_OPS_ERROR_FORMAT = "Could not save data due to the following error: %s";

    public static final String FILE_OPS_PERMISSION_ERROR_FORMAT =
        "Could not save data to file %s due to insufficient permissions to write to the file or the folder.";

    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final spleetwaise.address.model.Model addressBookModel;
    private final spleetwaise.transaction.model.Model transactionModel;
    private final Storage storage;
    private final AddressBookParser addressBookParser;
    private final TransactionParser transactionParser;

    /**
     * Constructs a {@code LogicManager} with the given {@code Model} and {@code Storage}.
     */
    public LogicManager(spleetwaise.address.model.Model addressBookModel,
                        spleetwaise.transaction.model.Model transactionModel, Storage storage) {
        this.addressBookModel = addressBookModel;
        this.transactionModel = transactionModel;
        this.storage = storage;
        addressBookParser = new AddressBookParser();
        transactionParser = new TransactionParser();
        ParserUtil.setAddressBookModel(addressBookModel);
    }

    @Override
    public CommandResult execute(String commandText) throws ParseException, SpleetWaiseCommandException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");

        CommandResult commandResult;

        spleetwaise.address.logic.commands.Command addressBookCommand = addressBookParser.parseCommand(commandText);
        spleetwaise.transaction.logic.commands.Command transactionCommand = transactionParser.parseCommand(commandText);

        if (addressBookCommand != null) {
            return executeAddressBookCommand(addressBookCommand);
        } else if (transactionCommand != null) {
            return executeTransactionCommand(transactionCommand);
        }

        throw new ParseException(String.format(MESSAGE_UNKNOWN_COMMAND, commandText));
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBookModel.getAddressBook();
    }

    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return addressBookModel.getFilteredPersonList();
    }

    @Override
    public Path getAddressBookFilePath() {
        return addressBookModel.getAddressBookFilePath();
    }

    @Override
    public GuiSettings getGuiSettings() {
        return addressBookModel.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        addressBookModel.setGuiSettings(guiSettings);
    }

    private CommandResult executeAddressBookCommand(spleetwaise.address.logic.commands.Command addressBookCommand)
            throws SpleetWaiseCommandException {
        CommandResult commandResult = addressBookCommand.execute(addressBookModel);

        // Save AddressBook data
        try {
            storage.saveAddressBook(addressBookModel.getAddressBook());
        } catch (AccessDeniedException e) {
            throw new CommandException(String.format(FILE_OPS_PERMISSION_ERROR_FORMAT, e.getMessage()), e);
        } catch (IOException ioe) {
            throw new CommandException(String.format(FILE_OPS_ERROR_FORMAT, ioe.getMessage()), ioe);
        }

        return commandResult;
    }

    private CommandResult executeTransactionCommand(spleetwaise.transaction.logic.commands.Command transactionCommand)
            throws SpleetWaiseCommandException {
        CommandResult commandResult = transactionCommand.execute(transactionModel);

        // TODO: Save Transactions data

        return commandResult;
    }
}
