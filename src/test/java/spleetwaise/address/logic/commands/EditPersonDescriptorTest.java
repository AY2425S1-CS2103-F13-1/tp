package spleetwaise.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import spleetwaise.address.logic.commands.EditCommand.EditPersonDescriptor;
import spleetwaise.address.testutil.EditPersonDescriptorBuilder;

public class EditPersonDescriptorTest {

    @Test
    public void equals() {
        // same values -> returns true
        EditPersonDescriptor descriptorWithSameValues = new EditPersonDescriptor(CommandTestUtil.DESC_AMY);
        Assertions.assertTrue(CommandTestUtil.DESC_AMY.equals(descriptorWithSameValues));

        // same object -> returns true
        Assertions.assertTrue(CommandTestUtil.DESC_AMY.equals(CommandTestUtil.DESC_AMY));

        // null -> returns false
        Assertions.assertFalse(CommandTestUtil.DESC_AMY.equals(null));

        // different types -> returns false
        Assertions.assertFalse(CommandTestUtil.DESC_AMY.equals(5));

        // different values -> returns false
        Assertions.assertFalse(CommandTestUtil.DESC_AMY.equals(CommandTestUtil.DESC_BOB));

        // different name -> returns false
        EditPersonDescriptor editedAmy = new EditPersonDescriptorBuilder(CommandTestUtil.DESC_AMY).withName(
            CommandTestUtil.VALID_NAME_BOB).build();
        Assertions.assertFalse(CommandTestUtil.DESC_AMY.equals(editedAmy));

        // different phone -> returns false
        editedAmy = new EditPersonDescriptorBuilder(CommandTestUtil.DESC_AMY).withPhone(CommandTestUtil.VALID_PHONE_BOB)
            .build();
        Assertions.assertFalse(CommandTestUtil.DESC_AMY.equals(editedAmy));

        // different email -> returns false
        editedAmy = new EditPersonDescriptorBuilder(CommandTestUtil.DESC_AMY).withEmail(CommandTestUtil.VALID_EMAIL_BOB)
            .build();
        Assertions.assertFalse(CommandTestUtil.DESC_AMY.equals(editedAmy));

        // different address -> returns false
        editedAmy = new EditPersonDescriptorBuilder(CommandTestUtil.DESC_AMY).withAddress(
            CommandTestUtil.VALID_ADDRESS_BOB).build();
        Assertions.assertFalse(CommandTestUtil.DESC_AMY.equals(editedAmy));

        // different tags -> returns false
        editedAmy =
            new EditPersonDescriptorBuilder(CommandTestUtil.DESC_AMY).withTags(CommandTestUtil.VALID_TAG_HUSBAND)
                .build();
        Assertions.assertFalse(CommandTestUtil.DESC_AMY.equals(editedAmy));
    }

    @Test
    public void toStringMethod() {
        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();
        String expected = EditPersonDescriptor.class.getCanonicalName() + "{name="
            + editPersonDescriptor.getName().orElse(null) + ", phone="
            + editPersonDescriptor.getPhone().orElse(null) + ", email="
            + editPersonDescriptor.getEmail().orElse(null) + ", address="
            + editPersonDescriptor.getAddress().orElse(null) + ", tags="
            + editPersonDescriptor.getTags().orElse(null) + "}";
        assertEquals(expected, editPersonDescriptor.toString());
    }
}
