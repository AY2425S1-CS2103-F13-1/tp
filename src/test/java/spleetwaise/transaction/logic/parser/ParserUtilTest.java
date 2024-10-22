package spleetwaise.transaction.logic.parser;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import spleetwaise.address.logic.parser.exceptions.ParseException;
import spleetwaise.address.model.AddressBookModel;
import spleetwaise.address.model.ModelManager;
import spleetwaise.address.model.person.Person;
import spleetwaise.address.model.person.Phone;
import spleetwaise.address.testutil.TypicalPersons;
import spleetwaise.commons.model.CommonModel;
import spleetwaise.transaction.model.transaction.Amount;
import spleetwaise.transaction.model.transaction.Date;
import spleetwaise.transaction.model.transaction.Description;

public class ParserUtilTest {
    private static final String INVALID_AMOUNT = "+123.456";
    private static final String VALID_AMOUNT = "-123.45";

    private static final String INVALID_DESCRIPTION = "";
    private static final String VALID_DESCRIPTION = "description";

    private static final String INVALID_DATE = "112024";
    private static final String VALID_DATE = "01012024";

    private static final Phone TEST_PHONE = TypicalPersons.ALICE.getPhone();

    @Test
    public void parseAmount_null_exceptionThrown() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseAmount(null));
    }

    @Test
    public void parseAmount_invalidAmount_exceptionThrown() {
        assertThrows(ParseException.class, () -> ParserUtil.parseAmount(INVALID_AMOUNT));
    }

    @Test
    public void parseAmount_validAmount_success() {
        Amount result = assertDoesNotThrow(() -> ParserUtil.parseAmount(VALID_AMOUNT));
        assertEquals(new Amount("-123.45"), result);
    }

    @Test
    public void parseDescription_null_exceptionThrown() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseDescription(null));
    }

    @Test
    public void parseDescription_invalidDescription_exceptionThrown() {
        assertThrows(ParseException.class, () -> ParserUtil.parseDescription(INVALID_DESCRIPTION));
    }

    @Test
    public void parseDescription_validDescription_success() {
        Description result = assertDoesNotThrow(() -> ParserUtil.parseDescription(VALID_DESCRIPTION));
        assertEquals(new Description("description"), result);
    }

    @Test
    public void parseDate_null_exceptionThrown() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseDate(null));
    }

    @Test
    public void parseDate_invalidDate_exceptionThrown() {
        assertThrows(ParseException.class, () -> ParserUtil.parseDate(INVALID_DATE));
    }

    @Test
    public void parseDate_validDate_success() {
        Date result = assertDoesNotThrow(() -> ParserUtil.parseDate(VALID_DATE));
        assertEquals(new Date("01012024"), result);
    }

    @Test
    public void getPersonFromPhone_null_exceptionThrown() {
        assertThrows(NullPointerException.class, () -> ParserUtil.getPersonFromPhone(null));
    }

    @Test
    public void getPersonFromPhone_personNotFound_exceptionThrown() {
        AddressBookModel abModel = new ModelManager();
        CommonModel.initialise(abModel, null);

        assertThrows(ParseException.class, () -> ParserUtil.getPersonFromPhone(TEST_PHONE));
    }

    @Test
    public void getPersonFromPhone_personFound_success() {
        AddressBookModel abModel = new ModelManager();
        CommonModel.initialise(abModel, null);
        abModel.addPerson(TypicalPersons.ALICE);

        Person testPerson = assertDoesNotThrow(() -> ParserUtil.getPersonFromPhone(TEST_PHONE));
        assertEquals(TypicalPersons.ALICE, testPerson);
    }
}
