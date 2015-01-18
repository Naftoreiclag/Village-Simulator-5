/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

import com.jme3.asset.plugins.FileLocator;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FilenameUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Models
{
    static
    {
        SAM.a.registerLocator("plugins", FileLocator.class);
    }
    
    public static Map<String, ArrayList<File>> map = new HashMap<String, ArrayList<File>>();
    
    public static void debug()
    {
        for(Map.Entry pairs : map.entrySet())
        {
            System.out.println(pairs.getKey() + " = " + pairs.getValue());
        }
    }
    
    public static File getRealLocation(String input)
    {
        String[] inputs = input.split(":");
        
        String namespace;
        String name;
        if(inputs.length > 1)
        {
            namespace = inputs[0];
            name = inputs[inputs.length - 1];
        }
        else
        {
            namespace = "std";
            name = inputs[0];
        }
        
        List<File> plugins = map.get(namespace);
        File realLocation = null;
        
        if(plugins == null)
        {
            System.out.println("errrrrrr");
        }
        else
        {
            
            // Iterate backward
            for(int i = plugins.size() - 1; i >= 0; -- i)
            {
                File pluginDir = plugins.get(i);
                
                File sss = new File(pluginDir.getAbsolutePath() + "/" + name);
                System.out.println(sss);
                
                
                if(sss.exists())
                {
                    realLocation = sss;
                    break;
                }
                
                
            }
        }
        
        System.out.println("rel ");
        System.out.println(realLocation);
        
        return realLocation;
    }
    public static Spatial loadModel(String input) throws IOException, ParseException
    {
        File realLoc = getRealLocation(input); if(realLoc == null) { return null; }
        
        return loadModel(realLoc);
    }
    
    public static Spatial loadModel(File realLoc) throws IOException, ParseException
    {
        String ext = FilenameUtils.getExtension(realLoc.getAbsolutePath());
        
        if(ext.equalsIgnoreCase("json"))
        {
            System.out.println("loading json model");
            JSONParser parser = new JSONParser();
            JSONObject data = (JSONObject) parser.parse(new FileReader(realLoc));
            
            String mesh = (String) data.get("mesh");
            
            Spatial geo = loadModel(mesh);
            Material mat = loadMaterial(realLoc);
            geo.setMaterial(mat);
            
            return geo;
            
        }
        else if(ext.equalsIgnoreCase("j3o"))
        {
            System.out.println("loading j3o model");
            return SAM.a.loadModel(realLoc.getAbsolutePath().substring(65));
        }
        
        return null;
    }
    
    public static Material loadMaterial(String input) throws IOException, ParseException
    {
        File realLoc = getRealLocation(input); if(realLoc == null) { return null; }
        
        return loadMaterial(realLoc);
    }
    
    public static Material loadMaterial(File realLoc) throws IOException, ParseException
    {
        String ext = FilenameUtils.getExtension(realLoc.getAbsolutePath());
        
        if(ext.equalsIgnoreCase("json"))
        {
            JSONParser parser = new JSONParser();
            JSONObject data = (JSONObject) parser.parse(new FileReader(realLoc));
            
            Material mat = new Material(SAM.a, "Common/MatDefs/Light/Lighting.j3md");
            
            mat.setTexture("DiffuseMap", loadTexture((String) data.get("diffuse")));
            mat.setColor("Ambient", ColorRGBA.White);
            mat.setColor("Diffuse", ColorRGBA.White);
            mat.setBoolean("UseMaterialColors", true);
            
            return mat;
        }
        
        return null;
    }
    
    public static Texture loadTexture(String input) throws IOException, ParseException
    {
        File realLoc = getRealLocation(input); if(realLoc == null) { return null; }
        
        return loadTexture(realLoc);
    }
    
    public static Texture loadTexture(File realLoc) throws IOException, ParseException
    {
        System.out.println(realLoc.getAbsolutePath().substring(65));
        
        return SAM.a.loadTexture(realLoc.getAbsolutePath().substring(65));
    }
}
