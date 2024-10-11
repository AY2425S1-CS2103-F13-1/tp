package spleetwaise.address.logic.parser;

import static spleetwaise.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static spleetwaise.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import spleetwaise.address.logic.Messages;
import spleetwaise.address.logic.commands.AddCommand;
import spleetwaise.address.logic.commands.CommandTestUtil;
import spleetwaise.address.model.person.Address;
import spleetwaise.address.model.person.Email;
import spleetwaise.address.model.person.Name;
import spleetwaise.address.model.person.Person;
import spleetwaise.address.model.person.Phone;
import spleetwaise.address.model.tag.Tag;
import spleetwaise.address.testutil.PersonBuilder;
import spleetwaise.address.testutil.TypicalPersons;

public class AddCommandParserTest {

    private AddCommandParser parser = new AddCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Person expectedPerson =
            new PersonBuilder(TypicalPersons.BOB).withTags(CommandTestUtil.VALID_TAG_FRIEND).build();

        // whitespace only preamble
        assertParseSuccess(parser,
            CommandTestUtil.PREAMBLE_WHITESPACE + CommandTestUtil.NAME_DESC_BOB + CommandTestUtil.PHONE_DESC_BOB
                + CommandTestUtil.EMAIL_DESC_BOB
                + CommandTestUtil.ADDRESS_DESC_BOB + CommandTestUtil.TAG_DESC_FRIEND, new AddCommand(expectedPerson));

        // multiple tags - all accepted
        Person expectedPersonMultipleTags = new PersonBuilder(TypicalPersons.BOB).withTags(
                CommandTestUtil.VALID_TAG_FRIEND, CommandTestUtil.VALID_TAG_HUSBAND)
            .build();
        assertParseSuccess(parser,
            CommandTestUtil.NAME_DESC_BOB + CommandTestUtil.PHONE_DESC_BOB + CommandTestUtil.EMAIL_DESC_BOB
                + CommandTestUtil.ADDRESS_DESC_BOB + CommandTestUtil.TAG_DESC_HUSBAND + CommandTestUtil.TAG_DESC_FRIEND,
            new AddCommand(expectedPersonMultipleTags));
    }

    @Test
    public void parse_repeatedNonTagValue_failure() {
        String validExpectedPersonString =
            CommandTestUtil.NAME_DESC_BOB + CommandTestUtil.PHONE_DESC_BOB + CommandTestUtil.EMAIL_DESC_BOB
                + CommandTestUtil.ADDRESS_DESC_BOB + CommandTestUtil.TAG_DESC_FRIEND;

        // multiple names
        assertParseFailure(parser, CommandTestUtil.NAME_DESC_AMY + validExpectedPersonString,
            Messages.getErrorMessageForDuplicatePrefixes(CliSyntax.PREFIX_NAME));

        // multiple phones
        assertParseFailure(parser, CommandTestUtil.PHONE_DESC_AMY + validExpectedPersonString,
            Messages.getErrorMessageForDuplicatePrefixes(CliSyntax.PREFIX_PHONE));

        // multiple emails
        assertParseFailure(parser, CommandTestUtil.EMAIL_DESC_AMY + validExpectedPersonString,
            Messages.getErrorMessageForDuplicatePrefixes(CliSyntax.PREFIX_EMAIL));

        // multiple addresses
        assertParseFailure(parser, CommandTestUtil.ADDRESS_DESC_AMY + validExpectedPersonString,
            Messages.getErrorMessageForDuplicatePrefixes(CliSyntax.PREFIX_ADDRESS));

        // multiple fields repeated
        assertParseFailure(parser,
            validExpectedPersonString + CommandTestUtil.PHONE_DESC_AMY + CommandTestUtil.EMAIL_DESC_AMY
                + CommandTestUtil.NAME_DESC_AMY + CommandTestUtil.ADDRESS_DESC_AMY
                + validExpectedPersonString,
            Messages.getErrorMessageForDuplicatePrefixes(
                CliSyntax.PREFIX_NAME, CliSyntax.PREFIX_ADDRESS, CliSyntax.PREFIX_EMAIL, CliSyntax.PREFIX_PHONE));

        // invalid value followed by valid value

        // invalid name
        assertParseFailure(parser, CommandTestUtil.INVALID_NAME_DESC + validExpectedPersonString,
            Messages.getErrorMessageForDuplicatePrefixes(CliSyntax.PREFIX_NAME));

        // invalid email
        assertParseFailure(parser, CommandTestUtil.INVALID_EMAIL_DESC + validExpectedPersonString,
            Messages.getErrorMessageForDuplicatePrefixes(CliSyntax.PREFIX_EMAIL));

        // invalid phone
        assertParseFailure(parser, CommandTestUtil.INVALID_PHONE_DESC + validExpectedPersonString,
            Messages.getErrorMessageForDuplicatePrefixes(CliSyntax.PREFIX_PHONE));

        // invalid address
        assertParseFailure(parser, CommandTestUtil.INVALID_ADDRESS_DESC + validExpectedPersonString,
            Messages.getErrorMessageForDuplicatePrefixes(CliSyntax.PREFIX_ADDRESS));

        // valid value followed by invalid value

        // invalid name
        assertParseFailure(parser, validExpectedPersonString + CommandTestUtil.INVALID_NAME_DESC,
            Messages.getErrorMessageForDuplicatePrefixes(CliSyntax.PREFIX_NAME));

        // invalid email
        assertParseFailure(parser, validExpectedPersonString + CommandTestUtil.INVALID_EMAIL_DESC,
            Messages.getErrorMessageForDuplicatePrefixes(CliSyntax.PREFIX_EMAIL));

        // invalid phone
        assertParseFailure(parser, validExpectedPersonString + CommandTestUtil.INVALID_PHONE_DESC,
            Messages.getErrorMessageForDuplicatePrefixes(CliSyntax.PREFIX_PHONE));

        // invalid address
        assertParseFailure(parser, validExpectedPersonString + CommandTestUtil.INVALID_ADDRESS_DESC,
            Messages.getErrorMessageForDuplicatePrefixes(CliSyntax.PREFIX_ADDRESS));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // zero tags
        Person expectedPerson = new PersonBuilder(TypicalPersons.AMY).withTags().build();
        assertParseSuccess(parser,
            CommandTestUtil.NAME_DESC_AMY + CommandTestUtil.PHONE_DESC_AMY + CommandTestUtil.EMAIL_DESC_AMY
                + CommandTestUtil.ADDRESS_DESC_AMY,
            new AddCommand(expectedPerson));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser,
            CommandTestUtil.VALID_NAME_BOB + CommandTestUtil.PHONE_DESC_BOB + CommandTestUtil.EMAIL_DESC_BOB
                + CommandTestUtil.ADDRESS_DESC_BOB,
            expectedMessage);

        // missing phone prefix
        assertParseFailure(parser,
            CommandTestUtil.NAME_DESC_BOB + CommandTestUtil.VALID_PHONE_BOB + CommandTestUtil.EMAIL_DESC_BOB
                + CommandTestUtil.ADDRESS_DESC_BOB,
            expectedMessage);

        // missing email prefix
        assertParseFailure(parser,
            CommandTestUtil.NAME_DESC_BOB + CommandTestUtil.PHONE_DESC_BOB + CommandTestUtil.VALID_EMAIL_BOB
                + CommandTestUtil.ADDRESS_DESC_BOB,
            expectedMessage);

        // missing address prefix
        assertParseFailure(parser,
            CommandTestUtil.NAME_DESC_BOB + CommandTestUtil.PHONE_DESC_BOB + CommandTestUtil.EMAIL_DESC_BOB
                + CommandTestUtil.VALID_ADDRESS_BOB,
            expectedMessage);

        // all prefixes missing
        assertParseFailure(parser,
            CommandTestUtil.VALID_NAME_BOB + CommandTestUtil.VALID_PHONE_BOB + CommandTestUtil.VALID_EMAIL_BOB
                + CommandTestUtil.VALID_ADDRESS_BOB,
            expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser,
            CommandTestUtil.INVALID_NAME_DESC + CommandTestUtil.PHONE_DESC_BOB + CommandTestUtil.EMAIL_DESC_BOB
                + CommandTestUtil.ADDRESS_DESC_BOB
                + CommandTestUtil.TAG_DESC_HUSBAND + CommandTestUtil.TAG_DESC_FRIEND, Name.MESSAGE_CONSTRAINTS);

        // invalid phone
        assertParseFailure(parser,
            CommandTestUtil.NAME_DESC_BOB + CommandTestUtil.INVALID_PHONE_DESC + CommandTestUtil.EMAIL_DESC_BOB
                + CommandTestUtil.ADDRESS_DESC_BOB
                + CommandTestUtil.TAG_DESC_HUSBAND + CommandTestUtil.TAG_DESC_FRIEND, Phone.MESSAGE_CONSTRAINTS);

        // invalid email
        assertParseFailure(parser,
            CommandTestUtil.NAME_DESC_BOB + CommandTestUtil.PHONE_DESC_BOB + CommandTestUtil.INVALID_EMAIL_DESC
                + CommandTestUtil.ADDRESS_DESC_BOB
                + CommandTestUtil.TAG_DESC_HUSBAND + CommandTestUtil.TAG_DESC_FRIEND, Email.MESSAGE_CONSTRAINTS);

        // invalid address
        assertParseFailure(parser,
            CommandTestUtil.NAME_DESC_BOB + CommandTestUtil.PHONE_DESC_BOB + CommandTestUtil.EMAIL_DESC_BOB
                + CommandTestUtil.INVALID_ADDRESS_DESC
                + CommandTestUtil.TAG_DESC_HUSBAND + CommandTestUtil.TAG_DESC_FRIEND, Address.MESSAGE_CONSTRAINTS);

        // invalid tag
        assertParseFailure(parser,
            CommandTestUtil.NAME_DESC_BOB + CommandTestUtil.PHONE_DESC_BOB + CommandTestUtil.EMAIL_DESC_BOB
                + CommandTestUtil.ADDRESS_DESC_BOB
                + CommandTestUtil.INVALID_TAG_DESC + CommandTestUtil.VALID_TAG_FRIEND, Tag.MESSAGE_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser,
            CommandTestUtil.INVALID_NAME_DESC + CommandTestUtil.PHONE_DESC_BOB + CommandTestUtil.EMAIL_DESC_BOB
                + CommandTestUtil.INVALID_ADDRESS_DESC,
            Name.MESSAGE_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser,
            CommandTestUtil.PREAMBLE_NON_EMPTY + CommandTestUtil.NAME_DESC_BOB + CommandTestUtil.PHONE_DESC_BOB
                + CommandTestUtil.EMAIL_DESC_BOB
                + CommandTestUtil.ADDRESS_DESC_BOB + CommandTestUtil.TAG_DESC_HUSBAND + CommandTestUtil.TAG_DESC_FRIEND,
            String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
    }
}
