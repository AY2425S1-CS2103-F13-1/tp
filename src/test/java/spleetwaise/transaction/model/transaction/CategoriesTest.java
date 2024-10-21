package spleetwaise.transaction.model.transaction;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class CategoriesTest {
    private Categories testCatSet = new Categories("Food");

    @Test
    public void constructor_null_throwsNullPointerException() {
        String testString = null;

        assertThrows(NullPointerException.class, () -> new Categories(testString));
    }

    @Test
    public void add_empty_throwsNullPointerException() {
        String testCat = null;

        assertThrows(NullPointerException.class, () -> new Categories().add(testCat));
    }

    @Test
    public void remove_empty_throwsNullPointerException() {
        String testCat = null;

        assertThrows(NullPointerException.class, () -> new Categories().remove(testCat));
    }

    @Test
    public void isValidCategory_empty_throwsNullPointerException() {
        String testCat = null;

        assertThrows(NullPointerException.class, () -> Categories.isValidCategory(testCat));
    }

}
