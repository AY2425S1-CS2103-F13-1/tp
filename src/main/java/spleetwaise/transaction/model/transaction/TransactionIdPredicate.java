package spleetwaise.transaction.model.transaction;

import java.util.function.Predicate;

/**
 * Tests that a {@code Transaction}'s {@code Id} matches a transactionId given.
 */
public class TransactionIdPredicate implements Predicate<Transaction> {
    private final String transactionId;

    public TransactionIdPredicate(String transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public boolean test(Transaction transaction) {
        return transaction.getId().equals(transactionId);
    }
}

