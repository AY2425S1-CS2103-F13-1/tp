package spleetwaise.transaction.logic.parser;

import static spleetwaise.transaction.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import spleetwaise.address.model.ModelManager;
import spleetwaise.address.model.person.Person;
import spleetwaise.address.testutil.TypicalPersons;
import spleetwaise.transaction.model.transaction.Amount;
import spleetwaise.transaction.model.transaction.Date;
import spleetwaise.transaction.model.transaction.Description;
import spleetwaise.transaction.model.transaction.Transaction;

public class AddCategoryCommandParserTest {
    private static Person testPerson = TypicalPersons.ALICE;
    private static Amount testAmount = new Amount("1.23");
    private static Description testDescription = new Description("description");
    private static Date testDate = new Date("01012024");
    private static String cat = "Food";
    private AddCategoryCommandParser parser = new AddCategoryCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        ModelManager addressBookModel = new ModelManager();
        addressBookModel.addPerson(testPerson);
        ParserUtil.setAddressBookModel(addressBookModel);

        String userInput = " txn/1 c/Food";
        Transaction txn = new Transaction(testPerson, testAmount, testDescription);
        assertParseSuccess(parser, userInput, null);
    }
}
