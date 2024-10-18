package spleetwaise.address.model.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import spleetwaise.address.model.AddressBook;
import spleetwaise.address.model.ReadOnlyAddressBook;
import spleetwaise.address.model.person.Address;
import spleetwaise.address.model.person.Email;
import spleetwaise.address.model.person.Name;
import spleetwaise.address.model.person.Person;
import spleetwaise.address.model.person.Phone;
import spleetwaise.address.model.person.Remark;
import spleetwaise.address.model.tag.Tag;
import spleetwaise.transaction.model.ReadOnlyTransactionBook;
import spleetwaise.transaction.model.TransactionBook;
import spleetwaise.transaction.model.transaction.Amount;
import spleetwaise.transaction.model.transaction.Date;
import spleetwaise.transaction.model.transaction.Description;
import spleetwaise.transaction.model.transaction.Transaction;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    public static final Remark EMPTY_REMARK = new Remark("");

    private static final Person alexYeoh = new Person(new Name("Alex Yeoh"), new Phone("87438807"),
            new Email("alexyeoh@example.com"),
            new Address("Blk 30 Geylang Street 29, #06-40"), EMPTY_REMARK, getTagSet("friends")
    );
    private static final Person berniceYu = new Person(new Name("Bernice Yu"), new Phone("99272758"),
            new Email("berniceyu"
                    + "@example.com"),
            new Address("Blk 30 Lorong 3 Serangoon Gardens, #07-18"), EMPTY_REMARK,
            getTagSet("colleagues", "friends")
    );
    private static final Person charlotteOliveiro = new Person(new Name("Charlotte Oliveiro"), new Phone("93210283"),
            new Email("charlotte@example.com"),
            new Address("Blk 11 Ang Mo Kio Street 74, #11-04"), EMPTY_REMARK, getTagSet("neighbours")
    );
    private static final Person davidLi = new Person(new Name("David Li"), new Phone("91031282"),
            new Email("lidavid@example.com"),
            new Address("Blk 436 Serangoon Gardens Street 26, #16-43"), EMPTY_REMARK, getTagSet("family")
    );
    private static final Person irfanIbrahim = new Person(new Name("Irfan Ibrahim"), new Phone("92492021"),
            new Email("irfan@example.com"),
            new Address("Blk 47 Tampines Street 20, #17-35"), EMPTY_REMARK, getTagSet("classmates")
    );
    private static final Person royBalakrishnan = new Person(new Name("Roy Balakrishnan"), new Phone("92624417"),
            new Email("royb@example.com"),
            new Address("Blk 45 Aljunied Street 85, #11-31"), EMPTY_REMARK, getTagSet("colleagues")
    );

    public static Person[] getSamplePersons() {
        return new Person[]{ alexYeoh, berniceYu, charlotteOliveiro, davidLi, irfanIbrahim, royBalakrishnan };
    }

    public static Transaction[] getSampleTransactions() {
        return new Transaction[]{
            new Transaction(alexYeoh, new Amount("10.00"), new Description("Mc Donald's"), new Date("01012024")),
            new Transaction(berniceYu, new Amount("5.50"), new Description("Starbucks"), new Date("02022024")),
            new Transaction(
                    charlotteOliveiro, new Amount("8.25"), new Description("Pizza Hut"), new Date("03032024")),
            new Transaction(davidLi, new Amount("12.00"), new Description("NTUC FairPrice"), new Date("04042024")),
            new Transaction(
                    irfanIbrahim, new Amount("-9.50"), new Description("Cold Storage"), new Date("05052024")),
            new Transaction(
                    royBalakrishnan, new Amount("11.25"), new Description("Old Chang Kee"), new Date("06062024"))
        };
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook sampleAb = new AddressBook();
        for (Person samplePerson : getSamplePersons()) {
            sampleAb.addPerson(samplePerson);
        }
        return sampleAb;
    }

    public static ReadOnlyTransactionBook getSampleTransactionBook() {
        TransactionBook sampleTb = new TransactionBook();
        for (Transaction t : getSampleTransactions()) {
            sampleTb.addTransaction(t);
        }
        return sampleTb;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings).map(Tag::new).collect(Collectors.toSet());
    }

}
