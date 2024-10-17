package spleetwaise.transaction.logic.parser;

import static spleetwaise.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static spleetwaise.address.logic.parser.CliSyntax.PREFIX_CATEGORY;
import static spleetwaise.address.logic.parser.CliSyntax.PREFIX_TXN;
import static spleetwaise.transaction.logic.commands.AddCommand.MESSAGE_USAGE;

import java.util.stream.Stream;

import spleetwaise.address.logic.parser.ArgumentMultimap;
import spleetwaise.address.logic.parser.ArgumentTokenizer;
import spleetwaise.address.logic.parser.Prefix;
import spleetwaise.address.logic.parser.exceptions.ParseException;
import spleetwaise.transaction.logic.commands.RemoveCategoryCommand;

/**
 * Parses input arguments and remove a category from a transaction object.
 */
public class RemoveCategoryCommandParser implements Parser<RemoveCategoryCommand> {

    /**
     * Parses the given {@code String} argument in the context of the transaction RemoveCategoryCommand and returns
     * an RemoveCategoryCommand object for execution.
     *
     * @param args The string argument to be parsed.
     * @return The RemoveCategoryCommand object to execute.
     * @throws ParseException string argument contains invalid arguments.
     */
    @Override
    public RemoveCategoryCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_TXN, PREFIX_CATEGORY);
        if (!arePrefixesPresent(argMultimap, PREFIX_TXN, PREFIX_CATEGORY)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_USAGE));
        }
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_TXN, PREFIX_CATEGORY);
        //Transaction txn = ParserUtil.getTransactionFromIndex(argMultimap.getValue(PREFIX_TXN).get());
        String category = argMultimap.getValue(PREFIX_CATEGORY).get();

        //return new RemoveCategoryCommand(txn, category);
        return null;
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
