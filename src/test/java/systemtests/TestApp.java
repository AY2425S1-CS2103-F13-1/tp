package systemtests;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

import javafx.stage.Screen;
import javafx.stage.Stage;
import spleetwaise.address.MainApp;
import spleetwaise.address.commons.core.Config;
import spleetwaise.address.commons.core.GuiSettings;
import spleetwaise.address.commons.exceptions.DataLoadingException;
import spleetwaise.address.model.AddressBook;
import spleetwaise.address.model.AddressBookModel;
import spleetwaise.address.model.ModelManager;
import spleetwaise.address.model.ReadOnlyAddressBook;
import spleetwaise.address.model.UserPrefs;
import spleetwaise.address.model.person.Person;
import spleetwaise.address.storage.JsonAddressBookStorage;
import spleetwaise.address.storage.UserPrefsStorage;

/**
 * TestApp extends MainApp to facilitate testing by setting up the application with configurable data and user
 * preferences. It allows initializing an address book with specific test data and provides access to various
 * application states for testing purposes.
 */
public class TestApp extends MainApp {

    /** Supplies the initial address book data for testing. */
    private Supplier<ReadOnlyAddressBook> initialDataSupplier;
    /** Path to the file where test data is saved. */
    private Path saveFileLocation;
    /** Path to the file storing user preferences for testing. */
    private Path prefFileLocation;

    /**
     * Creates a new TestApp instance with the specified initial data supplier, save file location, and preferences file
     * location.
     *
     * @param initialDataSupplier A supplier that provides the initial address book data.
     * @param saveFileLocation    Path to the test data file location.
     * @param prefFileLocation    Path to the user preferences file location.
     */
    public TestApp(Supplier<ReadOnlyAddressBook> initialDataSupplier, Path saveFileLocation, Path prefFileLocation) {
        super();
        this.initialDataSupplier = initialDataSupplier;
        this.saveFileLocation = saveFileLocation;
        this.prefFileLocation = prefFileLocation;

        JsonAddressBookStorage jsonAddressBookStorage;
        // If some initial local data has been provided, write those to the file
        if (initialDataSupplier.get() != null) {
            jsonAddressBookStorage = new JsonAddressBookStorage(saveFileLocation);
            try {
                jsonAddressBookStorage.saveAddressBook(initialDataSupplier.get());
            } catch (IOException ioe) {
                throw new AssertionError(ioe);
            }
        }
    }

    /**
     * Initializes the configuration with the given configuration file path and sets the user preferences file path for
     * testing purposes.
     *
     * @param configFilePath Path to the configuration file.
     * @return The initialized {@code Config} object.
     */
    @Override
    protected Config initConfig(Path configFilePath) {
        Config config = super.initConfig(configFilePath);
        config.setUserPrefsFilePath(prefFileLocation);
        return config;
    }

    /**
     * Initializes the user preferences with the given storage and sets up the GUI settings for testing by positioning
     * the window at the top-left corner of the primary screen.
     *
     * @param storage The storage to retrieve user preferences from.
     * @return The initialized {@code UserPrefs} object.
     */
    @Override
    protected UserPrefs initPrefs(UserPrefsStorage storage) {
        UserPrefs userPrefs = super.initPrefs(storage);
        double x = Screen.getPrimary().getVisualBounds().getMinX();
        double y = Screen.getPrimary().getVisualBounds().getMinY();
        userPrefs.setGuiSettings(new GuiSettings(995.0, 775.0, (int) x, (int) y));
        userPrefs.setAddressBookFilePath(saveFileLocation);
        return userPrefs;
    }

    /**
     * Reads and returns the address book from the storage.
     *
     * @return The {@code AddressBook} object read from the storage.
     * @throws AssertionError if the data is not in the AddressBook format.
     */
    public AddressBook readStorageAddressBook() {
        try {
            return new AddressBook(storage.readAddressBook().get());
        } catch (DataLoadingException dce) {
            throw new AssertionError("Data is not in the AddressBook format.", dce);
        }
    }

    /**
     * Returns the path to the address book file location used for testing.
     *
     * @return The {@code Path} to the test data file location.
     */
    public Path getStorageSaveLocation() {
        return storage.getAddressBookFilePath();
    }

    /**
     * Creates and returns a copy of the current model for testing, with the same address book and filtered person
     * list.
     *
     * @return A {@code Model} object representing the current state of the address book.
     */
    public AddressBookModel getAddrModel() {
        AddressBookModel copy = new ModelManager((addressBookModel.getAddressBook()), new UserPrefs());
        List<Person> toDisplay = addressBookModel.getFilteredPersonList();
        Optional<Predicate<Person>> predicate = toDisplay.stream()
                .map(person -> (Predicate<Person>) other -> other.equals(person)).reduce(Predicate::or);
        copy.updateFilteredPersonList(predicate.orElse(person -> false));
        return copy;
    }

    /**
     * Starts the UI of the application using the primary stage.
     *
     * @param primaryStage The primary stage for the UI.
     */
    @Override
    public void start(Stage primaryStage) {
        ui.start(primaryStage);
    }

    /**
     * The main entry point of the application for launching the test app.
     *
     * @param args The command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        launch(args);
    }

}
