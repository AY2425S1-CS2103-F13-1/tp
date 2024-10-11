package spleetwaise.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import spleetwaise.address.logic.commands.AddCommand;
import spleetwaise.address.logic.commands.ClearCommand;
import spleetwaise.address.logic.commands.DeleteCommand;
import spleetwaise.address.logic.commands.EditCommand;
import spleetwaise.address.logic.commands.EditCommand.EditPersonDescriptor;
import spleetwaise.address.logic.commands.ExitCommand;
import spleetwaise.address.logic.commands.FindCommand;
import spleetwaise.address.logic.commands.HelpCommand;
import spleetwaise.address.logic.commands.ListCommand;
import spleetwaise.address.logic.parser.exceptions.ParseException;
import spleetwaise.address.model.person.NameContainsKeywordsPredicate;
import spleetwaise.address.model.person.Person;
import spleetwaise.address.testutil.EditPersonDescriptorBuilder;
import spleetwaise.address.testutil.PersonBuilder;
import spleetwaise.address.testutil.PersonUtil;
import spleetwaise.address.testutil.TypicalIndexes;

public class AddressBookParserTest {

    private final AddressBookParser parser = new AddressBookParser();

    @Test
    public void parseCommand_add() throws Exception {
        Person person = new PersonBuilder().build();
        AddCommand command = (AddCommand) parser.parseCommand(PersonUtil.getAddCommand(person));
        assertEquals(new AddCommand(person), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
            DeleteCommand.COMMAND_WORD + " " + TypicalIndexes.INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new DeleteCommand(TypicalIndexes.INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Person person = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(person).build();
        EditCommand command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_WORD + " "
            + TypicalIndexes.INDEX_FIRST_PERSON.getOneBased() + " " + PersonUtil.getEditPersonDescriptorDetails(
            descriptor));
        assertEquals(new EditCommand(TypicalIndexes.INDEX_FIRST_PERSON, descriptor), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_find() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindCommand command = (FindCommand) parser.parseCommand(
            FindCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCommand(new NameContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " 3") instanceof ListCommand);
    }

    @Test
    public void parseCommand_unrecognisedInput_returnsNull() throws ParseException {
        assertNull(parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_returnsNull() throws ParseException {
        assertNull(parser.parseCommand("unknownCommand"));
    }
}
