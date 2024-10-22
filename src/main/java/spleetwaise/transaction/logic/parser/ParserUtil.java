package spleetwaise.transaction.logic.parser;

import static java.util.Objects.requireNonNull;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import spleetwaise.address.logic.parser.exceptions.ParseException;
import spleetwaise.address.model.ReadOnlyAddressBook;
import spleetwaise.address.model.person.Person;
import spleetwaise.address.model.person.Phone;
import spleetwaise.commons.model.CommonModel;
import spleetwaise.transaction.model.transaction.Amount;
import spleetwaise.transaction.model.transaction.Date;
import spleetwaise.transaction.model.transaction.Description;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    /**
     * Parses a {@code String amount} into a {@code Amount}. Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code amount} is invalid.
     */
    public static Amount parseAmount(String amount) throws ParseException {
        requireNonNull(amount);
        amount = amount.trim();
        if (!Amount.isValidAmount(amount)) {
            throw new ParseException(Amount.MESSAGE_CONSTRAINTS);
        }
        return new Amount(amount);
    }

    /**
     * Parses a {@code String description} into a {@code Description}. Leading and trailing whitespaces will be
     * trimmed.
     *
     * @throws ParseException if the given {@code description} is invalid.
     */
    public static Description parseDescription(String description) throws ParseException {
        requireNonNull(description);
        description = description.trim();
        if (!Description.isValidDescription(description)) {
            throw new ParseException(Description.MESSAGE_CONSTRAINTS);
        }
        return new Description(description);
    }

    /**
     * Parses a {@code String date} into a {@code Date}. Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code date} is invalid.
     */
    public static Date parseDate(String date) throws ParseException {
        requireNonNull(date);
        date = date.trim();
        if (!Date.isValidDate(date)) {
            throw new ParseException(Date.MESSAGE_CONSTRAINTS);
        }
        return new Date(date);
    }

    /**
     * Finds the corresponding Person who has the provided phone number.
     *
     * @param phone The phone to search using.
     * @return A Person who has the specified phone.
     * @throws ParseException No person was found with the phone.
     */
    public static Person getPersonFromPhone(Phone phone) throws ParseException {
        requireNonNull(phone);
        ReadOnlyAddressBook ab = CommonModel.getInstance().getAddressBook();
        ObservableList<Person> personList = ab.getPersonList();
        FilteredList<Person> filteredPersonList = personList.filtered((p) -> p.getPhone().equals(phone));
        if (filteredPersonList.isEmpty()) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        return filteredPersonList.get(0);
    }


}
