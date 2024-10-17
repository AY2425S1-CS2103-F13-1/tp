package spleetwaise.transaction.model.transaction;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Represents a Transaction's tags in the transaction book.
 */
public class Tags {
    private final HashSet<String> tagSet;

    /**
     * Constructs a {@code Tags}
     */
    public Tags() {
        tagSet = new HashSet<>();
    }

    /**
     * Constructs a {@code Tags}
     *
     * @param tagStr A String representation of all tags that needs to be added.
     */
    public Tags(String tagStr) {
        String[] arrTagStr = tagStr.split("/tag");
        this.tagSet = new HashSet<>();

        this.tagSet.addAll(Arrays.asList(arrTagStr));
    }

    /**
     * Constructs a {@code Tags}
     *
     * @param tagSet A Hashset that contains all tags the transaction should have.
     */
    public Tags(HashSet<String> tagSet) {
        this.tagSet = tagSet;
    }

    /**
     * Add a tag into the tagSet
     *
     * @param tag The tag to be added
     */
    public boolean add(String tag) {
        return tagSet.add(tag);
    }

    /**
     * remove a tag into the tagSet
     *
     * @param tag The tag to be removed
     */
    public boolean remove(String tag) {
        return tagSet.remove(tag);
    }

    /**
     * Check for a tag within the tagSet
     *
     * @param tag The tag that is checked for
     */
    public boolean contains(String tag) {
        return tagSet.contains(tag);
    }
}
