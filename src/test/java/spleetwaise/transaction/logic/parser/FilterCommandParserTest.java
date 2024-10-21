package spleetwaise.transaction.logic.parser;

import static spleetwaise.transaction.logic.parser.CommandParserTestUtil.assertParseFailure;
import static spleetwaise.transaction.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import spleetwaise.address.model.AddressBookModel;
import spleetwaise.address.model.person.Person;
import spleetwaise.address.model.person.Phone;
import spleetwaise.address.testutil.TypicalPersons;
import spleetwaise.commons.CommonModel;
import spleetwaise.transaction.logic.commands.FilterCommand;
import spleetwaise.transaction.model.ModelManager;
import spleetwaise.transaction.model.TransactionBookModel;
import spleetwaise.transaction.model.transaction.Amount;
import spleetwaise.transaction.model.transaction.Date;
import spleetwaise.transaction.model.transaction.Description;
import spleetwaise.transaction.testutil.TypicalTransactions;

public class FilterCommandParserTest {

    private static final Person testPerson = TypicalTransactions.SEANOWESME.getPerson();
    private static final Amount testAmount = TypicalTransactions.SEANOWESME.getAmount();
    private static final Description testDescription = TypicalTransactions.SEANOWESME.getDescription();
    private static final Date testDate = TypicalTransactions.SEANOWESME.getDate();
    private static final AddressBookModel abModel = new spleetwaise.address.model.ModelManager();
    private static final TransactionBookModel txnModel = new ModelManager();

    private final FilterCommandParser parser = new FilterCommandParser();

    @BeforeAll
    public static void setUp() {
        abModel.setAddressBook(TypicalPersons.getTypicalAddressBook());
        txnModel.setTransactionBook(TypicalTransactions.getTypicalTransactionBook());
        CommonModel.initialise(abModel, txnModel);
    }

    @Test
    public void parse_personField_success() {
        String userInput = " p/94351253";
        assertParseSuccess(parser, userInput, new FilterCommand(testPerson, null, null, null));
    }

    @Test
    public void parse_amountField_success() {
        String userInput = " amt/9999999999.99";
        assertParseSuccess(parser, userInput, new FilterCommand(null, testAmount, null, null));
    }

    @Test
    public void parse_descriptionField_success() {
        String userInput = " desc/Sean owes me a lot for a landed property in Sentosa";
        assertParseSuccess(parser, userInput, new FilterCommand(null, null, testDescription, null));
    }

    @Test
    public void parse_dateField_success() {
        String userInput = " date/10102024";
        assertParseSuccess(parser, userInput, new FilterCommand(null, null, null, testDate));
    }

    @Test
    public void parse_allFields_success() {
        String userInput = " p/94351253 amt/9999999999.99  "
                + "desc/Sean owes me a lot for a landed property in Sentosa date/10102024";
        assertParseSuccess(parser, userInput, new FilterCommand(testPerson, testAmount, testDescription, testDate));
    }

    @Test
    public void parse_invalidPersonField_failure() {
        String userInput = " p/9435125";
        assertParseFailure(parser, userInput, Phone.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidAmountField_failure() {
        String userInput = " amt/9.999";
        assertParseFailure(parser, userInput, Amount.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidDescriptionField_failure() {
        String userInput = " desc/";
        assertParseFailure(parser, userInput, Description.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidDateField_failure() {
        String userInput = " date/1010202";
        assertParseFailure(parser, userInput, Date.MESSAGE_CONSTRAINTS);
    }
}
