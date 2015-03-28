/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

import com.jme3.asset.TextureKey;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import naftoreiclag.villagefive.world.body.EntityBody;
import naftoreiclag.villagefive.world.entity.Entity;
import org.apache.commons.io.FilenameUtils;
import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.Mass;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class PluginResourceManager
{
    static
    {
        SAM.ASSETS.registerLocator("plugins", FileLocator.class);
    }
    
    public static Map<String, ArrayList<File>> map = new HashMap<String, ArrayList<File>>();
    
    public static String relativize(File absName)
    {
        String work = System.getProperty("user.dir") + "\\" + PluginLoader.pluginDir + "\\";
        String foo = absName.getAbsolutePath();
        
        if(foo.startsWith(work))
        {
            return foo.substring(work.length());
        }
        
        return foo;
    }
    
    public static void debug()
    {
        for(Map.Entry pairs : map.entrySet())
        {
            System.out.println(pairs.getKey() + " = " + pairs.getValue());
        }
    }
    
    public static File getRealLocation(String input)
    {
        if(input == null)
        {
            return null;
        }
        if("null".equals(input))
        {
            return null;
        }
        
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
            return SAM.ASSETS.loadModel(relativize(realLoc));
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
            
            Material mat;
            if(usesShaderBlow(data))
            {
                mat = new Material(SAM.ASSETS, "ShaderBlow/MatDefs/LightBlow/LightBlow.j3md");
                
                if(data.containsKey("rim_lighting"))
                {
                    mat.setColor("RimLighting", getRGBA((JSONArray) data.get("rim_lighting")));
                }
            }
            else
            {
                mat = new Material(SAM.ASSETS, "Common/MatDefs/Light/Lighting.j3md");
            }
            
            mat.setColor("Ambient", ColorRGBA.White);
            mat.setColor("Diffuse", ColorRGBA.White);
            mat.setBoolean("UseMaterialColors", true);

            mat.setTexture("DiffuseMap", loadTexture((String) data.get("diffuse")));
            mat.setTexture("GlowMap", loadTexture((String) data.get("glow")));
            
            
            
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
        TextureKey key = new TextureKey(relativize(realLoc));
        key.setFlipY(false);
        
        return SAM.ASSETS.loadTexture(key);
    }
    
    public static Body loadBody(Entity owner, String input) throws IOException, ParseException
    {
        File realLoc = getRealLocation(input); if(realLoc == null) { return null; }
        
        return loadBody(owner, realLoc);
    }
    
    public static Body loadBody(Entity owner, File realLoc) throws IOException, ParseException
    {
        Body body = new EntityBody(owner);
        
        JSONParser parser = new JSONParser();
        JSONObject data = (JSONObject) ((JSONObject) parser.parse(new FileReader(realLoc))).get("collision");
        
        String type = (String) data.get("type");
        
        if(type.equalsIgnoreCase("circle"))
        {
            double radius = (Double) data.get("radius");
            
            System.out.println("aaaaa=" + radius);
            
            body.addFixture(new Circle(radius), 5);
        }
        
        boolean immobile = false;
        if(data.containsKey("immobile"))
        {
            immobile = (Boolean) data.get("immobile");
        }
        
        if(immobile)
        {
            body.setMass(new Mass());
        }
        else
        {
            
        body.setMass();
        }
        
        
        return body;
    }

    private static boolean usesShaderBlow(JSONObject data)
    {
        return data.containsKey("rim_lighting") || data.containsKey("matcap");
    }

    private static ColorRGBA getRGBA(JSONArray jsonArray)
    {
        double r = (Double) jsonArray.get(0);
        double g = (Double) jsonArray.get(1);
        double b = (Double) jsonArray.get(2);
        
        return new ColorRGBA((float) r, (float) g, (float) b, 1f);
    }
}
