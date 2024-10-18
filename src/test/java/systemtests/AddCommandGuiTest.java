package systemtests;

import static spleetwaise.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static spleetwaise.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static spleetwaise.address.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static spleetwaise.address.logic.commands.CommandTestUtil.ADDRESS_DESC_BOB;
import static spleetwaise.address.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static spleetwaise.address.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static spleetwaise.address.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static spleetwaise.address.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static spleetwaise.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static spleetwaise.address.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static spleetwaise.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static spleetwaise.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static spleetwaise.address.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static spleetwaise.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static spleetwaise.address.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static spleetwaise.address.logic.commands.CommandTestUtil.REMARK_DESC_AMY;
import static spleetwaise.address.logic.commands.CommandTestUtil.REMARK_DESC_BOB;
import static spleetwaise.address.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static spleetwaise.address.logic.commands.CommandTestUtil.TAG_DESC_HUSBAND;
import static spleetwaise.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static spleetwaise.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static spleetwaise.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static spleetwaise.address.logic.parser.CliSyntax.PREFIX_TAG;
import static spleetwaise.address.testutil.TypicalPersons.KEYWORD_MATCHING_MEIER;

import org.junit.jupiter.api.Test;

import spleetwaise.address.logic.Messages;
import spleetwaise.address.logic.commands.AddCommand;
import spleetwaise.address.model.AddressBookModel;
import spleetwaise.address.model.person.Address;
import spleetwaise.address.model.person.Email;
import spleetwaise.address.model.person.Name;
import spleetwaise.address.model.person.Person;
import spleetwaise.address.model.person.Phone;
import spleetwaise.address.model.tag.Tag;
import spleetwaise.address.testutil.PersonBuilder;
import spleetwaise.address.testutil.PersonUtil;
import spleetwaise.address.testutil.TypicalPersons;

/**
 * GUI tests for the AddCommand in the address book application. Ensures that adding new persons via the command box
 * behaves as expected.
 */
public class AddCommandGuiTest extends TestFxAppRunner {

    /**
     * Executes various add commands and checks for expected success or failure results.
     */
    @Test
    public void add() {
        /* Perform add operations on the shown unfiltered list */

        // Case: Add a person without tags to a non-empty address book -> success
        Person toAdd = TypicalPersons.AMY;
        String command = "   " + AddCommand.COMMAND_WORD + "  " + NAME_DESC_AMY + "  " + PHONE_DESC_AMY + " "
                + EMAIL_DESC_AMY + "   " + ADDRESS_DESC_AMY + "   " + TAG_DESC_FRIEND + "   " + REMARK_DESC_AMY;
        assertCommandSuccess(command, toAdd);

        // Case: Add a person with all fields same as another person except name -> success
        toAdd = new PersonBuilder(TypicalPersons.AMY).withName(VALID_NAME_BOB).build();
        command = AddCommand.COMMAND_WORD + NAME_DESC_BOB + PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY
                + TAG_DESC_FRIEND + REMARK_DESC_AMY;
        assertCommandSuccess(command, toAdd);

        // Case: Add to empty address book -> success
        deleteAllPersons();

        // Case: Add a person with tags, parameters in random order -> success
        toAdd = TypicalPersons.BOB;
        command = AddCommand.COMMAND_WORD + TAG_DESC_FRIEND + PHONE_DESC_BOB + ADDRESS_DESC_BOB + NAME_DESC_BOB
                + TAG_DESC_HUSBAND + EMAIL_DESC_BOB + REMARK_DESC_BOB;
        assertCommandSuccess(command, toAdd);

        // Case: Add a person, missing tags -> success
        assertCommandSuccess(TypicalPersons.HOON);

        /* Perform add operation on the shown filtered list */

        // Case: Filter person list before adding -> success
        showPersonsWithName(KEYWORD_MATCHING_MEIER);

        /* Perform invalid add operations */

        // Case: Add a duplicate person -> failure
        command = PersonUtil.getAddCommand(TypicalPersons.HOON);
        assertCommandFailure(command, AddCommand.MESSAGE_DUPLICATE_PERSON);

        // Case: Add duplicate person with different email -> failure
        toAdd = new PersonBuilder(TypicalPersons.HOON).withEmail(VALID_EMAIL_BOB).build();
        command = PersonUtil.getAddCommand(toAdd);
        assertCommandFailure(command, AddCommand.MESSAGE_DUPLICATE_PERSON);

        // Case: Add duplicate person with different email -> failure
        toAdd = new PersonBuilder(TypicalPersons.HOON).withAddress(VALID_ADDRESS_BOB).build();
        command = PersonUtil.getAddCommand(toAdd);
        assertCommandFailure(command, AddCommand.MESSAGE_DUPLICATE_PERSON);

        // Case: Add duplicate person with different email -> failure
        command = PersonUtil.getAddCommand(TypicalPersons.HOON) + " " + PREFIX_TAG.getPrefix() + "friends";
        assertCommandFailure(command, AddCommand.MESSAGE_DUPLICATE_PERSON);

        // Case: Add duplicate person with different email -> failure
        command = AddCommand.COMMAND_WORD + PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));

        // Case: Missing phone -> failure
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));

        // Case: Missing phone -> failure
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + PHONE_DESC_AMY + ADDRESS_DESC_AMY;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));

        /* Case: Missing address -> failure */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));

        /* Case: Invalid keyword -> failure */
        command = "adds " + PersonUtil.getPersonDetails(toAdd);
        assertCommandFailure(command, MESSAGE_UNKNOWN_COMMAND);

        /* Case: Invalid name -> failure */
        command = AddCommand.COMMAND_WORD + INVALID_NAME_DESC + PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY;
        assertCommandFailure(command, Name.MESSAGE_CONSTRAINTS);

        /* Case: Invalid phone -> failure */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + INVALID_PHONE_DESC + EMAIL_DESC_AMY + ADDRESS_DESC_AMY;
        assertCommandFailure(command, Phone.MESSAGE_CONSTRAINTS);

        /* Case: Invalid email -> failure */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + PHONE_DESC_AMY + INVALID_EMAIL_DESC + ADDRESS_DESC_AMY;
        assertCommandFailure(command, Email.MESSAGE_CONSTRAINTS);

        /* Case: Invalid address -> failure */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY + INVALID_ADDRESS_DESC;
        assertCommandFailure(command, Address.MESSAGE_CONSTRAINTS);

        /* Case: Invalid tag -> failure */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY
                + INVALID_TAG_DESC;
        assertCommandFailure(command, Tag.MESSAGE_CONSTRAINTS);
    }

    private void assertCommandSuccess(Person toAdd) {
        assertCommandSuccess(PersonUtil.getAddCommand(toAdd), toAdd);
    }

    private void assertCommandSuccess(String command, Person toAdd) {
        AddressBookModel expectedModel = getModel();
        expectedModel.addPerson(toAdd);
        String expectedResultMessage = String.format(AddCommand.MESSAGE_SUCCESS, Messages.format(toAdd));

        assertCommandSuccess(command, expectedModel, expectedResultMessage);
    }

    private void assertCommandSuccess(String command, AddressBookModel expectedModel, String expectedResultMessage) {
        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertCommandBoxShowsDefaultStyle();
    }

    private void assertCommandFailure(String command, String expectedResultMessage) {
        AddressBookModel expectedModel = getModel();

        executeCommand(command);
        assertApplicationDisplaysExpected(command, expectedResultMessage, expectedModel);
        assertCommandBoxShowsErrorStyle();
        assertStatusBarUnchanged();
    }
}
