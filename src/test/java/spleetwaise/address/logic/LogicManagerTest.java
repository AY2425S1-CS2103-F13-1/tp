package spleetwaise.address.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import spleetwaise.address.logic.commands.AddCommand;
import spleetwaise.address.logic.commands.CommandResult;
import spleetwaise.address.logic.commands.CommandTestUtil;
import spleetwaise.address.logic.commands.ListCommand;
import spleetwaise.address.logic.commands.exceptions.CommandException;
import spleetwaise.address.logic.parser.exceptions.ParseException;
import spleetwaise.address.model.ReadOnlyAddressBook;
import spleetwaise.address.model.UserPrefs;
import spleetwaise.address.model.person.Person;
import spleetwaise.address.storage.JsonAddressBookStorage;
import spleetwaise.address.storage.JsonUserPrefsStorage;
import spleetwaise.address.storage.StorageManager;
import spleetwaise.address.testutil.Assert;
import spleetwaise.address.testutil.PersonBuilder;
import spleetwaise.address.testutil.TypicalPersons;

public class LogicManagerTest {

    private static final IOException DUMMY_IO_EXCEPTION = new IOException("dummy IO exception");
    private static final IOException DUMMY_AD_EXCEPTION = new AccessDeniedException("dummy access denied exception");

    @TempDir
    public Path temporaryFolder;

    private spleetwaise.address.model.Model addressBookModel = new spleetwaise.address.model.ModelManager();
    private spleetwaise.transaction.model.Model transactionModel = new spleetwaise.transaction.model.ModelManager();
    private Logic logic;

    @BeforeEach
    public void setUp() {
        JsonAddressBookStorage addressBookStorage =
            new JsonAddressBookStorage(temporaryFolder.resolve("addressBook.json"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(temporaryFolder.resolve("userPrefs.json"));
        StorageManager storage = new StorageManager(addressBookStorage, userPrefsStorage);
        logic = new LogicManager(addressBookModel, transactionModel, storage);
    }

    @Test
    public void execute_invalidCommandFormat_throwsParseException() {
        String invalidCommand = "uicfhmowqewca";
        assertParseException(invalidCommand, Messages.MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void execute_commandExecutionError_throwsCommandException() {
        String deleteCommand = "delete 9";
        assertCommandException(deleteCommand, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validCommand_success() throws Exception {
        // Test both addressBook and transaction command
        String listCommand = ListCommand.COMMAND_WORD;
        String addTxnCommand = spleetwaise.transaction.logic.commands.AddCommand.COMMAND_WORD;

        assertCommandSuccess(listCommand, ListCommand.MESSAGE_SUCCESS, addressBookModel, transactionModel);
        assertCommandSuccess(addTxnCommand, spleetwaise.transaction.logic.commands.AddCommand.MESSAGE_SUCCESS,
            addressBookModel, transactionModel);
    }

    @Test
    public void execute_storageThrowsIoException_throwsCommandException() {
        assertCommandFailureForExceptionFromStorage(DUMMY_IO_EXCEPTION, String.format(
            LogicManager.FILE_OPS_ERROR_FORMAT, DUMMY_IO_EXCEPTION.getMessage()));
    }

    @Test
    public void execute_storageThrowsAdException_throwsCommandException() {
        assertCommandFailureForExceptionFromStorage(DUMMY_AD_EXCEPTION, String.format(
            LogicManager.FILE_OPS_PERMISSION_ERROR_FORMAT, DUMMY_AD_EXCEPTION.getMessage()));
    }

    @Test
    public void getFilteredPersonList_modifyList_throwsUnsupportedOperationException() {
        Assert.assertThrows(UnsupportedOperationException.class, () -> logic.getFilteredPersonList().remove(0));
    }

    /**
     * Executes the command and confirms that - no exceptions are thrown <br> - the feedback message is equal to
     * {@code expectedMessage} <br> - the internal model manager state is the same as that in {@code expectedModel}
     * <br>
     *
     * @see #assertCommandFailure(String, Class, String, spleetwaise.address.model.Model,
     * spleetwaise.transaction.model.Model)
     */
    private void assertCommandSuccess(String inputCommand, String expectedMessage,
        spleetwaise.address.model.Model expectedAddressBookModel,
        spleetwaise.transaction.model.Model expectedTransactionModel) throws CommandException, ParseException {
        CommandResult result = logic.execute(inputCommand);
        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertEquals(expectedAddressBookModel, addressBookModel);
        assertEquals(expectedTransactionModel, transactionModel);
    }

    /**
     * Executes the command, confirms that a ParseException is thrown and that the result message is correct.
     *
     * @see #assertCommandFailure(String, Class, String, spleetwaise.address.model.Model,
     * spleetwaise.transaction.model.Model)
     */
    private void assertParseException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, ParseException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that a CommandException is thrown and that the result message is correct.
     *
     * @see #assertCommandFailure(String, Class, String, spleetwaise.address.model.Model,
     * spleetwaise.transaction.model.Model)
     */
    private void assertCommandException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, CommandException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that the exception is thrown and that the result message is correct.
     *
     * @see #assertCommandFailure(String, Class, String, spleetwaise.address.model.Model,
     * spleetwaise.transaction.model.Model)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
        String expectedMessage) {
        spleetwaise.address.model.Model expectedAddressBookModel =
            new spleetwaise.address.model.ModelManager(addressBookModel.getAddressBook(), new UserPrefs());

        spleetwaise.transaction.model.Model expectedTransactionModel = new spleetwaise.transaction.model.ModelManager();
        assertCommandFailure(inputCommand, expectedException, expectedMessage, expectedAddressBookModel,
            expectedTransactionModel);
    }

    /**
     * Executes the command and confirms that - the {@code expectedException} is thrown <br> - the resulting error
     * message is equal to {@code expectedMessage} <br> - the internal model manager state is the same as that in
     * {@code expectedModel} <br>
     *
     * @see #assertCommandSuccess(String, String, spleetwaise.address.model.Model, spleetwaise.transaction.model.Model)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
        String expectedMessage,
        spleetwaise.address.model.Model expectedAddressBookModel,
        spleetwaise.transaction.model.Model expectedTransactionModel) {
        Assert.assertThrows(expectedException, expectedMessage, () -> logic.execute(inputCommand));
        assertEquals(expectedAddressBookModel, addressBookModel);
        assertEquals(expectedTransactionModel, transactionModel);
    }

    /**
     * Tests the Logic component's handling of an {@code IOException} thrown by the Storage component.
     *
     * @param e               the exception to be thrown by the Storage component
     * @param expectedMessage the message expected inside exception thrown by the Logic component
     */
    private void assertCommandFailureForExceptionFromStorage(IOException e, String expectedMessage) {
        Path prefPath = temporaryFolder.resolve("ExceptionUserPrefs.json");

        // Inject LogicManager with an AddressBookStorage that throws the IOException e when saving
        JsonAddressBookStorage addressBookStorage = new JsonAddressBookStorage(prefPath) {
            @Override
            public void saveAddressBook(ReadOnlyAddressBook addressBook, Path filePath) throws IOException {
                throw e;
            }
        };

        JsonUserPrefsStorage userPrefsStorage =
            new JsonUserPrefsStorage(temporaryFolder.resolve("ExceptionUserPrefs.json"));
        StorageManager storage = new StorageManager(addressBookStorage, userPrefsStorage);

        logic = new LogicManager(addressBookModel, transactionModel, storage);

        // Triggers the saveAddressBook method by executing an add command
        String addCommand = AddCommand.COMMAND_WORD + CommandTestUtil.NAME_DESC_AMY + CommandTestUtil.PHONE_DESC_AMY
            + CommandTestUtil.EMAIL_DESC_AMY + CommandTestUtil.ADDRESS_DESC_AMY;
        Person expectedPerson = new PersonBuilder(TypicalPersons.AMY).withTags().build();

        spleetwaise.address.model.Model expectedAddressBookModel = new spleetwaise.address.model.ModelManager();
        expectedAddressBookModel.addPerson(expectedPerson);

        spleetwaise.transaction.model.Model expectedTransactionModel =
            new spleetwaise.transaction.model.ModelManager();

        assertCommandFailure(addCommand, CommandException.class, expectedMessage, expectedAddressBookModel,
            expectedTransactionModel);
    }
}
