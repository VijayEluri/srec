package com.github.srec.play.jemmy.command;

import com.github.srec.play.Command;
import com.github.srec.play.exception.IllegalParametersException;

import static com.github.srec.play.jemmy.JemmyDSL.frame;

/**
 * @author Victor Tatai
 */
public class CloseCommand implements Command {
    @Override
    public String getName() {
        return "close";
    }

    @Override
    public void run(String... params) {
        if (params.length != 1) throw new IllegalParametersException("Missing parameter to close");
        frame(params[0]).close();
    }
    
    @Override
    public String toString() {
        return getName();
    }
}