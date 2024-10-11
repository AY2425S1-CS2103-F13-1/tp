package spleetwaise.transaction.model.transaction;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import spleetwaise.address.commons.util.AppUtil;

/**
 * Represents a Transaction's date in the transaction book.
 * Guarantees: immutable; is valid or declared in {@link #isValidDate(String)}
 */
public class Date {
    public static final String MESSAGE_CONSTRAINTS = "Date should only in the format of DDMMYYYY";

    public static final DateTimeFormatter VALIDATION_FORMATTER = DateTimeFormatter.ofPattern("ddMMyyyy");

    public static final DateTimeFormatter DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public final LocalDate date;

    /**
     * Constructs a {@code Date}
     *
     * @param date A valid date string.
     */
    public Date(String date) {
        requireNonNull(date);
        AppUtil.checkArgument(isValidDate(date), MESSAGE_CONSTRAINTS);
        this.date = LocalDate.parse(date, VALIDATION_FORMATTER);
    }

    /**
     * Returns true if a given string is a valid date.
     */
    public static boolean isValidDate(String test) {
        try {
            LocalDate.parse(test, VALIDATION_FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public LocalDate getDate() {
        return date;
    }

    public static Date getNowDate() {
        return new Date(LocalDate.now().format(VALIDATION_FORMATTER));
    }

    @Override
    public String toString() {
        return date.format(DISPLAY_FORMATTER);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Date)) {
            return false;
        }

        Date otherDate = (Date) other;
        return this.date.equals(otherDate.date);
    }

    @Override
    public int hashCode() {
        return this.date.hashCode();
    }

}
