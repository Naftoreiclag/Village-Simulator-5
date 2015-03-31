/* Copyright (c) 2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.console;

import java.util.List;
import naftoreiclag.villagefive.OverworldAppState;
import naftoreiclag.villagefive.world.World;
import naftoreiclag.villagefive.world.entity.Entity;
import naftoreiclag.villagefive.world.entity.PlayerEntity;

public class CommandDeleteStuff extends Command {

    @Override
    public boolean process(Console console, String input, OverworldAppState game) {
        if(input.equalsIgnoreCase("delete entity")) {
            
            PlayerEntity player = game.getPlayer();
            
            Entity lookAt = player.controller.interactRay();
            if(lookAt == null) {
                console.println("Error: no entity in sight!");
                return true;
            }
            
            lookAt.removeSelf();
            
            return true;
        }
        return false;
    }

    @Override
    public String[] getHelpLines() {
        return new String[]{"DELETE ENTITY\tDeletes the entity you are looking at."};
    }

}
