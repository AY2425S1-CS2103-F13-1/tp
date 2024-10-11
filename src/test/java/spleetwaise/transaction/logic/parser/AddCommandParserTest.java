package spleetwaise.transaction.logic.parser;

import static spleetwaise.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static spleetwaise.transaction.logic.parser.CommandParserTestUtil.assertParseFailure;
import static spleetwaise.transaction.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import spleetwaise.address.model.ModelManager;
import spleetwaise.address.model.person.Person;
import spleetwaise.address.model.person.Phone;
import spleetwaise.address.testutil.TypicalPersons;
import spleetwaise.transaction.logic.commands.AddCommand;
import spleetwaise.transaction.model.transaction.Amount;
import spleetwaise.transaction.model.transaction.Date;
import spleetwaise.transaction.model.transaction.Description;
import spleetwaise.transaction.model.transaction.Transaction;

public class AddCommandParserTest {

    private static Person testPerson = TypicalPersons.ALICE;
    private static Amount testAmount = new Amount("+1.23");
    private static Description testDescription = new Description("description");
    private static Date testDate = new Date("01012024");

    private AddCommandParser parser = new AddCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        ModelManager addressBookModel = new ModelManager();
        addressBookModel.addPerson(testPerson);
        ParserUtil.setAddressBookModel(addressBookModel);

        String userInput = " p/94351253 amt/+1.23 desc/description";
        Transaction txn = new Transaction(testPerson, testAmount, testDescription);
        assertParseSuccess(parser, userInput, new AddCommand(txn));
    }

    @Test
    public void parse_withOptionalDateField_success() {
        ModelManager addressBookModel = new ModelManager();
        addressBookModel.addPerson(testPerson);
        ParserUtil.setAddressBookModel(addressBookModel);

        String userInput = " p/94351253 amt/+1.23 desc/description date/01012024";
        Transaction txn = new Transaction(testPerson, testAmount, testDescription, testDate);
        assertParseSuccess(parser, userInput, new AddCommand(txn));
    }

    @Test
    public void parse_invalidPhone_exceptionThrown() {
        ModelManager addressBookModel = new ModelManager();
        ParserUtil.setAddressBookModel(addressBookModel);

        String userInput = " p/94351253 amt/+1.23 desc/description date/01012024";
        assertParseFailure(parser, userInput, Phone.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidAmount_exceptionThrown() {
        ModelManager addressBookModel = new ModelManager();
        addressBookModel.addPerson(testPerson);
        ParserUtil.setAddressBookModel(addressBookModel);

        String userInput = " p/94351253 amt/+1.234 desc/description date/01012024";
        assertParseFailure(parser, userInput, Amount.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidDescription_exceptionThrown() {
        ModelManager addressBookModel = new ModelManager();
        addressBookModel.addPerson(testPerson);
        ParserUtil.setAddressBookModel(addressBookModel);

        String userInput = " p/94351253 amt/+1.23 desc/ date/01012024";
        assertParseFailure(parser, userInput, Description.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidDate_exceptionThrown() {
        ModelManager addressBookModel = new ModelManager();
        addressBookModel.addPerson(testPerson);
        ParserUtil.setAddressBookModel(addressBookModel);

        String userInput = " p/94351253 amt/+1.23 desc/test date/2024";
        assertParseFailure(parser, userInput, Date.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_missingParam_exceptionThrown() {
        ModelManager addressBookModel = new ModelManager();
        addressBookModel.addPerson(testPerson);
        ParserUtil.setAddressBookModel(addressBookModel);

        String userInput = " p/94351253 desc/test";
        assertParseFailure(parser, userInput, String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
    }
}

