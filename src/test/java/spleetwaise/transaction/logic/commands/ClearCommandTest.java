package spleetwaise.transaction.logic.commands;

import org.junit.jupiter.api.Test;

import spleetwaise.transaction.model.ModelManager;
import spleetwaise.transaction.model.TransactionBook;
import spleetwaise.transaction.model.TransactionBookModel;
import spleetwaise.transaction.testutil.TypicalTransactions;

public class ClearCommandTest {

    @Test
    public void execute_emptyTransactionBook_success() {
        TransactionBookModel model = new ModelManager();
        TransactionBookModel expectedModel = new ModelManager();

        CommandTestUtil.assertCommandSuccess(new ClearCommand(), model, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_nonEmptyTransactionBook_success() {
        TransactionBookModel model = new ModelManager(TypicalTransactions.getTypicalTransactionBook());
        TransactionBookModel expectedModel = new ModelManager(TypicalTransactions.getTypicalTransactionBook());
        expectedModel.setTransactionBook(new TransactionBook());

        CommandTestUtil.assertCommandSuccess(new ClearCommand(), model, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

}
