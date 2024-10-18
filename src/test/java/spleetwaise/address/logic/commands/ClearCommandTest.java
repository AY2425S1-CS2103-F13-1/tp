package spleetwaise.address.logic.commands;

import org.junit.jupiter.api.Test;

import spleetwaise.address.model.AddressBook;
import spleetwaise.address.model.AddressBookModel;
import spleetwaise.address.model.ModelManager;
import spleetwaise.address.model.UserPrefs;
import spleetwaise.address.testutil.TypicalPersons;

public class ClearCommandTest {

    @Test
    public void execute_emptyAddressBook_success() {
        AddressBookModel model = new ModelManager();
        AddressBookModel expectedModel = new ModelManager();

        CommandTestUtil.assertCommandSuccess(new ClearCommand(), model, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_nonEmptyAddressBook_success() {
        AddressBookModel model = new ModelManager(TypicalPersons.getTypicalAddressBook(), new UserPrefs());
        AddressBookModel expectedModel = new ModelManager(TypicalPersons.getTypicalAddressBook(), new UserPrefs());
        expectedModel.setAddressBook(new AddressBook());

        CommandTestUtil.assertCommandSuccess(new ClearCommand(), model, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

}
