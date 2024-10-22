package spleetwaise.transaction.logic.parser;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import spleetwaise.address.logic.parser.exceptions.ParseException;
import spleetwaise.address.testutil.TypicalPersons;
import spleetwaise.commons.logic.commands.Command;
import spleetwaise.commons.model.CommonModel;
import spleetwaise.transaction.logic.commands.AddCommand;
import spleetwaise.transaction.logic.commands.ClearCommand;
import spleetwaise.transaction.testutil.TransactionUtil;


public class TransactionParserTest {

    private final TransactionParser parser = new TransactionParser();

    @Test
    public void parseCommand_add() throws Exception {
        spleetwaise.address.model.ModelManager aBModel = new spleetwaise.address.model.ModelManager();
        aBModel.addPerson(TypicalPersons.ALICE);
        CommonModel.initialise(aBModel, null);
        AddCommand command = (AddCommand) parser.parseCommand(TransactionUtil.getAddCommand());
        assert command != null;
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_unknown() throws Exception {
        Command command = parser.parseCommand("420YoloSwag");
        assertNull(command);
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
