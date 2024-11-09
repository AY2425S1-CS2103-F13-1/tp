package spleetwaise.address.model.person;

import static java.util.Objects.requireNonNull;

/**
 * Represents a Person's remark in the address book. Guarantees: immutable;
 */
public class Remark {
    public static final String MESSAGE_CONSTRAINTS = "Remarks should not be blank";
    public final String value;

    /**
     * Constructor for remark
     */
    public Remark(String remark) {
        requireNonNull(remark);
        value = remark.trim();
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Remark // instanceof handles nulls
                && value.equals(((Remark) other).value)); // state check
    }

    @Override
    public String toString() {
        return value;
    }
}
