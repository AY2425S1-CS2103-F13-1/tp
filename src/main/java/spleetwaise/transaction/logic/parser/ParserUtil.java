package spleetwaise.transaction.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import spleetwaise.address.commons.core.index.Index;
import spleetwaise.address.commons.util.StringUtil;
import spleetwaise.address.logic.Messages;
import spleetwaise.address.model.person.Person;
import spleetwaise.address.model.person.Phone;
import spleetwaise.commons.logic.parser.exceptions.ParseException;
import spleetwaise.commons.model.CommonModel;
import spleetwaise.transaction.model.transaction.Amount;
import spleetwaise.transaction.model.transaction.Category;
import spleetwaise.transaction.model.transaction.Date;
import spleetwaise.transaction.model.transaction.Description;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";
    public static final String MESSAGE_PHONE_NUMBER_IS_UNKNOWN = "Phone number is unknown.";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     *
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

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
     * Parses a {@code String catStr} into a {@code Category} that represents the category. Leading and trailing
     * whitespaces will be trimmed and capitalized
     */
    public static Category parseCategory(String catStr) throws ParseException {
        requireNonNull(catStr);
        String trimmedCategory = catStr.trim().toUpperCase();
        if (!Category.isValidCatName(trimmedCategory)) {
            throw new ParseException(Category.MESSAGE_CONSTRAINTS);
        }
        return new Category(trimmedCategory);
    }

    /**
     * Parses {@code Collection<String> Category} into a {@code Set<Category>}.
     */
    public static Set<Category> parseCategories(Collection<String> categoryStrs) throws ParseException {
        requireNonNull(categoryStrs);
        final Set<Category> categories = new HashSet<>();
        for (String categoryStr : categoryStrs) {
            categories.add(parseCategory(categoryStr));
        }
        return categories;
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
        Optional<Person> p = CommonModel.getInstance().getPersonByPhone(phone);
        if (p.isEmpty()) {
            throw new ParseException(MESSAGE_PHONE_NUMBER_IS_UNKNOWN);
        }
        return p.get();
    }

    public static Person getPersonFromAddressBookIndex(Index index) throws ParseException {
        requireNonNull(index);
        Optional<Person> p = CommonModel.getInstance().getPersonByIndex(index);
        if (p.isEmpty()) {
            throw new ParseException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        return p.get();
    }
}
