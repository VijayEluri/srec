/*
 * Copyright 2010 Victor Tatai
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
 * the specific language governing permissions and limitations under the License.
 */

package com.github.srec.command.base;

import com.github.srec.Location;
import com.github.srec.command.ExecutionContext;

/**
 * @author Victor Tatai
 */
public class EndCommand extends BaseCommand {
    public EndCommand(Location location) {
        super("end", location);
    }
    
    @Override
    public CommandFlow run(ExecutionContext context) {
        return CommandFlow.NEXT;
    }
}
