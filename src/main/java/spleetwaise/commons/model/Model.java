package spleetwaise.commons.model;

import spleetwaise.address.model.AddressBookModel;
import spleetwaise.transaction.model.TransactionBookModel;

/**
 * <p>Interface representing the model layer that encapsulates the business logic.</p>
 *
 * @see AddressBookModel
 * @see TransactionBookModel
 */
public interface Model extends AddressBookModel, TransactionBookModel {
    // Define methods that require data from both ab model and tb model here.
}
