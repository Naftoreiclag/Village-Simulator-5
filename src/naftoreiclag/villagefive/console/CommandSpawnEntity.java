/* Copyright (c) 2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.console;

import java.util.Map;
import naftoreiclag.villagefive.addon.AddonManager;
import naftoreiclag.villagefive.addon.LuaAddon;
import naftoreiclag.villagefive.addon.LuaEntity;
import naftoreiclag.villagefive.util.CommandyString;
import naftoreiclag.villagefive.util.math.Vec2;
import naftoreiclag.villagefive.world.World;
import naftoreiclag.villagefive.world.entity.Entity;
import naftoreiclag.villagefive.world.entity.EntityRegistry;

public class CommandSpawnEntity extends Command {

    @Override
    public boolean process(Console console, String input, World world) {
        
        CommandyString inputs = new CommandyString(input.split(" "));
        
        if(inputs.get(0).equalsIgnoreCase("spawn")) {
            
            String entity = inputs.get(1);
            
            if(entity.equals("")) {
                console.println("Enter an entity id.");
            }
            
            if(EntityRegistry.exists(entity))
            {
                Entity e = world.materializeEntityByName(entity);
                e.setLocation(new Vec2(256, 256));
            }
            else {
                console.println("No entity named " + entity);
            }
            
            return true;
        }
        return false;
    }

    @Override
    public String[] getHelpLines() {
        return new String[]{"SPAWN <ENTITY>: Perform some magic."};
    }

}
