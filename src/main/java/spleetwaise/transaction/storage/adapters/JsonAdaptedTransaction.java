package spleetwaise.transaction.storage.adapters;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import spleetwaise.address.commons.exceptions.IllegalValueException;
import spleetwaise.address.model.person.Person;
import spleetwaise.address.storage.JsonAdaptedPerson;
import spleetwaise.transaction.model.transaction.Amount;
import spleetwaise.transaction.model.transaction.Date;
import spleetwaise.transaction.model.transaction.Description;
import spleetwaise.transaction.model.transaction.Transaction;
/**
 * Adapter for serializing and deserializing Transaction objects into JSON.
 */
public class JsonAdaptedTransaction {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Transaction's %s field is missing!";

    /**
     * The unique identifier of the transaction.
     */
    private final String id;
    /**
     * The person involved in the transaction.
     */
    private final JsonAdaptedPerson person;
    /**
     * The amount of money transferred.
     */
    private final JsonAdaptedAmount amount;
    /**
     * A description of the transaction.
     */
    private final String description;
    /**
     * The date of the transaction.
     */
    private final String date;


    /**
     * Constructs a {@code JsonAdaptedTransaction} with the given transaction details.
     */
    @JsonCreator
    public JsonAdaptedTransaction(@JsonProperty("id") String id,
        @JsonProperty("person") JsonAdaptedPerson person,
        @JsonProperty("amount") JsonAdaptedAmount amount,
        @JsonProperty("description") String description,
        @JsonProperty("date") String date
    ) {
        this.id = id;
        this.person = person;
        this.amount = amount;
        this.description = description;
        this.date = date;
    }

    /**
     * Constructor for creating a JsonAdaptedTransaction from a Transaction object.
     *
     * @param transaction the transaction to create the adapter for
     */
    public JsonAdaptedTransaction(Transaction transaction) {
        this.id = transaction.getId();
        this.person = new JsonAdaptedPerson(transaction.getPerson());
        this.amount = new JsonAdaptedAmount(transaction.getAmount());
        this.description = transaction.getDescription().toString();
        this.date = transaction.getDate().toString();
    }

    /**
     * Converts the JSON adapter back into a Transaction object.
     *
     * @return the deserialized Transaction object
     * @throws IllegalValueException if any of the fields are missing or invalid
     */
    public Transaction toModelType() throws IllegalValueException {
        final Person p;
        final Amount a;
        final Description d;
        final Date dt;

        // Check missing fields
        if (person == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Person.class.getSimpleName()));
        }
        if (amount == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Amount.class.getSimpleName()));
        }
        if (description == null) {
            throw new IllegalValueException(
                String.format(MISSING_FIELD_MESSAGE_FORMAT, Description.class.getSimpleName()));
        }
        if (date == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Date.class.getSimpleName()));
        }

        // Validate
        if (!Description.isValidDescription(description)) {
            throw new IllegalValueException(Description.MESSAGE_CONSTRAINTS);
        }
        if (!Date.isValidDate(date)) {
            throw new IllegalValueException(Date.MESSAGE_CONSTRAINTS);
        }

        p = person.toModelType();
        a = amount.toModelType();
        d = new Description(description);
        dt = new Date(date);

        return new Transaction(p, a, d, dt);
    }
}
