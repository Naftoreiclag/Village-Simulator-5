/* Copyright (c) 2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.addon;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import naftoreiclag.villagefive.SAM;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

// Addons can be applied on world creation
// Some worlds that you download may specify that certain addons need to be 
public class AddonManager
{
    public static List<Addon> addonCollection = new ArrayList<Addon>();
    
    public static void reloadAddons()
    {
        addonCollection.clear();
        
        // List all of the folders located in pluginRoots
        List<File> pluginRoots = (List<File>) FileUtils.listFilesAndDirs(new File(SAM.ADDON_DIR), FalseFileFilter.FALSE, TrueFileFilter.TRUE);
        
        // Remove the plugins folder, not sure why its even included.
        pluginRoots.remove(0);
        
        // 
        for(File pluginRoot : pluginRoots)
        {
            String addon_root = pluginRoot.getPath() + "/";
            
            Globals globals = JsePlatform.standardGlobals();
            globals.set("ADDON_ROOT", addon_root);
            globals.loadfile("addons/globals.lua").call();

            LuaValue rawAddon = globals.loadfile(addon_root + "addon.lua").call();

            LuaTable addonTable = rawAddon.checktable();

            Addon addon = new Addon(addonTable);
            
            addonCollection.add(addon);
        }
    }
}
