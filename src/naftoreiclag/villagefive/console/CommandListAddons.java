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
import naftoreiclag.villagefive.world.entity.EntityRegistry;

public class CommandListAddons extends Command {

    @Override
    public boolean process(Console console, String input) {
        
        if(input.toLowerCase().startsWith("list addons")) {
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
            
            console.println("Total addons: " + addonCount);
            
            return true;
        }
        return false;
    }

    @Override
    public String[] getHelpLines() {
        return new String[]{"list addons: List loaded addons."};
    }

}
