package spleetwaise.transaction.logic.parser;

import static spleetwaise.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static spleetwaise.transaction.logic.commands.FilterCommand.MESSAGE_USAGE;
import static spleetwaise.transaction.logic.parser.CliSyntax.PREFIX_AMOUNT;
import static spleetwaise.transaction.logic.parser.CliSyntax.PREFIX_DATE;
import static spleetwaise.transaction.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static spleetwaise.transaction.logic.parser.CliSyntax.PREFIX_STATUS;

import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.stream.Stream;

import spleetwaise.address.logic.parser.ArgumentMultimap;
import spleetwaise.address.logic.parser.ArgumentTokenizer;
import spleetwaise.address.logic.parser.Prefix;
import spleetwaise.address.model.person.Person;
import spleetwaise.commons.core.index.Index;
import spleetwaise.commons.logic.parser.Parser;
import spleetwaise.commons.logic.parser.exceptions.ParseException;
import spleetwaise.transaction.logic.commands.FilterCommand;
import spleetwaise.transaction.model.FilterCommandPredicate;
import spleetwaise.transaction.model.filterpredicate.AmountFilterPredicate;
import spleetwaise.transaction.model.filterpredicate.DateFilterPredicate;
import spleetwaise.transaction.model.filterpredicate.DescriptionFilterPredicate;
import spleetwaise.transaction.model.filterpredicate.PersonFilterPredicate;
import spleetwaise.transaction.model.filterpredicate.StatusFilterPredicate;
import spleetwaise.transaction.model.transaction.Amount;
import spleetwaise.transaction.model.transaction.Date;
import spleetwaise.transaction.model.transaction.Description;
import spleetwaise.transaction.model.transaction.Status;
import spleetwaise.transaction.model.transaction.Transaction;

/**
 * Parses input arguments and creates a new transaction FilterCommand object.
 */
public class FilterCommandParser implements Parser<FilterCommand> {

    /**
     * Returns true if any of the prefixes are present in the given {@code ArgumentMultimap}.
     */
    private static boolean areAnyPrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).anyMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

    /**
     * Parses the given {@code String} argument in the context of the transaction FilterCommand and returns an
     * FilterCommand object for execution.
     *
     * @param args The string argument to be parsed.
     * @return The FilterCommand object to execute.
     * @throws ParseException string argument contains invalid arguments.
     */
    public FilterCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_AMOUNT, PREFIX_DESCRIPTION, PREFIX_DATE, PREFIX_STATUS);
        if (!areAnyPrefixesPresent(argMultimap, PREFIX_AMOUNT, PREFIX_DESCRIPTION, PREFIX_DATE, PREFIX_STATUS)
                && argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_USAGE));
        }
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_AMOUNT, PREFIX_DESCRIPTION, PREFIX_DATE, PREFIX_STATUS);

        ArrayList<Predicate<Transaction>> filterSubPredicates = new ArrayList<>();

        if (!argMultimap.getPreamble().isEmpty()) {
            Index index = ParserUtil.parseIndex(argMultimap.getPreamble());
            Person person = ParserUtil.getPersonFromAddressBookIndex(index);
            filterSubPredicates.add(new PersonFilterPredicate(person));
        }

        if (argMultimap.getValue(PREFIX_AMOUNT).isPresent()) {
            Amount amount = ParserUtil.parseAmount(argMultimap.getValue(PREFIX_AMOUNT).get());
            filterSubPredicates.add(new AmountFilterPredicate(amount));
        }

        if (argMultimap.getValue(PREFIX_DESCRIPTION).isPresent()) {
            Description description = ParserUtil.parseDescription(argMultimap.getValue(PREFIX_DESCRIPTION).get());
            filterSubPredicates.add(new DescriptionFilterPredicate(description));
        }

        if (argMultimap.getValue(PREFIX_DATE).isPresent()) {
            Date date = ParserUtil.parseDate(argMultimap.getValue(PREFIX_DATE).get());
            filterSubPredicates.add(new DateFilterPredicate(date));
        }

        if (argMultimap.getValue(PREFIX_STATUS).isPresent()) {
            Status status = ParserUtil.parseStatus(argMultimap.getValue(PREFIX_STATUS).get());
            filterSubPredicates.add(new StatusFilterPredicate(status));
        }

        assert !filterSubPredicates.isEmpty();

        FilterCommandPredicate filterPredicate = new FilterCommandPredicate(filterSubPredicates);

        return new FilterCommand(filterPredicate);
    }
}
