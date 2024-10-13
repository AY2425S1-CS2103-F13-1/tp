package spleetwaise.transaction.storage;


import java.util.Optional;

import spleetwaise.address.model.person.Person;

/**
 * Contains utility methods used for the storage
 */
public class StorageUtil {
    private static spleetwaise.address.model.Model addressBookModel;

    public static void setAddressBookModel(spleetwaise.address.model.Model model) {
        addressBookModel = model;
    }

    public static Optional<Person> getPerson(String personId) {
        return addressBookModel.getPersonById(personId);
    }

}
