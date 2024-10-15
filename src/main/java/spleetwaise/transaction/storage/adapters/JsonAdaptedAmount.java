package spleetwaise.transaction.storage.adapters;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import spleetwaise.address.commons.exceptions.IllegalValueException;
import spleetwaise.transaction.model.transaction.Amount;

/**
 * This package contains adapters for converting between Amount objects in our model and other formats.
 */
public class JsonAdaptedAmount {

    /**
     * The amount to be stored or retrieved.
     *
     * @param amount the amount as a string
     */
    private final String amount;

    /**
     * Create a new instance of JsonAdaptedAmount from a string. This will throw an exception if the string is not a
     * valid amount.
     *
     * @param amount the amount as a string
     * @throws IllegalValueException if the string is not a valid amount
     */
    @JsonCreator
    public JsonAdaptedAmount(@JsonProperty("amount") String amount) {
        this.amount = amount;
    }

    /**
     * Create a new instance of JsonAdaptedAmount from an Amount object.
     *
     * @param amount the Amount to convert from
     */
    public JsonAdaptedAmount(Amount amount) {
        this.amount = amount.toString();
    }

    /**
     * Convert this JsonAdaptedAmount back into a model type Amount. This will throw an exception if the string is not a
     * valid amount.
     *
     * @return the Amount
     * @throws IllegalValueException if the string is not a valid amount
     */
    public Amount toModelType() throws IllegalValueException {
        if (!Amount.isValidAmount(amount)) {
            throw new IllegalValueException(Amount.MESSAGE_CONSTRAINTS);
        }
        return new Amount(amount);
    }

    /**
     * Retrieves the amount as a string.
     *
     * @return The amount
     */
    public String getAmount() {
        return amount;
    }
}
