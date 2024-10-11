package spleetwaise.address.logic;

import java.nio.file.Path;

import javafx.collections.ObservableList;
import spleetwaise.address.commons.core.GuiSettings;
import spleetwaise.address.logic.commands.CommandResult;
import spleetwaise.address.logic.commands.exceptions.CommandException;
import spleetwaise.address.logic.parser.exceptions.ParseException;
import spleetwaise.address.model.Model;
import spleetwaise.address.model.ReadOnlyAddressBook;
import spleetwaise.address.model.person.Person;
import spleetwaise.commons.exceptions.SpleetWaiseCommandException;

/**
 * API of the Logic component
 */
public interface Logic {
    /**
     * Executes the command and returns the result.
     * @param commandText The command as entered by the user.
     * @return the result of the command execution.
     * @throws CommandException If an error occurs during command execution.
     * @throws ParseException If an error occurs during parsing.
     */
    CommandResult execute(String commandText) throws SpleetWaiseCommandException, ParseException;

    /**
     * Returns the AddressBook.
     *
     * @see Model#getAddressBook()
     */
    ReadOnlyAddressBook getAddressBook();

    /** Returns an unmodifiable view of the filtered list of persons */
    ObservableList<Person> getFilteredPersonList();

    /**
     * Returns the user prefs' address book file path.
     */
    Path getAddressBookFilePath();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Set the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);
}
