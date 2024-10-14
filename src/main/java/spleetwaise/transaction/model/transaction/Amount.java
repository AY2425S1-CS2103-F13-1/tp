package spleetwaise.transaction.model.transaction;

import static java.util.Objects.requireNonNull;

import java.math.BigDecimal;
import java.math.RoundingMode;

import spleetwaise.address.commons.util.AppUtil;

/**
 * Represents a Transaction's amount in the transaction book.
 * Guarantees: immutable; is valid or declared in {@link #isValidAmount(String)}
 */
public class Amount {

    public static final String MESSAGE_CONSTRAINTS =
            "Amount should only contain digits up to 2 decimal points delimited by . and prefixed with - for negative"
                    + " amounts";

    /*
     * The first character of amount must be + or - and only allow precision up to 2 decimal places
     */
    public static final String VALIDATION_REGEX = "^(\\-)?([\\d]+$|[\\d]+\\.[\\d]{1,2}$)";

    private static final int MAX_DECIMAL_PLACES = 2;

    public final BigDecimal amount;

    /**
     * Constructs a {@code Amount}
     *
     * @param amount A valid amount.
     */
    public Amount(String amount) {
        requireNonNull(amount);
        AppUtil.checkArgument(isValidAmount(amount), MESSAGE_CONSTRAINTS);
        this.amount = new BigDecimal(amount).setScale(MAX_DECIMAL_PLACES, RoundingMode.HALF_UP);
    }

    /**
     * Check if test string provided passes validation and is non-zero.
     *
     * @param test The test string to test.
     * @return Returns true if the test string passes the regex check and is non-zero value
     */
    public static boolean isValidAmount(String test) {
        return test.matches(VALIDATION_REGEX) && new BigDecimal(test).compareTo(BigDecimal.ZERO) != 0;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public boolean isNegative() {
        return amount.compareTo(BigDecimal.ZERO) < 0;
    }

    @Override
    public String toString() {
        return String.format("%.2f", amount);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Amount otherAmount)) {
            return false;
        }

        return otherAmount.amount.equals(amount);
    }

    @Override
    public int hashCode() {
        return amount.hashCode();
    }
}
