/* Copyright (c) 2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.console;

import naftoreiclag.villagefive.OverworldAppState;
import naftoreiclag.villagefive.world.World;

public class CommandSaveAndLoad extends Command{

    @Override
    public boolean process(Console console, String input, OverworldAppState game) {
        
        if(input.equalsIgnoreCase("save")) {
            
            game.saveworld();
            console.println("saved successfully");
            
            return true;
        } else if(input.equalsIgnoreCase("load")) {
            
            game.loadworld();
            console.println("loaded successfully");
            
            return true;
        }
        
        return false;
    }

    @Override
    public String[] getHelpLines() {
        return new String[]{
            "SAVE\tSaves the game.",
            "LOAD\tLoads the game."
        };
    }

}
