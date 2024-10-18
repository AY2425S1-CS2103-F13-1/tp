package systemtests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static spleetwaise.address.testutil.Assert.assertListMatching;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import guitests.guihandles.CommandBoxHandle;
import guitests.guihandles.MainWindowHandle;
import guitests.guihandles.MenuBarHandle;
import guitests.guihandles.PersonListPanelHandle;
import guitests.guihandles.ResultDisplayHandle;
import guitests.guihandles.StatusBarHandle;
import spleetwaise.address.logic.commands.ClearCommand;
import spleetwaise.address.logic.commands.FindCommand;
import spleetwaise.address.model.AddressBook;
import spleetwaise.address.model.AddressBookModel;
import spleetwaise.address.testutil.TestUtil;
import spleetwaise.address.testutil.TypicalPersons;
import spleetwaise.address.ui.CommandBox;

/**
 * This class serves as a base class for tests that require the use of the application's GUI and TestFx setup.
 */
public abstract class TestFxAppRunner {

    private static final Path SAVE_LOCATION_FOR_TESTING = TestUtil.getFilePathInSandboxFolder("sampleData.json");
    private static final Path PREF_LOCATION_FOR_TESTING = TestUtil.getFilePathInSandboxFolder("prefTesting.json");

    private static final List<String> COMMAND_BOX_DEFAULT_STYLE = Arrays.asList("text-input", "text-field");
    private static final List<String> COMMAND_BOX_ERROR_STYLE =
            Arrays.asList("text-input", "text-field", CommandBox.ERROR_STYLE_CLASS);

    private MainWindowHandle mainWindowHandle;
    private TestApp testApp;
    private TestFxAppSetup testAppSetup;

    /**
     * Initializes the TestFx environment before all tests.
     */
    @BeforeAll
    public static void setupBeforeClass() {
        TestFxAppSetup.initialize();
    }

    /**
     * Sets up the application, TestFx environment, and GUI handles before each test.
     */
    @BeforeEach
    public void setUp() {
        testAppSetup = new TestFxAppSetup();
        testApp = testAppSetup.setupApp(this::getInitialData, getDataFileLocation(),
                getPrefFileLocation()
        );
        mainWindowHandle = testAppSetup.setupMainWindowHandle();

        assertApplicationStartingStateIsCorrect();
    }

    /**
     * Stops the TestFx application after each test.
     */
    @AfterEach
    public void tearDown() {
        testAppSetup.stopApp();
    }

    /**
     * Returns the initial data for the address book used in tests.
     *
     * @return A typical address book with pre-defined persons.
     */
    protected AddressBook getInitialData() {
        return TypicalPersons.getTypicalAddressBook();
    }

    /**
     * Provides the file path where the test data will be stored.
     *
     * @return Path to the sample data JSON file.
     */
    protected Path getDataFileLocation() {
        return SAVE_LOCATION_FOR_TESTING;
    }

    /**
     * Provides the file path for storing application preferences during testing.
     *
     * @return Path to the preferences JSON file.
     */
    protected Path getPrefFileLocation() {
        return PREF_LOCATION_FOR_TESTING;
    }

    /**
     * Retrieves the main window handle for interacting with the GUI.
     *
     * @return The main window handle.
     */
    public MainWindowHandle getMainWindowHandle() {
        return mainWindowHandle;
    }

    /**
     * Retrieves the command box handle.
     *
     * @return The command box handle.
     */
    public CommandBoxHandle getCommandBox() {
        return mainWindowHandle.getCommandBox();
    }

    /**
     * Retrieves the person list panel handle.
     *
     * @return The person list panel handle.
     */
    public PersonListPanelHandle getPersonListPanel() {
        return mainWindowHandle.getPersonListPanel();
    }

    /**
     * Retrieves the menu bar handle.
     *
     * @return The menu bar handle.
     */
    public MenuBarHandle getMainMenu() {
        return mainWindowHandle.getMainMenu();
    }

    /**
     * Retrieves the status bar handle.
     *
     * @return The status bar handle.
     */
    public StatusBarHandle getStatusBarFooter() {
        return mainWindowHandle.getStatusBar();
    }

    /**
     * Retrieves the result display handle.
     *
     * @return The result display handle.
     */
    public ResultDisplayHandle getResultDisplay() {
        return mainWindowHandle.getResultDisplay();
    }

    /**
     * Executes a command by running it in the command box.
     *
     * @param command The command to execute.
     */
    protected void executeCommand(String command) {
        rememberStates();

        mainWindowHandle.getCommandBox().run(command);
    }

    /**
     * Filters the person list to show persons whose names match the given keyword.
     *
     * @param keyword The keyword to match names against.
     */
    protected void showPersonsWithName(String keyword) {
        executeCommand(FindCommand.COMMAND_WORD + " " + keyword);
        assertTrue(getModel().getFilteredPersonList().size() < getModel().getAddressBook().getPersonList().size());
    }

    /**
     * Deletes all persons in the address book.
     */
    protected void deleteAllPersons() {
        executeCommand(ClearCommand.COMMAND_WORD);
        assertEquals(0, getModel().getAddressBook().getPersonList().size());
    }

    /**
     * Verifies that the application displays the expected state.
     *
     * @param expectedCommandInput  The expected input in the command box.
     * @param expectedResultMessage The expected result message.
     * @param expectedModel         The expected model state.
     */
    protected void assertApplicationDisplaysExpected(
            String expectedCommandInput, String expectedResultMessage,
            AddressBookModel expectedModel
    ) {
        assertEquals(expectedCommandInput, getCommandBox().getInput());
        assertEquals(expectedResultMessage, getResultDisplay().getText());
        // TODO: this assertEquals compares instance id despite AddressBook has equals method
        // assertEquals(new AddressBook(expectedModel.getAddressBook()), testApp.readStorageAddressBook());
        assertListMatching(getPersonListPanel(), expectedModel.getFilteredPersonList());
    }

    /**
     * Stores the current state of the status bar and selected person card.
     */
    private void rememberStates() {
        StatusBarHandle handle = getStatusBarFooter();
        handle.rememberSaveLocation();
        getPersonListPanel().rememberSelectedPersonCard();
    }

    /**
     * Asserts that the command box is displaying the default style.
     */
    protected void assertCommandBoxShowsDefaultStyle() {
        assertEquals(COMMAND_BOX_DEFAULT_STYLE, getCommandBox().getStyleClass());
    }

    /**
     * Asserts that the command box is displaying the error style.
     */
    protected void assertCommandBoxShowsErrorStyle() {
        assertEquals(COMMAND_BOX_ERROR_STYLE, getCommandBox().getStyleClass());
    }

    /**
     * Asserts that the status bar state remains unchanged.
     */
    protected void assertStatusBarUnchanged() {
        StatusBarHandle handle = getStatusBarFooter();
        assertFalse(handle.isSaveLocationChanged());
    }

    /**
     * Verifies that the application's starting state is correct.
     */
    private void assertApplicationStartingStateIsCorrect() {
        assertEquals("", getCommandBox().getInput());
        assertEquals("", getResultDisplay().getText());
        assertListMatching(getPersonListPanel(), getModel().getFilteredPersonList());
        assertEquals(
                Paths.get(".").resolve(testApp.getStorageSaveLocation()).toString(),
                getStatusBarFooter().getSaveLocation()
        );
    }

    /**
     * Retrieves the model used by the application.
     *
     * @return The model instance.
     */
    protected AddressBookModel getModel() {
        return testApp.getAddrModel();
    }
}
