package com.github.srec.command.jemmy;

import com.github.srec.command.ExecutionContext;
import org.netbeans.jemmy.JemmyException;

import static com.github.srec.jemmy.JemmyDSL.click;

/**
 * @author Victor Tatai
 */
public class ClickCommand extends JemmyEventCommand {
    public ClickCommand() {
        super("click", "componentLocator");
    }

    @Override
    protected void runJemmy(ExecutionContext ctx, String... params) throws JemmyException {
        click(params[0]);
    }
}