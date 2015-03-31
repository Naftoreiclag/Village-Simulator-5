/* Copyright (c) 2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.console;

// Facade for console-y things.

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import naftoreiclag.villagefive.OverworldAppState;
import naftoreiclag.villagefive.world.World;

public final class Console {
    private final DevConsoleAppState realConsole;
    public OverworldAppState game;
    
    private final List<Command> knownCommands = new ArrayList<Command>();
    private final List<String> helpInfo = new ArrayList<String>();
    
    protected Console(DevConsoleAppState console)
    {
        this.realConsole = console;
        this.addCommand(new Command(){
            @Override
            public boolean process(Console console, String input, OverworldAppState game) {
                
                if(input.equalsIgnoreCase("r")) {
                    
                    console.processRawInput(console.previousInput);
                    
                    return true;
                }
                
                String[] inputs = input.split(" ");
                
                if(inputs[0].equalsIgnoreCase("help"))
                {
                    String search;
                    if(inputs.length <= 1) {
                        search = "";
                    } else {
                        search = inputs[1];
                    }
                    
                    console.println("Available commands:");

                    boolean foundCommandFromSearch = false;
                    for(String helpLine : helpInfo)
                    {
                        if(helpLine != null) {
                            if(helpLine.toLowerCase().startsWith(search.toLowerCase())) {
                                console.println('\t' + helpLine);
                                foundCommandFromSearch = true;
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
                "HELP\tDisplays this help menu.", 
                "HELP <SEARCH>\tShows only commands that start with <SEARCH>.",
                "CLEAR\tClears console.",
                "R\tRepeats previous command."};}
        });
        
        this.addCommand(new CommandHelloWorld());
        this.addCommand(new CommandListStuff());
        this.addCommand(new CommandSpawnStuff());
        this.addCommand(new CommandSaveAndLoad());
        this.addCommand(new CommandDeleteStuff());
        this.addCommand(new CommandWaila());
        
        this.println("This is a console. You have no idea how long this took to make.\n"
                + "Type \"help\" for help.");
    }
    
    private String previousInput;
    public String getPreviousInput() { return previousInput; }
    protected void processRawInput(String input)
    {
        if(!input.equalsIgnoreCase("r")) {
            previousInput = input;
        }
        
        boolean didAnyWork = false;
        for(Command command : knownCommands) {
            if(command.process(this, input, game)) {
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

    void addCommand(Command command) {
        this.knownCommands.add(command);
        
        String[] helpInfoArray = command.getHelpLines();
        
        if(helpInfoArray != null)
        {
            helpInfo.addAll(Arrays.asList(helpInfoArray));
        }
        Collections.sort(helpInfo);
    }
}
