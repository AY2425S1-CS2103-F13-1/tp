package spleetwaise.transaction.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import spleetwaise.address.commons.exceptions.DataLoadingException;
import spleetwaise.address.model.AddressBookModel;
import spleetwaise.transaction.model.ReadOnlyTransactionBook;
import spleetwaise.transaction.model.TransactionBook;

/**
 * Represents a storage for {@link TransactionBook}.
 */
public interface TransactionBookStorage {

    /**
     * Returns the file path of the data file.
     */
    Path getTransactionBookFilePath();

    /**
     * Returns TransactionBook data as a {@link ReadOnlyTransactionBook}. Returns {@code Optional.empty()} if storage
     * file is not found.
     *
     * @throws DataLoadingException if loading the data from storage failed.
     */
    Optional<ReadOnlyTransactionBook> readTransactionBook(AddressBookModel addressBookModel)
            throws DataLoadingException;

    /**
     * @see #getTransactionBookFilePath()
     */
    Optional<ReadOnlyTransactionBook> readTransactionBook(Path filePath, AddressBookModel addressBookModel)
            throws DataLoadingException;

    /**
     * Saves the given {@link ReadOnlyTransactionBook} to the storage.
     *
     * @param transactionBook cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveTransactionBook(ReadOnlyTransactionBook transactionBook) throws IOException;

    /**
     * @see #saveTransactionBook(ReadOnlyTransactionBook)
     */
    void saveTransactionBook(ReadOnlyTransactionBook transactionBook, Path filePath) throws IOException;
}
