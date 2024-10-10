package spleetwaise.transaction.testutil;

import spleetwaise.address.model.person.Person;
import spleetwaise.address.testutil.TypicalPersons;
import spleetwaise.transaction.model.transaction.Amount;
import spleetwaise.transaction.model.transaction.Date;
import spleetwaise.transaction.model.transaction.Description;
import spleetwaise.transaction.model.transaction.Transaction;

/**
 * A utility class to help with building Transaction objects.
 */
public class TransactionBuilder {

    public static final String DEFAULT_AMOUNT = "+12.3";
    public static final String DEFAULT_DESCRIPTION = "Test";
    public static final String DEFAULT_DATE = "01012024";
    private static final Person DEFAULT_PERSON = TypicalPersons.ALICE;
    private static final String DEFAULT_POSITIVE_AMOUNT = "+1.23";
    private static final String DEFAULT_NEGATIVE_AMOUNT = "-1.23";

    private Person person;
    private Amount amount;
    private Description description;
    private Date date;

    /**
     * Creates a {@code TransactionBuilder} with the default details.
     */
    public TransactionBuilder() {
        person = DEFAULT_PERSON;
        amount = new Amount(DEFAULT_POSITIVE_AMOUNT);
        description = new Description(DEFAULT_DESCRIPTION);
        date = new Date(DEFAULT_DATE);
    }

    /**
     * Initializes the TransactionBuilder with the data of {@code transactionToCopy}.
     */
    public TransactionBuilder(Transaction transactionToCopy) {
        person = transactionToCopy.getPerson();
        amount = transactionToCopy.getAmount();
        description = transactionToCopy.getDescription();
        date = transactionToCopy.getDate();
    }

    /**
     * Sets the {@code Person} of the {@code Transaction} that we are building.
     */
    public TransactionBuilder withPerson(Person person) {
        this.person = person;
        return this;
    }

    /**
     * Sets the {@code Amount} of the {@code Transaction} that we are building.
     */
    public TransactionBuilder withAmount(String amount) {
        this.amount = new Amount(amount);
        return this;
    }

    /**
     * Sets the {@code Description} of the {@code Transaction} that we are building.
     */
    public TransactionBuilder withDescription(String description) {
        this.description = new Description(description);
        return this;
    }

    /**
     * Sets the {@code Date} of the {@code Transaction} that we are building.
     */
    public TransactionBuilder withDate(String date) {
        this.date = new Date(date);
        return this;
    }

    /**
     * Builds and returns the {@code Transaction} object.
     */
    public Transaction build() {
        return new Transaction(person, amount, description, date);
    }
}
