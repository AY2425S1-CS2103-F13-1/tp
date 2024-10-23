package spleetwaise.transaction.logic.parser;

import static spleetwaise.transaction.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import spleetwaise.address.model.ModelManager;
import spleetwaise.address.model.person.Person;
import spleetwaise.address.testutil.TypicalPersons;
import spleetwaise.commons.model.CommonModel;
import spleetwaise.transaction.logic.commands.AddCategoryCommand;
import spleetwaise.transaction.model.transaction.Amount;
import spleetwaise.transaction.model.transaction.Description;
import spleetwaise.transaction.model.transaction.Transaction;

public class AddCategoryCommandParserTest {
    private static Person testPerson = TypicalPersons.ALICE;
    private static Amount testAmount = new Amount("1.23");
    private static Description testDescription = new Description("description");
    private static String cat = "FOOD";
    private AddCategoryCommandParser parser = new AddCategoryCommandParser();

    @BeforeEach
    void setup() {
        CommonModel.initialise(new ModelManager(), new spleetwaise.transaction.model.ModelManager());
    }

    @Test
    public void parse_allFieldsPresent_success() {
        CommonModel.getInstance().addPerson(testPerson);

        String userInput = " txn/1 cat/Food";
        Transaction txn = new Transaction(testPerson, testAmount, testDescription);
        CommonModel.getInstance().addTransaction(txn);
        assertParseSuccess(parser, userInput, new AddCategoryCommand(txn, cat));
    }
}
