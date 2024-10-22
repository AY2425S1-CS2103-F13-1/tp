package spleetwaise.address.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import spleetwaise.address.commons.core.GuiSettings;
import spleetwaise.address.logic.commands.AddCommand;
import spleetwaise.address.logic.commands.CommandTestUtil;
import spleetwaise.address.logic.commands.ListCommand;
import spleetwaise.address.logic.parser.exceptions.ParseException;
import spleetwaise.address.model.AddressBookModel;
import spleetwaise.address.model.ReadOnlyAddressBook;
import spleetwaise.address.model.UserPrefs;
import spleetwaise.address.model.person.Person;
import spleetwaise.address.storage.JsonAddressBookStorage;
import spleetwaise.address.storage.JsonUserPrefsStorage;
import spleetwaise.address.storage.StorageManager;
import spleetwaise.address.testutil.Assert;
import spleetwaise.address.testutil.PersonBuilder;
import spleetwaise.address.testutil.TypicalPersons;
import spleetwaise.commons.IdUtil;
import spleetwaise.commons.exceptions.SpleetWaiseCommandException;
import spleetwaise.commons.logic.commands.CommandResult;
import spleetwaise.commons.logic.commands.exceptions.CommandException;
import spleetwaise.commons.model.CommonModel;
import spleetwaise.transaction.model.ReadOnlyTransactionBook;
import spleetwaise.transaction.model.TransactionBookModel;
import spleetwaise.transaction.storage.JsonTransactionBookStorage;

public class LogicManagerTest {

    private static final IOException DUMMY_IO_EXCEPTION = new IOException("dummy IO exception");
    private static final IOException DUMMY_AD_EXCEPTION = new AccessDeniedException("dummy access denied exception");

    @TempDir
    public Path temporaryFolder;
    private AddressBookModel addressBookModel = new spleetwaise.address.model.ModelManager();
    private TransactionBookModel transactionModel = new spleetwaise.transaction.model.ModelManager();
    private Logic logic;

    @BeforeEach
    public void setUp() {
        JsonAddressBookStorage addressBookStorage =
                new JsonAddressBookStorage(temporaryFolder.resolve("addressBook.json"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(temporaryFolder.resolve("userPrefs.json"));
        JsonTransactionBookStorage transactionBookStorage =
                new JsonTransactionBookStorage(temporaryFolder.resolve("transactionBook.json"));
        StorageManager storage = new StorageManager(addressBookStorage, userPrefsStorage, transactionBookStorage);
        CommonModel.initialise(addressBookModel, transactionModel);
        logic = new LogicManager(storage);
    }

    @Test
    public void getAddressBook() {
        assertEquals(addressBookModel.getAddressBook(), logic.getAddressBook());
    }

    @Test
    public void getAddressBookFilePath() {
        assertEquals(addressBookModel.getAddressBookFilePath(), logic.getAddressBookFilePath());
    }

    @Test
    public void getGuiSettings() {
        assertEquals(addressBookModel.getGuiSettings(), logic.getGuiSettings());
    }

    @Test
    public void setGuiSettings() {
        GuiSettings settings = new GuiSettings();
        logic.setGuiSettings(settings);
        assertEquals(addressBookModel.getGuiSettings(), settings);
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

        addressBookModel.addPerson(TypicalPersons.ALICE);
        CommonModel.initialise(addressBookModel, transactionModel);

        // TODO: add a TransactionUtil class to handle generation of commands and expected messages
        String addTxnCommand = "addTxn p/94351253 amt/12.3 desc/Test date/01012024";
        String expectedMessageSuccess = String.format(
                spleetwaise.transaction.logic.commands.AddCommand.MESSAGE_SUCCESS,
                "[test-uuid] Alice Pauline(94351253): Test on 01/01/2024 for $12.30"
        );

        assertCommandSuccess(listCommand, ListCommand.MESSAGE_SUCCESS, addressBookModel, transactionModel);
        IdUtil.setDeterminate(true);
        assertCommandSuccess(addTxnCommand, expectedMessageSuccess, addressBookModel, transactionModel);
        IdUtil.setDeterminate(false);
    }

    @Test
    public void execute_storageThrowsIoException_throwsCommandException() {
        assertCommandFailureForExceptionFromStorage(
                DUMMY_IO_EXCEPTION,
                String.format(
                        LogicManager.FILE_OPS_ERROR_FORMAT,
                        DUMMY_IO_EXCEPTION.getMessage()
                )
        );
    }

    @Test
    public void execute_storageThrowsAdException_throwsCommandException() {
        assertCommandFailureForExceptionFromStorage(
                DUMMY_AD_EXCEPTION,
                String.format(
                        LogicManager.FILE_OPS_PERMISSION_ERROR_FORMAT,
                        DUMMY_AD_EXCEPTION.getMessage()
                )
        );
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
     * @see #assertCommandFailure(String, Class, String, AddressBookModel, TransactionBookModel)
     */
    private void assertCommandSuccess(
            String inputCommand, String expectedMessage,
            AddressBookModel expectedAddressBookModel,
            TransactionBookModel expectedTransactionModel
    )
            throws SpleetWaiseCommandException, ParseException {
        CommandResult result = logic.execute(inputCommand);
        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertEquals(expectedAddressBookModel, addressBookModel);
        assertEquals(expectedTransactionModel, transactionModel);
    }

    /**
     * Executes the command, confirms that a ParseException is thrown and that the result message is correct.
     *
     * @see #assertCommandFailure(String, Class, String, AddressBookModel, TransactionBookModel)
     */
    private void assertParseException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, ParseException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that a CommandException is thrown and that the result message is correct.
     *
     * @see #assertCommandFailure(String, Class, String, AddressBookModel, TransactionBookModel)
     */
    private void assertCommandException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, CommandException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that the exception is thrown and that the result message is correct.
     *
     * @see #assertCommandFailure(String, Class, String, AddressBookModel, TransactionBookModel)
     */
    private void assertCommandFailure(
            String inputCommand, Class<? extends Throwable> expectedException,
            String expectedMessage
    ) {
        AddressBookModel expectedAddressBookModel =
                new spleetwaise.address.model.ModelManager(addressBookModel.getAddressBook(), new UserPrefs());

        TransactionBookModel expectedTransactionModel = new spleetwaise.transaction.model.ModelManager();
        assertCommandFailure(inputCommand, expectedException, expectedMessage, expectedAddressBookModel,
                expectedTransactionModel
        );
    }

    /**
     * Executes the command and confirms that - the {@code expectedException} is thrown <br> - the resulting error
     * message is equal to {@code expectedMessage} <br> - the internal model manager state is the same as that in
     * {@code expectedModel} <br>
     *
     * @see #assertCommandSuccess(String, String, AddressBookModel, TransactionBookModel)
     */
    private void assertCommandFailure(
            String inputCommand, Class<? extends Throwable> expectedException,
            String expectedMessage, AddressBookModel expectedAddressBookModel,
            TransactionBookModel expectedTransactionModel
    ) {
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

        // Inject LogicManager with AddressBookStorage and TransactionBookStorage that throws the IOException
        // when saving
        JsonAddressBookStorage addressBookStorage = new JsonAddressBookStorage(prefPath) {
            @Override
            public void saveAddressBook(ReadOnlyAddressBook addressBook, Path filePath) throws IOException {
                throw e;
            }
        };

        JsonTransactionBookStorage transactionBookStorage = new JsonTransactionBookStorage(prefPath) {
            @Override
            public void saveTransactionBook(ReadOnlyTransactionBook transactionBook, Path filePath) throws IOException {
                throw e;
            }
        };

        JsonUserPrefsStorage userPrefsStorage =
                new JsonUserPrefsStorage(temporaryFolder.resolve("ExceptionUserPrefs.json"));
        StorageManager storage = new StorageManager(addressBookStorage, userPrefsStorage, transactionBookStorage);

        CommonModel.initialise(addressBookModel, transactionModel);
        logic = new LogicManager(storage);

        // Triggers the saveAddressBook method by executing an add command
        String abAddCommand = AddCommand.COMMAND_WORD + CommandTestUtil.NAME_DESC_AMY + CommandTestUtil.PHONE_DESC_AMY
                + CommandTestUtil.EMAIL_DESC_AMY + CommandTestUtil.ADDRESS_DESC_AMY + CommandTestUtil.REMARK_DESC_AMY;
        // Triggers the saveTransactionBook method by executing a list command (simpler)
        String tbListCommand = spleetwaise.transaction.logic.commands.ListCommand.COMMAND_WORD;

        IdUtil.setDeterminate(true);
        Person expectedPerson = new PersonBuilder(TypicalPersons.AMY).withId(IdUtil.getId()).withTags().build();

        AddressBookModel expectedAddressBookModel = new spleetwaise.address.model.ModelManager();
        expectedAddressBookModel.addPerson(expectedPerson);

        TransactionBookModel expectedTransactionModel = new spleetwaise.transaction.model.ModelManager();


        assertCommandFailure(abAddCommand, CommandException.class, expectedMessage, expectedAddressBookModel,
                expectedTransactionModel
        );
        assertCommandFailure(tbListCommand, CommandException.class, expectedMessage, expectedAddressBookModel,
                expectedTransactionModel
        );
        IdUtil.setDeterminate(false);


    }
}
