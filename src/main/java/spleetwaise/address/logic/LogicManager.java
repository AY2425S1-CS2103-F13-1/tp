package spleetwaise.address.logic;

import static spleetwaise.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import spleetwaise.address.commons.core.GuiSettings;
import spleetwaise.address.commons.core.LogsCenter;
import spleetwaise.address.logic.parser.AddressBookParser;
import spleetwaise.address.logic.parser.exceptions.ParseException;
import spleetwaise.address.model.ReadOnlyAddressBook;
import spleetwaise.address.model.person.Person;
import spleetwaise.address.storage.Storage;
import spleetwaise.commons.exceptions.SpleetWaiseCommandException;
import spleetwaise.commons.logic.commands.Command;
import spleetwaise.commons.logic.commands.CommandResult;
import spleetwaise.commons.logic.commands.exceptions.CommandException;
import spleetwaise.commons.model.CommonModel;
import spleetwaise.transaction.logic.parser.TransactionParser;
import spleetwaise.transaction.model.transaction.Transaction;

/**
 * The main LogicManager of the app.
 */
public class LogicManager implements Logic {

    public static final String FILE_OPS_ERROR_FORMAT = "Could not save data due to the following error: %s";

    public static final String FILE_OPS_PERMISSION_ERROR_FORMAT =
            "Could not save data to file %s due to insufficient permissions to write to the file or the folder.";

    private final Logger logger = LogsCenter.getLogger(LogicManager.class);
    private final Storage storage;
    private final AddressBookParser addressBookParser;
    private final TransactionParser transactionParser;

    /**
     * Constructs a {@code LogicManager} with the given {@code Model} and {@code Storage}.
     */
    public LogicManager(Storage storage) {
        this.storage = storage;
        this.addressBookParser = new AddressBookParser();
        this.transactionParser = new TransactionParser();
    }

    @Override
    public CommandResult execute(String commandText) throws ParseException, SpleetWaiseCommandException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");

        // Parse command
        Command addressBookCommand = addressBookParser.parseCommand(commandText);
        Command transactionCommand = transactionParser.parseCommand(commandText);

        // Execute commands
        if (addressBookCommand != null) {
            return executeAddressBookCommand(addressBookCommand);
        } else if (transactionCommand != null) {
            return executeTransactionCommand(transactionCommand);
        }

        // Update storage

        throw new ParseException(String.format(MESSAGE_UNKNOWN_COMMAND, commandText));
    }

    /**
     * Checks if the command is related to transactions.
     *
     * @param commandText The command to check.
     * @return True if the command is a transaction command, false otherwise.
     */
    public boolean isTransactionCommand(String commandText) {
        try {
            Command transactionCommand = transactionParser.parseCommand(
                    commandText);
            return transactionCommand != null;
        } catch (ParseException pe) {
            return false;
        }
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return CommonModel.getInstance().getAddressBook();
    }

    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return CommonModel.getInstance().getFilteredPersonList();
    }

    @Override
    public Path getAddressBookFilePath() {
        return CommonModel.getInstance().getAddressBookFilePath();
    }

    @Override
    public GuiSettings getGuiSettings() {
        return CommonModel.getInstance().getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        CommonModel.getInstance().setGuiSettings(guiSettings);
    }

    @Override
    public ObservableList<Transaction> getFilteredTransactionList() {
        return CommonModel.getInstance().getFilteredTransactionList();
    }

    // TODO: We need to write both storages because AB commands might result in changes to TB data
    private CommandResult executeAddressBookCommand(Command addressBookCommand)
            throws SpleetWaiseCommandException {
        CommandResult commandResult = addressBookCommand.execute();

        // Save AddressBook data
        try {
            storage.saveAddressBook(CommonModel.getInstance().getAddressBook());
        } catch (AccessDeniedException e) {
            throw new CommandException(String.format(FILE_OPS_PERMISSION_ERROR_FORMAT, e.getMessage()), e);
        } catch (IOException ioe) {
            throw new CommandException(String.format(FILE_OPS_ERROR_FORMAT, ioe.getMessage()), ioe);
        }

        return commandResult;
    }

    private CommandResult executeTransactionCommand(Command transactionCommand)
            throws SpleetWaiseCommandException {
        CommandResult commandResult = transactionCommand.execute();

        // Save TransactionBook data
        try {
            storage.saveTransactionBook(CommonModel.getInstance().getTransactionBook());
        } catch (AccessDeniedException e) {
            throw new CommandException(String.format(FILE_OPS_PERMISSION_ERROR_FORMAT, e.getMessage()), e);
        } catch (IOException ioe) {
            throw new CommandException(String.format(FILE_OPS_ERROR_FORMAT, ioe.getMessage()), ioe);
        }

        return commandResult;
    }
}
