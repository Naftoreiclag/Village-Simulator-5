/* Copyright (c) 2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.addon;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import naftoreiclag.villagefive.SAM;
import naftoreiclag.villagefive.world.entity.EntityRegistry;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

// Addons can be applied upon world creation.
// Some worlds that you download may specify that certain addons need to be present in the collection.
public class AddonManager
{
    public static Map<String, LuaAddon> addonCollection = new HashMap<String, LuaAddon>();
    
    
    public static void reloadAddons()
    {
        addonCollection.clear();
        
        File[] pluginRoots = (new File(SAM.ADDON_DIR)).listFiles((FileFilter) DirectoryFileFilter.DIRECTORY);
        
        // 
        for(File pluginRoot : pluginRoots)
        {
            System.out.println(pluginRoot);
            String addon_root = pluginRoot.getPath().replace('\\', '/') + "/";
            
            Globals globals = JsePlatform.standardGlobals();
            globals.set("ADDON_ROOT", addon_root);
            globals.loadfile("lua/globals.lua").call();

            LuaTable data = globals.loadfile(addon_root + "addon.lua").call().checktable();

            LuaAddon addon = new LuaAddon(pluginRoot.getName() + "\\", data);
            
            addonCollection.put(addon.id, addon);
        }
        
        for(Map.Entry<String, LuaAddon> pair : addonCollection.entrySet())
        {
            LuaAddon addon = pair.getValue();
            
            System.out.println(addon.id);
            for(LuaEntity entity : addon.entities)
            {
                System.out.println(entity.parent.id + ":" + entity.id);
                EntityRegistry.register(entity);
            }
        }
        
        EntityRegistry.debug();
    }
}
