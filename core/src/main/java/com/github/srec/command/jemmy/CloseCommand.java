package com.github.srec.command.jemmy;

import com.github.srec.command.ExecutionContext;
import com.github.srec.command.ExecutionContextCommand;
import com.github.srec.command.value.Value;
import org.netbeans.jemmy.JemmyException;

import java.util.Map;

import static com.github.srec.jemmy.JemmyDSL.frame;

/**
 * @author Victor Tatai
 */
@ExecutionContextCommand
public class CloseCommand extends JemmyEventCommand {
    public CloseCommand() {
        super("close", createParametersDefinition(LOCATOR));
    }

    @Override
    protected void runJemmy(ExecutionContext ctx, Map<String, Value> params) throws JemmyException {
        frame(coerceToString(params.get(LOCATOR), ctx)).close();
    }
}