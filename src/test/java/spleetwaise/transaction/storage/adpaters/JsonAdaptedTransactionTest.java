package spleetwaise.transaction.storage.adpaters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import spleetwaise.address.commons.exceptions.IllegalValueException;
import spleetwaise.address.storage.JsonAdaptedPerson;
import spleetwaise.address.testutil.TypicalPersons;
import spleetwaise.commons.IdUtil;
import spleetwaise.transaction.model.transaction.Date;
import spleetwaise.transaction.model.transaction.Description;
import spleetwaise.transaction.model.transaction.Transaction;
import spleetwaise.transaction.storage.adapters.JsonAdaptedAmount;
import spleetwaise.transaction.storage.adapters.JsonAdaptedTransaction;
import spleetwaise.transaction.testutil.DateUtil;

public class JsonAdaptedTransactionTest {

    private static final String VALID_DESCRIPTION = "description";
    private static final String INVALID_DESCRIPTION = "a".repeat(Description.MAX_LENGTH + 1);

    /**
     * Test the constructor of a JsonAdaptedTransaction with valid values.
     */
    @Test
    public void testConstructor() throws IllegalValueException {
        // Test Json constructor
        JsonAdaptedPerson jPerson = new JsonAdaptedPerson(TypicalPersons.BENSON);
        JsonAdaptedAmount jAmt = new JsonAdaptedAmount("-10.00");
        JsonAdaptedTransaction jTrans = new JsonAdaptedTransaction(IdUtil.getId(), jPerson, jAmt, VALID_DESCRIPTION,
                DateUtil.VALID_DATE
        );
        Transaction t = new Transaction(jTrans.getId(), jPerson.toModelType(), jAmt.toModelType(),
                new Description(VALID_DESCRIPTION),
                new Date(DateUtil.VALID_DATE)
        );

        assertEquals(t, jTrans.toModelType());

        // Test transaction constructor
        jTrans = new JsonAdaptedTransaction(t);

        assertEquals(t, jTrans.toModelType());
    }

    @Test
    public void testConstructor_invalidId() {
        JsonAdaptedPerson jPerson = new JsonAdaptedPerson(TypicalPersons.BENSON);
        JsonAdaptedAmount jAmt = new JsonAdaptedAmount("-10.00");
        JsonAdaptedTransaction jTrans = new JsonAdaptedTransaction(null, jPerson, jAmt, VALID_DESCRIPTION,
                DateUtil.VALID_DATE
        );
        assertThrows(IllegalValueException.class, () -> jTrans.toModelType());
    }

    @Test
    public void testConstructor_invalidPerson() {
        JsonAdaptedAmount jAmt = new JsonAdaptedAmount("-10.00");
        JsonAdaptedTransaction jTrans = new JsonAdaptedTransaction(IdUtil.getId(), null, jAmt, VALID_DESCRIPTION,
                DateUtil.VALID_DATE
        );
        assertThrows(IllegalValueException.class, () -> jTrans.toModelType());
    }

    @Test
    public void testConstructor_invalidAmount() {
        JsonAdaptedPerson jPerson = new JsonAdaptedPerson(TypicalPersons.BENSON);
        JsonAdaptedTransaction jTrans = new JsonAdaptedTransaction(IdUtil.getId(), jPerson, null, VALID_DESCRIPTION,
                DateUtil.VALID_DATE
        );
        assertThrows(IllegalValueException.class, () -> jTrans.toModelType());
    }

    @Test
    public void testConstructor_nullDescription() {
        JsonAdaptedPerson jPerson = new JsonAdaptedPerson(TypicalPersons.BENSON);
        JsonAdaptedAmount jAmt = new JsonAdaptedAmount("-10.00");
        JsonAdaptedTransaction jTrans = new JsonAdaptedTransaction(IdUtil.getId(), jPerson, jAmt, null,
                DateUtil.VALID_DATE
        );
        assertThrows(IllegalValueException.class, () -> jTrans.toModelType());
    }

    @Test
    public void testConstructor_nullDate() {
        JsonAdaptedPerson jPerson = new JsonAdaptedPerson(TypicalPersons.BENSON);
        JsonAdaptedAmount jAmt = new JsonAdaptedAmount("-10.00");
        JsonAdaptedTransaction jTrans = new JsonAdaptedTransaction(IdUtil.getId(), jPerson, jAmt, VALID_DESCRIPTION,
                null
        );
        assertThrows(IllegalValueException.class, () -> jTrans.toModelType());
    }

    @Test
    public void testConstructor_invalidDate() {
        JsonAdaptedPerson jPerson = new JsonAdaptedPerson(TypicalPersons.BENSON);
        JsonAdaptedAmount jAmt = new JsonAdaptedAmount("-10.00");
        JsonAdaptedTransaction jTrans = new JsonAdaptedTransaction(IdUtil.getId(), jPerson, jAmt, VALID_DESCRIPTION,
                DateUtil.INVALID_DATE
        );
        assertThrows(IllegalValueException.class, () -> jTrans.toModelType());
    }

    @Test
    public void testConstructor_invalidDescription() {
        JsonAdaptedPerson jPerson = new JsonAdaptedPerson(TypicalPersons.BENSON);
        JsonAdaptedAmount jAmt = new JsonAdaptedAmount("-10.00");
        JsonAdaptedTransaction jTrans = new JsonAdaptedTransaction(IdUtil.getId(), jPerson, jAmt, INVALID_DESCRIPTION,
                DateUtil.VALID_DATE
        );
        assertThrows(IllegalValueException.class, () -> jTrans.toModelType());
    }

}
