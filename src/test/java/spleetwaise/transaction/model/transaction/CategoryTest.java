package spleetwaise.transaction.model.transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import spleetwaise.address.testutil.Assert;

public class CategoryTest {
    private Category testCat = new Category("FOOD");

    @Test
    public void constructor_null_throwsNullPointerException() {
        String testString = null;

        assertThrows(NullPointerException.class, () -> new Category(testString));
    }

    @Test
    public void constructor_invalidCatName_throwsIllegalArgumentException() {
        String invalidCatName = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Category(invalidCatName));
    }

    @Test
    public void isValidCatName() {
        // null cat name
        Assert.assertThrows(NullPointerException.class, () -> Category.isValidCatName(null));
    }

    @Test
    public void validCatName() {
        assertEquals(testCat, new Category("FOOD"));
    }
}
