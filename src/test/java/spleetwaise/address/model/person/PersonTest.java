package spleetwaise.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static spleetwaise.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static spleetwaise.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static spleetwaise.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static spleetwaise.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static spleetwaise.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import spleetwaise.address.testutil.Assert;
import spleetwaise.address.testutil.PersonBuilder;
import spleetwaise.address.testutil.TypicalPersons;

public class PersonTest {

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Person person = new PersonBuilder().build();
        Assert.assertThrows(UnsupportedOperationException.class, () -> person.getTags().remove(0));
    }

    @Test
    public void isSamePerson() {
        // Same id is a definitely not allowed and will be handled by storage layer
        // It is understood by default that new Person objects will always have different id.

        // same object -> returns true
        assertTrue(TypicalPersons.ALICE.isSamePerson(TypicalPersons.ALICE));

        // null -> returns false
        assertFalse(TypicalPersons.ALICE.isSamePerson(null));

        Person editedAlice =
                new PersonBuilder(TypicalPersons.ALICE).withId(TypicalPersons.ALICE.getId()).withPhone(VALID_PHONE_BOB)
                        .withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND).build();
        assertTrue(TypicalPersons.ALICE.hasSameId(editedAlice));
        // Even though isSamePerson returns false, hasSameId returns true. This is to facilitate the editing of
        // the person's details without changing the id
        assertFalse(TypicalPersons.ALICE.isSamePerson(editedAlice));

        // Different id but all details same -> returns true
        // Rationale 1: No two persons should have the exact same name and phone number (on top of having address,
        // email, tags the same), phone number is intuitively understood as unique.
        editedAlice = new PersonBuilder(TypicalPersons.ALICE).withId("edited-alice-uuid").build();
        assertTrue(TypicalPersons.ALICE.isSamePerson(editedAlice));

        // different phone and email -> returns false
        editedAlice =
                new PersonBuilder(TypicalPersons.ALICE).withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(TypicalPersons.ALICE.isSamePerson(editedAlice));

        // same name, all other attributes different -> returns false
        // Rational: There could exist two persons with the same name but different phone numbers
        editedAlice =
                new PersonBuilder(TypicalPersons.ALICE).withId("edited-alice-uuid").withPhone(VALID_PHONE_BOB)
                        .withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND).build();
        assertFalse(TypicalPersons.ALICE.isSamePerson(editedAlice));

        // different name, all other attributes same -> returns false
        // e.g. Alice Pauline -> Bob Pauline (Contact decided to transgender and change name)
        editedAlice = new PersonBuilder(TypicalPersons.ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(TypicalPersons.ALICE.isSamePerson(editedAlice));

        // name differs in case, all other attributes same -> returns false
        Person editedBob = new PersonBuilder(TypicalPersons.BOB).withName(VALID_NAME_BOB.toLowerCase()).build();
        assertFalse(TypicalPersons.BOB.isSamePerson(editedBob));

        // name has trailing spaces, all other attributes same -> returns false
        String nameWithTrailingSpaces = VALID_NAME_BOB + " ";
        editedBob = new PersonBuilder(TypicalPersons.BOB).withName(nameWithTrailingSpaces).build();
        assertFalse(TypicalPersons.BOB.isSamePerson(editedBob));

        // pass in null -> throws error
        assertFalse(TypicalPersons.BOB.isSamePerson(null));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Person aliceCopy = new PersonBuilder(TypicalPersons.ALICE).build();
        assertEquals(TypicalPersons.ALICE, aliceCopy);

        // same object -> returns true
        assertEquals(TypicalPersons.ALICE, TypicalPersons.ALICE);

        // null -> returns false
        assertNotEquals(null, TypicalPersons.ALICE);

        // different type -> returns false
        assertNotEquals(5, TypicalPersons.ALICE);

        // different person -> returns false
        assertNotEquals(TypicalPersons.ALICE, TypicalPersons.BOB);

        // different id -> returns false
        Person editedAlice = new PersonBuilder(TypicalPersons.ALICE).withId("edited-alice-uuid").build();
        assertNotEquals(TypicalPersons.ALICE, editedAlice);

        // different name -> returns false
        editedAlice = new PersonBuilder(TypicalPersons.ALICE).withName(VALID_NAME_BOB).build();
        assertNotEquals(TypicalPersons.ALICE, editedAlice);

        // different phone -> returns false
        editedAlice = new PersonBuilder(TypicalPersons.ALICE).withPhone(VALID_PHONE_BOB).build();
        assertNotEquals(TypicalPersons.ALICE, editedAlice);

        // different email -> returns false
        editedAlice = new PersonBuilder(TypicalPersons.ALICE).withEmail(VALID_EMAIL_BOB).build();
        assertNotEquals(TypicalPersons.ALICE, editedAlice);

        // different address -> returns false
        editedAlice = new PersonBuilder(TypicalPersons.ALICE).withAddress(VALID_ADDRESS_BOB).build();
        assertNotEquals(TypicalPersons.ALICE, editedAlice);

        // different tags -> returns false
        editedAlice = new PersonBuilder(TypicalPersons.ALICE).withTags(VALID_TAG_HUSBAND).build();
        assertNotEquals(TypicalPersons.ALICE, editedAlice);

        // pass in null -> returns false
        assertNotEquals(null, TypicalPersons.ALICE);
    }

    @Test
    public void toStringMethod() {
        String expected = Person.class.getCanonicalName() + "{name=" + TypicalPersons.ALICE.getName() + ", phone="
                + TypicalPersons.ALICE.getPhone()
                + ", email=" + TypicalPersons.ALICE.getEmail() + ", address=" + TypicalPersons.ALICE.getAddress()
                + ", tags=" + TypicalPersons.ALICE.getTags() + "}";
        assertEquals(expected, TypicalPersons.ALICE.toString());
    }

    @Test
    public void hashCodeMethod() {
        Set<Person> s1 = new HashSet<>();
        s1.add(TypicalPersons.ALICE);
        assertFalse(s1.contains(TypicalPersons.BOB));
        assertTrue(s1.contains(TypicalPersons.ALICE));
    }
}
