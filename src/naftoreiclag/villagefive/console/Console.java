/* Copyright (c) 2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.console;

// Facade for console-y things.

import java.util.ArrayList;
import java.util.List;

public final class Console {
    private final DevConsoleAppState realConsole;
    
    public final List<Command> knownCommands = new ArrayList<Command>();
    
    protected Console(DevConsoleAppState console)
    {
        this.realConsole = console;
        knownCommands.add(new Command(){
            @Override
            public boolean process(Console console, String input) {
                String[] inputs = input.split(" ");
                
                if(inputs[0].equals("help"))
                {
                    String search;
                    if(inputs.length <= 1) {
                        search = "";
                    } else {
                        search = inputs[1];
                    }
                    
                    console.println("Available commands:");

                    boolean foundCommandFromSearch = false;
                    for(Command command : knownCommands) {
                        String[] helpLines = command.getHelpLines();

                        for(String helpLine : helpLines)
                        {
                            if(helpLine != null) {
                                if(helpLine.startsWith(search)) {
                                    console.println("    " + helpLine);
                                    foundCommandFromSearch = true;
                                }
                            }
                        }
                    }

                    if(!foundCommandFromSearch) {
                        console.println("No commands found.");
                    }
                    
                    return true;
                } else if(inputs[0].equalsIgnoreCase("clear")) {
                    console.clear();
                    return true;
                }
                return false;
            }

            @Override
            public String[] getHelpLines() { return new String[]{
                "help: Displays this help menu.", 
                "help <search>: Shows only commands that start with <search>.",
                "clear: Clears console."};}
        });
        
        this.println("This is a console. You have no idea how long this took to make.\n"
                + "Type \"help\" for help.");
    }
    
    protected void processRawInput(String input)
    { 
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
    
    public void println(String printMe)
    {
        realConsole.printLine(printMe);
    }
    
    public void clear()
    {
        realConsole.clearOutput();
    }
}
