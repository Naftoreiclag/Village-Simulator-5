/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import naftoreiclag.villagefive.world.entity.EntityRegistry;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

class PluginLoader
{
    public final static String pluginDir = "plugins";
    
    public static List<Plugin> plugins = new ArrayList<Plugin>();

    static void loadPlugs() throws FileNotFoundException, IOException, ParseException
    {
        List<File> dirs = (List<File>) FileUtils.listFilesAndDirs(new File(pluginDir), FalseFileFilter.FALSE, TrueFileFilter.TRUE);
        
        // remove the base plugin directory
        dirs.remove(0);
        
        JSONParser parser = new JSONParser();
        
        for(File dir : dirs)
        {
            File metaF = FileUtils.getFile(dir, "plugin.json");
            FileReader metaR = new FileReader(metaF);
            JSONObject meta = (JSONObject) parser.parse(metaR);
            
            String pName = (String) meta.get("name");
            String pSpace = (String) meta.get("namespace");
            String pDesc = (String) meta.get("description");
            
            Plugin plugin = new Plugin(pName, pSpace, pDesc);
            
            System.out.println(meta);
            
            List<File> ents = (List<File>) FileUtils.listFiles(dir, new String[]{"entity.json", "ent.json"}, true);
            
            System.out.println(ents);
            
            for(File entF : ents)
            {
                FileReader entR = new FileReader(entF);
                
                JSONObject data = (JSONObject) parser.parse(entR);
                
                String name = (String) data.get("name");
                
                PluginEntity entity = new PluginEntity(plugin, name);
                plugin.entities.add(entity);
                
            }
            
            plugins.add(plugin);
        }
        
        for(Plugin plugin : plugins)
        {
            for(PluginEntity entity : plugin.entities)
            {
                EntityRegistry.register(entity);
            }
        }
        
    }

}
