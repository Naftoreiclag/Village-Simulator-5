/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

import com.jme3.scene.Mesh;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import static naftoreiclag.villagefive.Hills.height;
import static naftoreiclag.villagefive.Hills.width;
import naftoreiclag.villagefive.util.Layer;
import naftoreiclag.villagefive.util.ModelBuilder;

public class NewHills
{
    public static final int width = 25;
    public static final int height = 25;
    
    public static final int numLevels = 16;
    
    // Some kind of array to store floor decals
    
    public int[][] heights;
    
    public Mesh mesh;
    
    public NewHills()
    {
        loadDataFromFile();
        buildGeometry();
    }
    public void loadDataFromFile()
    {
        BufferedImage img = null;
        try
        {
            img = ImageIO.read(new File("donotinclude/heightmap.png"));
        } catch (IOException e)
        {
        }

        heights = new int[width][height];
        for(int x = 0; x < width; ++ x)
        {
            for(int z = 0; z < height; ++ z)
            {
                heights[x][z] = (img.getRGB(x, z) & 0x000000F0) / 16;
            }
        }
    }

    private void buildGeometry()
    {
        Layer mb = new Layer();
        
        for(int y = 0; y < numLevels; ++ y)
        {
            for(int x = 0; x < width - 1; ++ x)
            {
                for(int z = 0; z < height - 1; ++ z)
                {
                    addGeo(mb, x, y, z);
                }
            }
        }
        
        mesh = mb.bake();
    }
    
    public int access(int x, int z)
    {
        if(x < 0 || z < 0 || x >= width || z >= width)
        {
            return 0;
        }
        
        return heights[x][z];
    }

    private void addGeo(Layer mb, int x, int y, int z)
    {
        int type = 0;
        if(access(x - 1, z) >= y)
        {
            type += 1;
        }
        if(access(x, z - 1) >= y)
        {
            type += 2;
        }
        if(access(x + 1, z) >= y)
        {
            type += 4;
        }
        if(access(x, z + 1) >= y)
        {
            type += 8;
        }
        
        if(type == 12)
        {
            
        }
        else if(type == 9)
        {
            
        }
        else if(type == 3)
        {
            
        }
        else if(type == 6)
        {
            
        }
        else if(type == 14)
        {
            
        }
        else if(type == 13)
        {
            
        }
        else if(type == 11)
        {
            
        }
        else if(type == 7)
        {
            
        }
    }
}
