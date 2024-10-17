package spleetwaise.transaction.model.transaction;

import static java.util.Objects.requireNonNull;

import spleetwaise.address.commons.util.CollectionUtil;
import spleetwaise.address.model.person.Person;
import spleetwaise.commons.IdUtil;

/**
 * Represents a Transaction in the transaction book. Guarantees: details are present and not null, field values are
 * validated, immutable.
 */
public class Transaction {

    private final String id;
    private final Person person;
    private final Amount amount;
    private final Description description;
    private final Date date;
    private Categories cats = new Categories();

    /**
     * Represents a Transaction in the transaction book.
     *
     * @param id          The id of this transaction.
     * @param person      The person involved in this transaction.
     * @param amount      The amount involved in this transaction.
     * @param description The description of the transaction.
     * @param date        The date the transaction has taken place.
     */
    public Transaction(String id, Person person, Amount amount, Description description, Date date) {
        CollectionUtil.requireAllNonNull(person, amount, description, date);
        this.id = id;
        this.person = person;
        this.amount = amount;
        this.description = description;
        this.date = date;
    }

    /**
     * Represents a Transaction in the transaction book.
     *
     * @param person      The person involved in this transaction.
     * @param amount      The amount involved in this transaction.
     * @param description The description of the transaction.
     * @param date        The date the transaction has taken place.
     * @param cats        The categories the transaction has.
     */
    public Transaction(Person person, Amount amount, Description description, Date date, Categories cats) {
        this(IdUtil.getId(), person, amount, description, date);
        this.cats = cats;
    }

    public Transaction(Person person, Amount amount, Description description, Date date) {
        this(IdUtil.getId(), person, amount, description, date);
    }

    public Transaction(Person person, Amount amount, Description description) {
        this(IdUtil.getId(), person, amount, description, Date.getNowDate());
    }

    public String getId() {
        return id;
    }

    public Person getPerson() {
        return person;
    }

    public Amount getAmount() {
        return amount;
    }

    public Description getDescription() {
        return description;
    }

    public Date getDate() {
        return date;
    }

    public void addCategory(String cat) {
        cats.add(cat);
    }

    public void removeCategory(String cat) {
        cats.remove(cat);
    }

    public boolean containsCategory(String cat) {
        return cats.contains(cat);
    }

    /**
     * Returns true if both transactions have the same uuid.
     *
     * @param other The other object to compare
     * @return true of the object
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Transaction)) {
            return false;
        }
        Transaction otherTransaction = (Transaction) other;
        return this.person.equals(otherTransaction.getPerson())
                && this.amount.equals(otherTransaction.getAmount())
                && this.description.equals(otherTransaction.getDescription())
                && this.date.equals(otherTransaction.getDate());
    }

    /**
     * Returns true if both txns have the same id.
     */
    public boolean hasSameId(Transaction otherTxn) {
        requireNonNull(otherTxn);
        return otherTxn.getId().equals(getId());
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public String toString() {
        return String.format("[%s] %s(%s): %s on %s for $%s", id, person.getName(), person.getPhone(), description,
                date, amount
        );
    }
}
