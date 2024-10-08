package spleetwaise.transaction.ui;

import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import spleetwaise.address.commons.core.LogsCenter;
import spleetwaise.address.ui.UiPart;
import spleetwaise.transaction.model.transaction.Transaction;

import java.util.logging.Logger;

/**
 * Panel containing the list of transactions.
 */
public class TransactionListPanel extends UiPart<Region> {
    private static final String FXML = "TransactionListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(TransactionListPanel.class);

    @javafx.fxml.FXML
    private ListView<Transaction> transactionListView;

    /**
     * Creates a {@code TransactionListPanel} with the given {@code ObservableList}.
     */
    public TransactionListPanel(ObservableList<Transaction> personList) {
        super(FXML);
        transactionListView.setItems(personList);
        transactionListView.setCellFactory(listView -> new TransactionListPanel.TransactionListViewCell());
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Transaction} using a {@code TransactionCard}.
     */
    class TransactionListViewCell extends ListCell<Transaction> {
        @Override
        protected void updateItem(Transaction transaction, boolean empty) {
            super.updateItem(transaction, empty);

            if (empty || transaction == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new TransactionCard(transaction, getIndex() + 1).getRoot());
            }
        }
    }
}
