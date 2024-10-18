package spleetwaise.transaction.storage;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import spleetwaise.address.commons.exceptions.IllegalValueException;
import spleetwaise.address.model.AddressBookModel;
import spleetwaise.transaction.model.ReadOnlyTransactionBook;
import spleetwaise.transaction.model.TransactionBook;
import spleetwaise.transaction.model.transaction.Transaction;
import spleetwaise.transaction.storage.adapters.JsonAdaptedTransaction;

/**
 * An Immutable AddressBook that is serializable to JSON format.
 */
@JsonRootName(value = "transactionbook")
class JsonSerializableTransactionBook {

    public static final String MESSAGE_DUPLICATE_TRANSACTIONS = "Transaction list contains duplicate transaction(s).";

    private final List<JsonAdaptedTransaction> transactions = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableAddressBook} with the given persons.
     */
    @JsonCreator
    public JsonSerializableTransactionBook(@JsonProperty("transactions") List<JsonAdaptedTransaction> txns) {
        this.transactions.addAll(txns);
    }

    /**
     * Converts a given {@code ReadOnlyAddressBook} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableAddressBook}.
     */
    public JsonSerializableTransactionBook(ReadOnlyTransactionBook source) {
        transactions.addAll(
                source.getTransactionList().stream().map(JsonAdaptedTransaction::new).collect(Collectors.toList()));
    }

    /**
     * Converts this address book into the model's {@code AddressBook} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public TransactionBook toModelType(AddressBookModel addressBookModel) throws IllegalValueException {
        requireNonNull(addressBookModel);
        TransactionBook transactionBook = new TransactionBook();
        for (JsonAdaptedTransaction jsonAdaptedTxn : transactions) {
            Transaction txn = jsonAdaptedTxn.toModelType(addressBookModel);
            if (transactionBook.containsTransaction(txn) || transactionBook.containsTransactionById(txn)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_TRANSACTIONS);
            }
            transactionBook.addTransaction(txn);
        }
        return transactionBook;
    }

}
