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
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

class PluginLoader
{
    public final static String pluginDir = "plugins";

    static void loadPlugs() throws FileNotFoundException, IOException, ParseException
    {
        List<File> pluginDirs = (List<File>) FileUtils.listFilesAndDirs(new File(pluginDir), FalseFileFilter.FALSE, TrueFileFilter.TRUE);
        
        // remove the base plugin directory
        pluginDirs.remove(0);
        
        JSONParser parser = new JSONParser();
        
        for(File plug : pluginDirs)
        {
            File plugData = FileUtils.getFile(plug, "plugin.json");
            
            FileReader fr = new FileReader(plugData);
            
            JSONObject meta = (JSONObject) parser.parse(fr);
            
            
            String pName = (String) meta.get("name");
            String pSpace = (String) meta.get("namespace");
            String pDesc = (String) meta.get("description");
            
            Plugin plugin = new Plugin(pName, pSpace, pDesc);
            
            System.out.println(meta);
            
            List<File> entities = (List<File>) FileUtils.listFiles(plug, new String[]{"entity.json", "ent.json"}, true);
            
            System.out.println(entities);
            
            for(File ent : entities)
            {
                FileReader fr2 = new FileReader(ent);
                
                JSONObject data = (JSONObject) parser.parse(fr2);
                
                String name = (String) data.get("name");
                
                PluginEntity entity = new PluginEntity(plugin, name);
                
                
            }
        }
        
    }

}
