package spleetwaise.commons.model;

import static java.util.Objects.requireNonNull;

import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import spleetwaise.address.commons.core.GuiSettings;
import spleetwaise.address.model.AddressBookModel;
import spleetwaise.address.model.ReadOnlyAddressBook;
import spleetwaise.address.model.ReadOnlyUserPrefs;
import spleetwaise.address.model.person.Person;
import spleetwaise.transaction.model.ReadOnlyTransactionBook;
import spleetwaise.transaction.model.TransactionBookModel;
import spleetwaise.transaction.model.transaction.Transaction;

/**
 * The CommonModel class represents the common model that will be used by multiple parts of the application.
 */
public class CommonModel implements Model {
    // Singleton instance
    private static CommonModel model = null;

    private AddressBookModel addressBookModel;
    private TransactionBookModel transactionBookModel;

    private CommonModel(AddressBookModel abModel, TransactionBookModel tbModel) {
        addressBookModel = abModel;
        transactionBookModel = tbModel;
    }

    public static synchronized CommonModel getInstance() {
        requireNonNull(model);
        return model;
    }

    /**
     * Initialises the singleton instance of this class with the given address book and transaction book models.
     *
     * @param abModel The address book model to use
     * @param tbModel The transaction book model to use
     */
    public static synchronized void initialise(AddressBookModel abModel, TransactionBookModel tbModel) {
        model = new CommonModel(abModel, tbModel);
    }

    /**
     * De-initialises the singleton instance of this class.
     */
    public static synchronized void deinitialise() {
        model = null;
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        requireNonNull(addressBookModel, "AddressBook model cannot be null");
        return addressBookModel.getUserPrefs();
    }

    // AddressBook
    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(addressBookModel, "AddressBook model cannot be null");
        addressBookModel.setUserPrefs(userPrefs);
    }

    @Override
    public GuiSettings getGuiSettings() {
        requireNonNull(addressBookModel, "AddressBook model cannot be null");
        return addressBookModel.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(addressBookModel, "AddressBook model cannot be null");
        addressBookModel.setGuiSettings(guiSettings);
    }

    @Override
    public Path getAddressBookFilePath() {
        requireNonNull(addressBookModel, "AddressBook model cannot be null");
        return addressBookModel.getAddressBookFilePath();
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookModel, "AddressBook model cannot be null");
        addressBookModel.setAddressBookFilePath(addressBookFilePath);
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        requireNonNull(addressBookModel, "AddressBook model cannot be null");
        return addressBookModel.getAddressBook();
    }

    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        requireNonNull(addressBookModel, "AddressBook model cannot be null");
        addressBookModel.setAddressBook(addressBook);
    }

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(addressBookModel, "AddressBook model cannot be null");
        return addressBookModel.hasPerson(person);
    }

    @Override
    public void deletePerson(Person target) {
        requireNonNull(addressBookModel, "AddressBook model cannot be null");
        addressBookModel.deletePerson(target);
    }

    @Override
    public void addPerson(Person person) {
        requireNonNull(addressBookModel, "AddressBook model cannot be null");
        addressBookModel.addPerson(person);
    }

    @Override
    public void setPerson(Person target, Person editedPerson) {
        requireNonNull(addressBookModel, "AddressBook model cannot be null");
        addressBookModel.setPerson(target, editedPerson);
    }

    @Override
    public ObservableList<Person> getFilteredPersonList() {
        requireNonNull(addressBookModel, "AddressBook model cannot be null");
        return addressBookModel.getFilteredPersonList();
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(addressBookModel, "AddressBook model cannot be null");
        addressBookModel.updateFilteredPersonList(predicate);
    }

    @Override
    public Optional<Person> getPersonById(String id) {
        requireNonNull(addressBookModel, "AddressBook model cannot be null");
        return addressBookModel.getPersonById(id);
    }

    // TransactionBook
    @Override
    public ReadOnlyTransactionBook getTransactionBook() {
        requireNonNull(transactionBookModel, "TransactionBook model cannot be null");
        return transactionBookModel.getTransactionBook();
    }

    @Override
    public void setTransactionBook(ReadOnlyTransactionBook replacementBook) {
        requireNonNull(transactionBookModel, "TransactionBook model cannot be null");
        transactionBookModel.setTransactionBook(replacementBook);
    }

    @Override
    public void addTransaction(Transaction transaction) {
        requireNonNull(transactionBookModel, "TransactionBook model cannot be null");
        transactionBookModel.addTransaction(transaction);
    }

    @Override
    public boolean hasTransaction(Transaction transaction) {
        requireNonNull(transactionBookModel, "TransactionBook model cannot be null");
        return transactionBookModel.hasTransaction(transaction);
    }

    @Override
    public ObservableList<Transaction> getFilteredTransactionList() {
        requireNonNull(transactionBookModel, "TransactionBook model cannot be null");
        return transactionBookModel.getFilteredTransactionList();
    }

    @Override
    public void updateFilteredTransactionList(Predicate<Transaction> predicate) {
        requireNonNull(transactionBookModel, "TransactionBook model cannot be null");
        transactionBookModel.updateFilteredTransactionList(predicate);
    }
}
