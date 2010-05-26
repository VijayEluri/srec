package com.github.srec.command;

import com.github.srec.command.exception.CommandExecutionException;
import com.github.srec.command.parser.ParseLocation;
import com.github.srec.command.value.StringValue;
import com.github.srec.command.value.Value;

/**
 * Represents a literal value.
 * 
 * @author Victor Tatai
 */
public class LiteralCommand extends BaseCommand implements ValueCommand {
    private Value value;

    public LiteralCommand(Value value) {
        super(value.get().toString());
        this.value = value;
    }

    /**
     * Shortcut constructor for a string literal.
     *
     * @param value The value
     */
    public LiteralCommand(String value) {
        super(value);
        this.value = new StringValue(value);
    }

    public LiteralCommand(ParseLocation location, Value value) {
        super(value.get().toString(), location);
        this.value = value;
    }

    @Override
    public void run(ExecutionContext context) throws CommandExecutionException {
    }

    @Override
    public Value getValue(ExecutionContext context) {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
