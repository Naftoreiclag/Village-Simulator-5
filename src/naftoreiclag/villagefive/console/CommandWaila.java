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

public class CommandWaila extends Command {

    @Override
    public boolean process(Console console, String input, OverworldAppState game) {
        if(input.equalsIgnoreCase("waila")) {
            
            Entity lookAt = game.getPlayer().controller.interactRay();
            
            if(lookAt != null) {
                console.println("ENTITY:");
                console.println("json = " + lookAt.toJSONString());
                console.println("id = " + lookAt.getEntityId());
                console.println("location = " + lookAt.getLocation());
                return true;
            }
            
            console.println("NOTHING");
            
            return true;
        }
        return false;
    }

    @Override
    public String[] getHelpLines() {
        return new String[]{"WAILA\t(Tries to) tell you what you are looking at."};
    }

}
