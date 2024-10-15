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
            new TransactionBuilder().withPerson(TypicalPersons
                            .ALICE).withAmount("9999999999.99")
                    .withDescription("Sean owes me a lot for a landed property in Sentosa").withDate("10102024")
                    .withId("a1314e60-7a23-41cd-8960-d29d4edc841b")
                    .build();
    public static final Transaction BOBOWES =
            new TransactionBuilder().withPerson(TypicalPersons.BOB).withAmount("1000000.00")
                    .withDescription("Jenkins owed me for a loan of $1,000,000").withDate("11102025")
                    .withId("e2c93a3d-44f9-4b4b-8b5f-c5cd6f98e45a")
                    .build();

    public static final Transaction CARLBUYING =
            new TransactionBuilder().withPerson(TypicalPersons.CARL).withAmount("-5000.00")
                    .withDescription("Chen buying 5,000 shares of my company").withDate("01012024")
                    .withId("d4a7c1eb-0f3a-4419-b6e5-fdfda7c8e2cd")
                    .build();

    public static final Transaction DANIELDEBT =
            new TransactionBuilder().withPerson(TypicalPersons.DANIEL).withAmount("-20000.00")
                    .withDescription("Karen having debt of $20,000 to me").withDate("01022024")
                    .withId("a6bb3c5d-1e14-46ed-8f4b-7ddfc0dce9ff")
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
        return new ArrayList<>(List.of(SEANOWESME, BOBOWES)); // TODO: Add more default transactions for testing
    }
}
