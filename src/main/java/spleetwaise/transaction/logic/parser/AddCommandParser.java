package spleetwaise.transaction.logic.parser;

import static spleetwaise.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static spleetwaise.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static spleetwaise.transaction.logic.commands.AddCommand.MESSAGE_USAGE;
import static spleetwaise.transaction.logic.parser.CliSyntax.PREFIX_AMOUNT;
import static spleetwaise.transaction.logic.parser.CliSyntax.PREFIX_DATE;
import static spleetwaise.transaction.logic.parser.CliSyntax.PREFIX_DESCRIPTION;

import java.util.stream.Stream;

import spleetwaise.address.logic.parser.ArgumentMultimap;
import spleetwaise.address.logic.parser.ArgumentTokenizer;
import spleetwaise.address.logic.parser.Prefix;
import spleetwaise.address.logic.parser.exceptions.ParseException;
import spleetwaise.address.model.person.Person;
import spleetwaise.address.model.person.Phone;
import spleetwaise.transaction.logic.commands.AddCommand;
import spleetwaise.transaction.model.transaction.Amount;
import spleetwaise.transaction.model.transaction.Date;
import spleetwaise.transaction.model.transaction.Description;
import spleetwaise.transaction.model.transaction.Transaction;

/**
 * Parses input arguments and creates a new transaction AddCommand object.
 */
public class AddCommandParser implements Parser<AddCommand> {

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

    /**
     * Parses the given {@code String} argument in the context of the transaction AddCommand and returns an AddCommand
     * object for execution.
     *
     * @param args The string argument to be parsed.
     * @return The AddCommand object to execute.
     * @throws ParseException string argument contains invalid arguments.
     */
    public AddCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_PHONE, PREFIX_AMOUNT, PREFIX_DESCRIPTION, PREFIX_DATE);
        if (!arePrefixesPresent(argMultimap, PREFIX_PHONE, PREFIX_AMOUNT, PREFIX_DESCRIPTION)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_USAGE));
        }
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_PHONE, PREFIX_AMOUNT, PREFIX_DESCRIPTION);
        Phone phone = spleetwaise.address.logic.parser.ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get());
        Person person = ParserUtil.getPersonFromPhone(phone);
        Amount amount = ParserUtil.parseAmount(argMultimap.getValue(PREFIX_AMOUNT).get());
        Description description = ParserUtil.parseDescription(argMultimap.getValue(PREFIX_DESCRIPTION).get());

        Transaction transaction;

        if (argMultimap.getValue(PREFIX_DATE).isPresent()) {
            Date date = ParserUtil.parseDate(argMultimap.getValue(PREFIX_DATE).get());
            transaction = new Transaction(person, amount, description, date);
        } else {
            transaction = new Transaction(person, amount, description);
        }

        return new AddCommand(transaction);
    }
}
