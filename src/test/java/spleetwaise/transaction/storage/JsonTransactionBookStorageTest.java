package spleetwaise.transaction.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import spleetwaise.address.commons.exceptions.DataLoadingException;
import spleetwaise.address.model.AddressBookModel;
import spleetwaise.address.model.person.Person;
import spleetwaise.address.testutil.Assert;
import spleetwaise.address.testutil.TypicalPersons;
import spleetwaise.transaction.model.ReadOnlyTransactionBook;
import spleetwaise.transaction.model.TransactionBook;
import spleetwaise.transaction.testutil.TypicalTransactions;

public class JsonTransactionBookStorageTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonTransactionBookStorageTest");

    private static final Person[] TEST_PEOPLE = { TypicalPersons.CARL, TypicalPersons.DANIEL, TypicalPersons.BENSON,
                                                  TypicalPersons.ALICE, TypicalPersons.BOB };
    @TempDir
    public Path testFolder;
    private AddressBookModel addressBookModel;

    @BeforeEach
    public void setUp() {
        addressBookModel = new spleetwaise.address.model.ModelManager();
        for (Person p : TEST_PEOPLE) {
            addressBookModel.addPerson(p);
        }
    }

    @Test
    public void readTransactionBook_nullFilePath_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> readTxnBook(null));
    }


    private java.util.Optional<ReadOnlyTransactionBook> readTxnBook(String filePath) throws Exception {
        return new JsonTransactionBookStorage(Paths.get(filePath)).readTransactionBook(
                addToTestDataPathIfNotNull(filePath), addressBookModel);
    }

    private Path addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null ? TEST_DATA_FOLDER.resolve(prefsFileInTestDataFolder) : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readTxnBook("NonExistentFile.json").isPresent());
    }

    @Test
    public void read_notJsonFormat_exceptionThrown() {
        Assert.assertThrows(DataLoadingException.class, () -> readTxnBook("notJsonFormatTransactionBook.json"));
    }

    @Test
    public void readTxnBook_invalidPerson_throwDataLoadingException() {
        Assert.assertThrows(DataLoadingException.class,
                "spleetwaise.address.commons.exceptions.IllegalValueException: Person with id "
                        + "420yoloswag not found!", () -> readTxnBook("invalidPersonTransactionBook" + ".json")
        );
    }

    @Test
    public void readTxnBook_missingAmount_throwDataLoadingException() {
        Assert.assertThrows(
                DataLoadingException.class,
                "spleetwaise.address.commons.exceptions.IllegalValueException:"
                        + " Transaction's Amount field is missing!", (
                ) -> readTxnBook("missingAmountTransactionBook" + ".json")
        );
    }

    @Test
    public void readAndSaveTxnBook_allInOrder_success() throws Exception {
        Path filePath = testFolder.resolve("TempTxnBook.json");
        TransactionBook original = TypicalTransactions.getTypicalTransactionBook();
        JsonTransactionBookStorage jsonTxnBookStorage = new JsonTransactionBookStorage(filePath);

        // Save in new file and read back
        jsonTxnBookStorage.saveTransactionBook(original, filePath);
        ReadOnlyTransactionBook readBack = jsonTxnBookStorage.readTransactionBook(filePath, addressBookModel).get();
        assertEquals(original, new TransactionBook(readBack));

        // Modify data, overwrite exiting file, and read back
        original.addTransaction(TypicalTransactions.CARLBUYING);
        original.removeTransaction(TypicalTransactions.SEANOWESME);
        jsonTxnBookStorage.saveTransactionBook(original, filePath);
        readBack = jsonTxnBookStorage.readTransactionBook(filePath, addressBookModel).get();
        assertEquals(original, new TransactionBook(readBack));

        // Save and read without specifying file path
        original.addTransaction(TypicalTransactions.DANIELDEBT);
        jsonTxnBookStorage.saveTransactionBook(original); // file path not specified
        readBack = jsonTxnBookStorage.readTransactionBook(addressBookModel).get(); // file path not specified
        assertEquals(original, new TransactionBook(readBack));

    }

    /**
     * Saves {@code txnBook} at the specified {@code filePath}.
     */
    private void saveTxnBook(ReadOnlyTransactionBook txnBook, String filePath) {
        try {
            new JsonTransactionBookStorage(Paths.get(filePath)).saveTransactionBook(
                    txnBook, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveTxnBook_nullTxnBook_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> saveTxnBook(null, "SomeFile.json"));
    }

    @Test
    public void saveTxnBook_nullFilePath_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> saveTxnBook(new TransactionBook(), null));
    }

}
