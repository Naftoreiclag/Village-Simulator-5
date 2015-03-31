/* Copyright (c) 2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.console;

// Facade for console-y things.

import java.util.ArrayList;
import java.util.List;

public class Console {
    private final DevConsoleAppState realConsole;
    
    public final List<Command> knownCommands = new ArrayList<Command>();
    
    protected Console(DevConsoleAppState console)
    {
        this.realConsole = console;
    }
    
    protected void processRawInput(String input)
    {
        if(input.startsWith("help"))
        {
            String beginning;
        
            if(input.length() > 5) {
                beginning = input.substring(5);
            } else {
                beginning = "";
            }
            
            this.println("Available Commands:");
            
            if("".equals(beginning)) {
                this.println(" help: Displays this help menu.\n"
                    + " help <search>: Shows only commands that start with <search>");
            }
            
            boolean foundCommandFromSearch = false;
            for(Command command : knownCommands) {
                String helpLine = command.getHelpLine();

                if(helpLine != null) {
                    if(helpLine.startsWith(beginning)) {
                        this.println(" " + helpLine);
                        foundCommandFromSearch = true;
                    }
                }
            }
            
            if(!foundCommandFromSearch) {
                this.println("No commands found for [" + beginning + "]");
            }
        } else {
            boolean didAnyWork = false;
            for(Command command : knownCommands) {
                if(command.process(this, input)) {
                    didAnyWork = true;
                    break;
                }
            }
            
            if(!didAnyWork) {
                this.println("Unknown command. Type \"help\" for help.");
            }
        }
    }
    
    public void println(String printMe)
    {
        realConsole.printLine(printMe);
    }
}
