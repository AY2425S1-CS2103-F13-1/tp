package spleetwaise.address.logic.commands;

import org.junit.jupiter.api.Test;

import spleetwaise.address.model.Model;
import spleetwaise.address.model.ModelManager;

public class ExitCommandTest {

    private Model model = new ModelManager();
    private Model expectedModel = new ModelManager();

    @Test
    public void execute_exit_success() {
        CommandResult expectedCommandResult = new CommandResult(ExitCommand.MESSAGE_EXIT_ACKNOWLEDGEMENT, false, true);
        CommandTestUtil.assertCommandSuccess(new ExitCommand(), model, expectedCommandResult, expectedModel);
    }
}
