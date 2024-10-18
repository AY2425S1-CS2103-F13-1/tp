package spleetwaise.transaction.storage.adapters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import spleetwaise.address.commons.exceptions.IllegalValueException;
import spleetwaise.address.model.AddressBookModel;
import spleetwaise.address.model.person.Person;
import spleetwaise.address.storage.JsonAdaptedPerson;
import spleetwaise.address.testutil.TypicalPersons;
import spleetwaise.commons.IdUtil;
import spleetwaise.transaction.model.transaction.Date;
import spleetwaise.transaction.model.transaction.Description;
import spleetwaise.transaction.model.transaction.Transaction;
import spleetwaise.transaction.testutil.DateUtil;

public class JsonAdaptedTransactionTest {

    private static final String VALID_DESCRIPTION = "description";
    private static final String INVALID_DESCRIPTION = "a".repeat(Description.MAX_LENGTH + 1);
    private static final Person[] TEST_PEOPLE = { TypicalPersons.BENSON };
    private AddressBookModel addressBookModel;


    @BeforeEach
    public void setUp() {
        addressBookModel = new spleetwaise.address.model.ModelManager();
        for (Person p : TEST_PEOPLE) {
            addressBookModel.addPerson(p);
        }
    }

    /**
     * Test the constructor of a JsonAdaptedTransaction with valid values.
     */
    @Test
    public void testConstructor() throws IllegalValueException {
        // Test Json constructor
        JsonAdaptedPerson jPerson = new JsonAdaptedPerson(TypicalPersons.BENSON);
        JsonAdaptedAmount jAmt = new JsonAdaptedAmount("-10.00");
        JsonAdaptedTransaction jTrans = new JsonAdaptedTransaction(IdUtil.getId(), TypicalPersons.BENSON.getId(), jAmt,
                VALID_DESCRIPTION,
                DateUtil.VALID_DATE
        );
        Transaction t = new Transaction(jTrans.getId(), jPerson.toModelType(), jAmt.toModelType(),
                new Description(VALID_DESCRIPTION),
                new Date(DateUtil.VALID_DATE)
        );

        assertEquals(t, jTrans.toModelType(addressBookModel));

        // Test transaction constructor
        jTrans = new JsonAdaptedTransaction(t);

        assertEquals(t, jTrans.toModelType(addressBookModel));
        assertEquals(jAmt.getAmount(), jTrans.getAmount().getAmount());
        assertEquals(VALID_DESCRIPTION, jTrans.getDescription());
        assertEquals(DateUtil.VALID_DATE, jTrans.getDate());
    }

    @Test
    public void testConstructor_invalidId() {
        JsonAdaptedAmount jAmt = new JsonAdaptedAmount("-10.00");
        JsonAdaptedTransaction jTrans = new JsonAdaptedTransaction(null, TypicalPersons.BENSON.getId(), jAmt,
                VALID_DESCRIPTION,
                DateUtil.VALID_DATE
        );
        assertThrows(IllegalValueException.class, () -> jTrans.toModelType(addressBookModel));
    }

    @Test
    public void testConstructor_invalidPerson() {
        JsonAdaptedAmount jAmt = new JsonAdaptedAmount("-10.00");
        JsonAdaptedTransaction jTrans = new JsonAdaptedTransaction(IdUtil.getId(), null, jAmt, VALID_DESCRIPTION,
                DateUtil.VALID_DATE
        );
        assertThrows(IllegalValueException.class, () -> jTrans.toModelType(addressBookModel));
    }

    @Test
    public void testConstructor_invalidPersonId() {
        JsonAdaptedAmount jAmt = new JsonAdaptedAmount("-10.00");
        JsonAdaptedTransaction jTrans = new JsonAdaptedTransaction(IdUtil.getId(), TypicalPersons.ALICE.getId(), jAmt,
                VALID_DESCRIPTION,
                DateUtil.VALID_DATE
        );
        assertThrows(IllegalValueException.class, () -> jTrans.toModelType(addressBookModel));
    }

    @Test
    public void testConstructor_invalidAmount() {
        JsonAdaptedTransaction jTrans = new JsonAdaptedTransaction(IdUtil.getId(), TypicalPersons.BENSON.getId(), null,
                VALID_DESCRIPTION,
                DateUtil.VALID_DATE
        );
        assertThrows(IllegalValueException.class, () -> jTrans.toModelType(addressBookModel));
    }

    @Test
    public void testConstructor_nullDescription() {
        JsonAdaptedAmount jAmt = new JsonAdaptedAmount("-10.00");
        JsonAdaptedTransaction jTrans = new JsonAdaptedTransaction(IdUtil.getId(), TypicalPersons.BENSON.getId(), jAmt,
                null,
                DateUtil.VALID_DATE
        );
        assertThrows(IllegalValueException.class, () -> jTrans.toModelType(addressBookModel));
    }

    @Test
    public void testConstructor_nullDate() {
        JsonAdaptedAmount jAmt = new JsonAdaptedAmount("-10.00");
        JsonAdaptedTransaction jTrans = new JsonAdaptedTransaction(IdUtil.getId(), TypicalPersons.BENSON.getId(), jAmt,
                VALID_DESCRIPTION,
                null
        );
        assertThrows(IllegalValueException.class, () -> jTrans.toModelType(addressBookModel));
    }

    @Test
    public void testConstructor_invalidDate() {
        JsonAdaptedAmount jAmt = new JsonAdaptedAmount("-10.00");
        JsonAdaptedTransaction jTrans = new JsonAdaptedTransaction(IdUtil.getId(), TypicalPersons.BENSON.getId(), jAmt,
                VALID_DESCRIPTION,
                DateUtil.INVALID_DATE
        );
        assertThrows(IllegalValueException.class, () -> jTrans.toModelType(addressBookModel));
    }

    @Test
    public void testConstructor_invalidDescription() {
        JsonAdaptedAmount jAmt = new JsonAdaptedAmount("-10.00");
        JsonAdaptedTransaction jTrans = new JsonAdaptedTransaction(IdUtil.getId(), TypicalPersons.BENSON.getId(), jAmt,
                INVALID_DESCRIPTION,
                DateUtil.VALID_DATE
        );
        assertThrows(IllegalValueException.class, () -> jTrans.toModelType(addressBookModel));
    }

}
