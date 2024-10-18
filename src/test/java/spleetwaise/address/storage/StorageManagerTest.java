package spleetwaise.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import spleetwaise.address.commons.core.GuiSettings;
import spleetwaise.address.model.AddressBook;
import spleetwaise.address.model.AddressBookModel;
import spleetwaise.address.model.ReadOnlyAddressBook;
import spleetwaise.address.model.UserPrefs;
import spleetwaise.address.model.person.Person;
import spleetwaise.address.testutil.TypicalPersons;
import spleetwaise.transaction.model.ReadOnlyTransactionBook;
import spleetwaise.transaction.model.TransactionBook;
import spleetwaise.transaction.storage.JsonTransactionBookStorage;
import spleetwaise.transaction.testutil.TypicalTransactions;

public class StorageManagerTest {

    private static final Person[] TEST_PEOPLE = { TypicalPersons.CARL, TypicalPersons.DANIEL, TypicalPersons.BENSON,
                                                  TypicalPersons.ALICE, TypicalPersons.BOB };
    @TempDir
    public Path testFolder;
    private AddressBookModel addressBookModel;
    private StorageManager storageManager;

    @BeforeEach
    public void setUp() {
        JsonAddressBookStorage addressBookStorage = new JsonAddressBookStorage(getTempFilePath("ab"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(getTempFilePath("prefs"));
        JsonTransactionBookStorage transactionBookStorage = new JsonTransactionBookStorage(
                getTempFilePath("tb"));
        storageManager = new StorageManager(addressBookStorage, userPrefsStorage, transactionBookStorage);

        // Set up StorageUtil for transaction storage
        addressBookModel = new spleetwaise.address.model.ModelManager();
        for (Person p : TEST_PEOPLE) {
            addressBookModel.addPerson(p);
        }
    }

    private Path getTempFilePath(String fileName) {
        return testFolder.resolve(fileName);
    }

    @Test
    public void prefsReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link JsonUserPrefsStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link JsonUserPrefsStorageTest} class.
         */
        UserPrefs original = new UserPrefs();
        original.setGuiSettings(new GuiSettings(300, 600, 4, 6));
        storageManager.saveUserPrefs(original);
        UserPrefs retrieved = storageManager.readUserPrefs().get();
        assertEquals(original, retrieved);
    }

    @Test
    public void addressBookReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link JsonAddressBookStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link JsonAddressBookStorageTest} class.
         */
        AddressBook original = TypicalPersons.getTypicalAddressBook();
        storageManager.saveAddressBook(original);
        ReadOnlyAddressBook retrieved = storageManager.readAddressBook().get();
        assertEquals(original, new AddressBook(retrieved));
    }

    @Test
    public void getAddressBookFilePath() {
        assertNotNull(storageManager.getAddressBookFilePath());
    }


    @Test
    public void transactionBookReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link JsonTransactionBookStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link JsonTransactionBookStorageTest} class.
         */
        TransactionBook original = TypicalTransactions.getTypicalTransactionBook();
        storageManager.saveTransactionBook(original);
        ReadOnlyTransactionBook retrieved = storageManager.readTransactionBook(addressBookModel).get();
        assertEquals(original, new TransactionBook(retrieved));
    }

    @Test
    public void getTransactionBookFilePath() {
        assertNotNull(storageManager.getTransactionBookFilePath());
    }
}
