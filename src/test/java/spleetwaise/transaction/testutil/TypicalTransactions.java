package spleetwaise.transaction.testutil;

import java.util.ArrayList;
import java.util.List;

import spleetwaise.address.testutil.TypicalPersons;
import spleetwaise.transaction.model.TransactionBook;
import spleetwaise.transaction.model.transaction.Transaction;

/**
 * A utility class containing a list of {@code Transaction} objects to be used in tests.
 */
public class TypicalTransactions {

    public static final Transaction SEANOWESME =
            new TransactionBuilder().withPerson(TypicalPersons.ALICE).withAmount("+9999999999.99")
                    .withDescription("Sean owes me a lot for a landed property in Sentosa").withDate("10102024")
                    .build();

    private TypicalTransactions() {
    } // prevents instantiation

    /**
     * Returns an {@code AddressBook} with all the typical persons.
     */
    public static TransactionBook getTypicalTransactionBook() {
        TransactionBook tb = new TransactionBook();
        for (Transaction transaction : getTypicalTransactions()) {
            tb.addTransaction(transaction);
        }
        return tb;
    }

    public static List<Transaction> getTypicalTransactions() {
        return new ArrayList<>(List.of(SEANOWESME)); // TODO: Add more default transactions for testing
    }
}
