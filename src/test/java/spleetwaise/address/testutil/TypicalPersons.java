package spleetwaise.address.testutil;

import static spleetwaise.address.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static spleetwaise.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static spleetwaise.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static spleetwaise.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static spleetwaise.address.logic.commands.CommandTestUtil.VALID_ID_AMY;
import static spleetwaise.address.logic.commands.CommandTestUtil.VALID_ID_BOB;
import static spleetwaise.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static spleetwaise.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static spleetwaise.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static spleetwaise.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static spleetwaise.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static spleetwaise.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import spleetwaise.address.model.AddressBook;
import spleetwaise.address.model.person.Person;

/**
 * A utility class containing a list of {@code Person} objects to be used in tests.
 */
public class TypicalPersons {

    public static final Person ALICE = new PersonBuilder().withId("29705d8f-6a2b-4de3-8b86-e8efb3f2215f")
            .withName("Alice Pauline").withAddress("123, Jurong West Ave 6, #08-111").withEmail("alice@example.com")
            .withPhone("94351253").withTags("friends").build();
    public static final Person BENSON = new PersonBuilder().withId("3929a136-823a-4806-af62-e96b241167b5")
            .withName("Benson Meier")
            .withAddress("311, Clementi Ave 2, #02-25").withEmail("johnd@example.com").withPhone("98765432")
            .withTags("owesMoney", "friends").build();
    public static final Person CARL = new PersonBuilder().withId("60196500-3d01-4e6c-81de-44ab6fc9e7e5")
            .withName("Carl Kurz").withPhone(
                    "95352563").withEmail("heinz@example.com").withAddress("wall street").build();
    public static final Person DANIEL = new PersonBuilder().withId("b89d7c94-9823-4b6a-ae46-b7c55ee41727")
            .withName("Daniel Meier").withPhone(
                    "87652533").withEmail("cornelia@example.com").withAddress("10th street").withTags("friends")
            .build();
    public static final Person ELLE = new PersonBuilder().withId("42984eac-421f-4966-994d-6f360d7d2882")
            .withName("Elle Meyer").withPhone(
                    "9482224").withEmail("werner@example.com").withAddress("michegan ave").build();
    public static final Person FIONA = new PersonBuilder().withId("c5a93d37-44fd-4f4d-964b-6f85c715d6e3")
            .withName("Fiona Kunz").withPhone(
                    "9482427").withEmail("lydia@example.com").withAddress("little tokyo").build();
    public static final Person GEORGE = new PersonBuilder().withId("c5a93d37-44fd-4f4d-964b-6f85c715djf93")
            .withName("George Best").withPhone(
                    "9482442").withEmail("anna@example.com").withAddress("4th street").build();

    // Manually added
    public static final Person HOON = new PersonBuilder().withId("c5a93d37-44fd-4f4d-964b-6f85c715djqw3")
            .withName("Hoon Meier").withPhone("8482424").withEmail("stefan@example.com")
            .withAddress("little india").build();
    public static final Person IDA = new PersonBuilder().withId("c5a93d37-44fd-4f4d-964b-6f85c715djgr5")
            .withName("Ida Mueller").withPhone("8482131")
            .withEmail("hans@example.com").withAddress("chicago ave").build();

    // Manually added - Person's details found in {@code CommandTestUtil}
    public static final Person AMY = new PersonBuilder().withId(VALID_ID_AMY).withName(VALID_NAME_AMY)
            .withPhone(VALID_PHONE_AMY).withEmail(VALID_EMAIL_AMY).withAddress(VALID_ADDRESS_AMY)
            .withTags(VALID_TAG_FRIEND).build();
    public static final Person BOB =
            new PersonBuilder().withId(VALID_ID_BOB).withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                    .withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB)
                    .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND)
                    .build();

    public static final String KEYWORD_MATCHING_MEIER = "Meier"; // A keyword that matches MEIER

    private TypicalPersons() {
    } // prevents instantiation

    /**
     * Returns an {@code AddressBook} with all the typical persons.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Person person : getTypicalPersons()) {
            ab.addPerson(person);
        }
        return ab;
    }

    public static List<Person> getTypicalPersons() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }
}
