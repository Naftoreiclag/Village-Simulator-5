/* Copyright (c) 2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.console;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import naftoreiclag.villagefive.OverworldAppState;
import naftoreiclag.villagefive.addon.AddonManager;
import naftoreiclag.villagefive.addon.LuaAddon;
import naftoreiclag.villagefive.addon.LuaEntity;
import naftoreiclag.villagefive.world.World;
import naftoreiclag.villagefive.world.entity.AddonEntity;
import naftoreiclag.villagefive.world.entity.Entity;
import naftoreiclag.villagefive.world.entity.EntityRegistry;

public class CommandListStuff extends Command {

    @Override
    public boolean process(Console console, String input, OverworldAppState game) {
        
        if(input.toLowerCase().startsWith("index addon")) {
            int addonCount = 0;
            
            for(Map.Entry<String, LuaAddon> pair : AddonManager.addonCollection.entrySet()) {
                LuaAddon addon = pair.getValue();

                console.println("[" + addon.name + "] id=" + addon.id + "\n"
                                + "  Entities:");
                for(LuaEntity entity : addon.entities) {
                    console.println("     " + entity.parent.id + ":" + entity.id);
                    EntityRegistry.register(entity);
                }
                ++ addonCount;
            }
            
            console.println("\nTotal addons: " + addonCount);
            
            return true;
        } else if(input.toLowerCase().startsWith("index entities") || input.toLowerCase().startsWith("index entity")) {
            int entityCount = 0;
            
            List<String> entities = new ArrayList<String>();
            
            for(Map.Entry<String, LuaEntity> pair : EntityRegistry.luaEntities.entrySet()) {
                entities.add(pair.getKey());
                
                ++ entityCount;
            }
            for(Map.Entry<String, Class<? extends Entity>> pair : EntityRegistry.javaEntities.entrySet()) {
                entities.add(pair.getKey());
                
                ++ entityCount;
            }
            
            Collections.sort(entities);
            
            StringBuilder output = new StringBuilder();
            for(String s : entities)
            {
                output.append(s);
                output.append('\n');
            }
            console.println(output.toString());
            
            console.println("Total entities: " + entityCount);
            
            return true;
        } else if(input.toLowerCase().startsWith("index item")) {
            console.println("In addition to all those listed items, all entities count as items.");
            return true;
        }
        return false;
    }

    @Override
    public String[] getHelpLines() {
        return new String[]{"INDEX ADDONS\tList loaded addons.",
        "INDEX ENTITIES\tList loaded entities.",
        "INDEX ITEMS\tList loaded items."};
    }

}
