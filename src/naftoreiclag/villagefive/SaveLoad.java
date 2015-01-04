/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import naftoreiclag.villagefive.util.serializable.PlotSerial;
import naftoreiclag.villagefive.world.World;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class SaveLoad
{
    public static void save(World data) throws IOException
    {
        JSONObject obj = new JSONObject();
        obj.put("world", data);
        FileWriter fw = new FileWriter(new File("saves/save.json"));
        obj.writeJSONString(fw);
        fw.flush();
    }
    
    public static World load(Node rootNode, AssetManager assetManager) throws IOException, ParseException
    {
        World world = new World(rootNode, assetManager);
        
        JSONParser parser = new JSONParser();
        
        File file = new File("saves/save.json");
        FileReader fr = new FileReader(file);
        JSONObject root = (JSONObject) parser.parse(fr);
        JSONObject worldj = (JSONObject) root.get("world");
        
        System.out.println(worldj);
        
        world.spawnFromJson(worldj);
        
        return world;
    }
}
