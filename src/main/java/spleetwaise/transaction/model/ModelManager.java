package spleetwaise.transaction.model;

import static java.util.Objects.requireNonNull;

import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import spleetwaise.address.commons.core.LogsCenter;
import spleetwaise.transaction.model.transaction.Transaction;

/**
 * Represents the in-memory model of the transaction data.
 */
public class ModelManager implements Model {

    private static final Logger logger = LogsCenter.getLogger(spleetwaise.address.model.ModelManager.class);

    /** {@code Predicate} that always evaluate to true */
    private static final Predicate<Transaction> PREDICATE_SHOW_ALL_TXNS = unused -> true;

    private final TransactionBook transactionBook;
    private final FilteredList<Transaction> filteredTransactions;


    /**
     * Initializes a ModelManager with the given transactionBook.
     */
    public ModelManager(TransactionBook transactionBook) {
        requireNonNull(transactionBook);

        logger.fine("Initializing Transaction Model...");

        this.transactionBook = transactionBook;
        filteredTransactions = new FilteredList<>(this.transactionBook.getTransactionList());
    }

    public ModelManager() {
        this(new TransactionBook());
    }

    @Override
    public void setTransactionBook(ReadOnlyTransactionBook replacementBook) {
        requireNonNull(replacementBook);
        transactionBook.setTransactions(replacementBook);
    }

    @Override
    public ReadOnlyTransactionBook getTransactionBook() {
        return transactionBook;
    }

    @Override
    public void addTransaction(Transaction transaction) {
        transactionBook.addTransaction(transaction);
        updateFilteredTransactionList(PREDICATE_SHOW_ALL_TXNS);
    }

    @Override
    public ObservableList<Transaction> getFilteredTransactionList() {
        return filteredTransactions;
    }

    @Override
    public void updateFilteredTransactionList(Predicate<Transaction> predicate) {
        filteredTransactions.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Model)) {
            return false;
        }

        ModelManager otherModelManager = (ModelManager) other;
        return transactionBook.equals(otherModelManager.transactionBook)
                && filteredTransactions.equals(otherModelManager.filteredTransactions);
    }

}
