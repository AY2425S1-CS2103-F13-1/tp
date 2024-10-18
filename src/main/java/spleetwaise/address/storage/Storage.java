package spleetwaise.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import spleetwaise.address.commons.exceptions.DataLoadingException;
import spleetwaise.address.model.AddressBookModel;
import spleetwaise.address.model.ReadOnlyAddressBook;
import spleetwaise.address.model.ReadOnlyUserPrefs;
import spleetwaise.address.model.UserPrefs;
import spleetwaise.transaction.model.ReadOnlyTransactionBook;
import spleetwaise.transaction.storage.TransactionBookStorage;

/**
 * API of the Storage component
 */
public interface Storage extends AddressBookStorage, UserPrefsStorage, TransactionBookStorage {

    @Override
    Optional<UserPrefs> readUserPrefs() throws DataLoadingException;

    @Override
    void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException;

    @Override
    Path getAddressBookFilePath();

    @Override
    Optional<ReadOnlyAddressBook> readAddressBook() throws DataLoadingException;

    @Override
    void saveAddressBook(ReadOnlyAddressBook addressBook) throws IOException;

    @Override
    Path getTransactionBookFilePath();

    @Override
    Optional<ReadOnlyTransactionBook> readTransactionBook(AddressBookModel addressBookModel)
            throws DataLoadingException;

    @Override
    void saveTransactionBook(ReadOnlyTransactionBook transactionBook) throws IOException;

}
